package com.borsam.service.patient.impl;

import com.borsam.repository.dao.patient.PatientServiceDao;
import com.borsam.repository.entity.patient.PatientService;
import com.borsam.repository.entity.patient.PatientServiceKey;
import com.borsam.service.patient.PatientServiceService;
import com.hiteam.common.base.service.impl.BaseServiceImpl;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * Service - 患者购买的服务信息
 * Created by Sebarswee on 2015/7/22.
 */
@Service("patientServiceServiceImpl")
public class PatientServiceServiceImpl extends BaseServiceImpl<PatientService, PatientServiceKey> implements PatientServiceService {

    @Resource(name = "patientServiceDaoImpl")
    public void setBaseDao(PatientServiceDao patientServiceDao) {
        super.setBaseDao(patientServiceDao);
    }
}
