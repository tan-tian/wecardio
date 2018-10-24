package com.borsam.repository.entity.service;

import com.hiteam.common.base.repository.entity.BaseEntity;
import com.hiteam.common.base.repository.entity.LongEntity;

import javax.persistence.*;
import javax.validation.constraints.Digits;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.util.Date;

/**
 * Entity - 服务项
 * Created by tantian on 2015/7/20.
 */
@Entity
@IdClass(ServiceKey.class)
@Table(name = "service")
public class Service extends LongEntity {

    private Long oid;               // 机构ID
    private Long type;              // 服务配置ID
    /**
     * 服务编号
     */
    private String code;
    /**
     * 价格
     */
    private BigDecimal price = new BigDecimal(0);
    /**
     * 是否24小时服务
     */
    private Boolean isAllDay;
    /**
     * 创建时间
     */
    private Integer created;
    private String remark;
    /**
     * 是否启用
     */
    private Boolean isEnabled = true;
    /**
     * 服务项名称
     */
    private String name;
    /**
     * 机构名称
     */
    private String orgName;

    /**
     * 保存前处理
     */
    @PrePersist
    public void prePersist() {
        Long c = new Date().getTime() / 1000;
        this.setCreated(c.intValue());
        //编号由 机构ID-服务项ID 组成
        this.setCode(String.valueOf(this.getOid()) + "-" + String.valueOf(this.getType()));
    }

    @PreUpdate
    public void preUpdate() {
        //编号由 机构ID-服务项ID 组成
        this.setCode(String.valueOf(this.getOid()) + "-" + String.valueOf(this.getType()));
    }

    @Override
    @Transient
    public Long getId() {
        return super.getId();
    }

    /**
     * 获取机构ID
     *
     * @return 机构ID
     */
    @Id
    public Long getOid() {
        return oid;
    }

    @Transient
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Transient
    public String getOrgName() {
        return orgName;
    }

    public void setOrgName(String orgName) {
        this.orgName = orgName;
    }

    /**
     * @return
     */
    @Column(name = "s_code")
    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    /**
     * 是否启用此服务项
     *
     * @return Boolean
     */
    @Column(name = "i_is_enabled", nullable = false)
    public Boolean getIsEnabled() {
        return isEnabled;
    }

    public void setIsEnabled(Boolean isEnabled) {
        this.isEnabled = isEnabled;
    }

    /**
     * 设置机构ID
     *
     * @param oid 机构ID
     */
    public void setOid(Long oid) {
        this.oid = oid;
    }

    /**
     * 获取服务配置ID
     *
     * @return 服务配置ID
     */
    @Id
    public Long getType() {
        return type;
    }

    /**
     * 设置服务配置ID
     *
     * @param type 服务配置ID
     */
    public void setType(Long type) {
        this.type = type;
    }

    /**
     * 获取价格
     *
     * @return 价格
     */
    @NotNull
    @Min(0)
    @Digits(integer = 10, fraction = 2)
    @Column(name = "price", nullable = false, precision = 10, scale = 2)
    public BigDecimal getPrice() {
        return price;
    }

    /**
     * 设置价格
     *
     * @param price 价格
     */
    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    /**
     * 获取是否24小时服务
     *
     * @return 是否24小时服务
     */
    @Column(name = "all_day", nullable = false)
    public Boolean getIsAllDay() {
        return isAllDay;
    }

    /**
     * 设置是否24小时服务
     *
     * @param isAllDay 是否24小时服务
     */
    public void setIsAllDay(Boolean isAllDay) {
        this.isAllDay = isAllDay;
    }

    /**
     * 获取创建时间
     *
     * @return 创建时间
     */
    @Column(name = "created", nullable = false, updatable = false)
    public Integer getCreated() {
        return created;
    }

    /**
     * 设置创建时间
     *
     * @param created 创建时间
     */
    public void setCreated(Integer created) {
        this.created = created;
    }

    @Max(500)
    @Column(name = "s_remark")
    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }
}
