package com.borsam.web.spring.interceptor;

import com.borsam.pojo.wf.WfCode;
import com.borsam.repository.entity.doctor.DoctorAccount;
import com.borsam.repository.entity.doctor.DoctorProfile;
import com.borsam.service.doctor.DoctorAccountService;
import com.hiteam.common.service.enums.EnumService;
import org.springframework.ui.ModelMap;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * Interceptor - 机构拦截器
 * Created by Sebarswee on 2015/8/21.
 */
public class DoctorInterceptor extends HandlerInterceptorAdapter {

    @Resource(name = "doctorAccountServiceImpl")
    private DoctorAccountService doctorAccountService;

    @Resource(name = "enumServiceImpl")
    private EnumService enumService;

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
        HttpSession session = request.getSession();
        Boolean isOrgAuthenticated = (Boolean) session.getAttribute("DOCTOR_AUTHENTICATED");
        if (isOrgAuthenticated == null || !isOrgAuthenticated) {
            DoctorAccount doctorAccount = doctorAccountService.getCurrent();
            DoctorProfile doctorProfile = doctorAccount.getDoctorProfile();
            if (doctorProfile != null) {
                if (doctorProfile.getAuditState() ==  2) {
                    session.setAttribute("DOCTOR_AUTHENTICATED", true);
                } else {
                    modelAndView.setViewName("/doctor/index/home");
                    ModelMap modelMap = modelAndView.getModelMap();
                    modelMap.clear();
                    enumService.transformEnum(doctorProfile);
                    modelMap.addAttribute("doctor", doctorProfile);
                    modelMap.addAttribute("wfCode", WfCode.DOCTOR);
                }
            } else {
                modelAndView.setViewName("/doctor/index/home");
            }
        }
    }
}
