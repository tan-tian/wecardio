package com.borsam.web.controller.patient;

import com.borsam.pojo.security.Principal;
import com.borsam.pub.UserType;
import com.borsam.repository.entity.doctor.DoctorImage;
import com.borsam.repository.entity.doctor.DoctorProfile;
import com.borsam.repository.entity.patient.PatientActivity;
import com.borsam.repository.entity.patient.PatientDoctorOpinion;
import com.borsam.repository.entity.patient.PatientProfile;
import com.borsam.repository.entity.record.Record;
import com.borsam.service.doctor.DoctorImageService;
import com.borsam.service.doctor.DoctorProfileService;
import com.borsam.service.patient.PatientActivityService;
import com.borsam.service.patient.PatientDoctorOpinionService;
import com.borsam.service.patient.PatientProfileService;
import com.borsam.service.record.RecordService;
import com.hiteam.common.base.pojo.search.Filter;
import com.hiteam.common.base.pojo.search.Order;
import com.hiteam.common.util.collections.CollectionUtil;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authz.annotation.RequiresRoles;
import org.apache.shiro.subject.Subject;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

/**
 * Controller - 患者管理
 * Created by Sebarswee on 2015/7/7.
 */
@Controller("patientPatientController")
@RequestMapping("/patient/patient")
public class PatientController extends com.borsam.web.controller.admin.PatientController {
    @Resource(name = "patientDoctorOpinionServiceImpl")
    private PatientDoctorOpinionService patientDoctorOpinionService;

    @Resource(name = "patientActivityServiceImpl")
    private PatientActivityService patientActivityService;

    @Resource(name = "patientProfileServiceImpl")
    private PatientProfileService patientProfileService;

    @Resource(name = "doctorImageServiceImpl")
    private DoctorImageService doctorImageService;

    @Resource(name = "recordServiceImpl")
    private RecordService recordService;

    /**
     * 获取最新的医嘱
     *
     * @return 医嘱
     */
    @ResponseBody
    @RequiresRoles(value = {"patient"})
    @RequestMapping(value = "/getNewestOpinion", method = RequestMethod.POST)
    public PatientDoctorOpinion getNewestOpinion() {
        List<Filter> filters = new ArrayList<>();
        filters.add(Filter.eq("receive_id.id", getLoginId().getId()));

        List<Order> orders = new ArrayList<>();
        orders.add(Order.desc("created"));

        List<PatientDoctorOpinion> opinions = patientDoctorOpinionService.findList(1, filters, orders);
        return CollectionUtil.getFirst(opinions);
    }

    @RequestMapping(value = "/toDetail",method = RequestMethod.GET)
    public String toDetail(Model model) {

        Principal principal = getLoginId();
        Long id = principal.getId();
        String userType = principal.getUserType().name();

        model.addAttribute("userType", userType);
        model.addAttribute("entity", patientProfileService.find(id));
        return "/admin/patient/detail";
    }

    /**
     * 获取最新的活动记录
     *
     * @return 活动记录
     */
    @ResponseBody
    @RequiresRoles(value = {"patient"})
    @RequestMapping(value = "/getNewestActivity", method = RequestMethod.POST)
    public PatientActivity getNewestActivity() {
        List<Filter> filters = new ArrayList<>();
        filters.add(Filter.eq("uid.id", getLoginId().getId()));
        filters.add(Filter.eq("type", UserType.doctor));

        List<Order> orders = new ArrayList<>();
        orders.add(Order.desc("created"));

        List<PatientActivity> opinions = patientActivityService.findList(1, filters, orders);

        PatientActivity activity = CollectionUtil.getFirst(opinions);

        if (activity != null) {
            DoctorImage doctorImage = doctorImageService.find(activity.getCreate_id());
            activity.setHeadPath(doctorImage == null ? "" : doctorImage.getThumbnail());
        }

        return activity;
    }

    @ResponseBody
    @RequiresRoles(value = {"patient"})
    @RequestMapping(value = "/getCurrent", method = RequestMethod.POST)
    public PatientProfile getCurrent() {
        PatientProfile patientProfile = patientProfileService.find(getLoginId().getId());
        return patientProfile;
    }

    @ResponseBody
    @RequiresRoles(value = {"patient"})
    @RequestMapping(value = "/getNewestRecord", method = RequestMethod.POST)
    public List<Record> getNewestRecord() {
        List<Filter> filters = new ArrayList<>();
        filters.add(Filter.eq("patient.id", getLoginId().getId()));

        List<Order> orders = new ArrayList<>();
        orders.add(Order.desc("created"));

        List<Record> records = recordService.findList(4, filters, orders);
        return records;
    }

    /**
     * 当前登录人
     *
     * @return Long
     */
    public Principal getLoginId() {
        Subject subject = SecurityUtils.getSubject();
        Principal principal = (Principal) subject.getPrincipal();
        return principal;
    }
}
