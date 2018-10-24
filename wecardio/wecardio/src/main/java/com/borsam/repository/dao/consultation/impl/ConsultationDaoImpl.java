package com.borsam.repository.dao.consultation.impl;

import com.borsam.repository.dao.consultation.ConsultationDao;
import com.borsam.repository.entity.consultation.Consultation;
import com.hiteam.common.base.repository.dao.impl.BaseDaoImpl;
import org.springframework.stereotype.Repository;

import javax.persistence.FlushModeType;

/**
 * Dao - 会诊申请
 * Created by tantian on 2015/7/24.
 */
@Repository("consultationDaoImpl")
public class ConsultationDaoImpl extends BaseDaoImpl<Consultation, Long> implements ConsultationDao {

    @Override
    public void updateTradeNo(Long id, String tradeNo) {
        String jpql = "update Consultation  set tradeNo = :tradeNo where id = :id";
        this.entityManager.createQuery(jpql).setFlushMode(FlushModeType.COMMIT).setParameter("id", id).setParameter("tradeNo", tradeNo).executeUpdate();
    }
}
