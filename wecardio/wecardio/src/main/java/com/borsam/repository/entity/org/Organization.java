package com.borsam.repository.entity.org;

import com.borsam.repository.entity.doctor.DoctorProfile;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.hiteam.common.base.repository.entity.BaseEntity;
import com.hiteam.common.base.repository.entity.LongEntity;
import com.hiteam.common.util.json.DateSerializer;
import com.hiteam.common.web.I18Util;
import org.apache.commons.lang.StringUtils;
import org.hibernate.search.annotations.DocumentId;
import org.hibernate.search.annotations.Field;
import org.hibernate.search.annotations.Index;
import org.hibernate.search.annotations.Store;
import org.hibernate.validator.constraints.Email;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotEmpty;

import javax.persistence.*;
import javax.validation.Valid;
import javax.validation.constraints.Digits;
import javax.validation.constraints.Min;
import java.math.BigDecimal;
import java.util.*;

/**
 * Entity - 机构信息
 * Created by tantian on 2015/6/23.
 */
@Entity
@Table(name = "organization")
public class Organization extends LongEntity {

    /**
     * 性别
     */
    public enum Gender {
        unset, female, male, other
    }

    /**
     * 类型
     */
    public enum Type {
        personal, organization
    }

    private String name;                    // 名称
    private String firstName;               // first name
    private String secondName;              // second name
    private String intro;                   // 描述
    private Long faceTime;                  // 设置头像时间
    private String address;                 // 地址
    private String telephone;               // 电话
    private String contact;                 // 联系人
    private String email;                   // Email
    private Long birthdate;                 // 生日
    private Gender sex;                     // 性别
    private String ic;                      // 身份证号
    private String pic;                     // TODO delete ?
    private Type type;                      // 类型：0-个人 1-机构
    private String doctorSecondName;        // 医生 second name
    private String doctorFirstName;         // 医生 firstName
    private Long doctorBirthDate;           // 医生生日
    private DoctorProfile.Gender doctorSex; // 医生性别
    private String doctorIc;                // 医生身份证号
    private String doctorPic;               // 医生照片
    private Integer auditState;             // 状态
    private String zoneCode;                // 区域代码
    private Boolean isWalletActive;         // 钱包是否激活
    private Integer nationCode;             // 国家
    private String nationName;              // 国家
    private Integer regionCode;             // 区域
    private String regionName;              // 区域
    private Integer provinceCode;           // 省份
    private String provinceName;            // 省份
    private Integer cityCode;               // 城市
    private String cityName;                // 城市
    private String postCode;                // 邮编
    private BigDecimal rate;                // 分成比例
    private String code;                    // 编号
    private Boolean isSign;                 // 是否签订协议
    private Integer doctorNum;              // 医生人数
    private BigDecimal orderMoney;          // 诊单金额
    private Integer patientNum;             // 患者数量
    private Integer orderNum;               // 诊单数量
    private Integer serviceNum;             // 服务数量
    private Integer commentNum;             // 评价总数
    private Integer commentScore;           // 评价总分
    private Long guid;                      // 流程guid
    private Long created;                   // 创建时间
    private Long modify;                    // 修改时间
    private Long headpidId;                 // 封面图片ID

    /**
     * 医生
     */
    private Set<DoctorProfile> doctors = new HashSet<DoctorProfile>();

    /**
     * 机构图片
     */
    private List<OrganizationImage> orgImages = new ArrayList<OrganizationImage>();

    /**
     * 机构医生证书
     */
    private List<OrganizationDoctorImage> doctorImages = new ArrayList<OrganizationDoctorImage>();

    /**
     * 获取ID
     * @return ID
     */
    @JsonProperty
    @DocumentId
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.TABLE, generator = "tableGenerator")
    @TableGenerator(name = "tableGenerator", table = "id_maker", pkColumnName = "maker_name",
            valueColumnName = "maker_value", pkColumnValue = "organization", allocationSize = 1)
    public Long getId() {
        return super.getId();
    }

    /**
     * 获取名称
     * @return 名称
     */
    @JsonProperty
    @NotEmpty
    @Length(max = 100)
    @Column(name = "name", nullable = false, length = 100)
    public String getName() {
        return name;
    }

    /**
     * 设置名称
     * @param name 名称
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * 获取first name
     * @return first name
     */
    @Column(name = "first_name", nullable = false, length = 50)
    public String getFirstName() {
        return firstName;
    }

    /**
     * 设置first name
     * @param firstName first name
     */
    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    /**
     * 获取second name
     * @return second name
     */
    @Column(name = "second_name", nullable = false, length = 50)
    public String getSecondName() {
        return secondName;
    }

    /**
     * 设置second name
     * @param secondName second name
     */
    public void setSecondName(String secondName) {
        this.secondName = secondName;
    }

    /**
     * 获取描述
     * @return 描述
     */
    @Length(max = 4000)
    @Column(name = "intro", nullable = false, length = 4000)
    public String getIntro() {
        return intro;
    }

    /**
     * 设置描述
     * @param intro 描述
     */
    public void setIntro(String intro) {
        this.intro = intro;
    }

    /**
     * 获取头像设置时间
     * @return 头像设置时间
     */
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
     * 获取地址
     * @return 地址
     */
    @Length(max = 200)
    @Column(name = "address", nullable = false, length = 200)
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
     * 获取电话
     * @return 电话
     */
    @Length(max = 20)
    @Column(name = "telephone", nullable = false, length = 20)
    public String getTelephone() {
        return telephone;
    }

    /**
     * 设置电话
     * @param telephone 电话
     */
    public void setTelephone(String telephone) {
        this.telephone = telephone;
    }

    /**
     * 获取联系人
     * @return 联系人
     */
    @Length(max = 20)
    @Column(name = "contact", nullable = false, length = 20)
    public String getContact() {
        return contact;
    }

    /**
     * 设置联系人
     * @param contact 联系人
     */
    public void setContact(String contact) {
        this.contact = contact;
    }

    /**
     * 获取Email
     * @return Email
     */
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
     * 获取生日
     * @return 生日
     */
    @Column(name = "birthdate")
    public Long getBirthdate() {
        return birthdate;
    }

    /**
     * 设置生日
     * @param birthdate 生日
     */
    public void setBirthdate(Long birthdate) {
        this.birthdate = birthdate;
    }

    /**
     * 获取性别
     * @return 性别
     */
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
     * 获取身份证号
     * @return 身份证号
     */
    @Length(max = 50)
    @Column(name = "ic", nullable = false, length = 50)
    public String getIc() {
        return ic;
    }

    /**
     * 设置身份证号
     * @param ic 身份证号
     */
    public void setIc(String ic) {
        this.ic = ic;
    }

    /**
     * 获取图片地址
     * @return 图片地址
     */
    @Lob
    @Column(name = "pic")
    public String getPic() {
        return pic;
    }

    /**
     * 设置图片地址
     * @param pic 图片地址
     */
    public void setPic(String pic) {
        this.pic = pic;
    }

    /**
     * 获取机构类型
     * @return 机构类型
     */
    @Column(name = "type", nullable = false)
    public Type getType() {
        return type;
    }

    /**
     * 设置机构类型
     * @param type 机构类型
     */
    public void setType(Type type) {
        this.type = type;
    }

    /**
     * 获取医生second name
     * @return 医生second name
     */
    @Length(max = 50)
    @Column(name = "doctor_second_name", nullable = false, length = 50)
    public String getDoctorSecondName() {
        return doctorSecondName;
    }

    /**
     * 设置医生second name
     * @param doctorSecondName 医生second name
     */
    public void setDoctorSecondName(String doctorSecondName) {
        this.doctorSecondName = doctorSecondName;
    }

    /**
     * 获取医生first name
     * @return 医生 first name
     */
    @Length(max = 50)
    @Column(name = "doctor_first_name", nullable = false, length = 50)
    public String getDoctorFirstName() {
        return doctorFirstName;
    }

    /**
     * 设置医生first name
     * @param doctorFirstName 医生first name
     */
    public void setDoctorFirstName(String doctorFirstName) {
        this.doctorFirstName = doctorFirstName;
    }

    /**
     * 获取医生生日
     * @return 医生生日
     */
    @Column(name = "doctor_birthdate")
    public Long getDoctorBirthDate() {
        return doctorBirthDate;
    }

    /**
     * 设置医生生日
     * @param doctorBirthDate 医生生日
     */
    public void setDoctorBirthDate(Long doctorBirthDate) {
        this.doctorBirthDate = doctorBirthDate;
    }

    /**
     * 获取医生性别
     * @return 医生性别
     */
    @Column(name = "doctor_sex", nullable = false)
    public DoctorProfile.Gender getDoctorSex() {
        return doctorSex;
    }

    /**
     * 设置医生性别
     * @param doctorSex 医生性别
     */
    public void setDoctorSex(DoctorProfile.Gender doctorSex) {
        this.doctorSex = doctorSex;
    }

    /**
     * 获取医生身份证号
     * @return 医生身份证号
     */
    @Length(max = 50)
    @Column(name = "doctor_ic", nullable = false, length = 50)
    public String getDoctorIc() {
        return doctorIc;
    }

    /**
     * 设置医生身份证号
     * @param doctorIc 医生身份证号
     */
    public void setDoctorIc(String doctorIc) {
        this.doctorIc = doctorIc;
    }

    /**
     * 获取医生图片地址
     * @return 医生图片地址
     */
    @Lob
    @Column(name = "doctor_pic")
    public String getDoctorPic() {
        return doctorPic;
    }

    /**
     * 设置医生图片地址
     * @param doctorPic 医生图片地址
     */
    public void setDoctorPic(String doctorPic) {
        this.doctorPic = doctorPic;
    }

    /**
     * 获取审核状态
     * @return 审核状态 0 - 待审核 1 - 待提交 2 - 激活 3 - 停止
     */
    @JsonProperty
    @Column(name = "audit_state", nullable = false)
    public Integer getAuditState() {
        return auditState;
    }

    /**
     * 设置审核状态
     * @param auditState 审核状态 0 - 待审核 1 - 待提交 2 - 激活 3 - 停止
     */
    public void setAuditState(Integer auditState) {
        this.auditState = auditState;
    }

    /**
     * 获取区域代码
     * @return 区域代码
     */
    @Length(max = 20)
    @Column(name = "zone_code", nullable = false, length = 20)
    public String getZoneCode() {
        return zoneCode;
    }

    /**
     * 设置区域代码
     * @param zoneCode 区域代码
     */
    public void setZoneCode(String zoneCode) {
        this.zoneCode = zoneCode;
    }

    /**
     * 获取钱包是否激活
     * @return 钱包是否激活
     */
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

    /**
     * 获取分成比例，单位（%）
     * @return 分成比例
     */
    @Min(0)
    @Digits(integer = 10, fraction = 2)
    @Column(name = "rate", precision = 10, scale = 2)
    public BigDecimal getRate() {
        return rate;
    }

    /**
     * 设置分成比例
     * @param rate 分成比例
     */
    public void setRate(BigDecimal rate) {
        this.rate = rate;
    }

    /**
     * 获取机构编号
     * @return 机构编号
     */
    @JsonProperty
    @Length(max = 128)
    @Column(name = "code", length = 128)
    public String getCode() {
        return code;
    }

    /**
     * 设置机构编号
     * @param code 机构编号
     */
    public void setCode(String code) {
        this.code = code;
    }

    /**
     * 获取是否签订协议
     * @return 是否签订协议
     */
    @Column(name = "sign_protocal")
    public Boolean getIsSign() {
        return isSign != null ? isSign : false;
    }

    /**
     * 设置是否签订协议
     * @param isSign 是否签订协议
     */
    public void setIsSign(Boolean isSign) {
        this.isSign = isSign;
    }

    /**
     * 获取医生数量
     * @return 医生数量
     */
    @JsonProperty
    @Field(store = Store.YES, index = Index.NO)
    @Column(name = "doctor_num")
    public Integer getDoctorNum() {
        return doctorNum != null ? doctorNum : 0;
    }

    /**
     * 设置医生数量
     * @param doctorNum 医生数量
     */
    public void setDoctorNum(Integer doctorNum) {
        this.doctorNum = doctorNum;
    }

    /**
     * 获取诊单金额
     * @return 诊单金额
     */
    @Min(0)
    @Digits(integer = 12, fraction = 3)
    @Column(name = "order_money", precision = 21, scale = 6)
    public BigDecimal getOrderMoney() {
        return orderMoney != null ? orderMoney : new BigDecimal(0);
    }

    /**
     * 设置诊单金额
     * @param orderMoney 诊单金额
     */
    public void setOrderMoney(BigDecimal orderMoney) {
        this.orderMoney = orderMoney;
    }

    /**
     * 获取患者数量
     * @return 患者数量
     */
    @JsonProperty
    @Field(store = Store.YES, index = Index.NO)
    @Column(name = "patient_num")
    public Integer getPatientNum() {
        return patientNum != null ? patientNum : 0;
    }

    /**
     * 设置患者数量
     * @param patientNum 患者数量
     */
    public void setPatientNum(Integer patientNum) {
        this.patientNum = patientNum;
    }

    /**
     * 获取诊单数量
     * @return 诊单数量
     */
    @JsonProperty
    @Field(store = Store.YES, index = Index.NO)
    @Column(name = "order_num")
    public Integer getOrderNum() {
        return orderNum != null ? orderNum : 0;
    }

    /**
     * 设置诊单数量
     * @param orderNum 诊单数量
     */
    public void setOrderNum(Integer orderNum) {
        this.orderNum = orderNum;
    }

    /**
     * 获取服务数量
     * @return 服务数量
     */
    @Field(store = Store.YES, index = Index.NO)
    @Column(name = "service_num")
    public Integer getServiceNum() {
        return serviceNum != null ? serviceNum : 0;
    }

    /**
     * 设置服务数量
     * @param serviceNum 服务数量
     */
    public void setServiceNum(Integer serviceNum) {
        this.serviceNum = serviceNum;
    }

    /**
     * 获取评价总数
     * @return 评价总数
     */
    @Field(store = Store.YES, index = Index.UN_TOKENIZED)
    @Column(name = "comment_num")
    public Integer getCommentNum() {
        return commentNum != null ? commentNum : 0;
    }

    /**
     * 设置评价总数
     * @param commentNum 评价总数
     */
    public void setCommentNum(Integer commentNum) {
        this.commentNum = commentNum;
    }

    /**
     * 获取评价总分
     * @return 评价总分
     */
    @Column(name = "comment_score")
    public Integer getCommentScore() {
        return commentScore != null ? commentScore : 0;
    }

    /**
     * 设置评价总分
     * @param commentScore 评价总分
     */
    public void setCommentScore(Integer commentScore) {
        this.commentScore = commentScore;
    }

    /**
     * 获取流程guid
     * @return 流程guid
     */
    @JsonProperty
    @Column(name = "guid")
    public Long getGuid() {
        return guid;
    }

    /**
     * 设置流程guid
     * @param guid 流程guid
     */
    public void setGuid(Long guid) {
        this.guid = guid;
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
     * 获取修改时间
     * @return 修改时间
     */
    @Column(name = "modify_time", nullable = false)
    public Long getModify() {
        return modify;
    }

    /**
     * 设置修改时间
     * @param modify 修改时间
     */
    public void setModify(Long modify) {
        this.modify = modify;
    }

    /**
     * 获取封面图片ID
     * @return 封面图片ID
     */
    @Column(name = "headpic_id")
    public Long getHeadpidId() {
        return headpidId;
    }

    /**
     * 设置封面图片ID
     * @param headpidId 封面图片ID
     */
    public void setHeadpidId(Long headpidId) {
        this.headpidId = headpidId;
    }

    /**
     * 获取医生
     * @return 医生
     */
    @OneToMany(mappedBy = "org", fetch = FetchType.LAZY)
    public Set<DoctorProfile> getDoctors() {
        return doctors;
    }

    /**
     * 设置医生
     * @param doctors 医生
     */
    public void setDoctors(Set<DoctorProfile> doctors) {
        this.doctors = doctors;
    }

    /**
     * 获取机构图片
     * @return 机构图片
     */
    @Valid
    @OneToMany(mappedBy = "org", fetch = FetchType.LAZY, cascade = CascadeType.REMOVE)
    public List<OrganizationImage> getOrgImages() {
        return orgImages;
    }

    /**
     * 设置机构图片
     * @param orgImages 机构图片
     */
    public void setOrgImages(List<OrganizationImage> orgImages) {
        this.orgImages = orgImages;
    }

    /**
     * 获取机构医生证书
     * @return 机构医生证书
     */
    @Valid
    @OneToMany(mappedBy = "org", fetch = FetchType.LAZY, cascade = CascadeType.REMOVE)
    public List<OrganizationDoctorImage> getDoctorImages() {
        return doctorImages;
    }

    /**
     * 设置机构医生证书
     * @param doctorImages 机构医生证书
     */
    public void setDoctorImages(List<OrganizationDoctorImage> doctorImages) {
        this.doctorImages = doctorImages;
    }

    /**
     * 保存前处理
     */
    @PrePersist
    public void prePersist() {
        this.setCreated(new Date().getTime() / 1000);
        this.setModify(new Date().getTime() / 1000);
    }

    /**
     * 更新前处理
     */
    @PreUpdate
    public void preUpdate() {
        this.setModify(new Date().getTime() / 1000);
    }

    /**
     * 删除前处理
     */
    @PreRemove
    public void preRemove() {
        Set<DoctorProfile> doctors = getDoctors();
        if (doctors != null) {
            for (DoctorProfile doctor : doctors) {
                doctor.setOrg(null);
            }
        }
    }


    /*---------- Transient ----------*/

    /**
     * 获取机构法人出生日期
     * @return 机构法人出生日期
     */
    @JsonProperty
    @JsonSerialize(using = DateSerializer.class)
    @Transient
    public Date getOrgBirthday() {
        if (getBirthdate() != null &&  getBirthdate() != 0) {
            return new Date(getBirthdate() * 1000);
        } else {
            return null;
        }
    }

    /**
     * 设置机构法人出生日期
     * @param orgBirthday 出生日期
     */
    public void setOrgBirthday(Date orgBirthday) {
        if (orgBirthday != null) {
            setBirthdate(orgBirthday.getTime() / 1000);
        } else {
            setBirthdate(0L);
        }
    }

    /**
     * 获取医生出生日期
     * @return 医生出生日期
     */
    @JsonProperty
    @JsonSerialize(using = DateSerializer.class)
    @Transient
    public Date getDoctorBirthDay() {
        if (getDoctorBirthDate() != null &&  getDoctorBirthDate() != 0) {
            return new Date(getDoctorBirthDate() * 1000);
        } else {
            return null;
        }
    }

    /**
     * 设置医生出生日期
     * @param doctorBirthDay 出生日期
     */
    public void setDoctorBirthDay(Date doctorBirthDay) {
        if (doctorBirthDay != null) {
            setDoctorBirthDate(doctorBirthDay.getTime() / 1000);
        } else {
            setDoctorBirthDate(0L);
        }
    }

    /**
     * 获取医生姓名
     * @return 医生姓名
     */
    @JsonProperty
    @Transient
    public String getDoctorName() {
        if (StringUtils.isNotEmpty(getDoctorFirstName())
                && StringUtils.isNotEmpty(getDoctorSecondName())) {
            return getDoctorFirstName() + " " + getDoctorSecondName();
        } else {
            return null;
        }
    }

    /**
     * 获取机构类型名称
     * @return 机构类型名称
     */
    @JsonProperty
    @Transient
    public String getTypeName() {
        return I18Util.getMessage("common.enum.orgType." + getType().name());
    }

    /**
     * 获取机构负责人姓名
     * @return 机构负责人姓名
     */
    @JsonProperty
    @Transient
    public String getUsername() {
        return getFirstName() +  getSecondName();
    }

    /**
     * 获取更新日期
     * @return 更新日期
     */
    @JsonProperty
    @JsonSerialize(using = DateSerializer.class)
    @Transient
    public Date getModifyDate() {
        if (getModify() != null &&  getModify() != 0) {
            return new Date(getModify() * 1000);
        } else {
            return null;
        }
    }

    /**
     * 获取封面图
     * @return 图片地址
     */
    @JsonProperty
    @Transient
    public String getImage() {
        if (getOrgImages() != null && !getOrgImages().isEmpty()) {
            return getOrgImages().get(0).getMedium();
        }
        return "/resources/images/addimg.jpg";
    }

    /**
     * 获取性别
     * @return 性别
     */
    @JsonProperty
    @Transient
    public String getSexText() {
        return I18Util.getMessage("common.enum.sex." + getSex().name());
    }

    /**
     * 获取分成比例（%）
     * @return 分成比例（%）
     */
    @Transient
    public String getRatePercent() {
        return getRate() != null ? getRate().multiply(new BigDecimal(100)).setScale(0).toString() + "%" : null;
    }
}
