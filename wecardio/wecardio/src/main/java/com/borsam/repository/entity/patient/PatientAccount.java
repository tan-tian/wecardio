package com.borsam.repository.entity.patient;

import com.hiteam.common.base.repository.entity.BaseEntity;
import com.hiteam.common.base.repository.entity.LongEntity;

import org.hibernate.search.annotations.DocumentId;
import org.hibernate.validator.constraints.Email;
import org.hibernate.validator.constraints.NotEmpty;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.Date;

/**
 * Entity - 患者账号
 * Created by tantian on 2015/6/17.
 */
@Entity
@Table(name = "account_patient")
public class PatientAccount extends LongEntity {

    private String email;                   // 用户账号，使用邮箱作为账号
    private String mobile;                  // 手机
    private String password;                // 密码，MD5加密
    private Boolean isDelete;               // 是否删除：0-否，1-是
    private Long created;                   // 创建时间，保存时间戳
    private PatientProfile patientProfile;  // 患者信息

    /**
     * 获取ID
     * @return ID
     */
    @DocumentId
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.TABLE, generator = "tableGenerator")
    @TableGenerator(name = "tableGenerator", table = "id_maker", pkColumnName = "maker_name",
            valueColumnName = "maker_value", pkColumnValue = "patient", allocationSize = 1)
    public Long getId() {
        return super.getId();
    }

    /**
     * 获取用户账号
     * @return 用户账号
     */
    @NotEmpty(groups = Save.class)
    @Email
    @Column(name = "email", nullable = false, updatable = false, unique = true, length = 50)
    public String getEmail() {
        return email;
    }

    /**
     * 设置用户账号
     * @param email 用户账号
     */
    public void setEmail(String email) {
        this.email = email;
    }

    /**
     * 获取手机号码
     * @return 手机号码
     */
    @Column(name = "mobile", length = 20)
    public String getMobile() {
        return mobile;
    }

    /**
     * 设置手机号码
     * @param mobile 手机号码
     */
    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    /**
     * 获取密码
     * @return 密码
     */
    @NotEmpty(groups = Save.class)
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
     * 获取是否删除
     * @return 是否删除
     */
    @NotNull
    @Column(name = "delete_state", nullable = false)
    public Boolean getIsDelete() {
        return isDelete;
    }

    /**
     * 设置是否删除
     * @param isDelete 是否删除
     */
    public void setIsDelete(Boolean isDelete) {
        this.isDelete = isDelete;
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
     * @param createDate 创建时间
     */
    public void setCreated(Long createDate) {
        this.created = createDate;
    }

    /**
     * 获取患者信息
     * @return 患者信息
     */
    @OneToOne(fetch = FetchType.LAZY)
    @PrimaryKeyJoinColumn
    public PatientProfile getPatientProfile() {
        return patientProfile;
    }

    /**
     * 设置患者信息
     * @param patientProfile 患者信息
     */
    public void setPatientProfile(PatientProfile patientProfile) {
        this.patientProfile = patientProfile;
    }

    /**
     * 保存前处理
     */
    @PrePersist
    public void prePersist() {
        this.setCreated(new Date().getTime() / 1000);
    }
}
