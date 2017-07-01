package com.borsam.web.controller.doctor;

import com.borsam.pojo.security.Principal;
import com.borsam.pojo.wf.WfCode;
import com.borsam.repository.entity.doctor.DoctorProfile;
import com.borsam.service.doctor.DoctorProfileService;
import com.hiteam.common.service.enums.EnumService;
import com.hiteam.common.util.ConfigUtils;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.annotation.Resource;

/**
 * Controller - 医生主页
 * Created by Sebarswee on 2015/6/18.
 */
@Controller("doctorIndexController")
@RequestMapping(value = "/doctor")
public class IndexController {
    @Resource(name = "doctorProfileServiceImpl")
    private DoctorProfileService doctorProfileService;

    @Resource(name = "enumServiceImpl")
    private EnumService enumService;

    /**
     * 主页
     */
    @RequestMapping(method = RequestMethod.GET)
    public String index() {
        return "/doctor/index/main";
    }

    /**
     * 首页
     */
    @RequestMapping(value = "/home", method = RequestMethod.GET)
    public String home(Model model) {

        DoctorProfile doctorProfile = doctorProfileService.find(getPrincipal().getId());

        enumService.transformEnum(doctorProfile);
        
        model.addAttribute("wfCode", WfCode.DOCTOR);
        model.addAttribute("doctor", doctorProfile);

        return "/doctor/index/home";
    }

    private Principal getPrincipal() {
        Subject subject = SecurityUtils.getSubject();
        return (Principal) subject.getPrincipal();
    }

}
