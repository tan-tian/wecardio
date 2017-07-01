package com.borsam.repository.dao.patient.impl;

import com.borsam.repository.dao.patient.PatientDoctorOpinionDao;
import com.borsam.repository.entity.patient.PatientDoctorOpinion;
import com.hiteam.common.base.repository.dao.impl.BaseDaoImpl;
import org.springframework.stereotype.Repository;

/**
 * Dao - 活动记录
 * Created by Sebarswee on 2015/6/18.
 */
@Repository("patientDoctorOpinionDaoImpl")
public class PatientDoctorOpinionDaoImpl extends BaseDaoImpl<PatientDoctorOpinion, Long> implements PatientDoctorOpinionDao {

}
