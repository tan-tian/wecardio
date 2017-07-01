package com.borsam.web.controller.org;

import com.borsam.pojo.wallet.Settlement;
import com.borsam.repository.entity.doctor.DoctorAccount;
import com.borsam.repository.entity.doctor.DoctorProfile;
import com.borsam.repository.entity.org.Organization;
import com.borsam.repository.entity.org.OrganizationWallet;
import com.borsam.repository.entity.org.OrganizationWalletHistory;
import com.borsam.service.doctor.DoctorAccountService;
import com.borsam.service.org.OrganizationWalletHistoryService;
import com.borsam.service.org.OrganizationWalletService;
import com.borsam.service.org.OrganizationWalletVerifyService;
import com.hiteam.common.base.pojo.search.Page;
import com.hiteam.common.base.pojo.search.Pageable;
import com.hiteam.common.service.RSAService;
import com.hiteam.common.web.Message;
import com.hiteam.common.web.controller.BaseController;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import java.security.interfaces.RSAPublicKey;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * Controller - 钱包管理
 * Created by Sebarswee on 2015/8/10.
 */
@Controller("orgWalletController")
@RequestMapping(value = "/org/wallet")
public class WalletController extends BaseController {

    @Resource(name = "doctorAccountServiceImpl")
    private DoctorAccountService doctorAccountService;

    @Resource(name = "organizationWalletServiceImpl")
    private OrganizationWalletService organizationWalletService;

    @Resource(name = "organizationWalletVerifyServiceImpl")
    private OrganizationWalletVerifyService organizationWalletVerifyService;

    @Resource(name = "organizationWalletHistoryServiceImpl")
    private OrganizationWalletHistoryService organizationWalletHistoryService;

    @Resource(name = "rsaServiceImpl")
    private RSAService rsaService;

    /**
     * 主页面
     */
    @RequestMapping(method = RequestMethod.GET)
    public String index(Model model) {
        DoctorAccount doctorAccount = doctorAccountService.getCurrent();
        DoctorProfile doctorProfile = doctorAccount.getDoctorProfile();
        Organization organization = doctorProfile.getOrg();
        // 机构未创建
        if (organization == null || organization.getId() == 0L) {
            return "/org/organization/index";
        }
        // 钱包未激活
        if (!organization.getIsWalletActive()) {
            return "/org/wallet/notactivate";
        }
        OrganizationWallet organizationWallet = organizationWalletService.find(organization.getId());
        model.addAttribute("wallet", organizationWallet);
        return "/org/wallet/index";
    }

    /**
     * 激活页面
     */
    @RequestMapping(value = "/activate", method = RequestMethod.GET)
    public String activate(Model model) {
        RSAPublicKey publicKey = rsaService.generateKey();
        Map<String, String> map = rsaService.getModulusAndExponent(publicKey);
        model.addAttribute("modulus", map.get("modulus"));
        model.addAttribute("exponent", map.get("exponent"));
        return "/org/wallet/activate";
    }

    /**
     * 账户激活
     */
    @RequestMapping(value = "/activate/submit", method = RequestMethod.POST)
    @ResponseBody
    public Message submit() {
        String password = rsaService.decryptParameter("enPassword");
        rsaService.removePrivateKey();

        DoctorAccount doctorAccount = doctorAccountService.getCurrent();
        DoctorProfile doctorProfile = doctorAccount.getDoctorProfile();
        Organization organization = doctorProfile.getOrg();
        organizationWalletVerifyService.activate(organization.getId(), password);
        return SUCCESS_MSG;
    }

    /**
     * 查询未结算信息
     */
    @RequestMapping(value = "/unsettlement-list", method = RequestMethod.POST)
    @ResponseBody
    public List<Settlement> findUnSettlementList(Date startDate, Date endDate) {
        DoctorAccount doctorAccount = doctorAccountService.getCurrent();
        DoctorProfile doctorProfile = doctorAccount.getDoctorProfile();
        Organization organization = doctorProfile.getOrg();
        return organizationWalletHistoryService.findUnSettlementList(organization.getId(), startDate, endDate);
    }

    /**
     * 实收金额详情页面
     */
    @RequestMapping(value = "/withdraw/detail", method = RequestMethod.GET)
    public String withdrawDetail(String type, Model model) {
        model.addAttribute("type", type);
        return "/org/wallet/withdraw_detail";
    }

    /**
     * 实收金额详情列表
     */
    @RequestMapping(value = "/withdraw/detail/page", method = RequestMethod.POST)
    @ResponseBody
    public Page<OrganizationWalletHistory> withdrawList(String type, Pageable pageable) {
        DoctorAccount doctorAccount = doctorAccountService.getCurrent();
        DoctorProfile doctorProfile = doctorAccount.getDoctorProfile();
        Organization organization = doctorProfile.getOrg();

//        List<Filter> filters = new ArrayList<>();
//        filters.add(Filter.eq("oid", organization.getId()));
//        filters.add(Filter.eq("type", 0));
//
//        if ("real".equals(type)) {
//            filters.add(Filter.ne("status", 0));
//        } else if ("withdraw".equals(type)) {
//            filters.add(Filter.eq("status", 0));
//        }
//
//        pageable.setFilters(filters);
//        pageable.setOrderProperty("created");
//        pageable.setOrderDirection(Order.Direction.desc);

        Integer[] status;
        if ("real".equals(type)) {
            status = new Integer[] {1, 2};
        } else if ("withdraw".equals(type)) {
            status = new Integer[] {0};
        } else {
            status = null;
        }
        return organizationWalletHistoryService.detailList(organization.getId(), status, pageable.getPageNo(), pageable.getPageSize());
    }
}
