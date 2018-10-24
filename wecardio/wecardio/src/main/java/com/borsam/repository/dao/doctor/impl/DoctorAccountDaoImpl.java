package com.borsam.repository.dao.doctor.impl;

import com.borsam.repository.dao.doctor.DoctorAccountDao;
import com.borsam.repository.entity.doctor.DoctorAccount;
import com.hiteam.common.base.repository.dao.impl.BaseDaoImpl;
import org.springframework.stereotype.Repository;

import javax.persistence.FlushModeType;

/**
 * Dao - 医生账号
 * Created by tantian on 2015/6/18.
 */
@Repository("doctorAccountDaoImpl")
public class DoctorAccountDaoImpl extends BaseDaoImpl<DoctorAccount, Long> implements DoctorAccountDao {

    @Override
    public DoctorAccount findByUsername(String username) {
        if (username == null) {
            return null;
        }
        try {
            String jpql = "select account from DoctorAccount account where lower(account.email) = lower(:username)";
            return entityManager.createQuery(jpql, DoctorAccount.class).setFlushMode(FlushModeType.COMMIT).setParameter("username", username).getSingleResult();
        } catch (Exception e) {
            return null;
        }
    }

    @Override
    public boolean emailExists(String email) {
        if (email == null) {
            return false;
        }
        String jpql = "select count(account) from DoctorAccount account where lower(account.email) = lower(:email)";
        Long count = entityManager.createQuery(jpql, Long.class).setFlushMode(FlushModeType.COMMIT).setParameter("email", email).getSingleResult();
        return count > 0;
    }
}
