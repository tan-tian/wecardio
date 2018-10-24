package com.borsam.pojo.statistics;

import java.math.BigDecimal;

/**
 * <pre>
 * @Description: 机构销售金额统计
 * @author :tantian
 * @version: Ver 1.0
 * @Date: 2015-08-12 16:22
 * </pre>
 */
public class OrgYearCountData extends YearCountData {
    /**
     * 应收金额
     */
    private BigDecimal receiveAmount = BigDecimal.ZERO;
    /**
     * 未收金额
     */
    private BigDecimal notReceivedAmount = BigDecimal.ZERO;
    /**
     * 已收金额
     */
    private BigDecimal alreadyReceivedAmount = BigDecimal.ZERO;

    public BigDecimal getReceiveAmount() {
        return receiveAmount;
    }

    public void setReceiveAmount(BigDecimal receiveAmount) {
        this.receiveAmount = receiveAmount;
    }

    public BigDecimal getNotReceivedAmount() {
        return notReceivedAmount;
    }

    public void setNotReceivedAmount(BigDecimal notReceivedAmount) {
        this.notReceivedAmount = notReceivedAmount;
    }

    public BigDecimal getAlreadyReceivedAmount() {
        return alreadyReceivedAmount;
    }

    public void setAlreadyReceivedAmount(BigDecimal alreadyReceivedAmount) {
        this.alreadyReceivedAmount = alreadyReceivedAmount;
    }
}
