package com.borsam.web.controller.admin;

import com.borsam.pojo.wf.WfCode;
import com.borsam.pub.UserType;
import com.borsam.repository.entity.admin.Admin;
import com.borsam.repository.entity.extract.ExtractOrder;
import com.borsam.service.admin.AdminService;
import com.borsam.service.extract.ExtractOrderService;
import com.borsam.service.wf.QueryService;
import com.hiteam.common.base.pojo.search.Filter;
import com.hiteam.common.base.pojo.search.Page;
import com.hiteam.common.base.pojo.search.Pageable;
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
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Controller - 提现管理
 * Created by Sebarswee on 2015/8/13.
 */
@Controller("adminWithdrawController")
@RequestMapping(value = "/admin/withdraw")
public class WithdrawController extends BaseController {

    @Resource(name = "adminServiceImpl")
    private AdminService adminService;

    @Resource(name = "queryServiceImpl")
    private QueryService queryService;

    @Resource(name = "extractOrderServiceImpl")
    private ExtractOrderService extractOrderService;

    /**
     * 待办页面
     */
    @RequestMapping(value = "/todo", method = RequestMethod.GET)
    public String todo() {
        return "/admin/withdraw/todo";
    }

    /**
     * 详情页面
     */
    @RequestMapping(value = "/{id}/view", method = RequestMethod.GET)
    public String view(@PathVariable Long id, Model model) {
        model.addAttribute("wfCode", WfCode.WITHDRAW);
        model.addAttribute("order", extractOrderService.find(id));
        return "/admin/withdraw/view";
    }

    /**
     * 审核页面
     */
    @RequestMapping(value = "/{id}/audit", method = RequestMethod.GET)
    public String audit(@PathVariable Long id, Model model) {
        model.addAttribute("orderId", id);
        return "/admin/withdraw/audit";
    }

    /**
     * 出账页面
     */
    @RequestMapping(value = "/{id}/confirm", method = RequestMethod.GET)
    public String confirm(@PathVariable Long id, Model model) {
        model.addAttribute("orderId", id);
        return "/admin/withdraw/confirm";
    }

    /**
     * 待办列表
     */
    @RequestMapping(value = "/todo", method = RequestMethod.POST)
    @ResponseBody
    public Page<ExtractOrder> todoList(String orgName, Date startDate, Date endDate,
                                       Integer[] state, String orderNo, Pageable pageable) {
        Admin admin = adminService.getCurrent();
        List<Long> guidList = queryService.queryTodoGuids(WfCode.WITHDRAW, UserType.admin, admin.getId());

        List<Filter> filters = new ArrayList<Filter>();

        if (guidList != null && !guidList.isEmpty()) {
            filters.add(Filter.in("guid", guidList.toArray()));
        } else {
            return new Page<ExtractOrder>(null, 0, pageable);
        }

        // 机构名称
        if (StringUtils.isNotEmpty(orgName)) {
            filters.add(Filter.like("org.name", orgName));
        }

        // 申请时间
        if (startDate != null) {
            filters.add(Filter.ge("created", startDate.getTime() / 1000));
        }
        if (endDate != null) {
            filters.add(Filter.le("created", DateUtils.addDays(endDate, 1).getTime() / 1000));
        }

        // 状态
        if (state != null && state.length > 0) {
            filters.add(Filter.in("state", state));
        }

        // 单据编号
        if (StringUtils.isNotEmpty(orderNo)) {
            filters.add(Filter.like("orderNo", orderNo));
        }

        pageable.setFilters(filters);
        return extractOrderService.findPage(pageable);
    }

    /**
     * 审核
     */
    @RequestMapping(value = "/audit", method = RequestMethod.POST)
    @ResponseBody
    public Message audit(Long orderId, Boolean isPass, String remark) {
        if (orderId == null) {
            return ERROR_MSG;
        }
        if (isPass == null) {
            return Message.warn("admin.withdraw.message.isPass");
        }
        extractOrderService.audit(orderId, isPass, remark);
        return SUCCESS_MSG;
    }

    /**
     * 出账
     */
    @RequestMapping(value = "/confirm", method = RequestMethod.POST)
    @ResponseBody
    public Message confirm(Long orderId, String bankSeq, String remark) {
        if (orderId == null) {
            return ERROR_MSG;
        }
        extractOrderService.confirm(orderId, bankSeq, remark);
        return SUCCESS_MSG;
    }
}
