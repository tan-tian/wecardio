package com.borsam.repository.entity.doctor;

import com.borsam.repository.entity.org.Organization;

import javax.persistence.*;
import javax.validation.Valid;
import java.util.List;

/**
 * Created by tantian on 2017/6/15.
 */
@Entity
@Table(name = "profile_doctor", schema = "wecardio_test")
public class DoctorProfile {

    /**
     * 获取医生账号
     * @return 医生账号
     */
    @OneToOne(fetch = FetchType.LAZY)
    @PrimaryKeyJoinColumn
    public DoctorAccount getDoctorAccount() {
        return doctorAccount;
    }

    public void setDoctorAccount(DoctorAccount doctorAccount) {
        this.doctorAccount = doctorAccount;
    }

    public void setIsDelete(boolean isDelete) {
        this.isDelete = isDelete;
    }

    public boolean getIsDelete() {
        return isDelete;
    }

    /**
     * 性别
     */
    public enum Gender {
        unset, female, male, other
    }

    private long id;
    private String firstName;
    private String fullName;
    private String secondName;
    private long birthday;
    private String mobile;
    private String email;
    private String ic;
    private Gender sex;
    private int roles;
    private long faceTime;
    private String address;
    private String intro;
    private int created;
    private String thumbPath;
    private String headPath;
    private int auditState;
    private byte deleteState;
    private Long lastTime;
    private byte loginState;
    private String zoneCode;
    private Integer nationCode;
    private String nationName;
    private Integer regionCode;
    private String regionName;
    private Integer provinceCode;
    private String proviceName;
    private Integer cityCode;
    private String cityName;
    private Integer patientNum;
    private Integer orderNum;
    private Integer commentNum;
    private Integer commentScore;
    private Long guid;
    private Organization org;               // 所属机构
    private String provinceName;            // 省份
    private String postCode;                // 邮编
    private String account;
    private String headImg;                 // 头像图片地址
    private List<DoctorImage> doctorImages;
    private DoctorAccount doctorAccount;
    private Boolean isDelete;


    @Id
    @Column(name = "id")
    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    @Basic
    @Column(name = "first_name")
    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    @Basic
    @Column(name = "full_name")
    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    @Basic
    @Column(name = "second_name")
    public String getSecondName() {
        return secondName;
    }

    public void setSecondName(String secondName) {
        this.secondName = secondName;
    }

    @Basic
    @Column(name = "birthday")
    public long getBirthday() {
        return birthday;
    }

    public void setBirthday(long birthday) {
        this.birthday = birthday;
    }

    @Basic
    @Column(name = "mobile")
    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    @Basic
    @Column(name = "email")
    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    @Basic
    @Column(name = "ic")
    public String getIc() {
        return ic;
    }

    public void setIc(String ic) {
        this.ic = ic;
    }

    @Basic
    @Column(name = "sex")
    public Gender getSex() {
        return sex;
    }

    public void setSex(Gender sex) {
        this.sex = sex;
    }

    @Basic
    @Column(name = "roles")
    public int getRoles() {
        return roles;
    }

    public void setRoles(int roles) {
        this.roles = roles;
    }

    @Basic
    @Column(name = "face_time")
    public long getFaceTime() {
        return faceTime;
    }

    public void setFaceTime(long faceTime) {
        this.faceTime = faceTime;
    }

    @Basic
    @Column(name = "address")
    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    @Basic
    @Column(name = "intro")
    public String getIntro() {
        return intro;
    }

    public void setIntro(String intro) {
        this.intro = intro;
    }

    @Basic
    @Column(name = "created")
    public int getCreated() {
        return created;
    }

    public void setCreated(int created) {
        this.created = created;
    }

    @Basic
    @Column(name = "ThumbPath")
    public String getThumbPath() {
        return thumbPath;
    }

    public void setThumbPath(String thumbPath) {
        this.thumbPath = thumbPath;
    }

    @Basic
    @Column(name = "HeadPath")
    public String getHeadPath() {
        return headPath;
    }

    public void setHeadPath(String headPath) {
        this.headPath = headPath;
    }

    @Basic
    @Column(name = "audit_state")
    public int getAuditState() {
        return auditState;
    }

    public void setAuditState(int auditState) {
        this.auditState = auditState;
    }

    @Basic
    @Column(name = "delete_state")
    public byte getDeleteState() {
        return deleteState;
    }

    public void setDeleteState(byte deleteState) {
        this.deleteState = deleteState;
    }

    @Basic
    @Column(name = "last_time")
    public long getLastTime() {
        return lastTime;
    }

    public void setLastTime(long lastTime) {
        this.lastTime = lastTime;
    }

    @Basic
    @Column(name = "login_state")
    public byte getLoginState() {
        return loginState;
    }

    public void setLoginState(byte loginState) {
        this.loginState = loginState;
    }

    @Basic
    @Column(name = "zone_code")
    public String getZoneCode() {
        return zoneCode;
    }

    public void setZoneCode(String zoneCode) {
        this.zoneCode = zoneCode;
    }

    @Basic
    @Column(name = "nation_code")
    public Integer getNationCode() {
        return nationCode;
    }

    public void setNationCode(Integer nationCode) {
        this.nationCode = nationCode;
    }

    @Basic
    @Column(name = "nation_name")
    public String getNationName() {
        return nationName;
    }

    public void setNationName(String nationName) {
        this.nationName = nationName;
    }

    @Basic
    @Column(name = "region_code")
    public Integer getRegionCode() {
        return regionCode;
    }

    public void setRegionCode(Integer regionCode) {
        this.regionCode = regionCode;
    }

    @Basic
    @Column(name = "region_name")
    public String getRegionName() {
        return regionName;
    }

    public void setRegionName(String regionName) {
        this.regionName = regionName;
    }

    @Basic
    @Column(name = "province_code")
    public Integer getProvinceCode() {
        return provinceCode;
    }

    public void setProvinceCode(Integer provinceCode) {
        this.provinceCode = provinceCode;
    }

    @Basic
    @Column(name = "provice_name")
    public String getProviceName() {
        return proviceName;
    }

    public void setProviceName(String proviceName) {
        this.proviceName = proviceName;
    }

    @Basic
    @Column(name = "city_code")
    public Integer getCityCode() {
        return cityCode;
    }

    public void setCityCode(Integer cityCode) {
        this.cityCode = cityCode;
    }

    @Basic
    @Column(name = "city_name")
    public String getCityName() {
        return cityName;
    }

    public void setCityName(String cityName) {
        this.cityName = cityName;
    }

    @Basic
    @Column(name = "patient_num")
    public Integer getPatientNum() {
        return patientNum;
    }

    public void setPatientNum(Integer patientNum) {
        this.patientNum = patientNum;
    }

    @Basic
    @Column(name = "order_num")
    public Integer getOrderNum() {
        return orderNum;
    }

    public void setOrderNum(Integer orderNum) {
        this.orderNum = orderNum;
    }

    @Basic
    @Column(name = "comment_num")
    public Integer getCommentNum() {
        return commentNum;
    }

    public void setCommentNum(Integer commentNum) {
        this.commentNum = commentNum;
    }

    @Basic
    @Column(name = "comment_score")
    public Integer getCommentScore() {
        return commentScore;
    }

    public void setCommentScore(Integer commentScore) {
        this.commentScore = commentScore;
    }

    @Basic
    @Column(name = "guid")
    public Long getGuid() {
        return guid;
    }

    public void setGuid(Long guid) {
        this.guid = guid;
    }

    /**
     * 获取全名
     *
     * @return 全名
     */
    @Transient
    public String getName() {
        return (getFirstName() != null && getFullName() != null) ? getFirstName() + getFullName() : null;
    }

    /**
     * 获取所属机构
     *
     * @return 所属机构
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "oid")
    public Organization getOrg() {
        return org;
    }

    /**
     * 设置所属机构
     *
     * @param org 所属机构
     */
    public void setOrg(Organization org) {
        this.org = org;
    }

    public enum LoginState {
        ;
        public static byte offLine=0;
        public static byte onLine=1;
    }

    public String getProvinceName() {
        return provinceName;
    }

    public void setProvinceName(String provinceName) {
        this.provinceName = provinceName;
    }

    /**
     * 获取邮编
     * @return 邮编
     */
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

    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
    }

    public void setHeadImg(String headImg) {
        this.headImg = headImg;
    }

    public String getHeadImg() {
        return headImg;
    }

    @Valid
    @OneToMany(mappedBy = "doctor", fetch = FetchType.LAZY, cascade = CascadeType.REMOVE)
    public List<DoctorImage> getDoctorImages() {
        return doctorImages;
    }

    public void setDoctorImages(List<DoctorImage> doctorImages) {
        this.doctorImages = doctorImages;
    }
}
