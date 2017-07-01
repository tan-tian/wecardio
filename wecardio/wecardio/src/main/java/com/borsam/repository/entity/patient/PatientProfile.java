package com.borsam.repository.entity.patient;

import com.borsam.repository.entity.common.Language;
import com.borsam.repository.entity.doctor.DoctorProfile;
import com.borsam.repository.entity.org.Organization;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.hiteam.common.base.repository.entity.BaseEntity;
import com.hiteam.common.base.repository.entity.LongEntity;
import com.hiteam.common.util.json.DateSerializer;
import org.hibernate.search.annotations.DocumentId;
import org.hibernate.validator.constraints.Email;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotEmpty;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

/**
 * Entity - 患者信息
 * Created by Sebarswee on 2015/7/17.
 */
@Entity
@Table(name = "profile_patient")
public class PatientProfile extends LongEntity {

    /**
     * 性别
     */
    public enum Gender {
        unset, female, male, other
    }

    /**
     * 绑定类型
     */
    public enum BindType {
        org, self, other, unknow
    }

    private PatientAccount patientAccount;  // 患者账号
    private String firstName;               // firstName
    private String secondName;              // secondName
    private Long birthday;                  // 出生日期
    private String mobile;                  // 手机
    private String email;                   // Email
    private Gender sex;                     // 性别：0-女 1-男 2-其他
    private Long faceTime;                  // 头像设置时间
    private String indication;              // 病症
    private String medicine;                // 用药情况
    private String address;                 // 地址
    private Language language;              // 语种
    private Long created;                   // 创建时间
    private Organization org;               // 所属机构
    private DoctorProfile doctor;           // 所属医生
    private BindType bindType;              // 绑定类型：0-机构创建 1-用户选择 2-其他 3-未知
    private Long bindTime;                  // 绑定时间
    private Long loginTime;                 // 最后登录时间
    private Boolean isDelete;               // 是否删除：0-否 1-是
    private String lastConsultation;        // 上次报告结果
    private Boolean isWalletActive;         // 钱包是否激活：0-否 1-是
    private Integer nationCode;             // 国家
    private String nationName;              // 国家
    private Integer regionCode;             // 区域
    private String regionName;              // 区域
    private Integer provinceCode;           // 省份
    private String provinceName;            // 省份
    private Integer cityCode;               // 城市
    private String cityName;                // 城市
    private String postCode;                // 邮编
    private String thumbPath;               // 缩略图地址
    private String headPath;                // 头像路径
    private String fullName;                // 患者全名

    /**
     * 活动记录
     */
    private Set<PatientActivity> patientActivitys = new HashSet<PatientActivity>();

    /**
     * 医嘱
     */
    private Set<PatientDoctorOpinion> patientDoctorOpinions = new HashSet<PatientDoctorOpinion>();

    /**
     * 保存前处理
     */
    @PrePersist
    public void prePersist() {
        this.setCreated(new Date().getTime() / 1000);
    }

    @JsonProperty
    @DocumentId
    @Id
    @Column(name = "id")
    public Long getId() {
        return super.getId();
    }

    /**
     * 获取患者账号
     * @return 患者账号
     */
    @OneToOne(fetch = FetchType.LAZY)
    @PrimaryKeyJoinColumn
    public PatientAccount getPatientAccount() {
        return patientAccount;
    }

    /**
     * 设置患者账号
     * @param patientAccount 患者账号
     */
    public void setPatientAccount(PatientAccount patientAccount) {
        this.patientAccount = patientAccount;
    }

    /**
     * 获取firstName
     * @return firstName
     */
    @JsonProperty
    @NotEmpty
    @Length(max = 50)
    @Column(name = "first_name", nullable = false, length = 50)
    public String getFirstName() {
        return firstName;
    }

    /**
     * 设置firstName
     * @param firstName firstName
     */
    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    /**
     * 获取secondName
     * @return secondName
     */
    @JsonProperty
    @NotEmpty
    @Length(max = 50)
    @Column(name = "second_name", nullable = false, length = 50)
    public String getSecondName() {
        return secondName;
    }

    /**
     * 设置secondName
     * @param secondName secondName
     */
    public void setSecondName(String secondName) {
        this.secondName = secondName;
    }

    /**
     * 获取出生日期
     * @return 出生日期
     */
    @JsonProperty
    @Column(name = "birthday", nullable = false)
    public Long getBirthday() {
        return birthday;
    }

    /**
     * 设置出生日期
     * @param birthday 出生日期
     */
    public void setBirthday(Long birthday) {
        this.birthday = birthday;
    }

    /**
     * 获取手机
     * @return 手机
     */
    @JsonProperty
    @Length(max = 20)
    @Column(name = "mobile", nullable = false, length = 20)
    public String getMobile() {
        return mobile;
    }

    /**
     * 设置手机
     * @param mobile 手机
     */
    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    /**
     * 获取Email
     * @return Email
     */
    @JsonProperty
    @Email
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
     * 获取性别
     * @return 性别
     */
    @JsonProperty
    @Column(name = "sex", nullable = false)
    public Gender getSex() {
        return sex;
    }

    /**
     * 设置性别
     * @param sex 性别
     */
    public void setSex(Gender sex) {
        this.sex = sex;
    }

    /**
     * 获取头像设置时间
     * @return 头像设置时间
     */
    @JsonProperty
    @Column(name = "face_time", nullable = false)
    public Long getFaceTime() {
        return faceTime;
    }

    /**
     * 设置头像设置时间
     * @param faceTime 头像设置时间
     */
    public void setFaceTime(Long faceTime) {
        this.faceTime = faceTime;
    }

    /**
     * 获取病症
     * @return 病症
     */
    @JsonProperty
    @Lob
    @Column(name = "indication")
    public String getIndication() {
        return indication;
    }

    /**
     * 设置病症
     * @param indication 病症
     */
    public void setIndication(String indication) {
        this.indication = indication;
    }

    /**
     * 获取用药情况
     * @return 用药情况
     */
    @JsonProperty
    @Lob
    @Column(name = "medicine")
    public String getMedicine() {
        return medicine;
    }

    /**
     * 设置用药情况
     * @param medicine 用药情况
     */
    public void setMedicine(String medicine) {
        this.medicine = medicine;
    }

    /**
     * 获取地址
     * @return 地址
     */
    @JsonProperty
    @Lob
    @Column(name = "address")
    public String getAddress() {
        return address;
    }

    /**
     * 设置地址
     * @param address 地址
     */
    public void setAddress(String address) {
        this.address = address;
    }

    /**
     * 获取语种
     * @return 语种
     */
    @JsonProperty
    @Embedded
    public Language getLanguage() {
        return language;
    }

    /**
     * 设置语种
     * @param language 语种
     */
    public void setLanguage(Language language) {
        this.language = language;
    }

    /**
     * 获取创建时间
     * @return 创建时间
     */
    @JsonProperty
    @Column(name = "created", nullable = false)
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
     * 获取所属机构
     * @return 所属机构
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "oid")
    public Organization getOrg() {
        return org;
    }

    /**
     * 设置所属机构
     * @param org 所属机构
     */
    public void setOrg(Organization org) {
        this.org = org;
    }

    /**
     * 获取所属医生
     * @return 所属医生
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "did")
    public DoctorProfile getDoctor() {
        return doctor;
    }

    /**
     * 设置所属医生
     * @param doctor 设置所属医生
     */
    public void setDoctor(DoctorProfile doctor) {
        this.doctor = doctor;
    }

    /**
     * 获取绑定类型
     * @return 绑定类型
     */
    @JsonProperty
    @Column(name = "bind_type", nullable = false)
    public BindType getBindType() {
        return bindType;
    }

    /**
     * 设置绑定类型
     * @param bindType 绑定类型
     */
    public void setBindType(BindType bindType) {
        this.bindType = bindType;
    }

    /**
     * 获取绑定时间
     * @return 绑定时间
     */
    @JsonProperty
    @Column(name = "bind_time", nullable = false)
    public Long getBindTime() {
        return bindTime;
    }

    /**
     * 设置绑定时间
     * @param bindTime 绑定时间
     */
    public void setBindTime(Long bindTime) {
        this.bindTime = bindTime;
    }

    /**
     * 获取最后登录时间
     * @return 最后登录时间
     */
    @JsonProperty
    @Column(name = "login_time", nullable = false)
    public Long getLoginTime() {
        return loginTime;
    }

    /**
     * 设置最后登录时间
     * @param loginTime 最后登录时间
     */
    public void setLoginTime(Long loginTime) {
        this.loginTime = loginTime;
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
     * 获取上次的报告
     * @return 上次的报告
     */
    @Column(name = "last_consultation", nullable = false, length = 500)
    public String getLastConsultation() {
        return lastConsultation;
    }

    /**
     * 设置上次的报告
     * @param lastConsultation 上次的报告
     */
    public void setLastConsultation(String lastConsultation) {
        this.lastConsultation = lastConsultation;
    }

    /**
     * 获取钱包是否激活
     * @return 钱包是否激活
     */
    @NotNull
    @Column(name = "wallet_state", nullable = false)
    public Boolean getIsWalletActive() {
        return isWalletActive;
    }

    /**
     * 设置钱包是否激活
     * @param isWalletActive 钱包是否激活
     */
    public void setIsWalletActive(Boolean isWalletActive) {
        this.isWalletActive = isWalletActive;
    }

    /**
     * 获取国家
     * @return 国家
     */
    @JsonProperty
    @Column(name = "nation_code")
    public Integer getNationCode() {
        return nationCode;
    }

    /**
     * 设置国家
     * @param nationCode 国家
     */
    public void setNationCode(Integer nationCode) {
        this.nationCode = nationCode;
    }

    /**
     * 获取国家
     * @return 国家
     */
    @JsonProperty
    @Column(name = "nation_name", length = 128)
    public String getNationName() {
        return nationName;
    }

    /**
     * 设置国家
     * @param nationName 国家
     */
    public void setNationName(String nationName) {
        this.nationName = nationName;
    }

    /**
     * 获取区域
     * @return 区域
     */
    @JsonProperty
    @Column(name = "region_code")
    public Integer getRegionCode() {
        return regionCode;
    }

    /**
     * 设置区域
     * @param regionCode 区域
     */
    public void setRegionCode(Integer regionCode) {
        this.regionCode = regionCode;
    }

    /**
     * 获取区域
     * @return 区域
     */
    @JsonProperty
    @Column(name = "region_name", length = 128)
    public String getRegionName() {
        return regionName;
    }

    /**
     * 设置区域
     * @param regionName 区域
     */
    public void setRegionName(String regionName) {
        this.regionName = regionName;
    }

    /**
     * 获取省份
     * @return 省份
     */
    @JsonProperty
    @Column(name = "province_code")
    public Integer getProvinceCode() {
        return provinceCode;
    }

    /**
     * 设置省份
     * @param provinceCode 省份
     */
    public void setProvinceCode(Integer provinceCode) {
        this.provinceCode = provinceCode;
    }

    /**
     * 获取省份
     * @return 省份
     */
    @JsonProperty
    @Column(name = "provice_name", length = 128)
    public String getProvinceName() {
        return provinceName;
    }

    /**
     * 设置省份
     * @param provinceName 省份
     */
    public void setProvinceName(String provinceName) {
        this.provinceName = provinceName;
    }

    /**
     * 获取城市
     * @return 城市
     */
    @JsonProperty
    @Column(name = "city_code")
    public Integer getCityCode() {
        return cityCode;
    }

    /**
     * 设置城市
     * @param cityCode 城市
     */
    public void setCityCode(Integer cityCode) {
        this.cityCode = cityCode;
    }

    /**
     * 获取城市
     * @return 城市
     */
    @JsonProperty
    @Column(name = "city_name", length = 128)
    public String getCityName() {
        return cityName;
    }

    /**
     * 设置城市
     * @param cityName 城市
     */
    public void setCityName(String cityName) {
        this.cityName = cityName;
    }

    /**
     * 获取邮编
     * @return 邮编
     */
    @JsonProperty
    @Column(name = "postcode", length = 128)
    public String getPostCode() {
        return postCode;
    }

    /**
     * 设置邮编
     * @param postCode 邮编
     */
    public void setPostCode(String postCode) {
        this.postCode = postCode;
    }

    @JsonProperty
    @Column(name = "fullName", length = 128)
    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    /**
     * 获取缩略图地址
     * @return 缩略图地址
     */
    @JsonProperty
    @Column(name = "thumbpath", length = 128)
    public String getThumbPath() {
        return thumbPath;
    }

    /**
     * 设置缩略图地址
     * @param thumbPath 缩略图地址
     */
    public void setThumbPath(String thumbPath) {
        this.thumbPath = thumbPath;
    }

    @JsonProperty
    @Column(name = "HEADPATH", length = 128)
    public String getHeadPath() {
        return headPath;
    }

    public void setHeadPath(String headPath) {
        this.headPath = headPath;
    }

    /*------------------------------------ Trasient methods ------------------------------------*/
    /**
     * 获取患者姓名
     * @return 患者姓名
     */
    @JsonProperty
    @Transient
    public String getName() {
        return getFullName();
    }

    /**
     * 获取性别
     * @return 患者性别
     */
    @JsonProperty
    @Transient
    public String getSexName() {
        return getSex().name();
    }

    /**
     * 获取医生姓名
     * @return 医生姓名
     */
    @JsonProperty
    @Transient
    public String getDoctorName() {
        if (getDoctor() != null && getDoctor().getId() != 0L) {
            return getDoctor().getName();
        }
        return null;
    }

    /**
     * 获取机构名称
     * @return 机构名称
     */
    @JsonProperty
    @Transient
    public String getOrgName() {
        if (getOrg() != null && getOrg().getId() != 0L) {
            return getOrg().getName();
        }
        return null;
    }

    /**
     * 获取出生日期
     * @return 出生日期
     */
    @JsonProperty
    @JsonSerialize(using = DateSerializer.class)
    @Transient
    public Date getDateBirthday() {
        if (getBirthday() != null && getBirthday() != 0L) {
            return new Date(getBirthday() * 1000);
        }
        return null;
    }

    /**
     * 获取活动记录
     * @return 活动记录
     */
    @OneToMany(mappedBy = "uid", fetch = FetchType.LAZY, cascade = CascadeType.REMOVE)
    public Set<PatientActivity> getPatientActivitys() {
        return patientActivitys;
    }

    public void setPatientActivitys(Set<PatientActivity> patientActivitys) {
        this.patientActivitys = patientActivitys;
    }

    /**
     * 获取医嘱
     * @return 医嘱
     */
    @OneToMany(mappedBy = "receive_id", fetch = FetchType.LAZY, cascade = CascadeType.REMOVE)
    public Set<PatientDoctorOpinion> getPatientDoctorOpinions() {
        return patientDoctorOpinions;
    }

    public void setPatientDoctorOpinions(Set<PatientDoctorOpinion> patientDoctorOpinions) {
        this.patientDoctorOpinions = patientDoctorOpinions;
    }
}
