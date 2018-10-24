package com.borsam.repository.dao.patient.impl;

import com.borsam.repository.dao.patient.PatientAccountDao;
import com.borsam.repository.entity.patient.PatientAccount;
import com.hiteam.common.base.repository.dao.impl.BaseDaoImpl;
import org.springframework.stereotype.Repository;

import javax.persistence.FlushModeType;

/**
 * Dao - 患者账号
 * Created by tantian on 2015/6/18.
 */
@Repository("patientAccountDaoImpl")
public class PatientAccountDaoImpl extends BaseDaoImpl<PatientAccount, Long> implements PatientAccountDao {

    @Override
    public PatientAccount findByUsername(String username) {
        if (username == null) {
            return null;
        }
        try {
            // 患者用手机号登录
            String jpql = "select account from PatientAccount account where lower(account.mobile) = lower(:username) or lower(account.email) = lower(:username) ";
            return entityManager.createQuery(jpql, PatientAccount.class).setFlushMode(FlushModeType.COMMIT).setParameter("username", username).getSingleResult();
        } catch (Exception e) {
            return null;
        }
    }
}
