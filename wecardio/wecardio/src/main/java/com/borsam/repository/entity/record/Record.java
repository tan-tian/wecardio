package com.borsam.repository.entity.record;

import com.borsam.repository.entity.patient.PatientProfile;
import com.borsam.repository.entity.service.ServiceType;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.hiteam.common.base.repository.entity.BaseEntity;
import com.hiteam.common.base.repository.entity.LongEntity;
import com.hiteam.common.util.json.DateSerializer;
import com.hiteam.common.util.json.DateTimeSerializer;
import org.hibernate.annotations.NotFound;
import org.hibernate.annotations.NotFoundAction;
import org.hibernate.search.annotations.DocumentId;
import org.hibernate.validator.constraints.Length;

import javax.persistence.*;
import java.util.Date;

/**
 * Entity - 检查记录
 * Created by tantian on 2015/7/21.
 */
@Entity
@Table(name = "record")
public class Record extends LongEntity {

    /**
     * 咨询状态
     */
    public enum Stoplight {
        normal, red, yellow
    }

    /**
     * 颜色标记
     */
    public enum Flag {
        none, blue, yellow, red
    }

    private PatientProfile patient;     // 患者
    private ServiceType serviceType;    // 服务配置
    private String fileNo;              // 文件MD5 + '_' + 文件大小
    private String findings;            // 咨询结果
    private Stoplight stoplight;        // 红绿灯：'0-绿  1-红  2-黄'
    private String symptom;             // 症状
    private String ext;                 // 扩展分析
    private Long created;               // 创建时间
    private Long tested;                // 测试时间
    private String condition;           // 当前状况（症状使用此字段）
    private Flag flag;                  // 颜色标记：0 未标记  1-蓝色  2-黄色  3-红色
    private String extAuto;             //
    private Long extTime;               //
    private Boolean isCommit;           // 是否提交会诊
    private Long reportTime;            // 报告时间
    private Integer num;                // 诊单次数
    private String filePath;            // 文件路径
    private String reportPath;          // 报告路径


    /**
     * 保存前处理
     */
    @PrePersist
    public void prePersist() {
        this.setCreated(new Date().getTime() / 1000);
    }

    /**
     * 获取ID
     * @return ID
     */
    @Override
    @JsonProperty
    @DocumentId
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.TABLE, generator = "tableGenerator")
    @TableGenerator(name = "tableGenerator", table = "id_maker", pkColumnName = "maker_name",
            valueColumnName = "maker_value", pkColumnValue = "record", allocationSize = 1)
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
     * 获取服务配置
     * @return 服务配置
     */
    @NotFound(action = NotFoundAction.IGNORE)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "type", nullable = false)
    public ServiceType getServiceType() {
        return serviceType;
    }

    /**
     * 设置服务配置
     * @param serviceType 服务配置
     */
    public void setServiceType(ServiceType serviceType) {
        this.serviceType = serviceType;
    }

    /**
     * 获取fileNo
     * @return fileNo
     */
    @Column(name = "file_no", nullable = false, length = 100)
    public String getFileNo() {
        return fileNo;
    }

    /**
     * 设置fileNo
     * @param fileNo fileNo
     */
    public void setFileNo(String fileNo) {
        this.fileNo = fileNo;
    }

    /**
     * 获取诊断结果
     * @return 诊断结果
     */
    @Length(max = 1024)
    @Column(name = "findings", length = 1024)
    public String getFindings() {
        return findings;
    }

    /**
     * 设置诊断结果
     * @param findings 诊断结果
     */
    public void setFindings(String findings) {
        this.findings = findings;
    }

    /**
     * 获取诊断状态
     * @return 诊断状态
     */
    @JsonProperty
    @Column(name = "stoplight")
    public Stoplight getStoplight() {
        return stoplight;
    }

    /**
     * 设置诊断状态
     * @param stoplight 诊断状态
     */
    public void setStoplight(Stoplight stoplight) {
        this.stoplight = stoplight;
    }

    /**
     * 获取症状
     * @return 症状
     */
    @Length(max = 128)
    @Column(name = "symptom", length = 128)
    public String getSymptom() {
        return symptom;
    }

    /**
     * 设置症状
     * @param symptom 症状
     */
    public void setSymptom(String symptom) {
        this.symptom = symptom;
    }

    /**
     * 获取扩展分析
     * @return 扩展分析
     */
    @Lob
    @Column(name = "ext", nullable = false)
    public String getExt() {
        return ext;
    }

    /**
     * 设置扩展分析
     * @param ext 扩展分析
     */
    public void setExt(String ext) {
        this.ext = ext;
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
     * 获取测试时间
     * @return 测试时间
     */
    @Column(name = "tested", nullable = false)
    public Long getTested() {
        return tested;
    }

    /**
     * 设置测试时间
     * @param tested 测试时间
     */
    public void setTested(Long tested) {
        this.tested = tested;
    }

    /**
     * 获取当前状况
     * 注：condition为Mysql关键字
     * @return 当前状况
     */
    @Lob
    @Column(name = "\"condition\"", nullable = false)
    public String getCondition() {
        return condition;
    }

    /**
     * 设置当前状况
     * @param condition 当前状况
     */
    public void setCondition(String condition) {
        this.condition = condition;
    }

    /**
     * 获取颜色标记
     * @return 颜色标记
     */
    @JsonProperty
    @Column(name = "flag", nullable = false)
    public Flag getFlag() {
        return flag;
    }

    /**
     * 设置颜色标记
     * @param flag 颜色标记
     */
    public void setFlag(Flag flag) {
        this.flag = flag;
    }

    /**
     * 获取extAuto
     * @return extAuto
     */
    @Lob
    @Column(name = "ext_auto", nullable = false)
    public String getExtAuto() {
        return extAuto;
    }

    /**
     * 设置extAuto
     * @param extAuto extAuto
     */
    public void setExtAuto(String extAuto) {
        this.extAuto = extAuto;
    }

    /**
     * 获取extTime
     * @return extTime
     */
    @Column(name = "ext_time", nullable = false)
    public Long getExtTime() {
        return extTime;
    }

    /**
     * 设置extTime
     * @param extTime extTime
     */
    public void setExtTime(Long extTime) {
        this.extTime = extTime;
    }

    /**
     * 获取是否提交会诊
     * @return 是否提交会诊
     */
    @JsonProperty
    @Column(name = "state", nullable = false)
    public Boolean getIsCommit() {
        return isCommit;
    }

    /**
     * 设置是否提交会诊
     * @param isCommit 是否提交会诊
     */
    public void setIsCommit(Boolean isCommit) {
        this.isCommit = isCommit;
    }

    /**
     * 获取报告时间
     * @return 报告时间
     */
    @Column(name = "report_time", nullable = false)
    public Long getReportTime() {
        return reportTime;
    }

    /**
     * 设置报告时间
     * @param reportTime 报告时间
     */
    public void setReportTime(Long reportTime) {
        this.reportTime = reportTime;
    }

    /**
     * 获取诊断次数
     * @return 诊断次数
     */
    @JsonProperty
    @Column(name = "consultation_num")
    public Integer getNum() {
        return num != null ? num : 0;
    }

    /**
     * 设置诊断次数
     * @param num 诊断次数
     */
    public void setNum(Integer num) {
        this.num = num;
    }

    /**
     * 获取文件路径
     * @return 文件路径
     */
    @Column(name = "file_path", length = 256)
    public String getFilePath() {
        return filePath;
    }

    /**
     * 设置文件路径
     * @param filePath 文件路径
     */
    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    /**
     * 获取报告路径
     * @return 报告路径
     */
    @Column(name = "report_path", length = 256)
    public String getReportPath() {
        return reportPath;
    }

    /**
     * 设置报告路径
     * @param reportPath 报告路径
     */
    public void setReportPath(String reportPath) {
        this.reportPath = reportPath;
    }

    /*----------------------- Trasient methods -----------------------*/

    /**
     * 获取创建日期
     * @return 创建日期
     */
    @JsonProperty
    @JsonSerialize(using = DateSerializer.class)
    @Transient
    public Date getCreateDate() {
        if (getCreated() != null && getCreated() != 0L) {
            return new Date(getCreated() * 1000);
        } else {
            return null;
        }
    }

    /**
     * 获取测试日期
     * @return 测试日期
     */
    @JsonProperty
    @JsonSerialize(using = DateTimeSerializer.class)
    @Transient
    public Date getTestDate() {
        if (getTested() != null && getTested() != 0L) {
            return new Date(getTested() * 1000);
        } else {
            return null;
        }
    }

    /**
     * 获取报告日期
     * @return 报告日期
     */
    @JsonProperty
    @JsonSerialize(using = DateSerializer.class)
    @Transient
    public Date getReportDate() {
        if (getReportTime() != null && getReportTime() != 0L) {
            return new Date(getReportTime() * 1000);
        } else {
            return null;
        }
    }

    /**
     * 获取患者姓名
     * @return 患者姓名
     */
    @JsonProperty
    @Transient
    public String getPatientName() {
        return getPatient().getName();
    }

    /**
     * 获取医生姓名
     * @return 医生姓名
     */
    @JsonProperty
    @Transient
    public String getDoctorName() {
        if (getPatient().getDoctor() != null && getPatient().getDoctor().getId() != 0L) {
            return getPatient().getDoctor().getName();
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
        if (getPatient().getOrg() != null && getPatient().getOrg().getId() != 0L) {
            return getPatient().getOrg().getName();
        }
        return null;
    }

    /**
     * 获取服务名称
     * @return 服务名称
     */
    @JsonProperty
    @Transient
    public String getServiceTypeName() {
        return (getServiceType() != null) ? getServiceType().getName() : "";
    }

}
