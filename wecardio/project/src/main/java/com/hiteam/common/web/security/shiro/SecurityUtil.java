package com.hiteam.common.web.security.shiro;

import com.hiteam.common.service.RSAService;
import com.hiteam.common.util.spring.SpringUtils;
import com.hiteam.common.web.security.shiro.token.ExtAuthenticationToken;
import org.apache.shiro.SecurityUtils;

import javax.servlet.http.HttpServletRequest;

/**
 * <pre>
 * Description:
 * Author: Zhang zhongtao
 * Version:
 * Since: Ver 1.1
 * Date: 2014-12-01 05:22
 * </pre>
 */
public class SecurityUtil extends SecurityUtils {
    /**
     * 代码级别的自动登陆（此方法会设置cookie自动登陆）
     *
     * @param account 登陆账号
     * @param pwd     密码，未加密
     * @param request request
     */
    public static void autoLogigForCode(String account, String pwd, HttpServletRequest request) {
        ExtAuthenticationToken token = new ExtAuthenticationToken(account, pwd, null, null, true, request.getRemoteHost(),
                request.getHeader("USER-AGENT"), true);
        SecurityUtils.getSubject().login(token);
    }

    /**
     * 代码级别的自动登陆（此方法会设置cookie自动登陆）
     *
     * @param account 登陆账号
     * @param rasPwd     加密的密码
     * @param request request
     */
    public static void autoLogigRSAForCode(String account, String rasPwd, HttpServletRequest request) {
        RSAService rsaService = SpringUtils.getBean("rsaServiceImpl");
        String pwd = rsaService.decryptParameter("password");
        ExtAuthenticationToken token = new ExtAuthenticationToken(account, pwd, null, null, true, request.getRemoteHost(),
                request.getHeader("USER-AGENT"), true);
        SecurityUtils.getSubject().login(token);
    }
}
