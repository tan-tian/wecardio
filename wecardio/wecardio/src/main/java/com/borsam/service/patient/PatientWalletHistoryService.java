package com.borsam.service.patient;

import com.borsam.repository.entity.patient.PatientWalletHistory;
import com.hiteam.common.base.service.BaseService;

/**
 * Service - 患者账户流水信息
 * Created by tantian on 2015/8/4.
 */
public interface PatientWalletHistoryService extends BaseService<PatientWalletHistory, Long> {

    /**
     * 通过交易号查找记录
     * @param sn 交易号
     * @return 流水信息
     */
    public PatientWalletHistory findBySn(String sn);

    /**
     * 支付处理
     * @param history 流水记录
     */
    public void handle(PatientWalletHistory history);
}
