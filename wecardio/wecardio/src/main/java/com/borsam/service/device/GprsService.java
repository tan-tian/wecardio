package com.borsam.service.device;

import com.borsam.repository.entity.device.Gprs;
import com.borsam.repository.entity.doctor.DoctorAccount;
import com.borsam.repository.entity.patient.PatientProfile;
import com.hiteam.common.base.service.BaseService;

public interface GprsService  extends BaseService<Gprs, Long>{
	
	/**
     * 判断Uid是否存在
     * @param 患者
     * @return Uid是否存在
     */
    public boolean isBind(PatientProfile patientProfile);
    
    /**
     * 判断IMEI是否存在
     * @param IMEI
     * @return imei是否存在
     */
    public boolean isImeExist(String ime);
}
