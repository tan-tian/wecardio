package com.borsam.service.patient.impl;

import com.borsam.repository.dao.patient.PatientWalletDao;
import com.borsam.repository.dao.patient.PatientWalletHistoryDao;
import com.borsam.repository.entity.patient.PatientWallet;
import com.borsam.repository.entity.patient.PatientWalletHistory;
import com.borsam.service.patient.PatientWalletHistoryService;
import com.hiteam.common.base.service.impl.BaseServiceImpl;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import javax.persistence.LockModeType;
import java.math.BigDecimal;

/**
 * Service - 患者账户流水信息
 * Created by Sebarswee on 2015/8/4.
 */
@Service("patientWalletHistoryServiceImpl")
public class PatientWalletHistoryServiceImpl extends BaseServiceImpl<PatientWalletHistory, Long> implements PatientWalletHistoryService {

    @Resource(name = "patientWalletDaoImpl")
    private PatientWalletDao patientWalletDao;

    @Resource(name = "patientWalletHistoryDaoImpl")
    private PatientWalletHistoryDao patientWalletHistoryDao;

    @Resource(name = "patientWalletHistoryDaoImpl")
    public void setBaseDao(PatientWalletHistoryDao patientWalletHistoryDao) {
        super.setBaseDao(patientWalletHistoryDao);
    }

    @Override
    @Transactional(readOnly = true)
    public PatientWalletHistory findBySn(String sn) {
        return patientWalletHistoryDao.findBySn(sn);
    }

    @Override
    public void handle(PatientWalletHistory history) {
        patientWalletHistoryDao.refresh(history, LockModeType.PESSIMISTIC_WRITE);
        if (history != null && history.getSuccess() == 0) {
            if (history.getType() == PatientWalletHistory.Type.recharge) {
                PatientWallet patientWallet = patientWalletDao.find(history.getUid());
                if (patientWallet != null) {
                    patientWalletDao.lock(patientWallet, LockModeType.PESSIMISTIC_WRITE);
                    BigDecimal modifyBalance = history.getMoney();
                    if (modifyBalance != null && modifyBalance.compareTo(new BigDecimal(0)) != 0 && patientWallet.getTotal().add(modifyBalance).compareTo(new BigDecimal(0)) >= 0) {
                        patientWallet.setTotal(patientWallet.getTotal().add(modifyBalance));
                    }
                    patientWalletDao.merge(patientWallet);
                }
            }
            history.setSuccess(1);  // 1-成功
            patientWalletHistoryDao.merge(history);
        }
    }
}
