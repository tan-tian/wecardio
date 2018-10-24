package com.borsam.web.controller.admin;

import com.borsam.pojo.security.Principal;
import com.borsam.pojo.wf.WfCode;
import com.borsam.pub.UserType;
import com.borsam.repository.entity.admin.Admin;
import com.borsam.repository.entity.doctor.DoctorProfile;
import com.borsam.service.admin.AdminService;
import com.borsam.service.doctor.DoctorProfileService;
import com.borsam.service.wf.QueryService;
import com.hiteam.common.base.pojo.search.Filter;
import com.hiteam.common.base.pojo.search.Order;
import com.hiteam.common.base.pojo.search.Page;
import com.hiteam.common.base.pojo.search.Pageable;
import com.hiteam.common.service.enums.EnumService;
import com.hiteam.common.util.pojo.EnumBean;
import com.hiteam.common.web.Message;
import com.hiteam.common.web.controller.BaseController;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.time.DateUtils;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Controller - 医生管理
 * Created by tantian on 2015/7/15.
 */
@Controller("adminDoctorController")
@RequestMapping(value = "/admin/doctor")
public class DoctorController extends BaseController {

    @Resource(name = "doctorProfileServiceImpl")
    private DoctorProfileService doctorProfileService;

    @Resource(name = "adminServiceImpl")
    private AdminService adminService;

    @Resource(name = "enumServiceImpl")
    private EnumService enumService;

    @Resource(name = "queryServiceImpl")
    private QueryService queryService;

    /**
     * 管理首页
     */
    @RequestMapping(method = RequestMethod.GET)
    public String list() {
        return "/admin/doctor/list";
    }

    /**
     * 详情页面（非流程）
     */
    @RequestMapping(value = "/{did}/detail", method = RequestMethod.GET)
    public String detail(@PathVariable Long did, Model model) {
        DoctorProfile doctorProfile = doctorProfileService.find(did);
        enumService.transformEnum(doctorProfile);
        model.addAttribute("wfCode", WfCode.DOCTOR);
        model.addAttribute("doctor", doctorProfile);
        return "/admin/doctor/detail";
    }

    /**
     * 编辑页面
     */
    @RequestMapping(value = "/{did}/update", method = RequestMethod.GET)
    public String update(@PathVariable Long did, Model model) {
        DoctorProfile doctorProfile = doctorProfileService.find(did);
        enumService.transformEnum(doctorProfile);
        model.addAttribute("doctor", doctorProfile);
        return "/admin/doctor/update";
    }

    /**
     * 待办页面
     */
    @RequestMapping(value = "/todo", method = RequestMethod.GET)
    public String todo() {
        return "/admin/doctor/todo";
    }

    /**
     * 详情页面（流程）
     */
    @RequestMapping(value = "/{did}/view", method = RequestMethod.GET)
    public String view(@PathVariable Long did, Model model) {
        DoctorProfile doctorProfile = doctorProfileService.find(did);
        enumService.transformEnum(doctorProfile);
        model.addAttribute("wfCode", WfCode.DOCTOR);
        model.addAttribute("doctor", doctorProfile);
        return "/admin/doctor/view";
    }

    /**
     * 初审页面
     */
    @RequestMapping(value = "/{did}/audit", method = RequestMethod.GET)
    public String audit(@PathVariable Long did, Model model) {
        model.addAttribute("did", did);
        return "/admin/doctor/audit";
    }

    /**
     * 终审页面
     */
    @RequestMapping(value = "/{did}/confirm", method = RequestMethod.GET)
    public String confirm(@PathVariable Long did, Model model) {
        model.addAttribute("did", did);
        return "/admin/doctor/confirm";
    }

    /**
     * 分页查询
     */
    @RequestMapping(value = "/page", method = RequestMethod.POST)
    @ResponseBody
    public Page<DoctorProfile> page(String name, Integer[] loginState, Integer[] roles, Long[] org, Pageable pageable) {
        List<Filter> filters = new ArrayList<Filter>();
        filters.add(Filter.ne("roles", 1));         // 不查机构管理员
        filters.add(Filter.eq("isDelete", false));  // 不查删除的
        // 只查审核通过
        filters.add(Filter.eq("auditState", 2));

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
        pageable.setFilters(filters);
        pageable.setOrderProperty("created");
        pageable.setOrderDirection(Order.Direction.desc);
        Page<DoctorProfile> page = doctorProfileService.findPage(pageable);
        enumService.transformEnum(page.getContent());
        return page;
    }

    /**
     * 待办列表
     */
    @RequestMapping(value = "/todoList", method = RequestMethod.POST)
    @ResponseBody
    public Page<DoctorProfile> todoList(String name, Date startDate, Date endDate, Pageable pageable) {
        Admin admin = adminService.getCurrent();
        List<Long> guidList = queryService.queryTodoGuids(WfCode.DOCTOR, UserType.admin, admin.getId());

        List<Filter> filters = new ArrayList<Filter>();

        if (guidList != null && !guidList.isEmpty()) {
            filters.add(Filter.in("guid", guidList.toArray()));
        } else {
            return new Page<DoctorProfile>(null, 0, pageable);
        }
        filters.add(Filter.eq("auditState", 1));    // 只查待审核
        // 名称
        if (StringUtils.isNotEmpty(name)) {
            filters.add(Filter.like("firstName", name));
        }
        // 提交时间
        if (startDate != null) {
            filters.add(Filter.ge("created", startDate.getTime() / 1000));
        }
        if (endDate != null) {
            filters.add(Filter.le("created", DateUtils.addDays(endDate, 1).getTime() / 1000));
        }
        pageable.setFilters(filters);
        pageable.setOrderProperty("created");
        pageable.setOrderDirection(Order.Direction.desc);
        return doctorProfileService.findPage(pageable);
    }

    /**
     * 审核
     */
    @RequestMapping(value = "/audit", method = RequestMethod.POST)
    @ResponseBody
    public Message audit(Long did, Boolean isPass, String remark) {
        if (did == null) {
            return ERROR_MSG;
        }
        if (isPass == null) {
            return Message.warn("admin.doctor.message.isPass");
        }
        doctorProfileService.audit(did, isPass, remark);
        return SUCCESS_MSG;
    }

    /**
     * 删除
     */
    @RequestMapping(value = "/delete", method = RequestMethod.POST)
    @ResponseBody
    public Message delete(Long[] did) {
        if (did == null || did.length == 0) {
            return Message.warn("common.message.delete.select");
        }
        List<DoctorProfile> doctorProfiles = doctorProfileService.findList(did);
        for (DoctorProfile doctorProfile : doctorProfiles) {
            if (doctorProfile.getPatientNum() != null && doctorProfile.getPatientNum() != 0) {
                return Message.warn("common.message.doctor.patientNum");
            }
        }
        doctorProfileService.disable(did);
        return SUCCESS_MSG;
    }

    /**
     * 编辑医生提交
     */
    @RequestMapping(value = "/update", method = RequestMethod.POST)
    @ResponseBody
    public Message update(DoctorProfile doctorProfile, String[] roleIds) {
        DoctorProfile old = doctorProfileService.find(doctorProfile.getId());
        old.setSex(doctorProfile.getSex());
        old.setRoles(doctorProfile.getRoles());
        old.setMobile(doctorProfile.getMobile());
        old.setEmail(doctorProfile.getEmail());
        old.setAddress(doctorProfile.getAddress());
        old.setIntro(doctorProfile.getIntro());
        old.setNationCode(doctorProfile.getNationCode());
        old.setNationName(doctorProfile.getNationName());
        old.setProvinceCode(doctorProfile.getProvinceCode());
        old.setProvinceName(doctorProfile.getProvinceName());
        old.setCityCode(doctorProfile.getCityCode());
        old.setCityName(doctorProfile.getCityName());

        if (roleIds != null) {
            Integer roles = 0;
            for (String roleId : roleIds) {
                roles += Integer.parseInt(roleId);
            }
            old.setRoles(roles);
        }

        doctorProfileService.update(old);
        return SUCCESS_MSG;
    }

    /**
     * 机构列表（for 下拉框）
     */
    @RequestMapping(value = "/sel", method = RequestMethod.POST)
    @ResponseBody
    public List<EnumBean> sel(String name,Long iOrgId,Integer iType) {

        Principal principal = getLoginId();

        //根据医生、机构角色获取机构对象
        switch (principal.getUserType()) {
            case doctor:
            	DoctorProfile doctorProfile0= doctorProfileService.find(principal.getId());
                if (doctorProfile0 != null && doctorProfile0.getOrg() != null) {
                    iOrgId = doctorProfile0.getOrg().getId();
                }
                break;
            case org:
                DoctorProfile doctorProfile2 = doctorProfileService.find(principal.getId());
                if (doctorProfile2 != null && doctorProfile2.getOrg() != null) {
                    iOrgId = doctorProfile2.getOrg().getId();
                }
                break;
            default:
                break;
        }

        return doctorProfileService.sel(name,iOrgId,iType);
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
