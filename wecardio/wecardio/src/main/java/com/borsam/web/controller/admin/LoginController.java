package com.borsam.web.controller.admin;

import com.borsam.pub.UserType;
import com.hiteam.common.service.RSAService;
import com.hiteam.common.util.ConfigUtils;
import com.hiteam.common.web.WebUtil;
import com.hiteam.common.web.controller.BaseController;
import org.apache.shiro.web.filter.authc.FormAuthenticationFilter;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import javax.annotation.Resource;
import java.security.interfaces.RSAPublicKey;
import java.util.Map;
import java.util.UUID;

/**
 * Controller - 管理后台登录
 * Created by Sebarswee on 2015/6/16.
 */
@Controller("adminLoginController")
@RequestMapping(value = "/admin/login")
public class LoginController extends BaseController {

    private final String LOGIN_URL = "/admin/login/index";

    @Resource(name = "rsaServiceImpl")
    private RSAService rsaService;

    /**
     * 管理后台登录页面
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
        WebUtil.setSessionData("userType", UserType.admin);
        WebUtil.setSessionData("userTypePath", UserType.admin.getPath());
        return LOGIN_URL;
    }

    /**
     * 登录失败
     * @return 登录页面
     */
    @RequestMapping(method = RequestMethod.POST)
    public String fail(@RequestParam(FormAuthenticationFilter.DEFAULT_USERNAME_PARAM) String userName, Model model) {
        model.addAttribute(FormAuthenticationFilter.DEFAULT_USERNAME_PARAM, userName);
        WebUtil.setSessionData("userType", UserType.admin);
        WebUtil.setSessionData("userTypePath", UserType.admin.getPath());
        return LOGIN_URL;
    }

}
