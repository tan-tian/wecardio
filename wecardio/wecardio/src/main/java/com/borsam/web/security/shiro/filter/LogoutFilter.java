package com.borsam.web.security.shiro.filter;

import com.borsam.pojo.security.Principal;
import com.borsam.pub.UserType;
import com.borsam.repository.entity.doctor.DoctorAccount;
import com.borsam.repository.entity.doctor.DoctorProfile;
import com.borsam.service.doctor.DoctorAccountService;
import com.borsam.service.doctor.DoctorProfileService;
import com.hiteam.common.web.WebUtil;
import org.apache.shiro.subject.Subject;

import javax.annotation.Resource;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;

/**
 * Filter - 登出
 * Created by tantian on 2015/6/18.
 */
public class LogoutFilter extends org.apache.shiro.web.filter.authc.LogoutFilter {

    @Resource(name = "doctorAccountServiceImpl")
    private DoctorAccountService doctorAccountService;

    @Resource(name = "doctorProfileServiceImpl")
    private DoctorProfileService doctorProfileService;

    @Override
    protected boolean preHandle(ServletRequest servletRequest, ServletResponse servletResponse) throws Exception {
        Subject subject = getSubject(servletRequest, servletResponse);
        if (subject !=  null && subject.getPrincipal() != null) {
            Principal principal = (Principal) subject.getPrincipal();
            // 登录角色为医生，登出时修改医生登录状态为离线
            if (UserType.doctor.equals(principal.getUserType())) {
                DoctorAccount doctorAccount = doctorAccountService.findByUsername(principal.getUsername());
                DoctorProfile doctorProfile = doctorAccount.getDoctorProfile();
                if (doctorProfile != null) {
                    doctorProfile.setLoginState(DoctorProfile.LoginState.offLine);
                    doctorProfileService.update(doctorProfile);
                }
            }
            String userTypePath = principal.getUserType().getPath();
            WebUtil.setSessionData("userTypePath", userTypePath);
            setRedirectUrl("/" + userTypePath + "/login");
        } else {
            HttpServletRequest request = (HttpServletRequest) servletRequest;
            String contextPath = request.getContextPath();
            String path = request.getRequestURI();
            String userTypePath = null;

            for (UserType userType : UserType.values()) {
                if (path.startsWith(contextPath + "/" + userType.getPath())) {
                    userTypePath = userType.getPath();
                    break;
                }
            }
            WebUtil.setSessionData("userTypePath", userTypePath);
            setRedirectUrl("/" + userTypePath + "/login");
        }
        return super.preHandle(servletRequest, servletResponse);
    }
}
