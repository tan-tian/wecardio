package com.borsam.repository.entity.extract;

import com.borsam.repository.entity.org.Organization;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.hiteam.common.base.repository.entity.BaseEntity;
import com.hiteam.common.base.repository.entity.LongEntity;
import com.hiteam.common.util.json.DateSerializer;
import com.hiteam.common.web.I18Util;
import org.hibernate.search.annotations.DocumentId;

import javax.persistence.*;
import javax.validation.constraints.Digits;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Entity - 提现单据
 * Created by Sebarswee on 2015/8/9.
 */
@Entity
@Table(name = "t_extract_order")
public class ExtractOrder extends LongEntity {

    private Organization org;           // 机构
    private String orderNo;             // 单据编号
    private BigDecimal money;           // 提现金额
    private Integer state;              // 单据状态：0-待修改   1-待审核  2-已出账  3-已完成
    private Long createId;              // 创建人ID
    private String createName;          // 创建人名称
    private Long guid;                  // guid
    private Long created;               // 创建时间
    private OutInfo outInfo;            // 出账信息

    /**
     * 月结信息
     */
    private List<MonthClear> monthClears = new ArrayList<MonthClear>();

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
            valueColumnName = "maker_value", pkColumnValue = "t_extract_order", allocationSize = 1)
    public Long getId() {
        return super.getId();
    }

    /**
     * 获取机构
     * @return 机构
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "oid", nullable = false)
    public Organization getOrg() {
        return org;
    }

    /**
     * 设置机构
     * @param org 机构
     */
    public void setOrg(Organization org) {
        this.org = org;
    }

    /**
     * 获取单据编号
     * @return 单据编号
     */
    @JsonProperty
    @Column(name = "order_no", nullable = false, length = 50)
    public String getOrderNo() {
        return orderNo;
    }

    /**
     * 设置单据编号
     * @param orderNo 单据编号
     */
    public void setOrderNo(String orderNo) {
        this.orderNo = orderNo;
    }

    /**
     * 获取提现金额
     * @return 提现金额
     */
    @JsonProperty
    @NotNull
    @Min(0)
    @Digits(integer = 10, fraction = 2)
    @Column(name = "money", nullable = false, precision = 10, scale = 2)
    public BigDecimal getMoney() {
        return money;
    }

    /**
     * 设置提现金额
     * @param money 提现金额
     */
    public void setMoney(BigDecimal money) {
        this.money = money;
    }

    /**
     * 获取单据状态
     * @return 单据状态：0-待修改   1-待审核  2-已出账  3-已完成
     */
    @Column(name = "state", nullable = false)
    public Integer getState() {
        return state;
    }

    /**
     * 设置单据状态
     * @param state 单据状态：0-待修改   1-待审核  2-已出账  3-已完成
     */
    public void setState(Integer state) {
        this.state = state;
    }

    /**
     * 获取创建人ID
     * @return 创建人ID
     */
    @Column(name = "create_id")
    public Long getCreateId() {
        return createId;
    }

    /**
     * 设置创建人ID
     * @param createId 创建人ID
     */
    public void setCreateId(Long createId) {
        this.createId = createId;
    }

    /**
     * 获取创建人名称
     * @return 创建人名称
     */
    @JsonProperty
    @Column(name = "create_name", length = 64)
    public String getCreateName() {
        return createName;
    }

    /**
     * 设置创建人名称
     * @param createName 创建人名称
     */
    public void setCreateName(String createName) {
        this.createName = createName;
    }

    /**
     * 获取guid
     * @return guid
     */
    @Column(name = "guid")
    public Long getGuid() {
        return guid;
    }

    /**
     * 设置guid
     * @param guid guid
     */
    public void setGuid(Long guid) {
        this.guid = guid;
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

    /**
     * 获取月结信息
     * @return 月结信息
     */
    @OneToMany(mappedBy = "order", fetch = FetchType.LAZY, cascade = CascadeType.REMOVE)
    public List<MonthClear> getMonthClears() {
        return monthClears;
    }

    /**
     * 设置月结信息
     * @param monthClears 月结信息
     */
    public void setMonthClears(List<MonthClear> monthClears) {
        this.monthClears = monthClears;
    }

    /**
     * 获取出账信息
     * @return 出账信息
     */
    @OneToOne(fetch = FetchType.LAZY)
    @PrimaryKeyJoinColumn
    public OutInfo getOutInfo() {
        return outInfo;
    }

    /**
     * 设置出账信息
     * @param outInfo 出账信息
     */
    public void setOutInfo(OutInfo outInfo) {
        this.outInfo = outInfo;
    }


    /**
     * 获取创建日期
     * @return 创建日期
     */
    @JsonProperty
    @JsonSerialize(using = DateSerializer.class)
    @Transient
    public Date getCreateDate() {
        if (getCreated() != null && getCreated() != 0L) {
            return new Date(getCreated() * 1000);
        }
        return null;
    }

    /**
     * 获取机构名称
     * @return 机构名称
     */
    @JsonProperty
    @Transient
    public String getOrgName() {
        return getOrg().getName();
    }

    /**
     * 获取状态名称
     * @return 状态名称
     */
    @JsonProperty
    @Transient
    public String getStateName() {
        if (getState() == 0) {
            return I18Util.getMessage("common.enum.withdraw.state.edit");
        } else if (getState() == 1) {
            return I18Util.getMessage("common.enum.withdraw.state.audit");
        } else if (getState() == 2) {
            return I18Util.getMessage("common.enum.withdraw.state.confirm");
        } else if (getState() == 3) {
            return I18Util.getMessage("common.enum.withdraw.state.finish");
        } else {
            return null;
        }
    }
}
