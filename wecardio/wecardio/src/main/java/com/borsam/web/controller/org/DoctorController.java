package com.borsam.web.controller.org;

import com.borsam.pojo.security.Principal;
import com.borsam.pojo.wf.WfCode;
import com.borsam.repository.entity.doctor.DoctorAccount;
import com.borsam.repository.entity.doctor.DoctorImage;
import com.borsam.repository.entity.doctor.DoctorProfile;
import com.borsam.service.doctor.DoctorAccountService;
import com.borsam.service.doctor.DoctorProfileService;
import com.hiteam.common.base.pojo.search.Filter;
import com.hiteam.common.base.pojo.search.Order;
import com.hiteam.common.base.pojo.search.Page;
import com.hiteam.common.base.pojo.search.Pageable;
import com.hiteam.common.service.enums.EnumService;
import com.hiteam.common.util.pojo.EnumBean;
import com.hiteam.common.web.Message;
import com.hiteam.common.web.controller.BaseController;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang.StringUtils;
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
import java.util.Iterator;
import java.util.List;

/**
 * Controller - 医生管理
 * Created by tantian on 2015/7/13.
 */
@Controller("orgDoctorController")
@RequestMapping("/org/doctor")
public class DoctorController extends BaseController {

    @Resource(name = "doctorProfileServiceImpl")
    private DoctorProfileService doctorProfileService;

    @Resource(name = "doctorAccountServiceImpl")
    private DoctorAccountService doctorAccountService;

    @Resource(name = "enumServiceImpl")
    private EnumService enumService;

    /**
     * 管理首页
     */
    @RequestMapping(method = RequestMethod.GET)
    public String list(String auditState, Model model) {
        model.addAttribute("auditState", auditState);
        return "/org/doctor/list";
    }

    /**
     * 新增页面
     */
    @RequestMapping(value = "/add", method = RequestMethod.GET)
    public String add(Model model) {
        DoctorAccount doctorAccount =  doctorAccountService.getCurrent();
        model.addAttribute("orgType", doctorAccount.getDoctorProfile().getOrg().getType());
        return "/org/doctor/add";
    }

    /**
     * 编辑页面
     */
    @RequestMapping(value = "/{did}/edit", method = RequestMethod.GET)
    public String edit(@PathVariable Long did, Model model) {
        model.addAttribute("doctor", doctorProfileService.find(did));
        DoctorAccount doctorAccount =  doctorAccountService.getCurrent();
        model.addAttribute("orgType", doctorAccount.getDoctorProfile().getOrg().getType());
        return "/org/doctor/edit";
    }

    /**
     * 编辑页面
     */
    @RequestMapping(value = "/{did}/update", method = RequestMethod.GET)
    public String update(@PathVariable Long did, Model model) {
        model.addAttribute("doctor", doctorProfileService.find(did));
        DoctorAccount doctorAccount =  doctorAccountService.getCurrent();
        model.addAttribute("orgType", doctorAccount.getDoctorProfile().getOrg().getType());
        return "/org/doctor/update";
    }

    /**
     * 详情页面
     */
    @RequestMapping(value = "/{did}/view", method = RequestMethod.GET)
    public String view(@PathVariable Long did, Model model) {
        DoctorProfile doctorProfile = doctorProfileService.find(did);
        enumService.transformEnum(doctorProfile);
        model.addAttribute("wfCode", WfCode.DOCTOR);
        model.addAttribute("doctor", doctorProfile);
        return "/org/doctor/view";
    }

    /**
     * 患者转移页面
     */
    @RequestMapping(value = "/change", method = RequestMethod.GET)
    public String change(String from, Model model) {
        model.addAttribute("from", from);
        return "/org/doctor/change";
    }

    /**
     * 分页查询
     */
    @RequestMapping(value = "/page", method = RequestMethod.POST)
    @ResponseBody
    public Page<DoctorProfile> page(String name, Integer[] loginState, Integer[] roles, Integer[] auditState, Pageable pageable) {
        List<Filter> filters = new ArrayList<Filter>();
        filters.add(Filter.ne("roles", 1));         // 不查机构管理员
        filters.add(Filter.eq("isDelete", false));  // 不查删除的

        // 只查当前机构
        DoctorAccount doctorAccount =  doctorAccountService.getCurrent();
        DoctorProfile doctorProfile = doctorAccount.getDoctorProfile();
        if (doctorProfile.getOrg() != null && doctorProfile.getOrg().getId() != 0L) {
            filters.add(Filter.eq("org", doctorProfile.getOrg()));
        } else {
            filters.add(Filter.isNull("org"));
        }

        if (StringUtils.isNotEmpty(name)) {
            filters.add(Filter.like("firstName", name));
        }
        if (loginState != null && loginState.length > 0) {
            filters.add(Filter.in("loginState", loginState));
        }
        if (roles != null && roles.length > 0) {
            filters.add(Filter.in("roles", roles));
        }
        if (auditState != null && auditState.length > 0) {
            filters.add(Filter.in("auditState", auditState));
        }

        pageable.setFilters(filters);
        pageable.setOrderProperty("created");
        pageable.setOrderDirection(Order.Direction.desc);
        Page<DoctorProfile> page = doctorProfileService.findPage(pageable);
        enumService.transformEnum(page.getContent());
        return page;
    }

    /**
     * 检查Email是否存在
     */
    @RequestMapping(value = "/checkEmail", method = RequestMethod.GET)
    @ResponseBody
    public boolean checkEmail(String account) {
        return StringUtils.isNotEmpty(account) && !doctorAccountService.emailExists(account);
    }

    /**
     * 新增医生提交
     */
    @RequestMapping(value = "/create", method = RequestMethod.POST)
    @ResponseBody
    public Message create(DoctorProfile doctorProfile, Date doctorBirthday, String account, String password, Boolean isSubmit) {
        if (StringUtils.isEmpty(account) || doctorAccountService.emailExists(account)) {
            return Message.warn("org.doctor.form.account");
        }
        for (Iterator<DoctorImage> iterator = doctorProfile.getDoctorImages().iterator(); iterator.hasNext();) {
            DoctorImage doctorImage = iterator.next();
            if (doctorImage == null || doctorImage.isEmpty()) {
                iterator.remove();
            }
        }
        if (!isValid(doctorProfile)) {
            return Message.warn("common.message.data");
        }
        if (doctorBirthday != null) {
            doctorProfile.setBirthday(doctorBirthday.getTime() / 1000);
        }
        doctorProfileService.create(doctorProfile, account, password, isSubmit);
        return SUCCESS_MSG;
    }

    /**
     * 编辑医生提交
     */
    @RequestMapping(value = "/reCreate", method = RequestMethod.POST)
    @ResponseBody
    public Message reCreate(DoctorProfile doctorProfile, Date doctorBirthday, String account, String password, Boolean isSubmit) {
        for (Iterator<DoctorImage> iterator = doctorProfile.getDoctorImages().iterator(); iterator.hasNext();) {
            DoctorImage doctorImage = iterator.next();
            if (doctorImage == null || doctorImage.isEmpty()) {
                iterator.remove();
            }
        }
        if (!isValid(doctorProfile)) {
            return Message.error("common.message.data");
        }
        if (doctorBirthday != null) {
            doctorProfile.setBirthday(doctorBirthday.getTime() / 1000);
        }
        doctorProfileService.reCreate(doctorProfile, account, password, isSubmit);
        return SUCCESS_MSG;
    }

    /**
     * 编辑医生提交
     */
    @RequestMapping(value = "/update", method = RequestMethod.POST)
    @ResponseBody
    public Message update(DoctorProfile doctorProfile, String password, String[] roleIds) {
        DoctorProfile old = doctorProfileService.find(doctorProfile.getId());
        DoctorAccount doctorAccount = old.getDoctorAccount();
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

        // 修改密码
        if (StringUtils.isNotEmpty(password) && !doctorAccount.getPassword().equals(password)) {
            doctorAccount.setPassword(DigestUtils.md5Hex(password));
            doctorAccountService.update(doctorAccount);
        }
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
     * 患者转移
     */
    @RequestMapping(value = "/devolve", method = RequestMethod.POST)
    @ResponseBody
    public Message devolve(Long[] from, Long to) {
        if (from == null || from.length == 0) {
            return Message.warn("common.message.doctor.change.from");
        }
        if (to == null) {
            return Message.warn("common.message.doctor.change.to");
        }
        doctorProfileService.devolve(from, to);
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