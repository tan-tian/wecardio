package com.borsam.pojo.wallet;

import java.math.BigDecimal;

/**
 * Bean - 平台汇总信息
 * Created by tantian on 2015/8/15.
 */
public class PlatformSummary {

    private BigDecimal saleAmount;          // 销售总额
    private BigDecimal payAmount;           // 应付金额（应付给机构的总额，累计）
    private BigDecimal alreadyPayAmount;    // 已付金额（已经付给机构的总额）
    private BigDecimal notPayAmount;        // 未付金额（未付给机构的金额）
    private BigDecimal rateAmount;          // 平台所得金额

    public PlatformSummary(BigDecimal saleAmount, BigDecimal payAmount, BigDecimal alreadyPayAmount, BigDecimal notPayAmount, BigDecimal rateAmount) {
        this.saleAmount = saleAmount;
        this.payAmount = payAmount;
        this.alreadyPayAmount = alreadyPayAmount;
        this.notPayAmount = notPayAmount;
        this.rateAmount = rateAmount;
    }

    /**
     * 获取销售总额
     * @return 销售总额
     */
    public BigDecimal getSaleAmount() {
        return saleAmount != null ? saleAmount : new BigDecimal(0);
    }

    /**
     * 设置销售总额
     * @param saleAmount 销售总额
     */
    public void setSaleAmount(BigDecimal saleAmount) {
        this.saleAmount = saleAmount;
    }

    /**
     * 获取应付金额
     * @return 应付金额
     */
    public BigDecimal getPayAmount() {
        return payAmount != null ? payAmount : new BigDecimal(0);
    }

    /**
     * 设置应付金额
     * @param payAmount 应付金额
     */
    public void setPayAmount(BigDecimal payAmount) {
        this.payAmount = payAmount;
    }

    /**
     * 获取已付金额
     * @return 已付金额
     */
    public BigDecimal getAlreadyPayAmount() {
        return alreadyPayAmount != null ? alreadyPayAmount : new BigDecimal(0);
    }

    /**
     * 设置已付金额
     * @param alreadyPayAmount 已付金额
     */
    public void setAlreadyPayAmount(BigDecimal alreadyPayAmount) {
        this.alreadyPayAmount = alreadyPayAmount;
    }

    /**
     * 获取未付金额
     * @return 未付金额
     */
    public BigDecimal getNotPayAmount() {
        return notPayAmount != null ? notPayAmount : new BigDecimal(0);
    }

    /**
     * 设置未付金额
     * @param notPayAmount 未付金额
     */
    public void setNotPayAmount(BigDecimal notPayAmount) {
        this.notPayAmount = notPayAmount;
    }

    /**
     * 获取平台所得金额
     * @return 平台所得金额
     */
    public BigDecimal getRateAmount() {
        return rateAmount != null ? rateAmount : new BigDecimal(0);
    }

    /**
     * 设置平台所得金额
     * @param rateAmount 平台所得金额
     */
    public void setRateAmount(BigDecimal rateAmount) {
        this.rateAmount = rateAmount;
    }
}
