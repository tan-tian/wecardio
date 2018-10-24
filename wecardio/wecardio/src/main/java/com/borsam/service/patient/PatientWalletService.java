package com.borsam.service.patient;

import com.borsam.repository.entity.patient.PatientWallet;
import com.hiteam.common.base.service.BaseService;

/**
 * Service - 患者钱包
 * Created by tantian on 2015/7/31.
 */
public interface PatientWalletService extends BaseService<PatientWallet, Long> {

    /**
     * 获取患者钱包
     * @param uid 患者ID
     * @return 患者钱包
     */
    public PatientWallet getWallet(Long uid);
}
