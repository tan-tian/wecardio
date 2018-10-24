package com.borsam.repository.entity.patient;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.hiteam.common.base.repository.entity.BaseEntity;
import com.hiteam.common.base.repository.entity.LongEntity;

import org.hibernate.search.annotations.DocumentId;

import javax.persistence.*;
import javax.validation.constraints.Digits;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

/**
 * Entity - 患者钱包
 * Created by tantian on 2015/7/31.
 */
@Entity
@Table(name = "patient_wallet")
public class PatientWallet extends LongEntity {

    private PatientProfile patient;         // 患者
    private BigDecimal total;               // 余额

    @JsonProperty
    @DocumentId
    @Id
    @Column(name = "uid")
    public Long getId() {
        return super.getId();
    }

    /**
     * 获取患者
     * @return 患者
     */
    @OneToOne(fetch = FetchType.LAZY)
    @PrimaryKeyJoinColumn
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
     * 获取余额
     * @return 余额
     */
    @NotNull(groups = Save.class)
    @Min(0)
    @Digits(integer = 10, fraction = 2)
    @Column(name = "total", nullable = false, precision = 10, scale = 2)
    public BigDecimal getTotal() {
        return total;
    }

    /**
     * 设置余额
     * @param total 余额
     */
    public void setTotal(BigDecimal total) {
        this.total = total;
    }
}
