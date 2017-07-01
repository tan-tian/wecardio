package com.borsam.pojo.wallet;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.math.BigDecimal;

/**
 * Bean - 机构汇总信息
 * Created by Sebarswee on 2015/8/15.
 */
public class OrgSummary {

    private String orgName;                 // 机构名称
    private BigDecimal saleAmount;          // 销售总额
    private BigDecimal payAmount;           // 已收金额
    private BigDecimal unPayAmount;         // 未收金额

    public OrgSummary(String orgName, BigDecimal saleAmount, BigDecimal payAmount, BigDecimal unPayAmount) {
        this.orgName = orgName;
        this.saleAmount = saleAmount;
        this.payAmount = payAmount;
        this.unPayAmount = unPayAmount;
    }

    /**
     * 获取机构名称
     * @return 机构名称
     */
    @JsonProperty
    public String getOrgName() {
        return orgName;
    }

    /**
     * 设置机构名称
     * @param orgName 机构名称
     */
    public void setOrgName(String orgName) {
        this.orgName = orgName;
    }

    /**
     * 获取销售总额
     * @return 销售总额
     */
    @JsonProperty
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
     * 获取已收金额
     * @return 已收金额
     */
    @JsonProperty
    public BigDecimal getPayAmount() {
        return payAmount != null ? payAmount : new BigDecimal(0);
    }

    /**
     * 设置已收金额
     * @param payAmount 已收金额
     */
    public void setPayAmount(BigDecimal payAmount) {
        this.payAmount = payAmount;
    }

    /**
     * 获取未收金额
     * @return 未收金额
     */
    @JsonProperty
    public BigDecimal getUnPayAmount() {
        return unPayAmount != null ? unPayAmount : new BigDecimal(0);
    }

    /**
     * 设置未收金额
     * @param unPayAmount 未收金额
     */
    public void setUnPayAmount(BigDecimal unPayAmount) {
        this.unPayAmount = unPayAmount;
    }

}
