package com.borsam.service.patient.impl;

import com.borsam.repository.dao.patient.PatientWalletDao;
import com.borsam.repository.entity.patient.PatientWallet;
import com.borsam.service.patient.PatientWalletService;
import com.hiteam.common.base.service.impl.BaseServiceImpl;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;

/**
 * Service - 患者钱包
 * Created by tantian on 2015/7/31.
 */
@Service("patientWalletServiceImpl")
public class PatientWalletServiceImpl extends BaseServiceImpl<PatientWallet, Long> implements PatientWalletService {

    @Resource(name = "patientWalletDaoImpl")
    private PatientWalletDao patientWalletDao;

    @Resource(name = "patientWalletDaoImpl")
    public void setBaseDao(PatientWalletDao patientWalletDao) {
        super.setBaseDao(patientWalletDao);
    }

    @Override
    @Transactional(readOnly = true)
    public PatientWallet getWallet(Long uid) {
        return patientWalletDao.getWallet(uid);
    }
}
