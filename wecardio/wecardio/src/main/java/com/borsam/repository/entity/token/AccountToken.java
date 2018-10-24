package com.borsam.repository.entity.token;

import com.hiteam.common.base.repository.entity.BaseEntity;
import com.hiteam.common.base.repository.entity.LongEntity;

import javax.persistence.Column;
import javax.persistence.MappedSuperclass;
import javax.persistence.Transient;
import java.util.Date;

/**
 * Entity - Token基类
 * Created by tantian on 2015/6/27.
 */
@MappedSuperclass
public class AccountToken extends LongEntity {

    private String token;
    private Long updated;

    /**
     * 获取token
     * @return token
     */
    @Column(name = "token", nullable = false, length = 50)
    public String getToken() {
        return token;
    }

    /**
     * 设置token
     * @param token token
     */
    public void setToken(String token) {
        this.token = token;
    }

    /**
     * 获取时间
     * @return 时间
     */
    @Column(name = "updated", nullable = false)
    public Long getUpdated() {
        return updated;
    }

    /**
     * 设置时间
     * @param updated 时间
     */
    public void setUpdated(Long updated) {
        this.updated = updated;
    }

    /**
     * 判断是否已过期
     * @return 是否已过期
     */
    @Transient
    public boolean hasExpired() {
        return getUpdated() != null && new Date().after(new Date(getUpdated() * 1000));
    }
}
