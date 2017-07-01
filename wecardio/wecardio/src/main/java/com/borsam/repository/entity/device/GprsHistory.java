package com.borsam.repository.entity.device;



import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

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

import com.borsam.repository.entity.doctor.DoctorProfile;
import com.borsam.repository.entity.org.Organization;
import com.borsam.repository.entity.patient.PatientProfile;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.hiteam.common.base.repository.entity.BaseEntity;
import com.hiteam.common.base.repository.entity.LongEntity;
import com.hiteam.common.base.repository.entity.StringEntity;
import com.hiteam.common.web.I18Util;

/**
 * Entity 设备操作记录表 
 * @author Tony 2016/03/17
 *
 */

@Entity
@Table(name = "gprs_history")
public class GprsHistory extends LongEntity {
	 /**
     * 操作类型
     */
    public enum Type {
        bind,  		 //绑定
        remove,     //解绑
        mark,       //设置编号
    }
    
	private Long id;                        				 //主键id
	private Gprs gprs;							 			 //设备
	private Type note; 										 //操作类型  0 绑定 1解绑 2设置编号
	private DoctorProfile doctor;							 //操作医生
	private Long operateTime;								 //操作时间
	
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
            valueColumnName = "maker_value", pkColumnValue = "gprs_history", allocationSize = 1)
    public Long getId() {
        return super.getId();
    }
    
    /**
     * 获取设备id
     * @return 设备id
     */
    @JsonProperty
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ime_id", nullable = false)
	public Gprs getGprs() {
		return gprs;
	}

	public void setGprs(Gprs gprs) {
		this.gprs = gprs;
	}
	
	 /**
     * 获取备注
     * @return 备注
     */
	@JsonProperty
	@Column(name="note",nullable=false)
	public Type getNote() {
		return note;
	}

	public void setNote(Type note) {
		this.note = note;
	}
	
	/**
     * 获取操作医生
     * @return 操作设备医生
     */
	@JsonProperty
	@ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "did", nullable = false)
	public DoctorProfile getDoctor() {
		return doctor;
	}

	public void setDoctor(DoctorProfile doctor) {
		this.doctor = doctor;
	}
	
	/**
     * 获取操作时间
     * @return 操作设备医生
     */
	@JsonProperty
	@Column(name="operate_time")
	public Long getOperateTime() {
		return operateTime;
	}

	public void setOperateTime(Long operateTime) {
		this.operateTime = operateTime;
	}
	
	/**
     * 获取操作类型
     * @return 操作类型
     */
	@JsonProperty
	@Transient
	public String getType() {
		return I18Util.getMessage("common.device.operateType"+getNote().ordinal());
	}
	
	/**
     * 获取操作医生名称
     * @return 操作类型
     */
	@JsonProperty
	@Transient
	public String getDoctorName() {
		return getDoctor().getName();
	}
	
	/**
     * 获取操作时间日期格式
     * @return 操作类型
     */
	 @Transient
	 @JsonProperty
	 public Date getTime() throws Exception{
		 SimpleDateFormat format =  new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");  
	     Long time=new Long(getOperateTime()*1000);  
	     String d = format.format(time);  
	     Date date=format.parse(d);
	    return date;
	 }
}
