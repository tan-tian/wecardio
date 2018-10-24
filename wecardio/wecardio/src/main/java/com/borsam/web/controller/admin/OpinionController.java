package com.borsam.web.controller.admin;

import com.borsam.pojo.security.Principal;
import com.borsam.repository.entity.doctor.DoctorProfile;
import com.borsam.repository.entity.patient.PatientDoctorOpinion;
import com.borsam.service.doctor.DoctorProfileService;
import com.borsam.service.org.OrganizationService;
import com.borsam.service.patient.PatientDoctorOpinionService;
import com.hiteam.common.base.pojo.search.Filter;
import com.hiteam.common.base.pojo.search.Page;
import com.hiteam.common.base.pojo.search.Pageable;
import com.hiteam.common.util.collections.MapUtil;
import com.hiteam.common.util.lang.StringUtil;
import com.hiteam.common.web.controller.BaseController;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * <pre>
 * @Description:
 * @author :tantian
 * @version: Ver 1.0
 * @Date: 2015-10-19 21:02
 * </pre>
 */
@Controller("opinionController")
@RequestMapping("/admin/opinion")
public class OpinionController extends BaseController {
    @Resource(name = "patientDoctorOpinionServiceImpl")
    private PatientDoctorOpinionService patientDoctorOpinionService;
    @Resource(name = "organizationServiceImpl")
    private OrganizationService organizationService;

    @Resource(name = "doctorProfileServiceImpl")
    private DoctorProfileService doctorProfileService;

    /**
     * 查询页面
     */
    @RequestMapping(value = "/list", method = RequestMethod.GET)
    public String list() {
        return "/admin/opinion/list";
    }

    /**
     * 分页查询
     */
    @RequestMapping(value = "/query", method = RequestMethod.POST)
    @ResponseBody
    public Page<PatientDoctorOpinion> query(@RequestParam Map params) {
        String content = MapUtil.getString(params,"content","");
        Long doctor = MapUtil.getLong(params, "doctor");
        Long org = MapUtil.getLong(params, "org");
        int pageNo = MapUtil.getIntValue(params, "pageNo", 0);
        Long patientId = null;

        Principal principal = getLoginId();

        //根据医生、机构角色获取机构对象
        switch (principal.getUserType()) {
            case doctor:
                DoctorProfile doctorProfile1 = doctorProfileService.find(principal.getId());
                doctor = doctorProfile1.getId();
            case org:
                DoctorProfile doctorProfile2 = doctorProfileService.find(principal.getId());
                org = doctorProfile2.getOrg().getId();
                break;
            case patient:
                patientId = principal.getId();
                break;
            default:
                break;
        }

        List<Filter> filters = new ArrayList<>();

        if (StringUtil.isNotBlank(content)) {
            filters.add(Filter.like("content", content));
        }

        if (org != null) {
            filters.add(Filter.eq("send_id.org", org));
        }

        if (doctor != null) {
            filters.add(Filter.eq("send_id", doctor));
        }

        if (patientId != null) {
            filters.add(Filter.eq("receive_id.id", patientId));
        }

        Pageable pageable = new Pageable();
        pageable.setPageNo(pageNo);
        pageable.setPageSize(10);
        pageable.setFilters(filters);
        return patientDoctorOpinionService.findPage(pageable);
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
