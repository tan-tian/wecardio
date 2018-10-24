package com.borsam.repository.entity.patient;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.hiteam.common.base.repository.entity.BaseEntity;
import com.hiteam.common.util.json.DateTimeSerializer;

import javax.persistence.*;
import javax.validation.constraints.Min;
import java.util.Date;

/**
 * Entity - 患者购买的服务信息
 * Created by tantian on 2015/7/22.
 */
@Entity
@Table(name = "patient_service")
public class PatientService extends BaseEntity {

    private PatientServiceKey key;          // 复合主键
    private Integer totalCount;             // 总数
    private Integer count;                  // 剩余次数
    private Long expiredTime;               // 到期时间

    /**
     * 获取主键
     * @return 主键
     */
    @EmbeddedId
    public PatientServiceKey getKey() {
        return key;
    }

    /**
     * 设置主键
     * @param key 主键
     */
    public void setKey(PatientServiceKey key) {
        this.key = key;
    }

    /**
     * 获取总数
     * @return 总数
     */
    @JsonProperty
    @Min(0)
    @Column(name = "total_count")
    public Integer getTotalCount() {
        return totalCount;
    }

    /**
     * 设置总数
     * @param totalCount 总数
     */
    public void setTotalCount(Integer totalCount) {
        this.totalCount = totalCount;
    }

    /**
     * 获取剩余次数
     * @return 剩余次数
     */
    @JsonProperty
    @Min(0)
    @Column(name = "count", nullable = false)
    public Integer getCount() {
        return count;
    }

    /**
     * 设置剩余次数
     * @param count 剩余次数
     */
    public void setCount(Integer count) {
        this.count = count;
    }

    /**
     * 获取过期时间
     * @return 过期时间
     */
    @Column(name = "expired_time", nullable = false)
    public Long getExpiredTime() {
        return expiredTime;
    }

    /**
     * 设置过期时间
     * @param expiredTime 过期时间
     */
    public void setExpiredTime(Long expiredTime) {
        this.expiredTime = expiredTime;
    }

    /*------------------------------------ Trasient methods ------------------------------------*/

    /**
     * 获取机构ID
     * @return 机构ID
     */
    @JsonProperty
    @Transient
    public Long getOid() {
        return getKey().getOrg().getId();
    }

    /**
     * 获取机构名称
     * @return 机构名称
     */
    @JsonProperty
    @Transient
    public String getOrgName() {
        return getKey().getOrg().getName();
    }

    /**
     * 获取服务配置ID
     * @return 服务配置ID
     */
    @JsonProperty
    @Transient
    public Long getType() {
        return getKey().getServiceType().getId();
    }

    /**
     * 获取服务名称
     * @return 服务名称
     */
    @JsonProperty
    @Transient
    public String getServiceName() {
        return getKey().getServiceType().getName();
    }

    /**
     * 是否过期
     * @return 是否过期
     */
    @Transient
    public boolean hasExpired() {
        Long expiredTime = getExpiredTime();
        return expiredTime != null && expiredTime != 0L && new Date().after(new Date(expiredTime * 1000));
    }

    /**
     * 获取过期时间
     * @return 过期时间
     */
    @JsonProperty
    @JsonSerialize(using = DateTimeSerializer.class)
    @Transient
    public Date getExpiredDate() {
        if (getExpiredTime() != null && getExpiredTime() != 0L) {
            return new Date(getExpiredTime() * 1000);
        }
        return null;
    }
}
