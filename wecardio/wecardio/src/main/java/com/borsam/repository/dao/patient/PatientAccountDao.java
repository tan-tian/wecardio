package com.borsam.repository.dao.patient;

import com.borsam.repository.entity.patient.PatientAccount;
import com.hiteam.common.base.repository.dao.BaseDao;

/**
 * Dao - 患者账号
 * Created by Sebarswee on 2015/6/18.
 */
public interface PatientAccountDao extends BaseDao<PatientAccount, Long> {

    /**
     * 根据用户名查找患者账号
     * @param username 用户名
     * @return 患者账号
     */
    public PatientAccount findByUsername(String username);
}
