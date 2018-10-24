package com.borsam.repository.entity.org;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.hiteam.common.base.repository.entity.BaseEntity;
import com.hiteam.common.base.repository.entity.LongEntity;
import com.hiteam.common.web.I18Util;
import org.hibernate.search.annotations.DocumentId;

import javax.persistence.*;
import javax.validation.constraints.Digits;
import javax.validation.constraints.Min;
import java.math.BigDecimal;

/**
 * Entity - 机构钱包流水
 * Created by tantian on 2015/8/10.
 */
@Entity
@Table(name = "organization_wallet_history")
public class OrganizationWalletHistory extends LongEntity {

    private Long oid;                   // 机构ID
    private BigDecimal money;           // 销售金额
    private BigDecimal rate;            // 分成比例
    private Long fromUid;               // 患者ID
    private Integer type;               // 类型：0-收入 1-转出
    private String fromTradeNo;         // 单据编号
    private Long created;               // 创建时间
    private String verdict;             // 备注
    private Integer ticketType;         // 单据类型：0-购买服务单据  1-诊单消费单据
    private Integer year;               // 年份
    private Integer month;              // 月份
    private Integer status;             // 状态：0-结算 1-未结算 (2-结算中，即已申请结算)

    private BigDecimal sumMoney;        // 累计余额（非持久字段）

    /**
     * 获取ID
     * @return ID
     */
    @Override
    @DocumentId
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.TABLE, generator = "tableGenerator")
    @TableGenerator(name = "tableGenerator", table = "id_maker", pkColumnName = "maker_name",
            valueColumnName = "maker_value", pkColumnValue = "organization_wallet_history", allocationSize = 1)
    public Long getId() {
        return super.getId();
    }

    /**
     * 获取机构标识
     * @return 机构标识
     */
    @Column(name = "oid", nullable = false)
    public Long getOid() {
        return oid;
    }

    /**
     * 设置机构标识
     * @param oid 机构标识
     */
    public void setOid(Long oid) {
        this.oid = oid;
    }

    /**
     * 获取销售金额
     * @return 销售金额
     */
    @JsonProperty
    @Min(0)
    @Digits(integer = 10, fraction = 2)
    @Column(name = "money", nullable = false, precision = 10, scale = 2)
    public BigDecimal getMoney() {
        return money;
    }

    /**
     * 设置销售金额
     * @param money 销售金额
     */
    public void setMoney(BigDecimal money) {
        this.money = money;
    }

    /**
     * 获取分成比例，单位（%）
     * @return 分成比例
     */
    @JsonProperty
    @Min(0)
    @Digits(integer = 10, fraction = 2)
    @Column(name = "rate", nullable = false, precision = 10, scale = 2)
    public BigDecimal getRate() {
        return rate;
    }

    /**
     * 设置分成比例
     * @param rate 分成比例
     */
    public void setRate(BigDecimal rate) {
        this.rate = rate;
    }

    /**
     * 获取患者标识
     * @return 患者标识
     */
    @Column(name = "from_uid", nullable = false)
    public Long getFromUid() {
        return fromUid;
    }

    /**
     * 设置患者标识
     * @param fromUid 患者标识
     */
    public void setFromUid(Long fromUid) {
        this.fromUid = fromUid;
    }

    /**
     * 获取类型
     * @return 类型：0-收入 1-转出
     */
    @JsonProperty
    @Column(name = "type", nullable = false)
    public Integer getType() {
        return type;
    }

    /**
     * 设置类型
     * @param type 类型：0-收入 1-转出
     */
    public void setType(Integer type) {
        this.type = type;
    }

    /**
     * 获取单据编号
     * @return 单据编号
     */
    @JsonProperty
    @Column(name = "from_trade_no", nullable = false, length = 100)
    public String getFromTradeNo() {
        return fromTradeNo;
    }

    /**
     * 设置单据编号
     * @param fromTradeNo 单据编号
     */
    public void setFromTradeNo(String fromTradeNo) {
        this.fromTradeNo = fromTradeNo;
    }

    /**
     * 获取创建时间
     * @return 创建时间
     */
    @JsonProperty
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
     * 获取备注
     * @return 备注
     */
    @JsonProperty
    @Lob
    @Column(name = "verdict", nullable = false)
    public String getVerdict() {
        return verdict;
    }

    /**
     * 设置备注
     * @param verdict 备注
     */
    public void setVerdict(String verdict) {
        this.verdict = verdict;
    }

    /**
     * 获取单据类型
     * @return 单据类型：0-购买服务单据  1-诊单消费单据
     */
    @JsonProperty
    @Column(name = "ticket_type")
    public Integer getTicketType() {
        return ticketType;
    }

    /**
     * 设置单据类型
     * @param ticketType 单据类型：0-购买服务单据  1-诊单消费单据
     */
    public void setTicketType(Integer ticketType) {
        this.ticketType = ticketType;
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
     * 获取状态
     * @return 状态：0-结算 1-未结算
     */
    public Integer getStatus() {
        return status;
    }

    /**
     * 设置状态
     * @param status 状态：0-结算 1-未结算
     */
    public void setStatus(Integer status) {
        this.status = status;
    }

    /**
     * 获取单据类型名称
     */
    @JsonProperty
    @Transient
    public String getTicketTypeName() {
        switch (getTicketType()) {
            case 0:
                return I18Util.getMessage("common.ticket.type.service");
            case 1:
                return I18Util.getMessage("common.ticket.type.consultation");
            default:
                return "";
        }
    }

    /**
     * 获取实际金额
     */
    @JsonProperty
    @Transient
    public BigDecimal getRealMoney() {
        return getMoney().multiply(getRate());
    }

    /**
     * 获取累计金额
     */
    @JsonProperty
    @Transient
    public BigDecimal getSumMoney() {
        return sumMoney;
    }

    public void setSumMoney(BigDecimal sumMoney) {
        this.sumMoney = sumMoney;
    }

}
