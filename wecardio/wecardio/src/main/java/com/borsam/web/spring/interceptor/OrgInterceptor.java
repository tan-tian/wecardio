package com.borsam.web.spring.interceptor;

import com.borsam.pojo.wf.WfCode;
import com.borsam.repository.entity.doctor.DoctorAccount;
import com.borsam.repository.entity.org.Organization;
import com.borsam.service.doctor.DoctorAccountService;
import com.borsam.service.org.OrganizationService;
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
public class OrgInterceptor extends HandlerInterceptorAdapter {

    @Resource(name = "doctorAccountServiceImpl")
    private DoctorAccountService doctorAccountService;

    @Resource(name = "organizationServiceImpl")
    private OrganizationService organizationService;

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
        HttpSession session = request.getSession();
        Boolean isOrgAuthenticated = (Boolean) session.getAttribute("ORG_AUTHENTICATED");
        if (isOrgAuthenticated == null || !isOrgAuthenticated) {
            DoctorAccount doctorAccount = doctorAccountService.getCurrent();
            if (doctorAccount != null && doctorAccount.getDoctorProfile() != null
                    && doctorAccount.getDoctorProfile().getOrg() != null
                    && doctorAccount.getDoctorProfile().getOrg().getId() != 0L) {
                Organization org = doctorAccount.getDoctorProfile().getOrg();
                if (org.getAuditState() ==  2) {
                    session.setAttribute("ORG_AUTHENTICATED", true);
                } else {
                    modelAndView.setViewName("/org/organization/view");
                    ModelMap modelMap = modelAndView.getModelMap();
                    modelMap.clear();
                    modelMap.addAttribute("org", org);
                    modelMap.addAttribute("wfCode", WfCode.ORG);
                }
            } else {
                modelAndView.setViewName("/org/organization/index");
            }
        }
    }
}
