package com.hiteam.common.web.security.shiro;

import org.apache.shiro.authc.AuthenticationException;

/**
 * <pre>
 * Description:
 * Author: tantian
 * Version:
 * Since: Ver 1.1
 * Date: 2015-01-21 10:01
 * </pre>
 */
public class AjaxException extends AuthenticationException {
    public static final String AJAX_EXCEPTION = "com.catt.common.web.security.shiro.AjaxException";
}
