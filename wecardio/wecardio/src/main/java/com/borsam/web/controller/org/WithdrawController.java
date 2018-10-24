package com.borsam.web.controller.org;

import com.borsam.pojo.wf.WfCode;
import com.borsam.pub.UserType;
import com.borsam.repository.entity.doctor.DoctorAccount;
import com.borsam.repository.entity.extract.ExtractOrder;
import com.borsam.repository.entity.extract.MonthClear;
import com.borsam.service.doctor.DoctorAccountService;
import com.borsam.service.extract.ExtractOrderService;
import com.borsam.service.extract.MonthClearService;
import com.borsam.service.wf.QueryService;
import com.hiteam.common.base.pojo.search.Filter;
import com.hiteam.common.base.pojo.search.Page;
import com.hiteam.common.base.pojo.search.Pageable;
import com.hiteam.common.util.json.JsonUtils;
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
import java.util.*;

/**
 * Controller - 提现管理
 * Created by tantian on 2015/8/17.
 */
@Controller("orgWithdrawController")
@RequestMapping(value = "/org/withdraw")
public class WithdrawController extends BaseController {

    @Resource(name = "doctorAccountServiceImpl")
    private DoctorAccountService doctorAccountService;

    @Resource(name = "queryServiceImpl")
    private QueryService queryService;

    @Resource(name = "extractOrderServiceImpl")
    private ExtractOrderService extractOrderService;

    @Resource(name = "monthClearServiceImpl")
    private MonthClearService monthClearService;

    /**
     * 待办页面
     */
    @RequestMapping(value = "/todo", method = RequestMethod.GET)
    public String todo() {
        return "/org/withdraw/todo";
    }

    /**
     * 编辑页面
     */
    @RequestMapping(value = "/{id}/edit", method = RequestMethod.GET)
    public String edit(@PathVariable Long id, Model model) {
        ExtractOrder order = extractOrderService.find(id);
        if (order.getState() != 0) {
            return ERROR_VIEW;
        }
        model.addAttribute("wfCode", WfCode.WITHDRAW);
        model.addAttribute("order", order);
        return "/org/withdraw/edit";
    }

    /**
     * 新增月结信息页面
     */
    @RequestMapping(value = "/{id}/settlement/select", method = RequestMethod.GET)
    public String select(@PathVariable Long id, Model model) {
        model.addAttribute("id", id);
        return "/org/withdraw/select";
    }

    /**
     * 待办列表
     */
    @RequestMapping(value = "/todo", method = RequestMethod.POST)
    @ResponseBody
    public Page<ExtractOrder> todoList(String orgName, Date startDate, Date endDate,
                                       Integer[] state, String orderNo, Pageable pageable) {
        DoctorAccount doctorAccount = doctorAccountService.getCurrent();
        List<Long> guidList = queryService.queryTodoGuids(WfCode.WITHDRAW, UserType.org, doctorAccount.getId());

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
     * 月结信息列表
     */
    @RequestMapping(value = "/settlement/list", method = RequestMethod.POST)
    @ResponseBody
    public List<MonthClear> settlementList(Long id) {
        List<Filter> filters = new ArrayList<Filter>();
        filters.add(Filter.eq("order", id));
        return monthClearService.findList(null, filters, null);
    }

    /**
     * 删除月结信息
     */
    @SuppressWarnings("unchecked")
    @RequestMapping(value = "/remove", method = RequestMethod.POST)
    @ResponseBody
    public Message removeSettlement(Long id, String data) {
        ExtractOrder order = extractOrderService.find(id);
        // 不是待修改状态不给操作
        if (order.getState() != 0) {
            return ERROR_MSG;
        }
        List<Map> list = JsonUtils.toList(data, HashMap.class);
        extractOrderService.removeSettlement(id, list);
        return SUCCESS_MSG;
    }

    /**
     * 新增月结信息
     */
    @SuppressWarnings("unchecked")
    @RequestMapping(value = "/settlement/add", method = RequestMethod.POST)
    @ResponseBody
    public Message addSettlement(Long id, String data) {
        ExtractOrder order = extractOrderService.find(id);
        // 不是待修改状态不给操作
        if (order.getState() != 0) {
            return ERROR_MSG;
        }
        List<Map> list = JsonUtils.toList(data, HashMap.class);
        extractOrderService.addSettlement(id, list);
        return SUCCESS_MSG;
    }

    /**
     * 重新提交申请
     */
    @RequestMapping(value = "/recreate", method = RequestMethod.POST)
    @ResponseBody
    public Message reCreate(Long id) {
        ExtractOrder order = extractOrderService.find(id);
        // 不是待修改状态不给操作
        if (order.getState() != 0) {
            return ERROR_MSG;
        }
        if (order.getMoney().compareTo(new BigDecimal(0)) == 0) {
            return Message.error("org.withdraw.message.choose");
        }
        extractOrderService.reCreate(order);
        return SUCCESS_MSG;
    }
}
