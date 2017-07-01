package com.hiteam.common.web.spring.interceptor;

import com.hiteam.common.web.WebUtil;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.UUID;

/**
 * 令牌
 */
public class TokenInterceptor extends HandlerInterceptorAdapter {

	/**
	 * 令牌属性名称
	 */
	private static final String TOKEN_ATTRIBUTE_NAME = "token";
	
	/**
	 * 令牌Cookie名称
	 */
	private static final String TOKEN_COOKIE_NAME = "token";
	
	/**
	 * 令牌参数名称
	 */
	private static final String TOKEN_PARAMETER_NAME = "token";
	
	/**
	 * 错误消息
	 */
	private static final String ERROR_MSG = "Bad or missing token!";

	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
		String token = WebUtil.getCookieVal(TOKEN_COOKIE_NAME);

		if (request.getMethod().equalsIgnoreCase("POST")) {
			if (WebUtil.isAjaxRequest()) {
				if (token != null && token.equals(request.getHeader(TOKEN_PARAMETER_NAME))) {
					return true;
				} else {
					response.addHeader("tokenStatus", "accessDenied");
				}
			} else {
				if (token != null && token.equals(request.getParameter(TOKEN_PARAMETER_NAME))) {
					return true;
				}
			}
			if (token == null) {
				token = UUID.randomUUID().toString();
				WebUtil.addCookie(TOKEN_COOKIE_NAME, token);
			}
			response.sendError(HttpServletResponse.SC_FORBIDDEN, ERROR_MSG);
			return false;
		}
		if (token == null) {
			token = UUID.randomUUID().toString();
			WebUtil.addCookie(TOKEN_COOKIE_NAME, token);
		}
		request.setAttribute(TOKEN_ATTRIBUTE_NAME, token);
		return true;
	}
}
