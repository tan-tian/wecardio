package com.borsam.web.controller.org;

import com.borsam.pub.UserType;
import com.borsam.repository.entity.doctor.DoctorAccount;
import com.borsam.repository.entity.doctor.DoctorProfile;
import com.borsam.repository.entity.doctor.DoctorToken;
import com.borsam.service.doctor.DoctorAccountService;
import com.borsam.service.doctor.DoctorTokenService;
import com.borsam.service.pub.MailService;
import com.hiteam.common.service.CaptchaService;
import com.hiteam.common.service.RSAService;
import com.hiteam.common.web.Message;
import com.hiteam.common.web.controller.BaseController;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.time.DateUtils;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpSession;
import java.security.interfaces.RSAPublicKey;
import java.util.Date;
import java.util.Map;
import java.util.UUID;

/**
 * Controller - 密码
 * Created by Sebarswee on 2015/9/2.
 */
@Controller("orgPasswordController")
@RequestMapping(value = "/org/password")
public class PasswordController extends BaseController {

    @Resource(name = "doctorAccountServiceImpl")
    private DoctorAccountService doctorAccountService;

    @Resource(name = "rsaServiceImpl")
    private RSAService rsaService;

    @Resource(name = "captchaServiceImpl")
    private CaptchaService captchaService;

    @Resource(name = "doctorTokenServiceImpl")
    private DoctorTokenService doctorTokenService;

    @Resource(name = "mailServiceImpl")
    private MailService mailService;

    /**
     * 修改密码页面
     */
    @RequestMapping(value = "/reset", method = RequestMethod.GET)
    public String reset(String username, String key, Model model) {
        if (StringUtils.isNotEmpty(username)) {
            DoctorAccount doctorAccount = doctorAccountService.findByUsername(username);
            if (doctorAccount == null) {
                model.addAttribute("message", Message.warn("common.message.data"));
                return ERROR_VIEW;
            }
            DoctorToken doctorToken = doctorTokenService.find(doctorAccount.getId());
            if (doctorToken == null || doctorToken.getToken() == null || !doctorToken.getToken().equals(key)) {
                model.addAttribute("message", Message.warn("common.message.data"));
                return ERROR_VIEW;
            }
            if (doctorToken.hasExpired()) {
                model.addAttribute("message", Message.warn("common.message.url.hasExpired"));
                return ERROR_VIEW;
            }
        }
        model.addAttribute("username", username);
        model.addAttribute("key", key);
        return "/org/password/reset";
    }

    /**
     * 找回密码
     */
    @RequestMapping(value = "/find", method = RequestMethod.GET)
    public String find(ModelMap model) {
        RSAPublicKey publicKey = rsaService.generateKey();
        Map<String, String> map = rsaService.getModulusAndExponent(publicKey);
        String captchaId = UUID.randomUUID().toString();
        map.put("captchaId", captchaId);
        model.putAll(map);
        return "/org/password/find";
    }

    /**
     * 修改密码提交
     */
    @RequestMapping(value = "/reset", method = RequestMethod.POST)
    @ResponseBody
    public Message reset(String username, String key, String oldPassword, String newPassword) {
        DoctorAccount doctorAccount;
        DoctorToken doctorToken = null;
        if (StringUtils.isNotEmpty(username) && StringUtils.isNotEmpty(key)) {
            doctorAccount = doctorAccountService.findByUsername(username);
            doctorToken = doctorTokenService.find(doctorAccount.getId());
        } else {
            doctorAccount = doctorAccountService.getCurrent();
        }
        if (!DigestUtils.md5Hex(oldPassword).equals(doctorAccount.getPassword())) {
            return Message.warn("common.message.oldPassword");
        }
        doctorAccount.setPassword(DigestUtils.md5Hex(newPassword));
        doctorAccountService.update(doctorAccount);

        if (doctorToken != null) {
            doctorToken.setToken("");
            doctorToken.setUpdated(new Date().getTime() / 1000);
            doctorTokenService.update(doctorToken);
        }
        return SUCCESS_MSG;
    }

    /**
     * 找回密码提交
     */
    @RequestMapping(value = "/find", method = RequestMethod.POST)
    @ResponseBody
    public Message find(String captcha, String account, String email, HttpSession session) {
        if (!captchaService.isValid(session.getId(), captcha)) {
            return Message.warn("common.captcha.invalid");
        }

        if (StringUtils.isEmpty(account) || StringUtils.isEmpty(email)) {
            return Message.warn("common.message.data");
        }
        DoctorAccount doctorAccount = doctorAccountService.findByUsername(account);
        DoctorProfile doctorProfile = doctorAccount.getDoctorProfile();
        if (doctorAccount == null) {
            return Message.warn("common.login.unknownAccount");
        }
        if (!doctorProfile.getEmail().equalsIgnoreCase(email)) {
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
        mailService.sendFindPasswordMail(UserType.org, email, doctorAccount.getEmail(), token);
        return Message.success("common.password.mailSuccess");
    }
}
