package com.borsam.web.controller.patient;

import com.borsam.pub.UserType;
import com.borsam.repository.entity.patient.PatientAccount;
import com.borsam.repository.entity.patient.PatientProfile;
import com.borsam.repository.entity.patient.PatientToken;
import com.borsam.service.patient.PatientAccountService;
import com.borsam.service.patient.PatientTokenService;
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
 * Created by tantian on 2015/9/2.
 */
@Controller("patientPasswordController")
@RequestMapping(value = "/patient/password")
public class PasswordController extends BaseController {

    @Resource(name = "patientAccountServiceImpl")
    private PatientAccountService patientAccountService;

    @Resource(name = "rsaServiceImpl")
    private RSAService rsaService;

    @Resource(name = "captchaServiceImpl")
    private CaptchaService captchaService;

    @Resource(name = "patientTokenServiceImpl")
    private PatientTokenService patientTokenService;

    @Resource(name = "mailServiceImpl")
    private MailService mailService;

    /**
     * 修改密码页面
     */
    @RequestMapping(value = "/reset", method = RequestMethod.GET)
    public String reset(String username, String key, Model model) {
        if (StringUtils.isNotEmpty(username)) {
            PatientAccount patientAccount = patientAccountService.findByUsername(username);
            if (patientAccount == null) {
                model.addAttribute("message", Message.warn("common.message.data"));
                return ERROR_VIEW;
            }
            PatientToken patientToken = patientTokenService.find(patientAccount.getId());
            if (patientToken == null || patientToken.getToken() == null || !patientToken.getToken().equals(key)) {
                model.addAttribute("message", Message.warn("common.message.data"));
                return ERROR_VIEW;
            }
            if (patientToken.hasExpired()) {
                model.addAttribute("message", Message.warn("common.message.url.hasExpired"));
                return ERROR_VIEW;
            }
        }
        model.addAttribute("username", username);
        model.addAttribute("key", key);
        return "/patient/password/reset";
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
        return "/patient/password/find";
    }

    /**
     * 修改密码提交
     */
    @RequestMapping(value = "/reset", method = RequestMethod.POST)
    @ResponseBody
    public Message reset(String username, String key, String oldPassword, String newPassword) {
        PatientAccount patientAccount;
        PatientToken patientToken = null;
        if (StringUtils.isNotEmpty(username) && StringUtils.isNotEmpty(key)) {
            patientAccount = patientAccountService.findByUsername(username);
            patientToken = patientTokenService.find(patientAccount.getId());
        } else {
            patientAccount = patientAccountService.getCurrent();
        }

        if (!DigestUtils.md5Hex(oldPassword).equals(patientAccount.getPassword())) {
            return Message.warn("common.message.oldPassword");
        }
        patientAccount.setPassword(DigestUtils.md5Hex(newPassword));
        patientAccountService.update(patientAccount);

        if (patientToken != null) {
            patientToken.setToken("");
            patientToken.setUpdated(new Date().getTime() / 1000);
            patientTokenService.update(patientToken);
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
        PatientAccount patientAccount = patientAccountService.findByUsername(account);
        PatientProfile patientProfile = patientAccount.getPatientProfile();
        if (patientAccount == null) {
            return Message.warn("common.login.unknownAccount");
        }
        if (!patientProfile.getEmail().equalsIgnoreCase(email)) {
            return Message.warn("common.password.invalidEmail");
        }

        // 发送激活邮件
        PatientToken token = patientTokenService.find(patientAccount.getId());
        if (token == null) {
            token = new PatientToken();
            token.setId(patientAccount.getId());
            token.setToken(UUID.randomUUID().toString());
            token.setUpdated((DateUtils.addDays(new Date(), 7).getTime()) / 1000);   // 7天内有效
            patientTokenService.save(token);
        } else {
            token.setToken(UUID.randomUUID().toString());
            token.setUpdated((DateUtils.addDays(new Date(), 7).getTime()) / 1000);   // 7天内有效
            patientTokenService.update(token);
        }
        mailService.sendFindPasswordMail(UserType.patient, email, patientProfile.getFullName(), token);
        return Message.success("common.password.mailSuccess");
    }
}
