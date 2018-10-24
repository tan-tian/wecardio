package com.borsam.repository.dao.patient.impl;

import com.borsam.repository.dao.patient.PatientTokenDao;
import com.borsam.repository.entity.patient.PatientToken;
import com.hiteam.common.base.repository.dao.impl.BaseDaoImpl;
import org.springframework.stereotype.Repository;

/**
 * Dao - 患者Token
 * Created by tantian on 2015/10/15.
 */
@Repository("patientTokenDaoImpl")
public class PatientTokenDaoImpl extends BaseDaoImpl<PatientToken, Long> implements PatientTokenDao {
}
