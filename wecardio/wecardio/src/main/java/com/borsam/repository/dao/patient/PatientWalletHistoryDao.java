package com.borsam.repository.dao.patient;

import com.borsam.repository.entity.patient.PatientWalletHistory;
import com.borsam.repository.entity.service.ServicePackage;
import com.hiteam.common.base.repository.dao.BaseDao;

import java.util.Calendar;

/**
 * Dao - 患者账户流水信息
 * Created by tantian on 2015/8/4.
 */
public interface PatientWalletHistoryDao extends BaseDao<PatientWalletHistory, Long> {

    /**
     * 通过交易号查找记录
     * @param sn 交易号
     * @return 流水信息
     */
    public PatientWalletHistory findBySn(String sn);

    /**
     * 购买服务包时，新增记录
     * @param servicePackage 服务包
     * @param uid 患者ID
     * @param now 当前时间
     * @return PatientWalletHistory
     */
    public PatientWalletHistory addLogInSellService(ServicePackage servicePackage, Long uid, Calendar now);
}
