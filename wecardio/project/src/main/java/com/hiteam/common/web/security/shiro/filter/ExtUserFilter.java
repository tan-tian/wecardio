package com.hiteam.common.web.security.shiro.filter;

import com.hiteam.common.util.json.JsonUtils;
import com.hiteam.common.web.Message;
import com.hiteam.common.web.WebUtil;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.web.filter.authc.UserFilter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletResponse;

/**
 * <pre>
 * Description:
 * Author: Zhang zhongtao
 * Version:
 * Since: Ver 1.1
 * Date: 2014-11-17 17:13
 * </pre>
 */
public class ExtUserFilter extends UserFilter {
    private Logger logger = LoggerFactory.getLogger(ExtUserFilter.class);

    @Override
    protected boolean isAccessAllowed(ServletRequest request, ServletResponse response, Object mappedValue) {
        boolean isTrue = super.isAccessAllowed(request, response, mappedValue);

        Subject subject = getSubject(request, response);

        return isTrue;
    }

    @Override
    protected boolean onAccessDenied(ServletRequest request, ServletResponse reps) throws Exception {
        if (WebUtil.isAjaxRequest()) {
            HttpServletResponse response = (HttpServletResponse) reps;
            response.addHeader("loginStatus", "accessDenied");
//            response.sendError(HttpServletResponse.SC_FORBIDDEN);
            WebUtil.responseJson(JsonUtils.toJson(Message.warn("登陆超时")));
            return false;
        }
        boolean isTrue = super.onAccessDenied(request, reps);
        return isTrue;
    }
}
