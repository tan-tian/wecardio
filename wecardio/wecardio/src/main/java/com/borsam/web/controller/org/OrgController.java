package com.borsam.web.controller.org;

import com.borsam.pojo.wf.WfCode;
import com.borsam.repository.entity.doctor.DoctorAccount;
import com.borsam.repository.entity.forum.ForumInfo;
import com.borsam.repository.entity.org.Organization;
import com.borsam.repository.entity.org.OrganizationDoctorImage;
import com.borsam.repository.entity.org.OrganizationImage;
import com.borsam.service.doctor.DoctorAccountService;
import com.borsam.service.forum.ForumInfoService;
import com.borsam.service.org.OrganizationImageService;
import com.borsam.service.org.OrganizationService;
import com.borsam.service.pub.FileService;
import com.hiteam.common.base.pojo.search.Filter;
import com.hiteam.common.base.pojo.search.Order;
import com.hiteam.common.util.pojo.EnumBean;
import com.hiteam.common.web.Message;
import com.hiteam.common.web.controller.BaseController;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Controller - 机构管理
 * Created by tantian on 2015/6/29.
 */
@Controller("orgOrganizationController")
@RequestMapping("/org/organization")
public class OrgController extends BaseController {

    @Resource(name = "doctorAccountServiceImpl")
    private DoctorAccountService doctorAccountService;
    
    @Resource(name = "organizationImageServiceImpl")
    private OrganizationImageService organizationImageService;

    @Resource(name = "organizationServiceImpl")
    private OrganizationService organizationService;

    @Resource(name = "fileServiceImpl")
    private FileService fileService;

    @Resource(name = "forumInfoServiceImpl")
    private ForumInfoService forumInfoService;

    /**
     * 管理主页，如已申请开通机构，则返回机构详情页面
     */
    @RequestMapping(method = RequestMethod.GET)
    public String index(HttpServletResponse response, Model model) {
        DoctorAccount doctorAccount = doctorAccountService.getCurrent();
        if (doctorAccount != null && doctorAccount.getDoctorProfile() != null
                && doctorAccount.getDoctorProfile().getOrg() != null
                && doctorAccount.getDoctorProfile().getOrg().getId() != 0L) {
            Organization org = doctorAccount.getDoctorProfile().getOrg();
            model.addAttribute("org", org);

            // 最新评价TOP3
            List<Filter> filters = new ArrayList<Filter>();
            filters.add(Filter.eq("oid", org.getId()));
            List<Order> orders = new ArrayList<Order>();
            orders.add(Order.desc("created"));
            List<ForumInfo> forumInfos = forumInfoService.findList(3, filters, orders);
            model.addAttribute("forumInfos", forumInfos);
            model.addAttribute("wfCode", WfCode.ORG);

            response.setHeader("Pragma", "no-cache");
            response.setHeader("Cache-Control", "no-cache");
            response.setHeader("Cache-Control", "no-store");
            response.setDateHeader("Expires", 0);
            return "/org/organization/view";
        } else {
            return "/org/organization/index";
        }
    }

    /**
     * 机构开通申请页面
     */
    @RequestMapping(value = "/add", method = RequestMethod.GET)
    public String add() {
        return "/org/organization/add";
    }

    /**
     * 机构编辑页面
     */
    @RequestMapping(value = "/{id}/edit", method = RequestMethod.GET)
    public String edit(@PathVariable Long id, Model model) {
        Organization org = organizationService.find(id);
        model.addAttribute("org", org);
        if (org.getAuditState() != 2) {
            return "/org/organization/edit";
        } else {
            // 已通过审核的，只能改联系人信息和介绍
            return "/org/organization/update";
        }
    }

    /**
     * 保存机构信息
     * @param organization 机构信息
     * @param isSubmit 是否提交审批
     */
    @RequestMapping(value = "/save", method = RequestMethod.POST)
    @ResponseBody
    public Message save(Organization organization, Boolean isSubmit) {
        for (Iterator<OrganizationImage> iterator = organization.getOrgImages().iterator(); iterator.hasNext();) {
            OrganizationImage organizationImage = iterator.next();
            if (organizationImage == null || organizationImage.isEmpty()) {
                iterator.remove();
            }
        }
        for (Iterator<OrganizationDoctorImage> iterator = organization.getDoctorImages().iterator(); iterator.hasNext();) {
            OrganizationDoctorImage doctorImage = iterator.next();
            if (doctorImage == null || doctorImage.isEmpty()) {
                iterator.remove();
            }
        }
        if (!isValid(organization)) {
            return Message.error("common.message.data");
        }

        organizationService.create(organization, isSubmit);
        return SUCCESS_MSG;
    }

    /**
     * 更新机构信息
     * @param organization 机构信息
     * @param isSubmit 是否提交审批
     */
    @RequestMapping(value = "/update", method = RequestMethod.POST)
    @ResponseBody
    public Message update(Organization organization, Boolean isSubmit) {
        for (Iterator<OrganizationImage> iterator = organization.getOrgImages().iterator(); iterator.hasNext();) {
            OrganizationImage organizationImage = iterator.next();
            if (organizationImage == null || organizationImage.isEmpty()) {
                iterator.remove();
            }
        }
        for (Iterator<OrganizationDoctorImage> iterator = organization.getDoctorImages().iterator(); iterator.hasNext();) {
            OrganizationDoctorImage doctorImage = iterator.next();
            if (doctorImage == null || doctorImage.isEmpty()) {
                iterator.remove();
            }
     	}
        if (!isValid(organization)) {
            return Message.error("common.message.data");
        }

        organizationService.reCreate(organization, isSubmit);
        return SUCCESS_MSG;
    }

    /**
     * 更新机构信息
     * @param organization 机构信息
     */
    @RequestMapping(value = "/update1", method = RequestMethod.POST)
    @ResponseBody
    public Message update1(Organization organization) {
    	 for (Iterator<OrganizationImage> iterator = organization.getOrgImages().iterator(); iterator.hasNext();) {
             OrganizationImage organizationImage = iterator.next();
             if (organizationImage == null || organizationImage.isEmpty()) {
                 iterator.remove();
             }
         }
    	Organization old = organizationService.find(organization.getId());
        old.setNationCode(organization.getNationCode());
        old.setNationName(organization.getNationName());
        old.setRegionCode(organization.getRegionCode());
        old.setRegionName(organization.getRegionName());
        old.setProvinceCode(organization.getProvinceCode());
        old.setProvinceName(organization.getProvinceName());
        old.setCityCode(organization.getCityCode());
        old.setCityName(organization.getCityName());
        old.setAddress(organization.getAddress());
        old.setContact(organization.getContact());
        old.setEmail(organization.getEmail());
        old.setZoneCode(organization.getZoneCode());
        old.setIntro(organization.getIntro());
        old.setTelephone(organization.getTelephone());
        
        organizationImageService.removeImages(organization.getId());
        for (OrganizationImage organizationImage : organization.getOrgImages()) {
            organizationImageService.build(organizationImage);
            organizationImage.setOrg(organization);
            organizationImageService.save(organizationImage);
        }
        // 更新封面图片
        if (organization.getOrgImages() != null && !organization.getOrgImages().isEmpty()) {
            old.setHeadpidId(organization.getOrgImages().get(0).getId());
        }
        organizationService.update(old);
        return SUCCESS_MSG;
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
