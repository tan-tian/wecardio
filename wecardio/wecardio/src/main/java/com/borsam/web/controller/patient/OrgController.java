package com.borsam.web.controller.patient;

import com.borsam.repository.entity.forum.ForumInfo;
import com.borsam.repository.entity.org.Organization;
import com.borsam.repository.entity.patient.PatientAccount;
import com.borsam.repository.entity.patient.PatientProfile;
import com.borsam.service.forum.ForumInfoService;
import com.borsam.service.org.OrganizationService;
import com.borsam.service.patient.PatientAccountService;
import com.borsam.service.patient.PatientProfileService;
import com.hiteam.common.base.pojo.search.Filter;
import com.hiteam.common.base.pojo.search.Order;
import com.hiteam.common.base.pojo.search.Page;
import com.hiteam.common.base.pojo.search.Pageable;
import com.hiteam.common.util.pojo.EnumBean;
import com.hiteam.common.web.controller.BaseController;
import org.apache.commons.lang.StringUtils;
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
 * Controller - 机构管理
 * Created by tantian on 2015/7/7.
 */
@Controller("patientOrganizationController")
@RequestMapping("/patient/organization")
public class OrgController extends BaseController {

    @Resource(name = "organizationServiceImpl")
    private OrganizationService organizationService;

    @Resource(name = "patientProfileServiceImpl")
    private PatientProfileService patientProfileService;

    @Resource(name = "forumInfoServiceImpl")
    private ForumInfoService forumInfoService;

    @Resource(name = "patientAccountServiceImpl")
    private PatientAccountService patientAccountService;

    /**
     * 机构列表页面
     */
    @RequestMapping(value = "/list", method = RequestMethod.GET)
    public String list() {
        return "/patient/organization/list";
    }

    /**
     * 详情页面
     */
    @RequestMapping(value = "/{oid}/view", method = RequestMethod.GET)
    public String detail(@PathVariable Long oid, Model model) {
        Organization org = organizationService.find(oid);
        model.addAttribute("org", org);
        // 最新评价TOP3
        List<Filter> filters = new ArrayList<Filter>();
        filters.add(Filter.eq("oid", org.getId()));
        List<Order> orders = new ArrayList<Order>();
        orders.add(Order.desc("created"));
        List<ForumInfo> forumInfos = forumInfoService.findList(3, filters, orders);
        model.addAttribute("forumInfos", forumInfos);
        return "/patient/organization/view";
    }

    /**
     * 查询列表
     */
    @RequestMapping(value = "/page", method = RequestMethod.POST)
    @ResponseBody
    public Page<Organization> page(String name,Long pId, Pageable pageable) {
        PatientAccount patientAccount = patientAccountService.getCurrent();
        PatientProfile patient = patientAccount.getPatientProfile();
        List<Filter> filters = new ArrayList<Filter>();

        // VIP患者只能查看自己的机构
        if (patient.getOrg() != null && patient.getOrg().getId() != 0L) {
            filters.add(Filter.eq("id", patient.getOrg().getId()));
        }

        // 只查审核通过
        filters.add(Filter.in("auditState", new Integer[] {2, 3}));

        // 机构名称
        if (StringUtils.isNotEmpty(name)) {
            filters.add(Filter.like("name", name));
        }

        // ??? 这个是什么条件，机构没有patient这个字段的
        if(pId!=null)
        {
            PatientProfile patientProfile =  patientProfileService.find(pId);
            filters.add(Filter.eq("patient", patientProfile));
        }

        pageable.setFilters(filters);
        pageable.setOrderProperty("created");
        pageable.setOrderDirection(Order.Direction.desc);
        return organizationService.findPage(pageable);
    }

    /**
     * 机构列表（for 下拉框）
     */
    @RequestMapping(value = "/sel", method = RequestMethod.POST)
    @ResponseBody
    public List<EnumBean> sel(String name) {
        return organizationService.sel(name);
    }
}
