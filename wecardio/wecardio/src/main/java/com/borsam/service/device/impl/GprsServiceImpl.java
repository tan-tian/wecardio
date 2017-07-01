package com.borsam.service.device.impl;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.borsam.repository.dao.device.GprsDao;
import com.borsam.repository.dao.device.impl.GprsDaoImpl;
import com.borsam.repository.dao.doctor.DoctorAccountDao;
import com.borsam.repository.entity.device.Gprs;
import com.borsam.repository.entity.doctor.DoctorAccount;
import com.borsam.repository.entity.patient.PatientProfile;
import com.borsam.service.device.GprsService;
import com.hiteam.common.base.service.impl.BaseServiceImpl;

@Service("gprsServiceImpl")
public class GprsServiceImpl extends BaseServiceImpl<Gprs, Long> implements GprsService{
	
	@Resource(name = "GprsDaoImpl")
	    private GprsDao gprsDao;
	
	
	@Resource(name = "GprsDaoImpl")
    public void setBaseDao(GprsDao gprsDao) {
        super.setBaseDao(gprsDao);
    }
	
	@Transactional(readOnly = true)
    @Override
    public boolean isBind(PatientProfile patientProfile) {
        return gprsDao.isBind(patientProfile);
    }
	
	@Override
	public boolean isImeExist(String ime){
		return gprsDao.isImeExist(ime);
	}
}
