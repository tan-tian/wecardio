package com.hiteam.common.web.spring.exception;

import com.hiteam.common.util.json.JsonUtils;
import com.hiteam.common.web.I18Util;
import com.hiteam.common.web.Message;
import com.hiteam.common.web.WebUtil;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.SimpleMappingExceptionResolver;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.Map;


/**
 * <pre>
 * Description:
 * Author: tantian
 * Version:
 * Since: Ver 1.1
 * Date: 2014-11-03 14:21
 * </pre>
 */
public class ExtSimpleMappingExceptionResolver extends SimpleMappingExceptionResolver {
    private Logger logger = LoggerFactory.getLogger(ExtSimpleMappingExceptionResolver.class);
    @Override
    protected ModelAndView doResolveException(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) {
        String viewName = determineViewName(ex, request);
        if (viewName != null) {// JSP格式返回
            if (!WebUtil.isAjaxRequest()) {
                Integer statusCode = determineStatusCode(request, viewName);
                if (statusCode != null) {
                    applyStatusCodeIfPossible(request, response, statusCode);
                }
                return getModelAndView(viewName, ex, request);
            } else {// JSON格式返回

                Message message = null;

                String errorIn18 = "";
                String errorInfo = "";

                if (ex instanceof org.apache.shiro.authc.pam.UnsupportedTokenException) {
                    errorIn18 = "common.captcha.invalid";
                } else if (ex instanceof org.apache.shiro.authc.UnknownAccountException) {
                    errorIn18 = "common.login.unknownAccount";
                } else if (ex instanceof org.apache.shiro.authc.DisabledAccountException) {
                    errorIn18 = "common.login.disabledAccount";
                } else if (ex instanceof org.apache.shiro.authc.LockedAccountException) {
                    errorIn18 = "common.login.lockedAccount";
                } else if (ex instanceof org.apache.shiro.authc.IncorrectCredentialsException) {
                    errorIn18 = "common.login.incorrectCredentials";
                } else if (ex instanceof org.apache.shiro.authc.AuthenticationException) {
                    errorIn18 = "common.login.authentication";
                }

                if (StringUtils.isNotBlank(errorIn18)) {
                    Map modelMap = new HashMap();
                    errorInfo = I18Util.getMessage(errorIn18, new Object[0]);
                    modelMap.put("errorInfo", errorInfo);
                    modelMap.put("errorIn18", errorIn18);

                    response.addHeader("login", "fail");
                    response.addHeader("loginFailure", "accessDenied");
                    message = new Message(Message.Type.error, errorIn18, modelMap);
                } else {

                    String headerName = "exception";
                    String headerVal = "8888888";
                    //标识需要调整到登陆页面
                    if (viewName.toUpperCase().contains("LOGIN")) {
                        headerName = "loginStatus";
                        headerVal = "accessDenied";
                    }

                    response.setHeader(headerName, headerVal);
                    logger.error("doResolveException:", ex);
                    message = new Message(Message.Type.error, ex.getMessage());
                }

                WebUtil.responseJson(response, request, JsonUtils.toJson(message));

                return new ModelAndView(viewName);

            }
        } else {
            return null;
        }
    }
}
