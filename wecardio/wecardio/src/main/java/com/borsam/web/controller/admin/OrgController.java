package com.borsam.web.controller.admin;

import com.borsam.pojo.wf.WfCode;
import com.borsam.pub.UserType;
import com.borsam.repository.entity.admin.Admin;
import com.borsam.repository.entity.forum.ForumInfo;
import com.borsam.repository.entity.org.Organization;
import com.borsam.service.admin.AdminService;
import com.borsam.service.forum.ForumInfoService;
import com.borsam.service.org.OrganizationService;
import com.borsam.service.wf.QueryService;
import com.hiteam.common.base.pojo.search.Filter;
import com.hiteam.common.base.pojo.search.Order;
import com.hiteam.common.base.pojo.search.Page;
import com.hiteam.common.base.pojo.search.Pageable;
import com.hiteam.common.util.pojo.EnumBean;
import com.hiteam.common.web.Message;
import com.hiteam.common.web.controller.BaseController;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.time.DateUtils;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Controller - 机构管理
 * Created by Sebarswee on 2015/7/7.
 */
@Controller("adminOrganizationController")
@RequestMapping("/admin/organization")
public class OrgController extends BaseController {

    @Resource(name = "adminServiceImpl")
    private AdminService adminService;

    @Resource(name = "organizationServiceImpl")
    private OrganizationService organizationService;

    @Resource(name = "queryServiceImpl")
    private QueryService queryService;

    @Resource(name = "forumInfoServiceImpl")
    private ForumInfoService forumInfoService;

    /**
     * 管理主页，返回机构列表页面
     */
    @RequestMapping(method = RequestMethod.GET)
    public String index() {
        return "/admin/organization/list";
    }

    /**
     * 详情页面
     */
    @RequestMapping(value = "/{oid}/detail", method = RequestMethod.GET)
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

        // 机构系统账号
        model.addAttribute("account", organizationService.getAccount(oid));
        return "/admin/organization/detail";
    }

    /**
     * 分成设置页面
     */
    @RequestMapping(value = "/config", method = RequestMethod.GET)
    public String config(Long[] oid, Model model) {
        if (oid != null) {
            String ids = "";
            for (Long id : oid) {
                if (StringUtils.isNotEmpty(ids)) {
                    ids += ",";
                }
                ids += id;
            }
            model.addAttribute("oid", ids);

            if (oid.length == 1) {
                Organization organization = organizationService.find(oid[0]);
                model.addAttribute("rate", organization.getRate() != null ? organization.getRate().multiply(new BigDecimal(100)).setScale(0) : null);
            }
        }
        return "/admin/organization/config";
    }

    /**
     * 待办页面
     */
    @RequestMapping(value = "/todo", method = RequestMethod.GET)
    public String todo() {
        return "/admin/organization/todo";
    }

    /**
     * 详情页面（流程）
     */
    @RequestMapping(value = "/{oid}/view", method = RequestMethod.GET)
    public String view(@PathVariable Long oid, Model model) {
        model.addAttribute("wfCode", WfCode.ORG);
        model.addAttribute("org", organizationService.find(oid));
        return "/admin/organization/view";
    }

    /**
     * 初审页面
     */
    @RequestMapping(value = "/{oid}/audit", method = RequestMethod.GET)
    public String audit(@PathVariable Long oid, Model model) {
        model.addAttribute("oid", oid);
        return "/admin/organization/audit";
    }

    /**
     * 终审页面
     */
    @RequestMapping(value = "/{oid}/confirm", method = RequestMethod.GET)
    public String confirm(@PathVariable Long oid, Model model) {
        model.addAttribute("oid", oid);
        return "/admin/organization/confirm";
    }

    /**
     * 查询列表
     */
    @RequestMapping(value = "/list", method = RequestMethod.POST)
    @ResponseBody
    public Page<Organization> list(String name, Pageable pageable) {
        List<Filter> filters = new ArrayList<Filter>();

        // 只查审核通过
        filters.add(Filter.in("auditState", new Integer[] {2, 3}));

        // 机构名称
        if (StringUtils.isNotEmpty(name)) {
            filters.add(Filter.like("name", name));
        }
        pageable.setFilters(filters);
        pageable.setOrderProperty("created");
        pageable.setOrderDirection(Order.Direction.desc);
        return organizationService.findPage(pageable);
    }

    /**
     * 待办列表
     */
    @RequestMapping(value = "/todoList", method = RequestMethod.POST)
    @ResponseBody
    public Page<Organization> todoList(String name, Date startDate, Date endDate, Pageable pageable) {
        Admin admin = adminService.getCurrent();
        List<Long> guidList = queryService.queryTodoGuids(WfCode.ORG, UserType.admin, admin.getId());

        List<Filter> filters = new ArrayList<Filter>();

        if (guidList != null && !guidList.isEmpty()) {
            filters.add(Filter.in("guid", guidList.toArray()));
        } else {
            return new Page<Organization>(null, 0, pageable);
        }
        filters.add(Filter.eq("auditState", 0));    // 只查待审核
        // 机构名称
        if (StringUtils.isNotEmpty(name)) {
            filters.add(Filter.like("name", name));
        }
        // 提交时间
        if (startDate != null) {
            filters.add(Filter.ge("modify", startDate.getTime() / 1000));
        }
        if (endDate != null) {
            filters.add(Filter.le("modify", DateUtils.addDays(endDate, 1).getTime() / 1000));
        }
        pageable.setFilters(filters);
        return organizationService.findPage(pageable);
    }

    /**
     * 审核
     */
    @RequestMapping(value = "/audit", method = RequestMethod.POST)
    @ResponseBody
    public Message audit(Long oid, Boolean isPass, String remark) {
        if (oid == null) {
            return ERROR_MSG;
        }
        if (isPass == null) {
            return Message.warn("admin.organization.message.isPass");
        }
        organizationService.audit(oid, isPass, remark);
        return SUCCESS_MSG;
    }

    /**
     * 停止
     */
    @RequestMapping(value = "/stop", method = RequestMethod.POST)
    @ResponseBody
    public Message stop(Long[] oid) {
        if (oid != null && oid.length > 0) {
            for (Long id : oid) {
                Organization organization = organizationService.find(id);
                organization.setAuditState(3);
                organizationService.update(organization);
            }
        }
        return SUCCESS_MSG;
    }

    /**
     * 重开
     */
    @RequestMapping(value = "/open", method = RequestMethod.POST)
    @ResponseBody
    public Message open(Long[] oid) {
        if (oid != null && oid.length > 0) {
            for (Long id : oid) {
                Organization organization = organizationService.find(id);
                organization.setAuditState(2);
                organizationService.update(organization);
            }
        }
        return SUCCESS_MSG;
    }

    /**
     * 设置分成比例
     */
    @RequestMapping(value = "/rate/set", method = RequestMethod.POST)
    @ResponseBody
    public Message setRate(Long[] oid, BigDecimal rate) {
        if (oid != null && oid.length > 0) {
            for (Long id : oid) {
                Organization organization = organizationService.find(id);
                organization.setRate(rate.divide(new BigDecimal(100)));
                organizationService.update(organization);
            }
        }
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
