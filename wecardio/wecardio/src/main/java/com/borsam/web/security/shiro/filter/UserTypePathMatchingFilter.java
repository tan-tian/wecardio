package com.borsam.web.security.shiro.filter;

import com.borsam.pub.UserType;
import com.hiteam.common.web.WebUtil;
import org.apache.shiro.web.filter.PathMatchingFilter;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

/**
 * Filter - url模式过滤
 * Created by Sebarswee on 2015/6/18.
 */
public class UserTypePathMatchingFilter extends PathMatchingFilter {

    @Override
    protected boolean onPreHandle(ServletRequest servletRequest, ServletResponse servletResponse, Object mappedValue) throws Exception {
        String path = getPathWithinApplication(servletRequest);
        String userTypePath = null;

        for (UserType userType : UserType.values()) {
            if (path.startsWith("/" + userType.getPath())) {
                userTypePath = userType.getPath();
                break;
            }
        }
        WebUtil.setSessionData("userTypePath", userTypePath);
        return super.onPreHandle(servletRequest, servletResponse, mappedValue);
    }
}
