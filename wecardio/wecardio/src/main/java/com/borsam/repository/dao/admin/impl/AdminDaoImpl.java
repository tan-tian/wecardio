package com.borsam.repository.dao.admin.impl;

import com.borsam.repository.dao.admin.AdminDao;
import com.borsam.repository.entity.admin.Admin;
import com.hiteam.common.base.repository.dao.impl.BaseDaoImpl;
import org.springframework.stereotype.Repository;

import javax.persistence.FlushModeType;

/**
 * Dao - 平台管理员账号
 * Created by Sebarswee on 2015/6/19.
 */
@Repository("adminDaoImpl")
public class AdminDaoImpl extends BaseDaoImpl<Admin, Long> implements AdminDao {

    @Override
    public Admin findByUsername(String username) {
        if (username == null) {
            return null;
        }
        try {
            String jpql = "select account from Admin account where lower(account.username) = lower(:username)";
            return entityManager.createQuery(jpql, Admin.class).setFlushMode(FlushModeType.COMMIT).setParameter("username", username).getSingleResult();
        } catch (Exception e) {
            return null;
        }
    }
}
