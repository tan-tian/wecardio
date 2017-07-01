package com.borsam.service.device.impl;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.borsam.repository.dao.device.GprsDao;
import com.borsam.repository.dao.device.GprsHistoryDao;
import com.borsam.repository.dao.device.impl.GprsDaoImpl;
import com.borsam.repository.dao.doctor.DoctorAccountDao;
import com.borsam.repository.entity.device.Gprs;
import com.borsam.repository.entity.device.GprsHistory;
import com.borsam.repository.entity.doctor.DoctorAccount;
import com.borsam.repository.entity.patient.PatientProfile;
import com.borsam.service.device.GprsHistoryService;
import com.borsam.service.device.GprsService;
import com.hiteam.common.base.repository.dao.BaseDao;
import com.hiteam.common.base.service.impl.BaseServiceImpl;

@Service("gprsHistoryServiceImpl")
public class GprsHistoryServiceImpl extends BaseServiceImpl<GprsHistory, Long> implements GprsHistoryService{
	
	@Resource(name = "GprsHistoryDaoImpl")
    public void setBaseDao(BaseDao<GprsHistory, Long> gprsHistpryDao) {
        super.setBaseDao(gprsHistpryDao);
    }
}
