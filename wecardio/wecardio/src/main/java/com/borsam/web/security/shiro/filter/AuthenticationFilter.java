package com.borsam.web.security.shiro.filter;

import com.borsam.pub.UserType;
import com.borsam.web.security.shiro.token.UserTypeToken;
import com.hiteam.common.service.RSAService;
import com.hiteam.common.util.json.JsonUtils;
import com.hiteam.common.web.I18Util;
import com.hiteam.common.web.Message;
import com.hiteam.common.web.WebUtil;
import org.apache.commons.lang.StringUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.session.Session;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.web.filter.authc.FormAuthenticationFilter;
import org.apache.shiro.web.util.WebUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.Resource;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.security.interfaces.RSAPublicKey;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * Filter - 权限认证
 * Created by Sebarswee on 2015/6/18.
 */
public class AuthenticationFilter extends FormAuthenticationFilter {

    private Logger logger = LoggerFactory.getLogger(AuthenticationFilter.class);

    /**
     * 默认密码加密参数名
     */
    private static final String DEFAULT_EN_PASSWORD_PARAM = "enPassword";

    /**
     * 默认验证ID参数名
     */
    private static final String DEFAULT_CAPTCHA_ID_PARAM = "captchaId";

    /**
     * 默认验证码参数名
     */
    private static final String DEFAULT_CAPTCHA_PARAM = "captcha";

    private String enPasswordParam = DEFAULT_EN_PASSWORD_PARAM;
    private String captchaIdParam = DEFAULT_CAPTCHA_ID_PARAM;
    private String captchaParam = DEFAULT_CAPTCHA_PARAM;

    private String loginUrl = "/{userType}/login";
    private String successUrl = "/{userType}";

    @Resource(name = "rsaServiceImpl")
    private RSAService rsaService;

    @Override
    protected AuthenticationToken createToken(ServletRequest request, ServletResponse response) {
        String username = getUsername(request);
        String password = getPassword(request);
        String captchaId = getCaptchaId(request);
        String captcha = getCaptcha(request);
        boolean rememberMe = isRememberMe(request);
        UserType userType = WebUtil.getSessionData("userType");
        return new UserTypeToken(username, password, captchaId, captcha, userType, rememberMe);
    }

    @Override
    protected boolean onAccessDenied(ServletRequest servletRequest, ServletResponse servletResponse) throws Exception {
        if (isLoginRequest(servletRequest, servletResponse)) {
            if (isLoginSubmission(servletRequest, servletResponse)) {
                if (logger.isTraceEnabled()) {
                    logger.trace("Login submission detected.  Attempting to execute login.");
                }
                return executeLogin(servletRequest, servletResponse);
            } else {
                if (logger.isTraceEnabled()) {
                    logger.trace("Login page view.");
                }
                //allow them to see the login page ;)
                return true;
            }
        } else {
            if (logger.isTraceEnabled()) {
                logger.trace("Attempting to access a path which requires authentication.  Forwarding to the " +
                        "Authentication url [" + getLoginUrl() + "]");
            }

            if (WebUtil.isAjaxRequest()) {
                HttpServletResponse response = (HttpServletResponse) servletResponse;
                response.addHeader("loginStatus", "accessDenied");

                WebUtil.responseJson(JsonUtils.toJson(Message.warn("common.login.accessDenied")));
                return false;
            }

            saveRequestAndRedirectToLogin(servletRequest, servletResponse);
            return false;
        }
    }

    @Override
    protected boolean onLoginSuccess(org.apache.shiro.authc.AuthenticationToken token, Subject subject, ServletRequest servletRequest, ServletResponse servletResponse) throws Exception {
        Session session = subject.getSession();
        Map<Object, Object> attributes = new HashMap<Object, Object>();
        Collection<Object> keys = session.getAttributeKeys();
        for (Object key : keys) {
            attributes.put(key, session.getAttribute(key));
        }
        session.stop();
        session = subject.getSession();
        for (Map.Entry<Object, Object> entry : attributes.entrySet()) {
            session.setAttribute(entry.getKey(), entry.getValue());
        }

        if (WebUtil.isAjaxRequest()) {
            ((HttpServletResponse) servletResponse).addHeader("login", "success");
            WebUtil.responseJson(JsonUtils.toJson(Message.success("common.login.success")));
            return false;
        }

        return super.onLoginSuccess(token, subject, servletRequest, servletResponse);
    }

    @Override
    protected boolean onLoginFailure(org.apache.shiro.authc.AuthenticationToken token, AuthenticationException e, ServletRequest request, ServletResponse response) {
        boolean isTrue = super.onLoginFailure(token, e, request, response);
        String error = (String) request.getAttribute(FormAuthenticationFilter.DEFAULT_ERROR_KEY_ATTRIBUTE_NAME);
        String errorIn18 = "";
        String errorInfo = "";
        String errorCode = "";

        if (StringUtils.isNotBlank(error)) {
            if (error.equals("org.apache.shiro.authc.pam.UnsupportedTokenException")) {
                errorIn18 = "common.captcha.invalid";
            } else if (error.equals("org.apache.shiro.authc.UnknownAccountException")) {
                errorIn18 = "common.login.unknownAccount";
            } else if (error.equals("org.apache.shiro.authc.DisabledAccountException")) {
                errorIn18 = "common.login.disabledAccount";
                errorCode = "0000"; // 随便取的代码哦, 在机构的登录页有用到
            } else if (error.equals("org.apache.shiro.authc.LockedAccountException")) {
                errorIn18 = "common.login.lockedAccount";
            } else if (error.equals("org.apache.shiro.authc.IncorrectCredentialsException")) {
                errorIn18 = "common.login.incorrectCredentials";
            } else if (error.equals("org.apache.shiro.authc.AuthenticationException")) {
                errorIn18 = "common.login.authentication";
            }
        }

        if (StringUtils.isNotBlank(errorIn18)) {
            errorInfo = I18Util.getMessage(errorIn18);
        }

        String captchaId = UUID.randomUUID().toString();
        RSAPublicKey publicKey = rsaService.generateKey();
        Map<String, String> modelMap = rsaService.getModulusAndExponent(publicKey);
        modelMap.put("captchaId",captchaId);
        modelMap.put("errorInfo",errorInfo);
        modelMap.put("errorIn18",errorIn18);
        modelMap.put("errorCode", errorCode);

        if (WebUtil.isAjaxRequest()) {
            ((HttpServletResponse) response).addHeader("login", "fail");
            ((HttpServletResponse) response).addHeader("loginFailure", "accessDenied");
            WebUtil.responseJson(JsonUtils.toJson(Message.warn(errorIn18).addResult(modelMap)));
            return false;
        }

        return isTrue;
    }

    @Override
    public boolean onPreHandle(ServletRequest servletRequest, ServletResponse servletResponse, Object mappedValue) throws Exception {
        String userTypePath = WebUtil.getSessionData("userTypePath");
        if (userTypePath == null) {
            String path = getPathWithinApplication(servletRequest);
            for (UserType userType : UserType.values()) {
                if (path.startsWith("/" + userType.getPath())) {
                    userTypePath = userType.getPath();
                    break;
                }
            }
            WebUtil.setSessionData("userTypePath", userTypePath);
        }
        if (userTypePath != null) {
            setLoginUrl(this.loginUrl.replace("{userType}", userTypePath));
        }
        if (userTypePath != null) {
            setSuccessUrl(this.successUrl.replace("{userType}", userTypePath));
        }
        return super.onPreHandle(servletRequest, servletResponse, mappedValue);
    }

    @Override
    protected String getPassword(ServletRequest servletRequest) {
        String password = rsaService.decryptParameter(enPasswordParam);
        rsaService.removePrivateKey();
        return password;
    }

    /**
     * 获取用户类型
     * @param servletRequest servletRequest
     * @return 用户类型
     */
    protected String getUserType(ServletRequest servletRequest) {
        return WebUtils.getCleanParam(servletRequest, "userType");
    }

    /**
     * 获取验证码ID
     * @param servletRequest servletRequest
     * @return 验证码ID
     */
    protected String getCaptchaId(ServletRequest servletRequest) {
        return ((HttpServletRequest) servletRequest).getSession().getId();
    }

    /**
     * 获取验证码
     * @param servletRequest servletRequest
     * @return 验证码
     */
    protected String getCaptcha(ServletRequest servletRequest) {
        return WebUtils.getCleanParam(servletRequest, captchaParam);
    }

    /**
     * 获取"加密密码"参数名称
     * @return "加密密码"参数名称
     */
    public String getEnPasswordParam() {
        return enPasswordParam;
    }

    /**
     * 设置"加密密码"参数名称
     * @param enPasswordParam  "加密密码"参数名称
     */
    public void setEnPasswordParam(String enPasswordParam) {
        this.enPasswordParam = enPasswordParam;
    }

    /**
     * 获取"验证ID"参数名称
     * @return "验证ID"参数名称
     */
    public String getCaptchaIdParam() {
        return captchaIdParam;
    }

    /**
     * 设置"验证ID"参数名称
     * @param captchaIdParam "验证ID"参数名称
     */
    public void setCaptchaIdParam(String captchaIdParam) {
        this.captchaIdParam = captchaIdParam;
    }

    /**
     * 获取"验证码"参数名称
     * @return "验证码"参数名称
     */
    public String getCaptchaParam() {
        return captchaParam;
    }

    /**
     * 设置"验证码"参数名称
     * @param captchaParam "验证码"参数名称
     */
    public void setCaptchaParam(String captchaParam) {
        this.captchaParam = captchaParam;
    }
}
