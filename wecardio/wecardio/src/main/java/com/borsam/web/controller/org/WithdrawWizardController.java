package com.borsam.web.controller.org;

import com.borsam.pojo.wallet.Settlement;
import com.borsam.repository.entity.doctor.DoctorAccount;
import com.borsam.repository.entity.doctor.DoctorProfile;
import com.borsam.repository.entity.extract.ExtractOrder;
import com.borsam.repository.entity.extract.MonthClear;
import com.borsam.repository.entity.org.Organization;
import com.borsam.repository.entity.org.OrganizationWallet;
import com.borsam.service.doctor.DoctorAccountService;
import com.borsam.service.extract.ExtractOrderService;
import com.borsam.service.org.OrganizationWalletHistoryService;
import com.borsam.service.org.OrganizationWalletService;
import com.hiteam.common.web.controller.WizardFormController;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.bind.support.SessionStatus;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;

/**
 * Controller - 提现管理
 * Created by tantian on 2015/8/11.
 */
@Controller("orgWithdrawWizardController")
@RequestMapping(value = "/org/withdraw/wizard")
@SessionAttributes(value = { "order" })
public class WithdrawWizardController extends WizardFormController {

    @Resource(name = "doctorAccountServiceImpl")
    private DoctorAccountService doctorAccountService;

    @Resource(name = "organizationWalletServiceImpl")
    private OrganizationWalletService organizationWalletService;

    @Resource(name = "organizationWalletHistoryServiceImpl")
    private OrganizationWalletHistoryService organizationWalletHistoryService;

    @Resource(name = "extractOrderServiceImpl")
    private ExtractOrderService extractOrderService;

    @PostConstruct
    public void init() {
        // 分步视图
        setPageViews(new String[] {
                "/org/withdraw/choose",
                "/org/withdraw/payment",
                "/org/withdraw/addBank"
        });
        // 成功视图
        setSuccessView("/org/withdraw/success");
        // 取消视图
        setCancelView("/org/wallet/index");
    }

    /**
     * 如果模型数据中没有名字为order的对象，调用该方法并存储到模型数据中
     */
    @ModelAttribute("order")
    public ExtractOrder initOrder() {
        return new ExtractOrder();
    }

    /**
     * 返回值为void 将不暴露表单引用数据到模型数据
     */
    @ModelAttribute
    public void referenceData(@RequestParam(value = PARAM_PAGE, defaultValue="0") int currentPage, HttpServletRequest request,
                              Map<String, Object> model, @ModelAttribute("order") ExtractOrder order) {
        if (getTargetPage(request, currentPage) == 0) {
            /**
             * 步骤一为选择月结信息
             * 列出所有未提现的月结信息供选择，默认勾选前面选择的月份
             */
            DoctorAccount doctorAccount = doctorAccountService.getCurrent();
            DoctorProfile doctorProfile = doctorAccount.getDoctorProfile();
            Organization organization = doctorProfile.getOrg();
            List<Settlement> unSettlementList = organizationWalletHistoryService.findUnSettlementList(organization.getId(), null, null);
            if (unSettlementList != null && !unSettlementList.isEmpty()
                    && order != null && order.getMonthClears() != null && !order.getMonthClears().isEmpty()) {
                for (Settlement settlement : unSettlementList) {
                    for (MonthClear monthClear : order.getMonthClears()) {
                        if (settlement.getYear() == monthClear.getYear() && settlement.getMonth() == monthClear.getMonth()) {
                            settlement.setSelected(true);
                            break;
                        }
                    }
                }
            }
            // 清除已勾选的月结信息，由页面重新选择提交
            if (order != null && order.getMonthClears() != null && !order.getMonthClears().isEmpty()) {
                order.getMonthClears().clear();
            }
            model.put("unSettlementList", unSettlementList);
        } else if (getTargetPage(request, currentPage) == 1) {
            /**
             * 步骤二选择支付账户
             * 从钱包里面取出绑定的银行账户作为默认支付账户
             */
            DoctorAccount doctorAccount = doctorAccountService.getCurrent();
            DoctorProfile doctorProfile = doctorAccount.getDoctorProfile();
            Organization organization = doctorProfile.getOrg();
            OrganizationWallet organizationWallet = organizationWalletService.find(organization.getId());
            if (organizationWallet.isBindBank()) {
                model.put("isBindBank", true);
                model.put("bankBranch", organizationWallet.getBankBranch());
                model.put("bankUsername", organizationWallet.getBankUsername());
                model.put("bankNo", organizationWallet.getBankNo());
                model.put("bankNoMask", organizationWallet.getBankNoWithMask());
                model.put("bankIcon", organizationWallet.getBankIcon());
            } else {
                model.put("isBindBank", false);
            }
            if (order.getOutInfo() != null && order.getOutInfo().getType() == 0) {
                model.put("outInfo", order.getOutInfo());
            }
        }
    }

    /**
     * 重写完成方法
     */
    @Override
    @RequestMapping(params = "_finish")
    public String finish(@ModelAttribute("order") Object command, SessionStatus status) {
        ExtractOrder order = (ExtractOrder) command;
        extractOrderService.create(order);
        return super.finish(command, status);
    }
}
