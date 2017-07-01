package com.borsam.web.controller.org;

import com.borsam.pub.UserType;
import com.borsam.repository.entity.doctor.DoctorAccount;
import com.borsam.repository.entity.doctor.DoctorToken;
import com.borsam.service.doctor.DoctorAccountService;
import com.borsam.service.doctor.DoctorTokenService;
import com.borsam.service.pub.MailService;
import com.hiteam.common.service.RSAService;
import com.hiteam.common.util.ConfigUtils;
import com.hiteam.common.web.Message;
import com.hiteam.common.web.WebUtil;
import com.hiteam.common.web.controller.BaseController;
import org.apache.commons.lang.time.DateUtils;
import org.apache.shiro.web.filter.authc.FormAuthenticationFilter;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import java.security.interfaces.RSAPublicKey;
import java.util.Date;
import java.util.Map;
import java.util.UUID;

/**
 * Controller - 机构管理员登录
 * Created by Sebarswee on 2015/6/16.
 */
@Controller("orgLoginController")
@RequestMapping(value = "/org/login")
public class LoginController extends BaseController {

    private final String LOGIN_URL = "/org/login/index";

    @Resource(name = "rsaServiceImpl")
    private RSAService rsaService;

    @Resource(name = "doctorAccountServiceImpl")
    private DoctorAccountService doctorAccountService;

    @Resource(name = "doctorTokenServiceImpl")
    private DoctorTokenService doctorTokenService;

    @Resource(name = "mailServiceImpl")
    private MailService mailService;

    /**
     * 登录页面
     */
    @RequestMapping(method = RequestMethod.GET)
    @SuppressWarnings("unchecked")
    public String login(ModelMap modelMap) {
        RSAPublicKey publicKey = rsaService.generateKey();
        Map map = rsaService.getModulusAndExponent(publicKey);
        String captchaId = UUID.randomUUID().toString();
        map.put("captchaId", captchaId);
        modelMap.putAll(map);
        WebUtil.setSessionData("locale", ConfigUtils.config.getProperty("locale"));
        WebUtil.setSessionData("userType", UserType.org);
        WebUtil.setSessionData("userTypePath", UserType.org.getPath());
        return LOGIN_URL;
    }

    /**
     * 登录失败
     * @return 登录页面
     */
    @RequestMapping(method = RequestMethod.POST)
    public String fail(@RequestParam(FormAuthenticationFilter.DEFAULT_USERNAME_PARAM) String userName, Model model) {
        model.addAttribute(FormAuthenticationFilter.DEFAULT_USERNAME_PARAM, userName);
        WebUtil.setSessionData("userType", UserType.org);
        WebUtil.setSessionData("userTypePath", UserType.org.getPath());
        return LOGIN_URL;
    }

    @RequestMapping(value = "/mail/resend", method = RequestMethod.POST)
    @ResponseBody
    public Message resendMail(String username) {
        DoctorAccount doctorAccount = doctorAccountService.findByUsername(username);
        if (doctorAccount == null) {
            return Message.warn("common.login.unknownAccount");
        }
        if (!doctorAccount.getEmail().equalsIgnoreCase(username)) {
            return Message.warn("common.password.invalidEmail");
        }

        // 发送激活邮件
        DoctorToken token = doctorTokenService.find(doctorAccount.getId());
        if (token == null) {
            token = new DoctorToken();
            token.setId(doctorAccount.getId());
            token.setToken(UUID.randomUUID().toString());
            token.setUpdated((DateUtils.addDays(new Date(), 7).getTime()) / 1000);   // 7天内有效
            doctorTokenService.save(token);
        } else {
            token.setToken(UUID.randomUUID().toString());
            token.setUpdated((DateUtils.addDays(new Date(), 7).getTime()) / 1000);   // 7天内有效
            doctorTokenService.update(token);
        }
        mailService.sendActiveMail(username, username, token);
        return Message.success("org.login.sendmail.success");
    }
}
