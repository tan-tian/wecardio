package com.borsam.web.controller.doctor;

import com.borsam.pub.UserType;
import com.hiteam.common.service.RSAService;
import com.hiteam.common.util.ConfigUtils;
import com.hiteam.common.util.spring.SpringUtils;
import com.hiteam.common.web.WebUtil;
import com.hiteam.common.web.controller.BaseController;
import com.sun.tools.internal.ws.processor.model.Request;

import org.apache.commons.configuration.SystemConfiguration;
import org.apache.commons.lang.SystemUtils;
import org.apache.shiro.web.filter.authc.FormAuthenticationFilter;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import javax.annotation.Resource;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import java.security.interfaces.RSAPublicKey;
import java.util.Map;
import java.util.UUID;

/**
 * Controller - 医生登录
 * Created by tantian on 2015/6/16.
 */
@Controller("doctorLoginController")
@RequestMapping(value = "/doctor/login")
public class LoginController extends BaseController {
	public static ThreadLocal<HttpServletRequest> servletRequest = new ThreadLocal<HttpServletRequest>();

    private final String LOGIN_URL = "/doctor/login/index";
   
    public static void setRequest(HttpServletRequest request) {
        servletRequest.set(request);
    }
    @Resource(name = "rsaServiceImpl")
    private RSAService rsaService;

    /**
     * 登录页面
     */
    @RequestMapping(method = RequestMethod.GET)
    @SuppressWarnings("unchecked")
    public String login(ModelMap modelMap,HttpSession httpSession) {
    	
        RSAPublicKey publicKey = rsaService.generateKey();
        Map map = rsaService.getModulusAndExponent(publicKey);
        String captchaId = UUID.randomUUID().toString();
        map.put("captchaId", captchaId);
        modelMap.putAll(map);
        WebUtil.setSessionData("locale", ConfigUtils.config.getProperty("locale"));
        WebUtil.setSessionData("userType", UserType.doctor);
        WebUtil.setSessionData("userTypePath", UserType.doctor.getPath());
        return LOGIN_URL;
    }

    /**
     * 登录失败
     * @return 登录页面
     */
    @RequestMapping(method = RequestMethod.POST)
    public String fail(@RequestParam(FormAuthenticationFilter.DEFAULT_USERNAME_PARAM) String userName, Model model) {
        model.addAttribute(FormAuthenticationFilter.DEFAULT_USERNAME_PARAM, userName);
        WebUtil.setSessionData("userType", UserType.doctor);
        WebUtil.setSessionData("userTypePath", UserType.doctor.getPath());
        return LOGIN_URL;
    }

}
