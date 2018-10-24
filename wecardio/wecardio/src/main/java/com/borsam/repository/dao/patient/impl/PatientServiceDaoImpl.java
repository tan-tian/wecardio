package com.borsam.repository.dao.patient.impl;

import com.borsam.repository.dao.patient.PatientServiceDao;
import com.borsam.repository.entity.patient.PatientService;
import com.borsam.repository.entity.patient.PatientServiceKey;
import com.hiteam.common.base.repository.dao.impl.BaseDaoImpl;
import org.springframework.stereotype.Repository;

/**
 * Dao - 患者购买的服务信息
 * Created by tantian on 2015/7/22.
 */
@Repository("patientServiceDaoImpl")
public class PatientServiceDaoImpl extends BaseDaoImpl<PatientService, PatientServiceKey> implements PatientServiceDao {
}
