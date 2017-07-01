package com.hiteam.common.web.controller;

import com.hiteam.common.service.RSAService;
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
 * <pre>
 * Description:
 * Author: Zhang zhongtao
 * Version:
 * Since: Ver 1.1
 * Date: 2014-11-05 15:48
 * </pre>
 */
@RequestMapping({"/login"})
@Controller("loginController")
public class LoginController extends BaseController {
    @Resource(name = "rsaServiceImpl")
    private RSAService rsaService;

    /**
     * 获取登陆页面
     *
     * @return 登陆页面地址
     */
    private String getLoginUrl() {
        return "/login";
    }

    @RequestMapping(method = RequestMethod.GET)
    public String login(ModelMap modelMap) {
        RSAPublicKey publicKey = rsaService.generateKey();
        Map map = rsaService.getModulusAndExponent(publicKey);
        String captchaId = UUID.randomUUID().toString();
        map.put("captchaId", captchaId);
        modelMap.putAll(map);

        return getLoginUrl();
    }

    @RequestMapping(method = RequestMethod.POST)
    public String fail(@RequestParam(FormAuthenticationFilter.DEFAULT_USERNAME_PARAM) String userName, Model model) {
        model.addAttribute(FormAuthenticationFilter.DEFAULT_USERNAME_PARAM, userName);

        return getLoginUrl();
    }

}
