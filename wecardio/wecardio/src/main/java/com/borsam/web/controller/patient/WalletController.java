package com.borsam.web.controller.patient;

import com.borsam.repository.entity.patient.*;
import com.borsam.service.patient.*;
import com.borsam.service.pub.PluginService;
import com.hiteam.common.base.pojo.search.Filter;
import com.hiteam.common.base.pojo.search.Order;
import com.hiteam.common.base.pojo.search.Page;
import com.hiteam.common.base.pojo.search.Pageable;
import com.hiteam.common.service.RSAService;
import com.hiteam.common.web.Message;
import com.hiteam.common.web.controller.BaseController;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang.time.DateUtils;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import java.security.interfaces.RSAPublicKey;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * Controller - 钱包管理
 * Created by tantian on 2015/8/3.
 */
@Controller
@RequestMapping(value =  "/patient/wallet")
public class WalletController extends BaseController {

    @Resource(name = "patientWalletServiceImpl")
    private PatientWalletService patientWalletService;

    @Resource(name = "patientWalletVerifyServiceImpl")
    private PatientWalletVerifyService patientWalletVerifyService;

    @Resource(name = "patientAccountServiceImpl")
    private PatientAccountService patientAccountService;

    @Resource(name = "patientServiceServiceImpl")
    private PatientServiceService patientServiceService;

    @Resource(name = "patientWalletHistoryServiceImpl")
    private PatientWalletHistoryService patientWalletHistoryService;

    @Resource(name = "rsaServiceImpl")
    private RSAService rsaService;

    @Resource(name = "pluginServiceImpl")
    private PluginService pluginService;

    /**
     * 主页面
     */
    @RequestMapping(method = RequestMethod.GET)
    public String index(Model model) {
        PatientAccount patientAccount = patientAccountService.getCurrent();
        // 钱包未激活
        if (!patientAccount.getPatientProfile().getIsWalletActive()) {
            return "/patient/wallet/notactivate";
        }
        PatientWallet patientWallet = patientWalletService.find(patientAccount.getId());
        model.addAttribute("deposit", patientWallet.getTotal());
        return "/patient/wallet/index";
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
        return "/patient/wallet/activate";
    }

    /**
     * 修改密码页面
     */
    @RequestMapping(value = "/password/reset", method = RequestMethod.GET)
    public String reset() {
        return "/patient/wallet/reset";
    }

    /**
     * 充值页面
     */
    @RequestMapping(value = "/recharge", method = RequestMethod.GET)
    public String recharge(Model model) {
        model.addAttribute("paymentPlugins", pluginService.getPaymentPlugins(true));
        return "/patient/wallet/recharge";
    }

    /**
     * 账户激活
     */
    @RequestMapping(value = "/activate/submit", method = RequestMethod.POST)
    @ResponseBody
    public Message submit() {
        String password = rsaService.decryptParameter("enPassword");
        rsaService.removePrivateKey();

        PatientAccount patientAccount = patientAccountService.getCurrent();
        patientWalletVerifyService.activate(patientAccount.getId(), password);
        return SUCCESS_MSG;
    }

    /**
     * 修改密码
     */
    @RequestMapping(value = "/password/reset", method = RequestMethod.POST)
    @ResponseBody
    public Message resetPassword(String oldPassword, String newPassword) {
        PatientAccount patientAccount = patientAccountService.getCurrent();
        PatientWalletVerify patientWalletVerify = patientWalletVerifyService.find(patientAccount.getId());
        if (!DigestUtils.md5Hex(oldPassword).equals(patientWalletVerify.getPassword())) {
            return Message.warn("patient.wallet.message.oldPassword");
        }
        patientWalletVerify.setPassword(DigestUtils.md5Hex(newPassword));
        // 重置密码后，token失效
        patientWalletVerify.setToken("");
        patientWalletVerify.setTokenTime(new Date().getTime() / 1000);
        patientWalletVerifyService.update(patientWalletVerify);
        return SUCCESS_MSG;
    }

    /**
     * 分页查询已购服务
     */
    @RequestMapping(value = "/service/page", method = RequestMethod.POST)
    @ResponseBody
    public Page<PatientService> servicePage(Pageable pageable) {
        List<Filter> filters = new ArrayList<Filter>();
        PatientAccount patientAccount = patientAccountService.getCurrent();

        filters.add(Filter.eq("key.patient", patientAccount.getId()));

        pageable.setFilters(filters);
        return patientServiceService.findPage(pageable);
    }

    /**
     * 分页查询历史记录
     */
    @RequestMapping(value = "/history/page", method = RequestMethod.POST)
    @ResponseBody
    public Page<PatientWalletHistory> historyPage(Date startDate, Date endDate, Pageable pageable) {
        List<Filter> filters = new ArrayList<Filter>();
        PatientAccount patientAccount = patientAccountService.getCurrent();
        filters.add(Filter.eq("uid", patientAccount.getId()));

        if (startDate != null) {
            filters.add(Filter.ge("created", startDate.getTime() / 1000));
        }
        if (endDate != null) {
            filters.add(Filter.le("created", DateUtils.addDays(endDate, 1).getTime() / 1000));
        }

        pageable.setFilters(filters);
        pageable.setOrderProperty("created");
        pageable.setOrderDirection(Order.Direction.desc);
        return patientWalletHistoryService.findPage(pageable);
    }

    /**
     * 创建支付
     */
    @RequestMapping(value = "/recharge/create", method = RequestMethod.POST)
    @ResponseBody
    public Message createRecharge(String amount) {

        return Message.success("");
    }

}
