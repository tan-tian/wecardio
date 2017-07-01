package com.borsam.repository.entity.service;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.hiteam.common.base.repository.entity.BaseEntity;
import com.hiteam.common.base.repository.entity.LongEntity;

import org.hibernate.search.annotations.DocumentId;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotEmpty;

import javax.persistence.*;
import javax.validation.constraints.Digits;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.util.Date;
import java.util.Random;
import java.util.UUID;

/**
 * Entity - 服务配置
 * Created by Sebarswee on 2015/7/20.
 */
@Entity
@Table(name = "service_type")
@JsonAutoDetect
public class ServiceType extends LongEntity {

    private String name;            // 服务名称
    private BigDecimal from;        // 最低价
    private BigDecimal to;          // 最高价
    private Long created;           // 创建时间
    private Date createTime;
    private String uuid;//唯一标识
    /**是否启用*/
    private Boolean isEnabled = true;

    /**
     * 保存前处理
     */
    @PrePersist
    public void prePersist() {
        this.setCreated(new Date().getTime() / 1000);
        this.setUuid(UUID.randomUUID().toString().replaceAll("-",""));
    }

    @PostLoad
    public void postLoad() {
        this.setCreateTime(new Date(this.getCreated() * 1000L));
    }

    @Override
    @DocumentId
    @Id
    @Min(1)
    @Max(256)
    @Column(name = "type")
    public Long getId() {
        return super.getId();
    }

    @Column(name = "s_uuid", nullable = false, length = 128)
    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    @Transient
    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    /**
     * 获取服务名称
     * @return 服务名称
     */
    @NotEmpty
    @Length(max = 200)
    @Column(name = "type_name", nullable = false, length = 200)
    public String getName() {
        return name;
    }

    /**
     * 设置服务名称
     * @param name 设置服务名称
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * 是否启用此服务项
     * @return Boolean
     */
    @Column(name = "i_is_enabled", nullable = false)
    public Boolean getEnabled() {
        if (isEnabled == null) {
            isEnabled = true;
        }
        return isEnabled;
    }

    public void setEnabled(Boolean isEnabled) {
        this.isEnabled = isEnabled;
    }

    /**
     * 获取最低价
     * @return 最低价
     */
    @NotNull
    @Min(0)
    @Digits(integer = 10, fraction = 2)
    @Column(name = "price_from", nullable = false, precision = 10, scale = 2)
    public BigDecimal getFrom() {
        return from;
    }

    /**
     * 设置最低价
     * @param from 最低价
     */
    public void setFrom(BigDecimal from) {
        this.from = from;
    }

    /**
     * 获取最高价
     * @return 最高价
     */
    @NotNull
    @Min(0)
    @Digits(integer = 10, fraction = 2)
    @Column(name = "price_to", nullable = false, precision = 10, scale = 2)
    public BigDecimal getTo() {
        return to;
    }

    /**
     * 设置最高价
     * @param to 最高价
     */
    public void setTo(BigDecimal to) {
        this.to = to;
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
