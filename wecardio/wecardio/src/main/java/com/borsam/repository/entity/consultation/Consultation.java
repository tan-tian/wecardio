package com.borsam.repository.entity.consultation;

import com.borsam.repository.entity.doctor.DoctorProfile;
import com.borsam.repository.entity.org.Organization;
import com.borsam.repository.entity.patient.PatientProfile;
import com.borsam.repository.entity.record.Record;
import com.borsam.repository.entity.record.Record.Stoplight;
import com.borsam.repository.entity.service.ServiceType;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.hiteam.common.base.repository.entity.BaseEntity;
import com.hiteam.common.base.repository.entity.LongEntity;
import com.hiteam.common.util.json.DateSerializer;
import com.hiteam.common.util.json.DateTimeSerializer;
import com.hiteam.common.web.I18Util;
import org.hibernate.search.annotations.DocumentId;
import org.hibernate.validator.constraints.Length;

import javax.persistence.*;
import javax.validation.constraints.Digits;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.util.Date;

/**
 * Entity - 诊单信息
 * Created by Sebarswee on 2015/7/22.
 */
@Entity
@Table(name = "consultation")
public class Consultation extends LongEntity {

    public enum PayKind {
        servicePack, deposit
    }

    private PatientProfile patient;             // 患者
    private Long created;                       // 创建时间
    private Integer state;                      // 状态：0-待编辑 1-待审核 2-已完成
    private String verdict;                     // 诊断信息
    private Record record;                      // 检查记录
    private BigDecimal price;                   // 价格
    private ServiceType type;                   // 服务
    private Organization org;                   // 机构
    private DoctorProfile editDoctor;           // 技师
    private DoctorProfile auditDoctor;          // 审核医生
    private Long lockDoctor;                    // 锁定医生
    private Long lockTime;                      // 锁定时间
    private String doctorOpinion;               // 医生结论
    private String reportNo;                    // 文件标识
    private String ext;                         //
    private Long did = 0L;                      // 医生ID
    private DoctorProfile doctor;               // 医生
    private String tradeNo;                     //
    private PayKind payKind;                    // 支付方式
    private Integer status;                     // 结算状态：0-未结算 1-已结算
    private Integer year;                       // 年份
    private Integer month;                      // 月份
    private Long guid;                          // 流程guid
    private Stoplight stoplight;                // 诊单红绿灯状态

    /**
     * 保存前处理
     */
    @PrePersist
    public void prePersist() {
        this.setCreated(new Date().getTime() / 1000);
    }

    /**
     * 获取主键
     * @return 主键
     */
    @Override
    @JsonProperty
    @DocumentId
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.TABLE, generator = "tableGenerator")
    @TableGenerator(name = "tableGenerator", table = "id_maker", pkColumnName = "maker_name",
            valueColumnName = "maker_value", pkColumnValue = "consultation", allocationSize = 1)
    public Long getId() {
        return super.getId();
    }

    /**
     * 获取患者
     * @return 患者
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "uid", nullable = false)
    public PatientProfile getPatient() {
        return patient;
    }

    /**
     * 设置患者
     * @param patient 患者
     */
    public void setPatient(PatientProfile patient) {
        this.patient = patient;
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
     * 获取状态
     * @return 状态：0-待编辑 1-待审核 2-已完成
     */
    @JsonProperty
    @NotNull
    @Column(name = "state", nullable = false)
    public Integer getState() {
        return state;
    }

    /**
     * 设置状态
     * @param state 状态：0-待编辑 1-待审核 2-已完成
     */
    public void setState(Integer state) {
        this.state = state;
    }

    /**
     * 获取诊断信息
     * @return 诊断信息
     */
    @Lob
    @JsonProperty
    @Column(name = "verdict", nullable = false)
    public String getVerdict() {
        return verdict;
    }

    /**
     * 设置诊断信息
     * @param verdict 诊断信息
     */
    public void setVerdict(String verdict) {
        this.verdict = verdict;
    }

    /**
     * 获取检查记录
     * @return 检查记录
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "record_id", nullable = false)
    public Record getRecord() {
        return record;
    }

    /**
     * 设置检查记录
     * @param record 检查记录
     */
    public void setRecord(Record record) {
        this.record = record;
    }

    /**
     * 获取价格
     * @return 价格
     */
    @JsonProperty
    @NotNull
    @Min(0)
    @Digits(integer = 10, fraction = 2)
    @Column(name = "price", nullable = false, precision = 10, scale = 2)
    public BigDecimal getPrice() {
        return price;
    }

    /**
     * 设置价格
     * @param price 价格
     */
    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    /**
     * 获取服务
     * @return 服务
     */
   
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "type", nullable = false)
    public ServiceType getType() {
        return type;
    }

    /**
     * 设置服务
     * @param type 服务
     */
    public void setType(ServiceType type) {
        this.type = type;
    }

    /**
     * 获取机构
     * @return 机构
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "oid", nullable = false)
    public Organization getOrg() {
        return org;
    }

    /**
     * 设置机构
     * @param org 机构
     */
    public void setOrg(Organization org) {
        this.org = org;
    }

    /**
     * 获取技师
     * @return 技师
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "edit_doctor", insertable = false)
    public DoctorProfile getEditDoctor() {
        return editDoctor;
    }

    /**
     * 设置技师
     * @param editDoctor 技师
     */
    public void setEditDoctor(DoctorProfile editDoctor) {
        this.editDoctor = editDoctor;
    }

    /**
     * 获取审核医生
     * @return 审核医生
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "audit_doctor", insertable = false)
    public DoctorProfile getAuditDoctor() {
        return auditDoctor;
    }

    /**
     * 设置审核医生
     * @param auditDoctor 审核医生
     */
    public void setAuditDoctor(DoctorProfile auditDoctor) {
        this.auditDoctor = auditDoctor;
    }

    /**
     * 获取锁定医生
     * @return 锁定医生
     */
    @Column(name = "lock_doctor", nullable = false)
    public Long getLockDoctor() {
        return lockDoctor;
    }

    /**
     * 设置锁定医生
     * @param lockDoctor 锁定医生
     */
    public void setLockDoctor(Long lockDoctor) {
        this.lockDoctor = lockDoctor;
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
     * 获取医生结论
     * @return 医生结论
     */
    @JsonProperty
    @Length(max = 200)
    @Column(name = "doctor_opinion", nullable = false, length = 200)
    public String getDoctorOpinion() {
        return doctorOpinion;
    }

    /**
     * 设置医生结论
     * @param doctorOpinion 医生结论
     */
    public void setDoctorOpinion(String doctorOpinion) {
        this.doctorOpinion = doctorOpinion;
    }

    /**
     * 获取文件标识
     * @return 文件标识
     */
    @Column(name = "report_no", nullable = false, length = 50)
    public String getReportNo() {
        return reportNo;
    }

    /**
     * 设置文件标识
     * @param reportNo 文件标识
     */
    public void setReportNo(String reportNo) {
        this.reportNo = reportNo;
    }

    /**
     * 获取ext
     * @return ext
     */
    @Lob
    @Column(name = "ext")
    public String getExt() {
        return ext;
    }

    /**
     * 设置ext
     * @param ext ext
     */
    public void setExt(String ext) {
        this.ext = ext;
    }

    /**
     * 获取医生ID
     * @return 医生ID
     */
    @Column(name = "did", nullable = false)
    public Long getDid() {
        return did;
    }

    /**
     * 设置医生ID
     * @param did 医生ID
     */
    public void setDid(Long did) {
        this.did = did;
    }

    /**
     * 获取医生
     * @return 医生
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "did", insertable = false, updatable = false)
    public DoctorProfile getDoctor() {
        return doctor;
    }

    /**
     * 设置医生
     * @param doctor 医生
     */
    public void setDoctor(DoctorProfile doctor) {
        this.doctor = doctor;
    }

    /**
     * 获取tradeNo
     * @return tradeNo
     */
    @Column(name = "trade_no", nullable = false, length = 100)
    public String getTradeNo() {
        return tradeNo;
    }

    /**
     * 设置tradeNo
     * @param tradeNo tradeNo
     */
    public void setTradeNo(String tradeNo) {
        this.tradeNo = tradeNo;
    }

    /**
     * 获取支付方式
     * @return 支付方式
     */
    @Column(name = "pay_kind")
    public PayKind getPayKind() {
        return payKind;
    }

    /**
     * 设置支付方式
     * @param payKind 支付 方式
     */
    public void setPayKind(PayKind payKind) {
        this.payKind = payKind;
    }

    /**
     * 获取结算状态
     * @return 结算状态
     */
    @Column(name = "withdrawal_status")
    public Integer getStatus() {
        return status;
    }

    /**
     * 设置结算状态
     * @param status 结算状态
     */
    public void setStatus(Integer status) {
        this.status = status;
    }

    /**
     * 获取年份
     * @return 年份
     */
    @Column(name = "year")
    public Integer getYear() {
        return year;
    }

    /**
     * 设置年份
     * @param year 年份
     */
    public void setYear(Integer year) {
        this.year = year;
    }

    /**
     * 获取月份
     * @return 月份
     */
    @Column(name = "month")
    public Integer getMonth() {
        return month;
    }

    /**
     * 设置月份
     * @param month 设置月份
     */
    public void setMonth(Integer month) {
        this.month = month;
    }

    /**
     * 获取guid
     * @return guid
     */
    @JsonProperty
    @Column(name = "guid")
    public Long getGuid() {
        return guid;
    }

    /**
     * 设置guid
     * @param guid guid
     */
    public void setGuid(Long guid) {
        this.guid = guid;
    }

    /**
     * 获取创建日期
     * @return 创建日期
     */
    @JsonProperty
    @JsonSerialize(using = DateTimeSerializer.class)
    @Transient
    public Date getCreateDate() {
        if (getCreated() != null && getCreated() != 0L) {
            return new Date(getCreated() * 1000);
        }
        return null;
    }

    /**
     * 获取状态名称
     * @return 状态名称
     */
    @JsonProperty
    @Transient
    public String getStateName() {
        if (getState() == 0) {
            return I18Util.getMessage("common.enum.consultation.state.edit");
        } else if (getState() == 1) {
            return I18Util.getMessage("common.enum.consultation.state.audit");
        } else if (getState() == 2) {
            return I18Util.getMessage("common.enum.consultation.state.finish");
        } else {
            return null;
        }
    }

    /**
     * 获取服务时间
     * @return 服务时间
     */
    @JsonProperty
    @JsonSerialize(using = DateSerializer.class)
    @Transient
    public Date getServiceDate() {
        return (getRecord() != null && getRecord().getId() != 0L)?getRecord().getCreateDate():null;
    }

    /**
     * 获取服务名称
     * @return 服务名称
     */
    @JsonProperty
    @Transient
    public String getServiceTypeName() {
        return (getRecord() != null && getRecord().getId() != 0L)?getRecord().getServiceTypeName():getType().getName();
    }

    /**
     * 获取患者名称
     * @return 患者名称
     */
    @JsonProperty
    @Transient
    public String getPatientName() {
        return getPatient().getFullName();
    }

    /**
     * 获取主治医生名称
     * @return 主治医生名称
     */
    @JsonProperty
    @Transient
    public String getDoctorName() {
        return (getDoctor() != null && getDoctor().getId() != 0L) ? getDoctor().getName() : "";
    }
    
    /**
     * 获取操作医生名称
     * @return 主治操作名称
     */
    @JsonProperty
    @Transient
    public String getEditDoctorName() {
        return (getEditDoctor() != null && getEditDoctor().getId() != 0L) ? getEditDoctor().getName() : "";
    }
    
    /**
     * 获取审核医生名称
     * @return 审核医生名称
     */
    @JsonProperty
    @Transient
    public String getAuditDoctorName() {
        return (getAuditDoctor() != null && getAuditDoctor().getId() != 0L) ? getAuditDoctor().getName() : "";
    }
    
    /**
     * 获取机构名称
     * @return 机构名称
     */
    @JsonProperty
    @Transient
    public String getOrgName() {
        return (getOrg() != null && getOrg().getId() != 0L) ? getOrg().getName() : null;
    }

    /**
     * 获取支付方式名称
     * @return 支付方式名称
     */
    @JsonProperty
    @Transient
    public String getPayKindName() {
        return (getRecord() != null && getRecord().getId() != 0L)?I18Util.getMessage("common.enum.consultation.paykind." + getPayKind().name()):null;
    }

    /**
     * 获取测试时间
     * @return 测试时间
     */
    @JsonSerialize(using = DateTimeSerializer.class)
    @Transient
    public Date getTestDate() {
        return (getRecord() != null && getRecord().getId() != 0L)?getRecord().getTestDate():null;
    }

    /**
     * 获取报告时间
     * @return 报告时间
     */
    @JsonSerialize(using = DateSerializer.class)
    @Transient
    public Date getReportDate() {
        return (getRecord() != null && getRecord().getId() != 0L)?getRecord().getReportDate():null;
    }

    /**
     * 获取症状
     * @return 症状
     */
    @Transient
    public String getSymptom() {
        return (getRecord() != null && getRecord().getId() != 0L)?getRecord().getSymptom():"";
    }

    /**
     * 获取症状（症状使用此字段）
     * @return 症状
     */
    @Transient
    public String getCondition() {
        return (getRecord() != null && getRecord().getId() != 0L)?getRecord().getCondition():"";
    }

    /**
     * 获取检查记录ID
     * @return 检查记录ID
     */
    @Transient
    public Long getRecordId() {
        return (getRecord() != null && getRecord().getId() != 0L)?getRecord().getId():null;
    }
    /**
     * 设置红绿灯
     * @param 
     */
    public void setStoplight(Stoplight stoplight) {
        this.stoplight=stoplight;
    }
    /**
     * 获取红绿灯值
     * @return 红绿灯值
     */
    @Transient
    public Stoplight getStoplight() {
        return (getRecord() != null && getRecord().getId() != 0L)?getRecord().getStoplight():null;
    }

    /**
     * 获取记录提交时间
     * @return 记录提交时间
     */
    @JsonSerialize(using = DateTimeSerializer.class)
    @Transient
    public Date getRecordCreateDate() {
        return (getRecord() != null && getRecord().getId() != 0L)? getRecord().getCreateDate():null;
    }
    
}
