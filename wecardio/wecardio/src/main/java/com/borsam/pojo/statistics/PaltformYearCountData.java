package com.borsam.pojo.statistics;

import java.math.BigDecimal;

/**
 * <pre>
 * @Description: 平台销售金额统计
 * @author :Zhang zhongtao
 * @version: Ver 1.0
 * @Date: 2015-08-12 16:22
 * </pre>
 */
public class PaltformYearCountData extends YearCountData {
    /**
     * 应付金额
     */
    private BigDecimal payAmount = BigDecimal.ZERO;
    /**
     * 未付金额
     */
    private BigDecimal notPayAmount = BigDecimal.ZERO;
    /**
     * 已付金额
     */
    private BigDecimal alreadyPayAmount = BigDecimal.ZERO;
    /**
     * 平台所得金额
     */
    private BigDecimal rateAmount = BigDecimal.ZERO;

    public BigDecimal getRateAmount() {
        return rateAmount;
    }

    public void setRateAmount(BigDecimal rateAmount) {
        this.rateAmount = rateAmount;
    }

    public BigDecimal getPayAmount() {
        return payAmount;
    }

    public void setPayAmount(BigDecimal payAmount) {
        this.payAmount = payAmount;
    }

    public BigDecimal getNotPayAmount() {
        return notPayAmount;
    }

    public void setNotPayAmount(BigDecimal notPayAmount) {
        this.notPayAmount = notPayAmount;
    }

    public BigDecimal getAlreadyPayAmount() {
        return alreadyPayAmount;
    }

    public void setAlreadyPayAmount(BigDecimal alreadyPayAmount) {
        this.alreadyPayAmount = alreadyPayAmount;
    }
}
