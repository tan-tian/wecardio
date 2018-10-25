package com.hiteam.common.web.filter;

import com.hiteam.common.web.WebUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * <pre>
 * Description: 设置Web线程变量
 * Author: tantian
 * Version:
 * Since: Ver 1.1
 * Date: 2014-10-30 09:30
 * </pre>
 */
public class SetWebThreadLocalFilter implements Filter {
    private Logger logger = LoggerFactory.getLogger(SetWebThreadLocalFilter.class);

    public void init(FilterConfig config) throws ServletException {

    }

    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse,
                         FilterChain chain) throws IOException, ServletException {

        WebUtil.setResponse((HttpServletResponse) servletResponse);
        WebUtil.setRequest((HttpServletRequest) servletRequest);

        chain.doFilter(servletRequest, servletResponse);
        WebUtil.setResponse((HttpServletResponse) servletResponse);
        WebUtil.setRequest((HttpServletRequest) servletRequest);
    }

    public void destroy() {
    }
}