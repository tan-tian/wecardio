package com.borsam.repository.dao.patient.impl;

import com.borsam.repository.dao.patient.PatientActivityDao;
import com.borsam.repository.entity.patient.PatientActivity;
import com.hiteam.common.base.repository.dao.impl.BaseDaoImpl;
import org.springframework.stereotype.Repository;

/**
 * Dao - 活动记录
 * Created by tantian on 2015/6/18.
 */
@Repository("patientActivityDaoImpl")
public class PatientActivityDaoImpl extends BaseDaoImpl<PatientActivity, Long> implements PatientActivityDao {

}
