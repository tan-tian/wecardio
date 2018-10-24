package com.borsam.web.controller.org;

import com.borsam.repository.entity.doctor.DoctorAccount;
import com.borsam.repository.entity.doctor.DoctorToken;
import com.borsam.service.doctor.DoctorAccountService;
import com.borsam.service.doctor.DoctorProfileService;
import com.borsam.service.doctor.DoctorTokenService;
import com.hiteam.common.base.repository.entity.BaseEntity;
import com.hiteam.common.service.CaptchaService;
import com.hiteam.common.service.RSAService;
import com.hiteam.common.web.Message;
import com.hiteam.common.web.controller.BaseController;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.Date;
import java.util.UUID;

/**
 * Controller - 机构注册
 * Created by tantian on 2015/6/24.
 */
@Controller("orgRegisterController")
@RequestMapping("/org/register")
public class RegisterController extends BaseController {

    private Logger logger = LoggerFactory.getLogger(RegisterController.class);

    private static final String NOTIFY_VIEW = "/org/register/notify";

    @Resource(name = "rsaServiceImpl")
    private RSAService rsaService;

    @Resource(name = "captchaServiceImpl")
    private CaptchaService captchaService;

    @Resource(name = "doctorAccountServiceImpl")
    private DoctorAccountService doctorAccountService;

    @Resource(name = "doctorProfileServiceImpl")
    private DoctorProfileService doctorProfileService;

    @Resource(name = "doctorTokenServiceImpl")
    private DoctorTokenService doctorTokenService;

    /**
     * 注册页面
     */
    @RequestMapping(method = RequestMethod.GET)
    public String index(ModelMap model) {
        model.addAttribute("captchaId", UUID.randomUUID().toString());
        return "/org/register/index";
    }

    /**
     * 检查Email是否存在
     */
    @RequestMapping(value = "/checkEmail", method = RequestMethod.GET)
    @ResponseBody
    public boolean checkEmail(String email) {
        return StringUtils.isNotEmpty(email) && !doctorAccountService.emailExists(email);
    }

    /**
     * 注册提交
     */
    @RequestMapping(value = "/submit", method = RequestMethod.POST)
    @ResponseBody
    public Message submit(String captcha, String email, String mobile, HttpServletRequest request, HttpSession session) {
        String password = rsaService.decryptParameter("enPassword");
        rsaService.removePrivateKey();

        if (!captchaService.isValid(session.getId(), captcha)) {
            return Message.error("common.captcha.invalid");
        }

        if (!isValid(DoctorAccount.class, "email", email, BaseEntity.Save.class)
                || !isValid(DoctorAccount.class, "password", password, BaseEntity.Save.class)) {
            return Message.error("org.common.invalid");
        }

//        if (password.length() < Integer.parseInt(ConfigUtils.config.getProperty("passwordMinLength"))
//                || password.length() > Integer.parseInt(ConfigUtils.config.getProperty("passwordManLength"))) {
//            return Message.error("org.common.invalid");
//        }

        if (doctorAccountService.emailExists(email)) {
            return Message.error("org.register.exist");
        }

        doctorProfileService.register(email, password, mobile);
        return Message.success("org.register.success");
    }

    /**
     * 账号激活
     */
    @RequestMapping(value = "/active", method = RequestMethod.GET)
    public String active(String username, String key, Model model) {
        logger.info("机构激活, usernam={}, key={}", new Object[]{username, key});
        DoctorAccount doctorAccount = doctorAccountService.findByUsername(username);
        if (doctorAccount == null) {
            logger.info("机构账号{}不存在", username);
            model.addAttribute("message", Message.warn("org.register.invalid"));
            return NOTIFY_VIEW;
        }
        DoctorToken doctorToken = doctorTokenService.find(doctorAccount.getId());
        if (doctorToken == null || doctorToken.getToken() == null || !doctorToken.getToken().equals(key)) {
            logger.info("无效Token: {}", key);
            model.addAttribute("message", Message.warn("org.register.invalid"));
            return NOTIFY_VIEW;
        }
        if (doctorToken.hasExpired()) {
            logger.info("Token: {}已失效", key);
            model.addAttribute("message", Message.warn("org.register.hasExpired"));
            return NOTIFY_VIEW;
        }

        model.addAttribute("message", Message.success("org.register.activated"));
        doctorAccount.setIsActive(true);
        doctorAccountService.update(doctorAccount);

        doctorToken.setToken("");
        doctorToken.setUpdated(new Date().getTime() / 1000);
        doctorTokenService.update(doctorToken);
        logger.info("机构已成功激活");
//        return "redirect:/org/login";
        return NOTIFY_VIEW;
    }
}
