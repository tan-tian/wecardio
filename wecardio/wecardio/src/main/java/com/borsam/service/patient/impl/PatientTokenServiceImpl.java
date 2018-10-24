package com.borsam.service.patient.impl;

import com.borsam.repository.dao.patient.PatientTokenDao;
import com.borsam.repository.entity.patient.PatientToken;
import com.borsam.service.patient.PatientTokenService;
import com.hiteam.common.base.service.impl.BaseServiceImpl;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * Service - 患者Token
 * Created by tantian on 2015/10/15.
 */
@Service("patientTokenServiceImpl")
public class PatientTokenServiceImpl extends BaseServiceImpl<PatientToken, Long> implements PatientTokenService {

    @Resource(name = "patientTokenDaoImpl")
    public void setBaseDao(PatientTokenDao patientTokenDao) {
        super.setBaseDao(patientTokenDao);
    }
}
