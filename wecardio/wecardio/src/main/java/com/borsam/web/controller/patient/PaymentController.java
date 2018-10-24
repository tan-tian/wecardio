package com.borsam.web.controller.patient;

import com.borsam.plugin.PaymentPlugin;
import com.borsam.plugin.alipayDirect.AlipayDirectPlugin;
import com.borsam.repository.entity.patient.PatientAccount;
import com.borsam.repository.entity.patient.PatientWalletHistory;
import com.borsam.service.patient.PatientAccountService;
import com.borsam.service.patient.PatientWalletHistoryService;
import com.borsam.service.pub.PluginService;
import com.borsam.service.pub.SnService;
import com.hiteam.common.util.ConfigUtils;
import com.hiteam.common.web.Message;
import com.hiteam.common.web.controller.BaseController;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.transaction.Transactional;

import java.math.BigDecimal;
import java.util.Map;

/**
 * Controller - 支付
 * Created by tantian on 2015/8/6.
 */
@Controller("patientPaymentController")
@RequestMapping("/patient/payment")
public class PaymentController extends BaseController {

    @Resource(name = "pluginServiceImpl")
    private PluginService pluginService;

    @Resource(name = "patientAccountServiceImpl")
    private PatientAccountService patientAccountService;

    @Resource(name = "patientWalletHistoryServiceImpl")
    private PatientWalletHistoryService patientWalletHistoryService;

    @Resource(name = "snServiceImpl")
    private SnService snService;

    /**
     * 提交
     */
    @RequestMapping(value = "/submit", method = RequestMethod.POST)
    public String submit(PatientWalletHistory.Type type, String paymentPluginId, BigDecimal amount, HttpServletRequest request, HttpServletResponse response, ModelMap model) {
        PatientAccount patientAccount = patientAccountService.getCurrent();
        if (patientAccount == null) {
            return ERROR_VIEW;
        }
        PaymentPlugin paymentPlugin = pluginService.getPaymentPlugin(paymentPluginId);
        if (paymentPlugin == null || !paymentPlugin.isEnabled()) {
            return ERROR_VIEW;
        }
        PatientWalletHistory history = new PatientWalletHistory();
        String description = null;
        String returnUrl = null;
        String notifyUrl = null;

        if (type == PatientWalletHistory.Type.recharge) {
            if (amount == null || amount.compareTo(new BigDecimal(0)) <= 0 || amount.precision() > 15 || amount.scale() > 2) {
                return ERROR_VIEW;
            }
            history.setPayNo("");
            history.setType(PatientWalletHistory.Type.recharge);
            history.setSuccess(0);  // 0-等待返回结果
            history.setPayStyle(1); // TODO 支付类型枚举
            history.setMoney(paymentPlugin.calculateAmount(amount));
            history.setUid(patientAccount.getId());
            history.setVerdict(message("patient.wallet.recharge.title"));
            history.setOid(0L);
            history.setTradeNo("");
            patientWalletHistoryService.save(history);
            // 生成交易号
            history.setTradeNo(snService.generate(history.getId()));
            patientWalletHistoryService.update(history);
            description = message("patient.wallet.recharge.title");

            returnUrl = ConfigUtils.config.getProperty("siteUrl") + "/patient/payment/notify/" + PaymentPlugin.NotifyMethod.sync + "/" + history.getTradeNo();
            notifyUrl = ConfigUtils.config.getProperty("siteUrl") + "/patient/payment/notify/" + PaymentPlugin.NotifyMethod.async + "/" + history.getTradeNo();

            request.setAttribute("return_url", returnUrl);
            request.setAttribute("notify_url", notifyUrl);
        } else {
            return ERROR_VIEW;
        }

        Map<String, Object> parameterMap = paymentPlugin.getParameterMap(history.getTradeNo(), history.getMoney(), description, request);
//        if (StringUtils.isNotEmpty(returnUrl)) {
//            parameterMap.put("return_url", returnUrl);
//        }
//        if (StringUtils.isNotEmpty(notifyUrl)) {
//            parameterMap.put("notify_url", notifyUrl);
//        }
        model.addAttribute("requestUrl", paymentPlugin.getRequestUrl());
        model.addAttribute("requestMethod", paymentPlugin.getRequestMethod());
        model.addAttribute("requestCharset", paymentPlugin.getRequestCharset());
        model.addAttribute("parameterMap", parameterMap);
        
        if (StringUtils.isNotEmpty(paymentPlugin.getRequestCharset())) {
            response.setContentType("text/html; charset=" + paymentPlugin.getRequestCharset());
        }
        
        return "/patient/payment/submit";
    }

    /**
     * 通知
     */
    @RequestMapping("/notify/sync/{sn}")
    public String notify(@PathVariable String sn, HttpServletRequest request, ModelMap model) {
        PatientWalletHistory history = patientWalletHistoryService.findBySn(sn);
        System.out.println("+++++++同步通知成功");
        if (history != null) {
            // 支付宝交易号
            history.setPayNo(request.getParameter("trade_no"));
            //根据支付宝返回结果，修改充值记录状态
            String tradeStatus=request.getParameter("trade_status");
           patientWalletHistoryService.update(history);
            // TODO 历史表应该添加字段保存支付插件的ID，这里先默认使用支付宝即时交易，待后续扩展
            PaymentPlugin paymentPlugin = pluginService.getPaymentPlugin(AlipayDirectPlugin.class.getAnnotation(Component.class).value());
            if (paymentPlugin != null) {
            	if (paymentPlugin.verifyNotify(sn, history.getMoney(),PaymentPlugin.NotifyMethod.sync, request)) {
                    patientWalletHistoryService.handle(history);
                }
                model.addAttribute("notifyMessage", paymentPlugin.getNotifyMessage(sn, PaymentPlugin.NotifyMethod.sync, request));
            }
            model.addAttribute("payment", history);
        	}
        return "/patient/payment/notify";
    }
    
    /**
     * 通知(异步)
     */
    @ResponseBody
    @RequestMapping(value="/notify/async/{sn}",method = RequestMethod.POST)
    public String asyncNotify(@PathVariable String sn, HttpServletRequest request, ModelMap model) {
        PatientWalletHistory history = patientWalletHistoryService.findBySn(sn);
        if (history != null) {
            // 支付宝交易号
            history.setPayNo(request.getParameter("trade_no"));
            //根据支付宝返回结果，修改充值记录状态
            String tradeStatus=request.getParameter("trade_status");
            patientWalletHistoryService.update(history);
            // TODO 历史表应该添加字段保存支付插件的ID，这里先默认使用支付宝即时交易，待后续扩展
            PaymentPlugin paymentPlugin = pluginService.getPaymentPlugin(AlipayDirectPlugin.class.getAnnotation(Component.class).value());
            if (paymentPlugin != null) {
            	if (paymentPlugin.verifyNotify(sn, history.getMoney(), PaymentPlugin.NotifyMethod.async, request)) {
                    patientWalletHistoryService.handle(history);
                }
                model.addAttribute("notifyMessage", paymentPlugin.getNotifyMessage(sn, PaymentPlugin.NotifyMethod.async, request));
            }
            model.addAttribute("payment", history);
        	}
        return "success";
    }
}
