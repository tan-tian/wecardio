package com.borsam.repository.entity.org;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.hiteam.common.base.repository.entity.BaseEntity;
import com.hiteam.common.base.repository.entity.LongEntity;

import org.apache.commons.lang.StringUtils;
import org.hibernate.search.annotations.DocumentId;

import javax.persistence.*;
import javax.validation.constraints.Digits;
import javax.validation.constraints.Min;
import java.math.BigDecimal;
import java.util.Date;

/**
 * Entity - 机构钱包
 * Created by tantian on 2015/8/10.
 */
@Entity
@Table(name = "organization_wallet")
public class OrganizationWallet extends LongEntity {

    private Organization org;           // 机构
    private BigDecimal total;           // 销售金额
    private BigDecimal realTotal;       // 分成后实际金额（应付金额）
    private BigDecimal grandTotal;      // 累计分成金额（账户总额）
    private String bankUsername;        // 用户名
    private String bankNo;              // 银行卡号
    private String bankBranch;          // 开户行名称
    private Long lastTime;              // 更新时间

    /**
     * 保存前处理
     */
    @PrePersist
    public void prePersist() {
        this.setLastTime(new Date().getTime() / 1000);
    }

    /**
     * 更新前处理
     */
    @PreUpdate
    public void preUpdate() {
        this.setLastTime(new Date().getTime() / 1000);
    }

    @JsonProperty
    @DocumentId
    @Id
    @Column(name = "oid")
    public Long getId() {
        return super.getId();
    }

    /**
     * 获取机构
     * @return 机构
     */
    @OneToOne(fetch = FetchType.LAZY)
    @PrimaryKeyJoinColumn
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
     * 获取销售金额
     * @return 销售金额
     */
    @Min(0)
    @Digits(integer = 10, fraction = 2)
    @Column(name = "total", nullable = false, precision = 10, scale = 2)
    public BigDecimal getTotal() {
        return total;
    }

    /**
     * 设置销售金额
     * @param total 销售金额
     */
    public void setTotal(BigDecimal total) {
        this.total = total;
    }

    /**
     * 获取分成后实际金额
     * @return 分成后实际金额
     */
    @Min(0)
    @Digits(integer = 10, fraction = 2)
    @Column(name = "real_total", nullable = false, precision = 10, scale = 2)
    public BigDecimal getRealTotal() {
        return realTotal;
    }

    /**
     * 设置分成后实际金额
     * @param realTotal 分成后实际金额
     */
    public void setRealTotal(BigDecimal realTotal) {
        this.realTotal = realTotal;
    }

    /**
     * 获取累计分成金额（账户总额）
     * @return 累计分成金额（账户总额）
     */
    @Min(0)
    @Digits(integer = 10, fraction = 2)
    @Column(name = "grand_total", nullable = false, precision = 10, scale = 2)
    public BigDecimal getGrandTotal() {
        return grandTotal;
    }

    /**
     * 设置累计分成金额（账户总额）
     * @param grandTotal 累计分成金额（账户总额）
     */
    public void setGrandTotal(BigDecimal grandTotal) {
        this.grandTotal = grandTotal;
    }

    /**
     * 获取银行用户名
     * @return 银行用户名
     */
    @Column(name = "bank_username", nullable = false, length = 50)
    public String getBankUsername() {
        return bankUsername;
    }

    /**
     * 设置银行用户名
     * @param bankUsername 银行用户名
     */
    public void setBankUsername(String bankUsername) {
        this.bankUsername = bankUsername;
    }

    /**
     * 获取银行卡号
     * @return 银行卡号
     */
    @Column(name = "bank_no", nullable = false, length = 50)
    public String getBankNo() {
        return bankNo;
    }

    /**
     * 设置银行卡号
     * @param bankNo 银行卡号
     */
    public void setBankNo(String bankNo) {
        this.bankNo = bankNo;
    }

    /**
     * 获取开户行名称
     * @return 开户行名称
     */
    @Column(name = "bank_branch", nullable = false, length = 100)
    public String getBankBranch() {
        return bankBranch;
    }

    /**
     * 设置开户行名称
     * @param bankBranch 开户行名称
     */
    public void setBankBranch(String bankBranch) {
        this.bankBranch = bankBranch;
    }

    /**
     * 获取更新时间
     * @return 更新时间
     */
    @Column(name = "last_time")
    public Long getLastTime() {
        return lastTime;
    }

    /**
     * 设置更新时间
     * @param lastTime 更新时间
     */
    public void setLastTime(Long lastTime) {
        this.lastTime = lastTime;
    }

    /**
     * 获取提现金额
     * 提现金额 = 累计分成金额 - 应付金额
     * @return 提现金额
     */
    @Transient
    public BigDecimal getWithdrawTotal() {
        return getGrandTotal().subtract(getRealTotal());
    }

    /**
     * 是否已绑定银行账户
     * @return 是否绑定银行账户
     */
    @Transient
    public boolean isBindBank() {
        return StringUtils.isNotEmpty(getBankBranch()) && StringUtils.isNotEmpty(getBankNo()) && StringUtils.isNotEmpty(getBankUsername());
    }

    /**
     * 获取银行图标
     * @return 银行图标
     */
    @Transient
    public String getBankIcon() {
        if (isBindBank()) {
            // TODO o(>﹏<)o 这样判断会死人的，是否能通过卡号查询归属银行
            if ("中国银行".equals(getBankBranch())) {
                return "/resources/images/tixianshenqing/zhongguo.png";
            } else if ("中国工商银行".equals(getBankBranch())) {
                return "/resources/images/tixianshenqing/gonghang.png";
            }
        }
        return null;
    }

    /**
     * 获取银行卡号，只显示前6位和最后4位，中间以*号代替
     * @return 银行卡号
     */
    @Transient
    public String getBankNoWithMask() {
        if (isBindBank()) {
            String start = getBankNo().substring(0, 6);
            String end = getBankNo().substring(getBankNo().length() - 4);
            String mask = "";
            for (int i = 0; i < getBankNo().length() - 10; i++) {
                mask += "*";
            }
            return start + mask + end;
        }
        return null;
    }
}
