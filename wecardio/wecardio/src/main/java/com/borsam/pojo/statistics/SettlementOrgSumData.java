package com.borsam.pojo.statistics;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * <pre>
 * @Description:
 * @author :tantian
 * @version: Ver 1.0
 * @Date: 2015-08-15 19:17
 * </pre>
 */
public class SettlementOrgSumData extends SettlementSumData {
    /**机构ID*/
    private Long oid;
    /**总销售金额*/
    private BigDecimal saleAmountTotal = BigDecimal.ZERO;
    /**总应收金额*/
    private BigDecimal receiveAmountTotal = BigDecimal.ZERO;
    /**总未收金额*/
    private BigDecimal notReceivedAmountTotal = BigDecimal.ZERO;
    /**总已收金额*/
    private BigDecimal alreadyReceivedAmountTotal = BigDecimal.ZERO;
    /**0：已统计完成，1：未统计完成*/
    private Integer isExistUnSettle = 0;

    public SettlementOrgSumData() {
    }

    public SettlementOrgSumData(Integer year, Integer month,
                                BigDecimal saleAmountTotal,BigDecimal receiveAmountTotal,
                                BigDecimal notReceivedAmountTotal,BigDecimal alreadyReceivedAmountTotal){
        super(year,month);
        this.saleAmountTotal = saleAmountTotal;
        this.receiveAmountTotal = receiveAmountTotal;
        this.notReceivedAmountTotal = notReceivedAmountTotal;
        this.alreadyReceivedAmountTotal = alreadyReceivedAmountTotal;
    }

    public SettlementOrgSumData(Long oid, Integer year, Integer month,Integer isExistUnSettle,
                                BigDecimal saleAmountTotal,BigDecimal receiveAmountTotal,
                                BigDecimal notReceivedAmountTotal,BigDecimal alreadyReceivedAmountTotal) {
        super(year,month);
        this.oid = oid;
        this.isExistUnSettle = isExistUnSettle;
        this.saleAmountTotal = saleAmountTotal;
        this.receiveAmountTotal = receiveAmountTotal;
        this.notReceivedAmountTotal = notReceivedAmountTotal;
        this.alreadyReceivedAmountTotal = alreadyReceivedAmountTotal;
    }

    public Integer getIsExistUnSettle() {
        return isExistUnSettle;
    }

    public void setIsExistUnSettle(Integer isExistUnSettle) {
        this.isExistUnSettle = isExistUnSettle;
    }

    public Long getOid() {
        return oid;
    }

    public void setOid(Long oid) {
        this.oid = oid;
    }

    public BigDecimal getSaleAmountTotal() {
        return saleAmountTotal;
    }

    public void setSaleAmountTotal(BigDecimal saleAmountTotal) {
        this.saleAmountTotal = saleAmountTotal;
    }

    public BigDecimal getReceiveAmountTotal() {
        return receiveAmountTotal;
    }

    public void setReceiveAmountTotal(BigDecimal receiveAmountTotal) {
        this.receiveAmountTotal = receiveAmountTotal;
    }

    public BigDecimal getNotReceivedAmountTotal() {
        return notReceivedAmountTotal;
    }

    public void setNotReceivedAmountTotal(BigDecimal notReceivedAmountTotal) {
        this.notReceivedAmountTotal = notReceivedAmountTotal;
    }

    public BigDecimal getAlreadyReceivedAmountTotal() {
        return alreadyReceivedAmountTotal;
    }

    public void setAlreadyReceivedAmountTotal(BigDecimal alreadyReceivedAmountTotal) {
        this.alreadyReceivedAmountTotal = alreadyReceivedAmountTotal;
    }
}
