package com.borsam.service.patient;

import com.borsam.repository.entity.patient.PatientAccount;
import com.borsam.repository.entity.patient.PatientWalletVerify;
import com.hiteam.common.base.service.BaseService;
import com.hiteam.common.web.Message;

/**
 * Service - 患者钱包验证
 * Created by Sebarswee on 2015/7/23.
 */
public interface PatientWalletVerifyService extends BaseService<PatientWalletVerify, Long> {
    /***
     * 支付校验
     * @param patientAccount
     * @return Message
     */
    public Message verify(PatientAccount patientAccount);
    /**
     * 钱包激活
     * @param uid 患者id
     * @param password 支付密码
     */
    public void activate(Long uid, String password);
}
