package com.borsam.plugin.alipayDirect;

import com.borsam.plugin.PaymentPlugin;
import com.hiteam.common.util.ConfigUtils;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.security.KeyFactory;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.Signature;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

/**
 * Plugin - 支付宝（即时交易）
 * Created by tantian on 2015/8/5.
 */
@Component("alipayDirectPlugin")
public class AlipayDirectPlugin extends PaymentPlugin {

    @Override
    public String getName() {
        return "支付宝（即时交易）";
    }

    @Override
    public boolean isEnabled() {
        return Boolean.valueOf(ConfigUtils.config.getProperty("isAlipayDirectPluginEnable"));
    }

    @Override
    public String getPaymentName() {
        return "支付宝即时交易";
    }

    @Override
    public FeeType getFeeType() {
        return FeeType.valueOf(ConfigUtils.config.getProperty("alipayDirectPlugin.feeType"));
    }

    @Override
    public BigDecimal getFee() {
        return new BigDecimal(ConfigUtils.config.getProperty("alipayDirectPlugin.fee"));
    }

    @Override
    public String getLogo() {
        return "/resources/images/zhifu/zhifubao.jpg";
    }

    @Override
    public String getRequestUrl() {
        return "https://mapi.alipay.com/gateway.do";
    }

    @Override
    public RequestMethod getRequestMethod() {
        return RequestMethod.GET;
    }

    @Override
    public String getRequestCharset() {
        return "UTF-8";
    }

    @Override
    public Map<String, Object> getParameterMap(String sn, BigDecimal amount, String description, HttpServletRequest request) {
        Map<String, Object> parameterMap = new HashMap<String, Object>();
        parameterMap.put("service", "create_direct_pay_by_user");
        parameterMap.put("partner", ConfigUtils.config.getProperty("alipayDirectPlugin.partner"));
        parameterMap.put("_input_charset", "utf-8");
        parameterMap.put("sign_type", "RSA");
        // return_url为同步
        if (request.getAttribute("return_url") != null) {
            parameterMap.put("return_url", request.getAttribute("return_url"));
        }
        if (request.getAttribute("notify_url") != null) {
            parameterMap.put("notify_url", request.getAttribute("notify_url"));
        }
//        parameterMap.put("return_url", getNotifyUrl(sn, NotifyMethod.sync));
//        parameterMap.put("notify_url", getNotifyUrl(sn, NotifyMethod.async));
        parameterMap.put("out_trade_no", sn);
        parameterMap.put("subject", StringUtils.abbreviate(description.replaceAll("[^0-9a-zA-Z\\u4e00-\\u9fa5 ]", ""), 60));
        parameterMap.put("body", StringUtils.abbreviate(description.replaceAll("[^0-9a-zA-Z\\u4e00-\\u9fa5 ]", ""), 600));
        parameterMap.put("payment_type", "1");
        parameterMap.put("seller_id", ConfigUtils.config.getProperty("alipayDirectPlugin.partner"));
        parameterMap.put("total_fee", amount.setScale(2).toString());
//        parameterMap.put("show_url", "");
        parameterMap.put("paymethod", "directPay");
        // 只有开通了防钓鱼功能且开通了IP地址检查，才能使用请求参数exter_invoke_ip（客户端IP）
//        parameterMap.put("exter_invoke_ip", request.getLocalAddr());
        parameterMap.put("sign", generateSignRSA(parameterMap));
        return parameterMap;
    }

    @Override
    public boolean verifyNotify(String sn, BigDecimal amount, NotifyMethod notifyMethod, HttpServletRequest request) {
        // body, subject 会中文乱码
        Map<String, Object> map = new HashMap<String, Object>(request.getParameterMap());
        map.put("body", request.getParameter("body"));
        map.put("subject",request.getParameter("subject"));
       
        if (verifyRSA(map, request.getParameter("sign"))
                && ConfigUtils.config.getProperty("alipayDirectPlugin.partner").equals(request.getParameter("seller_id"))
                && sn.equals(request.getParameter("out_trade_no"))
                && ("TRADE_SUCCESS".equals(request.getParameter("trade_status")) || "TRADE_FINISHED".equals(request.getParameter("trade_status")))
                && amount.compareTo(new BigDecimal(request.getParameter("total_fee"))) == 0) {
            Map<String, Object> parameterMap = new HashMap<String, Object>();
            parameterMap.put("service", "notify_verify");
            parameterMap.put("partner", ConfigUtils.config.getProperty("alipayDirectPlugin.partner"));
            parameterMap.put("notify_id", request.getParameter("notify_id"));
            if ("true".equals(post("https://mapi.alipay.com/gateway.do", parameterMap))) {
                return true;
            }
        }
        return false;
    }

    @Override
    public String getNotifyMessage(String sn, NotifyMethod notifyMethod, HttpServletRequest request) {
        if (notifyMethod == NotifyMethod.async) {
            return "success";
        }
        return null;
    }

    @Override
    public Integer getTimeout() {
        return 21600;
    }

    /**
     * 生成签名MD5
     * @param parameterMap 参数
     * @return 签名
     */
    private String generateSignMD5(Map<String, ?> parameterMap) {
        return DigestUtils.md5Hex(joinKeyValue(new TreeMap<String, Object>(parameterMap), null, ConfigUtils.config.getProperty("alipayDirectPlugin.MD5key"), "&", true, "sign_type", "sign"));
    }

    /**
     * 生成签名
     * @param parameterMap 参数
     * @return 签名
     */
    private String generateSignRSA(Map<String, ?> parameterMap) {
        try {
            String sign = joinKeyValue(new TreeMap<String, Object>(parameterMap), null, null, "&", true, "sign_type", "sign");
            String privateKey = ConfigUtils.config.getProperty("alipayDirectPlugin.RSAkey");
            PKCS8EncodedKeySpec priPKCS8 = new PKCS8EncodedKeySpec(Base64.decodeBase64(privateKey));
            KeyFactory keyf = KeyFactory.getInstance("RSA");
            PrivateKey priKey = keyf.generatePrivate(priPKCS8);
            Signature signature = Signature.getInstance("SHA1WithRSA");
            signature.initSign(priKey);
            signature.update(sign.getBytes("utf-8"));
            byte[] signed = signature.sign();
            return new String(Base64.encodeBase64(signed));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 验证RSA签名
     * @param parameterMap 参数
     * @param signed 签名
     * @return 是否通过
     */
    private Boolean verifyRSA(Map<String, ?> parameterMap, String signed) {
        try {
            String sign = joinKeyValue(new TreeMap<String, Object>(parameterMap), null, null, "&", true, "sign_type", "sign");
            String publicKey = ConfigUtils.config.getProperty("alipay.public_key");
            KeyFactory keyf = KeyFactory.getInstance("RSA");
            PublicKey pubKey = keyf.generatePublic(new X509EncodedKeySpec(Base64.decodeBase64(publicKey)));
            Signature signature = Signature.getInstance("SHA1WithRSA");
            signature.initVerify(pubKey);
            signature.update(sign.getBytes("utf-8"));
            return signature.verify(Base64.decodeBase64(signed));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }
}
