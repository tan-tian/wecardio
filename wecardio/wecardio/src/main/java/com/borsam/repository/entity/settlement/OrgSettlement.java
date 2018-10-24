package com.borsam.repository.entity.settlement;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.hiteam.common.base.repository.entity.BaseEntity;
import com.hiteam.common.base.repository.entity.LongEntity;

import org.hibernate.search.annotations.DocumentId;

import javax.persistence.*;
import javax.validation.constraints.Digits;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.util.Date;

/**
 * <pre>
 * @Description: 机构结算信息表
 * @author :tantian
 * @version: Ver 1.0
 * @Date: 2015-08-12 16:40
 * </pre>
 */
@JsonAutoDetect
@Entity
@Table(name = "orgainization_settlement")
public class OrgSettlement extends LongEntity {

    @Override
    @DocumentId
    @Id
    @GeneratedValue(strategy = GenerationType.TABLE, generator = "tableGenerator")
    @TableGenerator(name = "tableGenerator", table = "id_maker", pkColumnName = "maker_name",
            valueColumnName = "maker_value", pkColumnValue = "orgainization_settlement", allocationSize = 1)
    public Long getId() {
        return super.getId();
    }

    /**
     * 机构标识
     */
    private Long oid;
    /**
     * 年份
     */
    private Integer year;
    /**
     * 月份
     */
    private Integer month;
    /**
     * 销售金额
     */
    private BigDecimal saleAmount = new BigDecimal(0);
    /**
     * 应收金额:receive_amount
     */
    private BigDecimal receiveAmount = new BigDecimal(0);
    /**
     * 未收金额:not_received_amount
     */
    private BigDecimal notReceivedAmount = new BigDecimal(0);
    /**
     * 已收金额:already_received_amount
     */
    private BigDecimal alreadyReceivedAmount = new BigDecimal(0);
    /**
     * 创建时间
     */
    private Long created;
    /**
     * 修改时间
     */
    private Long modifyTime;
    /**
     * 是否完成同步标识
     */
    private Boolean isSynch = false;

    /**
     * 保存前处理
     */
    @PrePersist
    public void prePersist() {
        Long c = new Date().getTime() / 1000;
        this.setCreated(c);
        this.setModifyTime(c);
    }

    /**
     * 修改前处理
     */
    @PreUpdate
    public void preUpdate() {
        Long c = new Date().getTime() / 1000;
        this.setModifyTime(c);
    }

    @NotNull
    @Column(name = "is_synch", nullable = false)
    public Boolean getSynch() {
        return isSynch;
    }

    public void setSynch(Boolean synch) {
        isSynch = synch;
    }

    @NotNull
    @Column(name = "oid", nullable = false)
    public Long getOid() {
        return oid;
    }

    public void setOid(Long oid) {
        this.oid = oid;
    }

    @Min(2000)
    @NotNull
    @Column(name = "year", nullable = false)
    public Integer getYear() {
        return year;
    }

    public void setYear(Integer year) {
        this.year = year;
    }

    @Min(1)
    @Max(12)
    @NotNull
    @Column(name = "month", nullable = false)
    public Integer getMonth() {
        return month;
    }

    public void setMonth(Integer month) {
        this.month = month;
    }


    @NotNull
    @Min(0)
    @Digits(integer = 10, fraction = 2)
    @Column(name = "sale_amount", nullable = false, precision = 10, scale = 2)
    public BigDecimal getSaleAmount() {
        return saleAmount;
    }

    public void setSaleAmount(BigDecimal saleAmount) {
        this.saleAmount = saleAmount;
    }

    @NotNull
    @Min(0)
    @Digits(integer = 10, fraction = 2)
    @Column(name = "receive_amount", nullable = false, precision = 10, scale = 2)
    public BigDecimal getReceiveAmount() {
        return receiveAmount;
    }

    public void setReceiveAmount(BigDecimal receiveAmount) {
        this.receiveAmount = receiveAmount;
    }

    @NotNull
    @Min(0)
    @Digits(integer = 10, fraction = 2)
    @Column(name = "not_received_amount", nullable = false, precision = 10, scale = 2)
    public BigDecimal getNotReceivedAmount() {
        return notReceivedAmount;
    }

    public void setNotReceivedAmount(BigDecimal notReceivedAmount) {
        this.notReceivedAmount = notReceivedAmount;
    }

    @NotNull
    @Min(0)
    @Digits(integer = 10, fraction = 2)
    @Column(name = "already_received_amount", nullable = false, precision = 10, scale = 2)
    public BigDecimal getAlreadyReceivedAmount() {
        return alreadyReceivedAmount;
    }

    public void setAlreadyReceivedAmount(BigDecimal alreadyReceivedAmount) {
        this.alreadyReceivedAmount = alreadyReceivedAmount;
    }

    @NotNull
    @Min(0)
    @Column(name = "created", nullable = false)
    public Long getCreated() {
        return created;
    }

    public void setCreated(Long created) {
        this.created = created;
    }

    @NotNull
    @Min(0)
    @Column(name = "modify_time", nullable = false)
    public Long getModifyTime() {
        return modifyTime;
    }

    public void setModifyTime(Long modifyTime) {
        this.modifyTime = modifyTime;
    }
}
