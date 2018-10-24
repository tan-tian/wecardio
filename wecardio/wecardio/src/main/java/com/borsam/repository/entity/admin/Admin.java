package com.borsam.repository.entity.admin;

import com.hiteam.common.base.repository.entity.BaseEntity;
import com.hiteam.common.base.repository.entity.LongEntity;

import org.hibernate.search.annotations.DocumentId;
import org.hibernate.validator.constraints.Email;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotEmpty;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import java.util.Date;

/**
 * Entity - 平台管理员账号
 * Created by tantian on 2015/6/19.
 */
@Entity
@Table(name = "t_admin")
public class Admin extends LongEntity {

    private String username;            // 账号
    private String password;            // 密码
    private String email;               // Email
    private String name;                // 姓名
    private Boolean isEnabled;          // 是否启用
    private Boolean isLocked;           // 是否锁定
    private Integer loginFailureCount;  // 登录失败次数
    private Long lockedDate;            // 锁定日期
    private Long loginDate;             // 最后登录日期
    private String loginIp;             // 最后登录IP
    private Long createDate;            // 创建时间

    /**
     * 获取ID
     * @return ID
     */
    @DocumentId
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.TABLE, generator = "tableGenerator")
    @TableGenerator(name = "tableGenerator", table = "id_maker", pkColumnName = "maker_name",
            valueColumnName = "maker_value", pkColumnValue = "t_admin", allocationSize = 1)
    public Long getId() {
        return super.getId();
    }

    /**
     * 获取用户账号
     * @return 用户账号
     */
    @NotEmpty(groups = Save.class)
    @Pattern(regexp = "^[0-9a-z_A-Z\\u4e00-\\u9fa5]+$")
    @Length(min = 2, max = 20)
    @Column(name = "username", nullable = false, updatable = false, unique = true, length = 50)
    public String getUsername() {
        return username;
    }

    /**
     * 设置用户账号
     * @param username 用户账号
     */
    public void setUsername(String username) {
        this.username = username;
    }

    /**
     * 获取密码
     * @return 密码
     */
    @NotEmpty(groups = Save.class)
    @Pattern(regexp = "^[^\\s&\"<>]+$")
    @Length(min = 4, max = 20)
    @Column(name = "password", nullable = false, length = 40)
    public String getPassword() {
        return password;
    }

    /**
     * 设置密码
     * @param password 密码
     */
    public void setPassword(String password) {
        this.password = password;
    }

    /**
     * 获取Email
     * @return Email
     */
    @NotEmpty
    @Email
    @Length(max = 50)
    @Column(name = "email", nullable = false, length = 50)
    public String getEmail() {
        return email;
    }

    /**
     * 设置Email
     * @param email Email
     */
    public void setEmail(String email) {
        this.email = email;
    }

    /**
     * 获取姓名
     * @return 姓名
     */
    @Length(max = 200)
    @Column(name = "name", length = 200)
    public String getName() {
        return name;
    }

    /**
     * 设置姓名
     * @param name 姓名
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * 获取是否启用
     * @return 是否启用
     */
    @NotNull
    @Column(name = "is_enabled", nullable = false)
    public Boolean getIsEnabled() {
        return isEnabled;
    }

    /**
     * 设置是否启用
     * @param isEnabled 是否启用
     */
    public void setIsEnabled(Boolean isEnabled) {
        this.isEnabled = isEnabled;
    }

    /**
     * 获取是否锁定
     * @return 是否锁定
     */
    @Column(name = "is_locked", nullable = false)
    public Boolean getIsLocked() {
        return isLocked;
    }

    /**
     * 设置是否锁定
     * @param isLocked 是否锁定
     */
    public void setIsLocked(Boolean isLocked) {
        this.isLocked = isLocked;
    }

    /**
     * 获取连续登陆失败次数
     * @return 失败次数
     */
    @Column(name = "login_failure_count", nullable = false)
    public Integer getLoginFailureCount() {
        return loginFailureCount;
    }

    /**
     * 设置登陆失败次数
     * @param loginFailureCount  失败次数
     */
    public void setLoginFailureCount(Integer loginFailureCount) {
        this.loginFailureCount = loginFailureCount;
    }

    /**
     * 获取锁定日期
     * @return 锁定日期
     */
    @Column(name = "locked_date")
    public Long getLockedDate() {
        return lockedDate;
    }

    /**
     * 设置锁定日期
     * @param lockedDate 锁定日期
     */
    public void setLockedDate(Long lockedDate) {
        this.lockedDate = lockedDate;
    }

    /**
     * 获取最后登录日期
     * @return 最后登录日期
     */
    @Column(name = "login_date")
    public Long getLoginDate() {
        return loginDate;
    }

    /**
     * 设置最后登录日期
     * @param loginDate 登录日期
     */
    public void setLoginDate(Long loginDate) {
        this.loginDate = loginDate;
    }

    /**
     * 获取最后登录IP
     * @return IP
     */
    @Column(name = "login_ip", length = 20)
    public String getLoginIp() {
        return loginIp;
    }

    /**
     * 设置最后登录IP
     * @param loginIp IP
     */
    public void setLoginIp(String loginIp) {
        this.loginIp = loginIp;
    }

    /**
     * 获取创建日期
     * @return 创建日期
     */
    @Column(name = "create_date", nullable = false, updatable = false)
    public Long getCreateDate() {
        return createDate;
    }

    /**
     * 设置创建日期
     * @param createDate 创建日期
     */
    public void setCreateDate(Long createDate) {
        this.createDate = createDate;
    }

    /**
     * 保存前处理
     */
    @PrePersist
    public void prePersist() {
        this.setCreateDate(new Date().getTime() / 1000);
    }
}
