package com.borsam.web.controller.patient;

import com.borsam.pojo.security.Principal;
import com.borsam.pojo.wf.WfCode;
import com.borsam.repository.entity.doctor.DoctorProfile;
import com.borsam.repository.entity.patient.PatientProfile;
import com.borsam.service.doctor.DoctorProfileService;
import com.borsam.service.patient.PatientProfileService;
import com.hiteam.common.base.pojo.search.Filter;
import com.hiteam.common.base.pojo.search.Order;
import com.hiteam.common.base.pojo.search.Page;
import com.hiteam.common.base.pojo.search.Pageable;
import com.hiteam.common.service.enums.EnumService;
import com.hiteam.common.util.pojo.EnumBean;
import com.hiteam.common.web.controller.BaseController;
import org.apache.commons.lang.StringUtils;
import org.apache.shiro.SecurityUtils;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

/**
 * Controller - 医生管理
 * Created by tantian on 2015/7/15.
 */
@Controller("patientDoctorController")
@RequestMapping(value = "/patient/doctor")
public class DoctorController extends BaseController {

    @Resource(name = "doctorProfileServiceImpl")
    private DoctorProfileService doctorProfileService;

    @Resource(name = "enumServiceImpl")
    private EnumService enumService;

    @Resource(name = "patientProfileServiceImpl")
    private PatientProfileService patientProfileService;

    /**
     * 列表页面
     */
    @RequestMapping(value = "/list", method = RequestMethod.GET)
    public String list(Model model) {
        PatientProfile patient = patientProfileService.find(((Principal) SecurityUtils.getSubject().getPrincipal()).getId());
        if (patient.getOrg() != null && patient.getOrg().getId() != 0L) {
            model.addAttribute("isVip", true);
        } else {
            model.addAttribute("isVip", false);
        }
        if (patient.getDoctor()!=null)
        model.addAttribute("doid",patient.getDoctor().getId());
        boolean bind=false;
        if (patient.getBindType().ordinal()==0){
        	bind=true;
        }
        model.addAttribute("bindType", bind);
        return "/patient/doctor/list";
    }

    /**
     * 详情页面
     */
    @RequestMapping(value = "/{did}/view", method = RequestMethod.GET)
    public String detail(@PathVariable Long did, Model model) {
        DoctorProfile doctorProfile = doctorProfileService.find(did);
        enumService.transformEnum(doctorProfile);
        model.addAttribute("wfCode", WfCode.DOCTOR);
        model.addAttribute("doctor", doctorProfile);
        return "/patient/doctor/view";
    }

    /**
     * 分页查询
     */
    @RequestMapping(value = "/page", method = RequestMethod.POST)
    @ResponseBody
    public Page<DoctorProfile> page(String name, Integer[] loginState, Integer[] roles, Long[] org, Long pId, Pageable pageable) {
        List<Filter> filters = new ArrayList<Filter>();
        filters.add(Filter.ne("roles", 1));         // 不查机构管理员
        filters.add(Filter.eq("isDelete", false));  // 不查删除的
        // 只查审核通过
        filters.add(Filter.eq("auditState", 2));

        // VIP患者只显示绑定机构的医生
        PatientProfile patient = patientProfileService.find(((Principal) SecurityUtils.getSubject().getPrincipal()).getId());
        if (patient.getOrg() != null && patient.getOrg().getId() != 0L) {
            filters.add(Filter.eq("org", patient.getOrg()));
        }
        
        //如果机构创建的患者，绑定后只能看到绑定的主治医生
//        if (patient.getDoctor() != null && patient.getDoctor().getId() != 0L &&
//        		patient.getBindType().ordinal() ==0 ) {
//            filters.add(Filter.eq("patient", patient));
//        }
        if (StringUtils.isNotEmpty(name)) {
            filters.add(Filter.like("firstName", name));
        }
        if (loginState != null && loginState.length > 0) {
            filters.add(Filter.in("loginState", loginState));
        }
        if (roles != null && roles.length > 0) {
            filters.add(Filter.in("roles", roles));
        }
        if (org != null && org.length > 0) {
            filters.add(Filter.in("org", org));
        }
        
        if (pId != null) {
            PatientProfile patientProfile =  patientProfileService.find(pId);
            filters.add(Filter.eq("patient", patientProfile));
        }

        pageable.setFilters(filters);
        pageable.setOrderProperty("created");
        pageable.setOrderDirection(Order.Direction.desc);
        Page<DoctorProfile> page = doctorProfileService.findPage(pageable);
        enumService.transformEnum(page.getContent());
        return page;
    }

    /**
     * 医生列表（for 下拉框）
     */
    @RequestMapping(value = "/sel", method = RequestMethod.POST)
    @ResponseBody
    public List<EnumBean> sel(String name,Long iOrgId,Integer iType) {
        return doctorProfileService.sel(name,iOrgId,iType);
    }
}
