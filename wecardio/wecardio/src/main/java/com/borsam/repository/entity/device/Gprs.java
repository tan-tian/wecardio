package com.borsam.repository.entity.device;



import java.io.Serializable;
import java.util.Calendar;


import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.TableGenerator;
import javax.persistence.Transient;

import org.hibernate.search.annotations.DocumentId;

import com.borsam.repository.entity.org.Organization;
import com.borsam.repository.entity.patient.PatientProfile;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.hiteam.common.base.repository.entity.BaseEntity;
import com.hiteam.common.base.repository.entity.LongEntity;
import com.hiteam.common.base.repository.entity.StringEntity;

/**
 * Entity 设备绑定信息表 2016/01/28
 * @author Tony 
 *
 */

@Entity
@Table(name = "gprs")
public class Gprs extends LongEntity {
	
	private String ime;                         //设备ime号
	private PatientProfile patient;             //患者
	private Long created;                       // 创建时间
	private Organization organization;          //所属机构
	private Long receiveTotal;                  //接收片段总数
	private Long receiveNow;                    //当前接收片段次数
	private Boolean isDelete;               	// 是否删除：0-否 1-是
	private String mark;						//设备标记
	private Long imeId;							//ime的id
	private Long type;							//设备类型 4-三导设备 6-Holter620设备 
	
	/**
	 * 获取设备类型
	 * @return type
	 */
	@JsonProperty
	@Column(name = "type")
	public Long getType() {
		return type;
	}

	public void setType(Long type) {
		this.type = type;
	}

	/**
     * 主键
     * @return 
     */
	@Id
	@Column(name = "ime_id")
	@Override
	@JsonProperty
	@GeneratedValue(strategy = GenerationType.TABLE, generator = "tableGenerator")
    @TableGenerator(name = "tableGenerator", table = "id_maker", pkColumnName = "maker_name",
            valueColumnName = "maker_value", pkColumnValue = "gprs", allocationSize = 1)
	public Long getId() {
		return imeId;
    }
	
	public void setId(Long imeId){
		this.imeId=imeId;
	}
	
	/**
	 * 获取IMEI号
	 * @return IMEI号
	 */
	@JsonProperty
	@Column(name = "ime")
	public String getIme() {
		return ime;
	}
	
	public void setIme(String ime) {
		this.ime = ime;
	}

/**
	 *  设备标记编号
	 * @return 设备编号
	 */
	@JsonProperty
	@Column(name = "mark")
	public String getMark() {
		return mark;
	}


	public void setMark(String mark) {
		this.mark = mark;
	}


	/**
	 *  标记该条记录是否已删除
	 * @return 是否删除 0-否 1-是
	 */
	@Column(name = "delete_state",nullable = false)
	 public Boolean getIsDelete() {
		return isDelete;
	}
	public void setIsDelete(Boolean isDelete) {
		this.isDelete = isDelete;
	}
	
	/**
     * 获取患者
     * @return 患者
     */
	@ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "uid",nullable=true)
    public PatientProfile getPatient() {
        return patient;
    }

	public void setPatient(PatientProfile patient) {
		this.patient = patient;
	}
	
	 /**
     * 获取创建时间
     * @return 创建时间
     */
    @Column(name = "created", nullable = false, updatable = false)
    @JsonProperty
	public Long getCreated() {
		return created;
	}

	public void setCreated(Long created) {
		this.created = created;
	}
	
	/**
	 * 获取机构
	 * @return
	 */
	@ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "oid")
	public Organization getOrganization() {
		return organization;
	}
	public void setOrganization(Organization organization) {
		this.organization = organization;
	}
	
	 /**
     * 获取总片段次数
     * @return 总片段次数
     */
    @Column(name = "receive_total")
    @JsonProperty
	public Long getReceiveTotal() {
		return receiveTotal;
	}
	public void setReceiveTotal(Long receiveTotal) {
		this.receiveTotal = receiveTotal;
	}
	
	 /**
     * 获取当前片段次数
     * @return 当前片段次数
     */
    @Column(name = "receive_now")
    @JsonProperty
	public Long getReceiveNow() {
		return receiveNow;
	}
	public void setReceiveNow(Long receiveNow) {
		this.receiveNow = receiveNow;
	}
	
	/**
	 * 获取绑定患者的姓名
	 * @return 患者姓名
	 */
	@Transient
	@JsonProperty
	public String getPatientName(){
		if (getPatient()==null)
			return null;
		else
			return getPatient().getFullName();
	}
	
	/**
	 * 获取绑定患者的性别
	 * @return 患者性别
	 */
	@Transient
	@JsonProperty
	public String getSexName(){
		if (getPatient()==null)
			return null;
		else
			return getPatient().getSexName();
	}
	
	/**
	 * 获取绑定患者的年龄
	 * @return 患者年龄
	 */
	@Transient
	@JsonProperty
	public Long getPatientAge(){
		if (getPatient()==null)
			return null;
		else{
		 Calendar cal = Calendar.getInstance();
		 int year=cal.get(Calendar.YEAR);
		 if (getPatient().getDateBirthday()!=null)
			 cal.setTime(getPatient().getDateBirthday());
		 else
			 return 0L;
		 int age=(year-cal.get(Calendar.YEAR));
		return Long.valueOf(age);
		}
	}
	
	/**
	 * 获取绑定患者的电话
	 * @return 患者电话
	 */
	@Transient
	@JsonProperty
	public String getPatientMobile(){
		if (getPatient()==null)
			return null;
		else
			return getPatient().getMobile();
	}
	
	/**
	 *  获取患者病史
	 *  @return 患者病史
	 */
	@Transient
	@JsonProperty
	public String getIndication(){
		if (getPatient()==null)
			return null;
		else
			return getPatient().getIndication();
	}
	
}
