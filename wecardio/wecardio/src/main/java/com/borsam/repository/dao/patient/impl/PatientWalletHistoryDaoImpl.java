package com.borsam.repository.dao.patient.impl;

import com.borsam.repository.dao.patient.PatientWalletHistoryDao;
import com.borsam.repository.entity.patient.PatientWalletHistory;
import com.borsam.repository.entity.service.ServicePackage;
import com.hiteam.common.base.repository.dao.impl.BaseDaoImpl;
import com.hiteam.common.util.lang.DateUtil;
import org.springframework.stereotype.Repository;

import javax.persistence.FlushModeType;
import java.util.Calendar;

/**
 * Dao - 患者账户流水信息
 * Created by tantian on 2015/8/4.
 */
@Repository("patientWalletHistoryDaoImpl")
public class PatientWalletHistoryDaoImpl extends BaseDaoImpl<PatientWalletHistory, Long> implements PatientWalletHistoryDao {

    @Override
    public PatientWalletHistory findBySn(String sn) {
        String jpql = "select history from PatientWalletHistory history where history.tradeNo = :sn";
        try {
            return this.entityManager.createQuery(jpql, PatientWalletHistory.class).setFlushMode(FlushModeType.COMMIT).setParameter("sn", sn).getSingleResult();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public PatientWalletHistory addLogInSellService(ServicePackage servicePackage, Long uid, Calendar now) {
        PatientWalletHistory history = new PatientWalletHistory();
        history.setMoney(servicePackage.getPrice());
        history.setOid(servicePackage.getOrg().getId());
        history.setPayNo(" ");
        //支付方式
        history.setPayStyle(0);
        //1 成功
        history.setSuccess(1);
        history.setUid(uid);
        history.setVerdict("购买服务包");
        history.setTradeNo(" ");
        history.setType(PatientWalletHistory.Type.buy);
        history.setDefinition(servicePackage.getTitle());
        //更新入库
        this.persist(history);
        history.setTradeNo(DateUtil.format(now.getTime(), "yyyyMMdd") + (now.getTimeInMillis() / 1000) + history.getId());
        this.merge(history);
        return history;
    }
}
