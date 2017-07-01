package com.borsam.pojo.statistics;

import java.io.Serializable;

/**
 * <pre>
 * @Description:
 * @author :Zhang zhongtao
 * @version: Ver 1.0
 * @Date: 2015-08-11 16:43
 * </pre>
 */
public class CountData implements Serializable {
    /**医生数量*/
    private Long doctorCount = 0L;
    /**患者数量*/
    private Long patientCouont = 0L;
    /**机构数量*/
    private Long orgCount = 0L;
    /**服务包数量*/
    private Long servicePackageCount = 0L;
    /**诊单数量*/
    private Long consultationCount = 0L;

    public Long getDoctorCount() {
        return doctorCount;
    }

    public void setDoctorCount(Long doctorCount) {
        this.doctorCount = doctorCount;
    }

    public Long getPatientCouont() {
        return patientCouont;
    }

    public void setPatientCouont(Long patientCouont) {
        this.patientCouont = patientCouont;
    }

    public Long getOrgCount() {
        return orgCount;
    }

    public void setOrgCount(Long orgCount) {
        this.orgCount = orgCount;
    }

    public Long getServicePackageCount() {
        return servicePackageCount;
    }

    public void setServicePackageCount(Long servicePackageCount) {
        this.servicePackageCount = servicePackageCount;
    }

    public Long getConsultationCount() {
        return consultationCount;
    }

    public void setConsultationCount(Long consultationCount) {
        this.consultationCount = consultationCount;
    }
}
