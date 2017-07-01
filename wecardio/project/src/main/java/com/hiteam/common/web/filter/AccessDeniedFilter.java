package com.hiteam.common.web.filter;

import javax.servlet.*;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 访问限制过滤器
 */
public class AccessDeniedFilter implements Filter {
	
	private static final String errMsg = "您无权访问该资源！";	// 错误消息

	@Override
	public void destroy() {

	}

	@Override
	public void doFilter(ServletRequest req, ServletResponse resp, FilterChain chain) 
			throws IOException, ServletException {
		HttpServletResponse response = (HttpServletResponse) resp;
		response.addHeader("Power-By", "catt");
	    response.sendError(HttpServletResponse.SC_FORBIDDEN, errMsg);
	}

	@Override
	public void init(FilterConfig config) throws ServletException {

	}

}
