package com.borsam.repository.entity.org;

import com.hiteam.common.base.repository.entity.BaseEntity;
import com.hiteam.common.base.repository.entity.LongEntity;

import org.hibernate.search.annotations.DocumentId;

import javax.persistence.*;

/**
 * Entity - 机构钱包验证
 * Created by Sebarswee on 2015/8/10.
 */
@Entity
@Table(name = "organization_wallet_verify")
public class OrganizationWalletVerify extends LongEntity {

    private String password;            // 密码
    private String token;               // token
    private Long tokenTime;             // token时间
    private Long lockTime;              // 锁定时间
    private Integer failureNum;         // 错误次数

    @Override
    @DocumentId
    @Id
    @Column(name = "oid")
    public Long getId() {
        return super.getId();
    }

    /**
     * 获取密码
     * @return 密码
     */
    @Column(name = "password", nullable = false, length = 50)
    public String getPassword() {
        return password;
    }

    /**
     * 设置密码
     * @param password 设置密码
     */
    public void setPassword(String password) {
        this.password = password;
    }

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
     * 获取token时间
     * @return token时间
     */
    @Column(name = "token_time", nullable = false)
    public Long getTokenTime() {
        return tokenTime;
    }

    /**
     * 设置token时间
     * @param tokenTime 时间
     */
    public void setTokenTime(Long tokenTime) {
        this.tokenTime = tokenTime;
    }

    /**
     * 获取锁定时间
     * @return 锁定时间
     */
    @Column(name = "lock_time", nullable = false)
    public Long getLockTime() {
        return lockTime;
    }

    /**
     * 设置锁定时间
     * @param lockTime 锁定时间
     */
    public void setLockTime(Long lockTime) {
        this.lockTime = lockTime;
    }

    /**
     * 获取失败次数
     * @return 失败次数
     */
    @Column(name = "failure_num", nullable = false)
    public Integer getFailureNum() {
        return failureNum;
    }

    /**
     * 设置失败次数
     * @param failureNum 失败次数
     */
    public void setFailureNum(Integer failureNum) {
        this.failureNum = failureNum;
    }

    /*----------------------- Trasient methods -----------------------*/

    /**
     * 获取是否锁定
     * @return 是否锁定
     */
    @Transient
    public boolean isLock() {
        return getLockTime() != null && getLockTime() != 0L;
    }
}
