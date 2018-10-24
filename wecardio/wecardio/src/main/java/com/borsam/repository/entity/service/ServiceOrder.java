package com.borsam.repository.entity.service;

import com.borsam.repository.entity.patient.PatientProfile;
import com.borsam.service.patient.PatientAccountService;
import com.borsam.service.patient.PatientProfileService;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.hiteam.common.base.repository.entity.BaseEntity;
import com.hiteam.common.base.repository.entity.LongEntity;

import org.hibernate.search.annotations.DocumentId;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotEmpty;

import javax.annotation.Resource;
import javax.persistence.*;
import javax.validation.constraints.Digits;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * <pre>
 * @Description: 购买服务订单信息
 * @author :tantian
 * @version: Ver 1.0
 * @Date: 2015-08-04 09:58
 * </pre>
 */
@Entity
@Table(name = "service_order")
public class ServiceOrder extends LongEntity {
    /**服务包名称*/
    private String title;

    /**服务包标识:package_id*/
    private Long packageId;

    /**患者ID*/
    private PatientProfile patient;

    /**机构ID*/
    private Long oid;

    /**服务项及对应的数量*/
    private String types;

    /**服务包价格*/
    private BigDecimal price;

    /**创建时间*/
    private Long created;

    /**订单编号：trade_no,与 patient_wallet_history.trade_no 保持一致*/
    private String tradeNo;

    /**是否有效 0 有效,1:无效*/
    private Integer invalid;
    
    /**代充医生id*/
    private Long did;
    
    @Resource(name = "patientProfileServiceImpl")
    private PatientProfileService patientProfileService;
    
    @Column(name = "did")
    public Long getDid() {
		return did;
	}

	public void setDid(Long did) {
		this.did = did;
	}

	/**
     * 保存前处理
     */
    @PrePersist
    public void prePersist() {
        this.setCreated(new Date().getTime() / 1000);
    }

    @Override
    @DocumentId
    @Id
    @GeneratedValue(strategy = GenerationType.TABLE, generator = "tableGenerator")
    @TableGenerator(name = "tableGenerator", table = "id_maker", pkColumnName = "maker_name",
            valueColumnName = "maker_value", pkColumnValue = "service_order", allocationSize = 1)
    public Long getId() {
        return super.getId();
    }

    @NotEmpty
    @Length(max = 50)
    @Column(name = "title")
    @JsonProperty
    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    @NotNull
    @Column(name = "package_id", nullable = false)
    public Long getPackageId() {
        return packageId;
    }

    public void setPackageId(Long packageId) {
        this.packageId = packageId;
    }

    @NotNull
    @ManyToOne
    @JoinColumn(name = "uid", nullable = false)
    public PatientProfile getPatient() {
        return patient;
    }

    public void setPatient(PatientProfile patient) {
        this.patient = patient;
    }

    @NotNull
    @Column(name = "oid" , nullable = false)
    public Long getOid() {
        return oid;
    }

    public void setOid(Long oid) {
        this.oid = oid;
    }

    @NotEmpty
    @Lob
    @Column(name = "types", nullable = false)
    public String getTypes() {
        return types;
    }

    public void setTypes(String types) {
        this.types = types;
    }

    @NotNull
    @Min(0)
    @Digits(integer = 10, fraction = 2)
    @Column(name = "price", nullable = false, precision = 10, scale = 2)
    @JsonProperty
    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    @NotNull
    @Column(name = "created",  nullable = false, updatable = false)
    @JsonProperty
    public Long getCreated() {
        return created;
    }

    public void setCreated(Long created) {
        this.created = created;
    }

    @NotEmpty
    @Length(max = 50)
    @Column(name = "trade_no", nullable = false)
    public String getTradeNo() {
        return tradeNo;
    }

    public void setTradeNo(String tradeNo) {
        this.tradeNo = tradeNo;
    }

    @NotNull
    @Min(0)
    @Column(name = "invalid", nullable = false)
    public Integer getInvalid() {
        return invalid;
    }

    public void setInvalid(Integer invalid) {
        this.invalid = invalid;
    }
    
    @Transient
    @JsonProperty
    public String getPatientName(){
    	return getPatient().getName();
    }
    
    @Transient
    @JsonProperty
    public Date getTime() throws Exception{
    	SimpleDateFormat format =  new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");  
        Long time=new Long(getCreated()*1000);  
        String d = format.format(time);  
        Date date=format.parse(d);
    	return date;
    }
}
