package com.borsam.repository.entity.extract;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.hiteam.common.base.repository.entity.BaseEntity;
import com.hiteam.common.base.repository.entity.LongEntity;

import org.hibernate.search.annotations.DocumentId;

import javax.persistence.*;
import javax.validation.constraints.Digits;
import javax.validation.constraints.Min;
import java.math.BigDecimal;
import java.util.Date;

/**
 * Entity - 结算月份信息表
 * Created by tantian on 2015/8/10.
 */
@Entity
@Table(name = "t_month_clear")
public class MonthClear extends LongEntity {

    private ExtractOrder order;         // 提现单据
    private Integer year;               // 年份
    private Integer month;              // 月份
    private BigDecimal applyMoney;      // 金额
    private BigDecimal outMoney;        // 出账金额
    private Long outTime;               // 出账时间
    private Long created;               // 创建时间

    /**
     * 保存前处理
     */
    @PrePersist
    public void prePersist() {
        this.setCreated(new Date().getTime() / 1000);
    }

    @Override
    @JsonProperty
    @DocumentId
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.TABLE, generator = "tableGenerator")
    @TableGenerator(name = "tableGenerator", table = "id_maker", pkColumnName = "maker_name",
            valueColumnName = "maker_value", pkColumnValue = "t_month_clear", allocationSize = 1)
    public Long getId() {
        return super.getId();
    }

    /**
     * 获取提现单据
     * @return 提现单据
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id", nullable = false)
    public ExtractOrder getOrder() {
        return order;
    }

    /**
     * 设置提现单据
     * @param order 提现单据
     */
    public void setOrder(ExtractOrder order) {
        this.order = order;
    }

    /**
     * 获取年份
     * @return 年份
     */
    @JsonProperty
    @Column(name = "year")
    public Integer getYear() {
        return year;
    }

    /**
     * 设置年份
     * @param year 年份
     */
    public void setYear(Integer year) {
        this.year = year;
    }

    /**
     * 获取月份
     * @return 月份
     */
    @JsonProperty
    @Column(name = "month")
    public Integer getMonth() {
        return month;
    }

    /**
     * 设置月份
     * @param month 月份
     */
    public void setMonth(Integer month) {
        this.month = month;
    }

    /**
     * 获取金额
     * @return 金额
     */
    @JsonProperty
    @Min(0)
    @Digits(integer = 10, fraction = 2)
    @Column(name = "apply_money", precision = 10, scale = 2)
    public BigDecimal getApplyMoney() {
        return applyMoney;
    }

    /**
     * 设置金额
     * @param applyMoney 金额
     */
    public void setApplyMoney(BigDecimal applyMoney) {
        this.applyMoney = applyMoney;
    }

    /**
     * 获取出账金额
     * @return 出账金额
     */
    @Min(0)
    @Digits(integer = 10, fraction = 2)
    @Column(name = "out_money", precision = 10, scale = 2)
    public BigDecimal getOutMoney() {
        return outMoney;
    }

    /**
     * 设置出账金额
     * @param outMoney 出账金额
     */
    public void setOutMoney(BigDecimal outMoney) {
        this.outMoney = outMoney;
    }

    /**
     * 获取出账时间
     * @return 出账时间
     */
    @Column(name = "out_time")
    public Long getOutTime() {
        return outTime;
    }

    /**
     * 设置出账时间
     * @param outTime 出账时间
     */
    public void setOutTime(Long outTime) {
        this.outTime = outTime;
    }

    /**
     * 获取创建时间
     * @return 创建时间
     */
    @Column(name = "created", nullable = false, updatable = false)
    public Long getCreated() {
        return created;
    }

    /**
     * 设置创建时间
     * @param created 创建时间
     */
    public void setCreated(Long created) {
        this.created = created;
    }
}
