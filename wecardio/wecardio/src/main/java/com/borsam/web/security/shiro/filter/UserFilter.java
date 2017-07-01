package com.borsam.web.security.shiro.filter;

import com.borsam.pojo.security.Principal;
import com.borsam.pub.UserType;
import com.hiteam.common.util.json.JsonUtils;
import com.hiteam.common.web.Message;
import com.hiteam.common.web.WebUtil;
import org.apache.shiro.subject.Subject;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletResponse;

/**
 * Filter - 用户
 * Created by Sebarswee on 2015/6/18.
 */
public class UserFilter extends org.apache.shiro.web.filter.authc.UserFilter {

    private boolean set = false;
    private String loginUrl;

    @Override
    public boolean onPreHandle(ServletRequest request, ServletResponse response, Object mappedValue) throws Exception {
        if (!set) {
            loginUrl = getLoginUrl();
            set = true;
        }

        String path = getPathWithinApplication(request);
        String userTypePath = null;
        for (UserType userType : UserType.values()) {
            if (path.startsWith("/" + userType.getPath())) {
                userTypePath = userType.getPath();
                break;
            }
        }
        if (userTypePath != null) {
            setLoginUrl(this.loginUrl.replace("{userType}", userTypePath));
        }

        return super.onPreHandle(request, response, mappedValue);
    }

    @Override
    protected boolean isAccessAllowed(ServletRequest request, ServletResponse response, Object mappedValue) {
        if (super.isAccessAllowed(request, response, mappedValue)) {
            /**
             * 判断访问地址与当前用户的类型是否一致
             */
            Subject subject = getSubject(request, response);
            String path = getPathWithinApplication(request);

            if (subject !=  null && subject.getPrincipal() != null) {
                Principal principal = (Principal) subject.getPrincipal();
                String userTypePath = principal.getUserType().getPath();

                // 当访问地址是定义好的角色中的一种，但与当前角色不一致时，返回false
                for (UserType userType : UserType.values()) {
                    if (path.startsWith("/" + userType.getPath()) && !path.startsWith("/" + userTypePath)) {
                        WebUtil.setSessionData("userTypePath", userType.getPath());
                        subject.logout();
                        return false;
                    }
                }
            }
            return true;
        }
//        WebUtil.setSessionData("userTypePath", null);
        return false;
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
        return super.onAccessDenied(request, reps);
    }
}
