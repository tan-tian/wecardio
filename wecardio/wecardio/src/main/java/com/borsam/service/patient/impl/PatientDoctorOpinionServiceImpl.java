package com.borsam.service.patient.impl;

import com.borsam.repository.dao.patient.PatientDoctorOpinionDao;
import com.borsam.repository.entity.patient.PatientDoctorOpinion;
import com.borsam.service.patient.PatientDoctorOpinionService;
import com.hiteam.common.base.service.impl.BaseServiceImpl;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * Service - 患者账号
 * Created by Sebarswee on 2015/6/18.
 */
@Service("patientDoctorOpinionServiceImpl")
public class PatientDoctorOpinionServiceImpl extends BaseServiceImpl<PatientDoctorOpinion, Long> implements PatientDoctorOpinionService {

    @Resource(name = "patientDoctorOpinionDaoImpl")
    private PatientDoctorOpinionDao patientDoctorOpinionDao;

    @Resource(name = "patientDoctorOpinionDaoImpl")
    public void setBaseDao(PatientDoctorOpinionDao patientDoctorOpinionDao) {
        super.setBaseDao(patientDoctorOpinionDao);
    }

}
