package com.borsam.repository.entity.extract;

import com.hiteam.common.base.repository.entity.BaseEntity;
import com.hiteam.common.base.repository.entity.LongEntity;

import org.hibernate.search.annotations.DocumentId;

import javax.persistence.*;
import javax.validation.constraints.Digits;
import javax.validation.constraints.Min;
import java.math.BigDecimal;
import java.util.Date;

/**
 * Entity - 出账记录
 * Created by tantian on 2015/8/9.
 */
@Entity
@Table(name = "t_out_info")
public class OutInfo extends LongEntity {

    private ExtractOrder order;         // 提现单据
    private Integer type;               // 支付方式：0-银行转账 1-现金支付
    private BigDecimal money;           // 出账金额
    private Integer bankType;           // 银行类型：见枚举配置
    private String accountName;         // 银行账户
    private String bankNo;              // 银行卡号
    private String bankName;            // 开户行名称
    private String bankSeq;             // 银行流水号
    private Long outTime;               // 出账时间
    private Long createId;              // 创建人ID
    private String createName;          // 创建人名称
    private Long created;               // 创建时间

    /**
     * 保存前处理
     */
    @PrePersist
    public void prePersist() {
        this.setCreated(new Date().getTime() / 1000);
    }

    @Override
    @DocumentId
    @Id
    @Column(name = "id")
    public Long getId() {
        return super.getId();
    }

    /**
     * 获取提现单据
     * @return 提现单据
     */
    @OneToOne(fetch = FetchType.LAZY)
    @PrimaryKeyJoinColumn
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
     * 获取支付方式
     * @return 支付方式：0-银行转账 1-现金支付
     */
    @Column(name = "type")
    public Integer getType() {
        return type;
    }

    /**
     * 设置支付方式
     * @param type 支付方式：0-银行转账 1-现金支付
     */
    public void setType(Integer type) {
        this.type = type;
    }

    /**
     * 获取出账金额
     * @return 出账金额
     */
    @Min(0)
    @Digits(integer = 10, fraction = 2)
    @Column(name = "out_money", precision = 10, scale = 2)
    public BigDecimal getMoney() {
        return money;
    }

    /**
     * 设置出账金额
     * @param money 出账金额
     */
    public void setMoney(BigDecimal money) {
        this.money = money;
    }

    /**
     * 获取银行类型
     * @return 银行类型
     */
    @Column(name = "bank_type")
    public Integer getBankType() {
        return bankType;
    }

    /**
     * 设置银行类型
     * @param bankType 银行类型
     */
    public void setBankType(Integer bankType) {
        this.bankType = bankType;
    }

    /**
     * 获取银行账户名称
     * @return 银行账户名称
     */
    @Column(name = "account_name", length = 64)
    public String getAccountName() {
        return accountName;
    }

    /**
     * 设置银行账户名称
     * @param accountName 银行账户名称
     */
    public void setAccountName(String accountName) {
        this.accountName = accountName;
    }

    /**
     * 获取银行卡号
     * @return 银行卡号
     */
    @Column(name = "bank_no", length = 64)
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
    @Column(name = "bank_name", length = 128)
    public String getBankName() {
        return bankName;
    }

    /**
     * 设置开户行名称
     * @param bankName 开户行名称
     */
    public void setBankName(String bankName) {
        this.bankName = bankName;
    }

    /**
     * 获取银行流水号
     * @return 银行流水号
     */
    @Column(name = "bank_seq", length = 128)
    public String getBankSeq() {
        return bankSeq;
    }

    /**
     * 设置银行流水号
     * @param bankSeq 银行流水号
     */
    public void setBankSeq(String bankSeq) {
        this.bankSeq = bankSeq;
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
     * 获取银行图标
     * @return 银行图标
     */
    @Transient
    public String getBankIcon() {
        if (getType() == 0) {
            switch (getBankType()) {
                case 0:
                    // 中国银行
                    return "/resources/images/tixianshenqing/zhongguo.png";
                case 1:
                    // 中国农业银行
                    return "/resources/images/bangding/nonghang.jpg";
                case 2:
                    // 中国工商银行
                    return "/resources/images/tixianshenqing/gonghang.png";
                case 3:
                    // 中国建设银行
                    return "/resources/images/bangding/jianshe.png";
                case 4:
                    // 招商银行
                    return "/resources/images/bangding/zhaoshang.png";
                default:
                    return null;
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
        if (getType() == 0) {
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
