package com.borsam.repository.dao.patient.impl;

import com.borsam.repository.dao.patient.PatientWalletVerifyDao;
import com.borsam.repository.entity.patient.PatientWalletVerify;
import com.hiteam.common.base.repository.dao.impl.BaseDaoImpl;
import org.springframework.stereotype.Repository;

/**
 * Dao - 患者钱包验证
 * Created by tantian on 2015/7/23.
 */
@Repository("patientWalletVerifyDaoImpl")
public class PatientWalletVerifyDaoImpl extends BaseDaoImpl<PatientWalletVerify, Long> implements PatientWalletVerifyDao {
}
