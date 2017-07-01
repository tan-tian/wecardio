package com.hiteam.common.web.security.shiro.filter;

import com.hiteam.common.service.RSAService;
import com.hiteam.common.util.json.JsonUtils;
import com.hiteam.common.web.I18Util;
import com.hiteam.common.web.Message;
import com.hiteam.common.web.WebUtil;
import com.hiteam.common.web.security.shiro.token.ExtAuthenticationToken;
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
import java.util.Map.Entry;
import java.util.UUID;

/**
 * 权限认证过滤器
 */
public class ExtAuthenticationFilter extends FormAuthenticationFilter {
    private Logger logger = LoggerFactory.getLogger(ExtAuthenticationFilter.class);
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

    @Resource(name = "rsaServiceImpl")
    private RSAService rsaService;

    @Override
    protected boolean executeLogin(ServletRequest request, ServletResponse response) throws Exception {
        return super.executeLogin(request, response);
    }

    @Override
    protected boolean isLoginRequest(ServletRequest request, ServletResponse response) {
        return super.isLoginRequest(request, response);
    }

    @Override
    protected AuthenticationToken createToken(ServletRequest req, ServletResponse resp) {
        String username = getUsername(req);
        String password = getPassword(req);
        String captchaId = getCaptchaId(req);
        String captcha = getCaptcha(req);
        boolean rememberMe = isRememberMe(req);
        String host = getHost(req);
        String sBrowserType = req.getParameter("sBrowserType");
        return new ExtAuthenticationToken(username, password, captchaId, captcha, rememberMe, host, sBrowserType);
    }

    @Override
    protected boolean isAccessAllowed(ServletRequest request, ServletResponse response, Object mappedValue) {
        return super.isAccessAllowed(request,response,mappedValue);
    }

    @Override
    protected void issueSuccessRedirect(ServletRequest request, ServletResponse response) throws Exception {
        if(WebUtil.isAjaxRequest()){
            WebUtil.responseJson(JsonUtils.toJson(Message.success("登陆成功")));
            return;
        }
        super.issueSuccessRedirect(request, response);
    }

    @Override
    protected boolean onAccessDenied(ServletRequest req, ServletResponse resp) throws Exception {

        if (isLoginRequest(req, resp)) {
            if (isLoginSubmission(req, resp)) {
                if (logger.isTraceEnabled()) {
                    logger.trace("Login submission detected.  Attempting to execute login.");
                }
                return executeLogin(req, resp);
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

            if(WebUtil.isAjaxRequest()){
                HttpServletResponse response = (HttpServletResponse)resp ;
                response.addHeader("loginStatus", "accessDenied");
//                response.sendError(HttpServletResponse.SC_FORBIDDEN);

                WebUtil.responseJson(JsonUtils.toJson(new Message(Message.Type.warn,"登陆超时")));
                return false;
            }

            saveRequestAndRedirectToLogin(req, resp);
            return false;
        }
    }

    @Override
    protected boolean onLoginSuccess(AuthenticationToken token, Subject subject,
                                     ServletRequest req, ServletResponse resp) throws Exception {
        Session session = subject.getSession();
        HashMap<Object, Object> attributes = new HashMap<Object, Object>();
        Collection<Object> keys = session.getAttributeKeys();

        for (Object key : keys) {
            attributes.put(key, session.getAttribute(key));
        }

        session.stop();
        session = subject.getSession();

        for (Entry<Object, Object> entry : attributes.entrySet()) {
            session.setAttribute(entry.getKey(), entry.getValue());
        }

        if (WebUtil.isAjaxRequest()) {
            ((HttpServletResponse)resp).addHeader("login", "success");
            WebUtil.responseJson(JsonUtils.toJson(new Message(Message.Type.success,"登陆成功")));
            return false;
        }

        return super.onLoginSuccess(token, subject, req, resp);

    }

    @Override
    protected boolean onLoginFailure(AuthenticationToken token, AuthenticationException e, ServletRequest request, ServletResponse response) {
        boolean isTrue = super.onLoginFailure(token, e, request, response);
        String error = (String)request.getAttribute(FormAuthenticationFilter.DEFAULT_ERROR_KEY_ATTRIBUTE_NAME);
        String errorIn18 = "";
        String errorInfo = "";

        if (StringUtils.isNotBlank(error)) {
            if (error.equals("org.apache.shiro.authc.pam.UnsupportedTokenException")) {
                errorIn18 = "common.captcha.invalid";
            } else if (error.equals("org.apache.shiro.authc.UnknownAccountException")) {
                errorIn18 = "common.login.unknownAccount";
            } else if (error.equals("org.apache.shiro.authc.DisabledAccountException")) {
                errorIn18 = "common.login.disabledAccount";
            } else if (error.equals("org.apache.shiro.authc.LockedAccountException")) {
                errorIn18 = "common.login.lockedAccount";
            } else if (error.equals("org.apache.shiro.authc.IncorrectCredentialsException")) {
                errorIn18 = "common.login.incorrectCredentials";
            } else if (error.equals("org.apache.shiro.authc.AuthenticationException")) {
                errorIn18 = "common.login.authentication";
            }
        }

        if(StringUtils.isNotBlank(errorIn18)){
            errorInfo = I18Util.getMessage(errorIn18, new Object[0]);
        }

        String captchaId = UUID.randomUUID().toString();
        RSAPublicKey publicKey = rsaService.generateKey();
        Map modelMap = rsaService.getModulusAndExponent(publicKey);
        modelMap.put("captchaId",captchaId);
        modelMap.put("errorInfo",errorInfo);
        modelMap.put("errorIn18",errorIn18);

        if (WebUtil.isAjaxRequest()) {
            ((HttpServletResponse)response).addHeader("login", "fail");
            ((HttpServletResponse)response).addHeader("loginFailure", "accessDenied");
            WebUtil.responseJson(JsonUtils.toJson(new Message(Message.Type.warn,errorIn18,modelMap)));
            return false;
        }

        return isTrue;
    }

    @Override
    protected String getPassword(ServletRequest req) {
        HttpServletRequest request = (HttpServletRequest) req;
        String password = rsaService.decryptParameter(this.enPasswordParam);
        rsaService.removePrivateKey();
        return password;
    }

    /**
     * 获取验证ID
     *
     * @param req
     * @return
     */
    protected String getCaptchaId(ServletRequest req) {
        String captchaId = ((HttpServletRequest) req).getSession().getId();
//        if (captchaId == null) {
//            captchaId = ((HttpServletRequest) req).getSession().getId();
//        }
        return captchaId;
    }

    /**
     * 获取验证码
     *
     * @param req
     * @return
     */
    protected String getCaptcha(ServletRequest req) {
        return WebUtils.getCleanParam(req, this.captchaParam);
    }

    public String getEnPasswordParam() {
        return this.enPasswordParam;
    }

    public void setEnPasswordParam(String enPasswordParam) {
        this.enPasswordParam = enPasswordParam;
    }

    public String getCaptchaIdParam() {
        return this.captchaIdParam;
    }

    public void setCaptchaIdParam(String captchaIdParam) {
        this.captchaIdParam = captchaIdParam;
    }

    public String getCaptchaParam() {
        return this.captchaParam;
    }

    public void setCaptchaParam(String captchaParam) {
        this.captchaParam = captchaParam;
    }
}
