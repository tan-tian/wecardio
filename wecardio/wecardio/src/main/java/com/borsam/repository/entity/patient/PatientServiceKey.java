package com.borsam.repository.entity.patient;

import com.borsam.repository.entity.org.Organization;
import com.borsam.repository.entity.service.ServiceType;

import javax.persistence.Embeddable;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import java.io.Serializable;

/**
 * Key - {@link PatientService}表的复合主键
 * Created by tantian on 2015/7/22.
 */
@Embeddable
public class PatientServiceKey implements Serializable {

    private PatientProfile patient;             // 患者
    private Organization org;                   // 机构
    private ServiceType serviceType;            // 服务

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (this == obj) {
            return true;
        }
        if (!PatientServiceKey.class.isAssignableFrom(obj.getClass())) {
            return false;
        }
        PatientServiceKey other = (PatientServiceKey) obj;
        return getPatient() != null && getOrg() != null && getServiceType() != null
                && getPatient().equals(other.getPatient()) && getOrg().equals(other.getOrg()) && getServiceType().equals(other.getServiceType());
    }

    @Override
    public int hashCode() {
        int i = 17;
        i += (getPatient() == null ? 0 : getPatient().hashCode() * 31);
        i += (getOrg() == null ? 0 : getOrg().hashCode() * 31);
        i += (getServiceType() == null ? 0 : getServiceType().hashCode() * 31);
        return i;
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
     * 获取服务
     * @return 服务
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "type", nullable = false)
    public ServiceType getServiceType() {
        return serviceType;
    }

    /**
     * 设置服务
     * @param serviceType 服务
     */
    public void setServiceType(ServiceType serviceType) {
        this.serviceType = serviceType;
    }
}
