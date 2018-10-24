package com.borsam.repository.dao.patient.impl;

import com.borsam.repository.dao.patient.PatientWalletDao;
import com.borsam.repository.entity.patient.PatientWallet;
import com.hiteam.common.base.repository.dao.impl.BaseDaoImpl;
import org.springframework.stereotype.Repository;

import javax.persistence.FlushModeType;

/**
 * Dao - 患者钱包
 * Created by tantian on 2015/7/31.
 */
@Repository("patientWalletDaoImpl")
public class PatientWalletDaoImpl extends BaseDaoImpl<PatientWallet, Long> implements PatientWalletDao {

    @Override
    public PatientWallet getWallet(Long uid) {
        String jpql = "select t from PatientWallet t where t.patient.id = :uid";
        try {
            return this.entityManager.createQuery(jpql, PatientWallet.class).setFlushMode(FlushModeType.COMMIT).setParameter("uid", uid).getSingleResult();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
