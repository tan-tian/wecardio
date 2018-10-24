package com.borsam.service.patient.impl;

import com.borsam.pojo.security.Principal;
import com.borsam.repository.dao.patient.PatientAccountDao;
import com.borsam.repository.dao.patient.PatientActivityDao;
import com.borsam.repository.entity.patient.PatientAccount;
import com.borsam.repository.entity.patient.PatientActivity;
import com.borsam.service.patient.PatientAccountService;
import com.borsam.service.patient.PatientActivityService;
import com.hiteam.common.base.service.impl.BaseServiceImpl;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;

/**
 * Service - 患者账号
 * Created by tantian on 2015/6/18.
 */
@Service("patientActivityServiceImpl")
public class PatientActivityServiceImpl extends BaseServiceImpl<PatientActivity, Long> implements PatientActivityService {

    @Resource(name = "patientActivityDaoImpl")
    private PatientActivityDao patientActivityDao;

    @Resource(name = "patientActivityDaoImpl")
    public void setBaseDao(PatientActivityDao patientActivityDao) {
        super.setBaseDao(patientActivityDao);
    }

}
