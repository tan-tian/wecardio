package com.borsam.repository.entity.patient;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.hiteam.common.base.repository.entity.BaseEntity;
import com.hiteam.common.base.repository.entity.LongEntity;
import com.hiteam.common.util.json.DateTimeSerializer;
import com.hiteam.common.web.I18Util;
import org.hibernate.search.annotations.DocumentId;

import javax.persistence.*;
import javax.validation.constraints.Digits;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.util.Date;

/**
 * Entity - 患者账户流水信息
 * Created by tantian on 2015/8/4.
 */
@Entity
@Table(name = "patient_wallet_history")
public class PatientWalletHistory extends LongEntity {

    /**
     * 交易类型
     */
    public enum Type {
        recharge,   // 充值
        refund,     // 退款
        buy,        // 购买支付
        consult,    // 诊单支付
        daichong    //代充
    }

    private Long uid;               // 患者ID
    private Long created;           // 创建时间
    private BigDecimal money;       // 金额
    private String verdict;         // 备注
    private Integer payStyle;       // 支付方式：0:钱包；1：支付宝
    private Long oid;               // 机构ID
    private Type type;              // 交易类型
    /**生成规则:yyyyMMdd+时间戳(精确到秒)+当前的主键ID值(patient_wallet_history.id)*/
    private String tradeNo;         // 诊单号
    private String payNo;           // 交易号
    private Integer success;        // 0-未返回 1-成功 2-失败
    private Long dcDid;				//代充医生ID
    private String definition;      //操作服务名称（服务包名称等）

    /**
     * 保存前处理
     */
    @PrePersist
    public void prePersist() {
        this.setCreated(new Date().getTime() / 1000);
    }

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
            valueColumnName = "maker_value", pkColumnValue = "patient_wallet_history", allocationSize = 1)
    public Long getId() {
        return super.getId();
    }

    /**
     * 获取患者ID
     * @return 患者ID
     */
    @NotNull
    @Column(name = "uid", nullable = false)
    public Long getUid() {
        return uid;
    }
    
    /**
     * 设置患者ID
     * @param uid 患者ID
     */
    public void setUid(Long uid) {
        this.uid = uid;
    }
    
    /**
     * 获取操作服务名称
     * @return
     */
    @JsonProperty
    @Column(name = "definition")
    public String getDefinition() {
		return definition;
	}
    
    /**
     * 设置操作的服务名称
     * @param 服务名称
     */
	public void setDefinition(String definition) {
		this.definition = definition;
	}

	/**
     * 获取代充医生ID
     * @return 代充医生ID
     */
    @Column(name = "help_doctor")
    public Long getDcDid() {
        return dcDid;
    }

    /**
     * 设置代充医生ID
     * @param dcDid 代充医生ID
     */
    public void setDcDid(Long dcDid) {
        this.dcDid = dcDid;
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
     * 获取金额
     * @return 金额
     */
    @JsonProperty
    @Min(0)
    @Digits(integer = 10, fraction = 2)
    @Column(name = "money", nullable = false, precision = 10, scale = 2)
    public BigDecimal getMoney() {
        return money;
    }

    /**
     * 设置金额
     * @param money 金额
     */
    public void setMoney(BigDecimal money) {
        this.money = money;
    }

    /**
     * 获取备注
     * @return 备注
     */
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
     * 获取支付方式
     * @return 支付方式
     */
    @Column(name = "pay_style")
    public Integer getPayStyle() {
        return payStyle;
    }

    /**
     * 设置支付方式
     * @param payStyle 支付方式
     */
    public void setPayStyle(Integer payStyle) {
        this.payStyle = payStyle;
    }

    /**
     * 获取机构ID
     * @return 机构ID
     */
    @Column(name = "to_oid", nullable = false)
    public Long getOid() {
        return oid;
    }

    /**
     * 设置机构ID
     * @param oid 机构ID
     */
    public void setOid(Long oid) {
        this.oid = oid;
    }

    /**
     * 获取交易类型
     * @return 交易类型
     */
    @NotNull
    @Column(name = "type", nullable = false)
    public Type getType() {
        return type;
    }

    /**
     * 设置交易类型
     * @param type 交易类型
     */
    public void setType(Type type) {
        this.type = type;
    }

    /**
     * 获取诊单号
     * @return 诊单号
     */
    @Column(name = "trade_no", nullable = false, length = 128)
    public String getTradeNo() {
        return tradeNo;
    }

    /**
     * 设置诊单号
     * @param tradeNo 诊单号
     */
    public void setTradeNo(String tradeNo) {
        this.tradeNo = tradeNo;
    }

    /**
     * 获取交易号
     * @return 交易号
     */
    @Column(name = "pay_no", length = 128)
    public String getPayNo() {
        return payNo;
    }

    /**
     * 设置交易号
     * @param payNo 交易号
     */
    public void setPayNo(String payNo) {
        this.payNo = payNo;
    }

    /**
     * 获取标识
     * @return 标识：0-未返回 1-成功 2-失败
     */
    @Column(name = "success", nullable = false)
    public Integer getSuccess() {
        return success;
    }

    /**
     * 设置标识
     * @param success 标识：0-未返回 1-成功 2-失败
     */
    public void setSuccess(Integer success) {
        this.success = success;
    }

    /**
     * 获取创建时间
     * @return 创建时间
     */
    @JsonProperty
    @JsonSerialize(using = DateTimeSerializer.class)
    @Transient
    public Date getCreateDate() {
        if (getCreated() != null && getCreated() != 0L) {
            return new Date(getCreated() * 1000);
        }
        return null;
    }

    /**
     * 获取交易号
     * @return 交易号
     */
    @JsonProperty
    @Transient
    public String getNo() {
        switch (getType()) {
            case recharge:
            case buy:
                return getPayNo();
            case refund:
            case consult:
                return getTradeNo();
        }
        return null;
    }

    /**
     * 获取交易类型名称
     * @return 交易类型名称
     */
    @JsonProperty
    @Transient
    public String getTypeName() {
        return I18Util.getMessage("patient.wallet.paytype." + getType().name());
    }
    
    /**
     * 获取交易金额正负值
     */
    @JsonProperty
    @Transient
    public String getTypeMoney() {
    	if (getType()==Type.buy || getType()==Type.consult)
    		return "-";
    	else
    		return "+";
    }
}
