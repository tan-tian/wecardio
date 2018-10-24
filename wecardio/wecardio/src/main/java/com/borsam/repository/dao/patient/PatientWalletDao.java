package com.borsam.repository.dao.patient;

import com.borsam.repository.entity.patient.PatientWallet;
import com.hiteam.common.base.repository.dao.BaseDao;

/**
 * Dao - 患者钱包
 * Created by tantian on 2015/7/31.
 */
public interface PatientWalletDao extends BaseDao<PatientWallet, Long> {

    /**
     * 获取患者钱包
     * @param uid 患者ID
     * @return 患者钱包
     */
    public PatientWallet getWallet(Long uid);
}
