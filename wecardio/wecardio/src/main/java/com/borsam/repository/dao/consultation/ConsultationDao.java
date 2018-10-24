package com.borsam.repository.dao.consultation;


import com.borsam.repository.entity.consultation.Consultation;
import com.hiteam.common.base.repository.dao.BaseDao;

/**
 * Dao - 医生账号
 * Created by tantian on 2015/6/18.
 */
public interface ConsultationDao extends BaseDao<Consultation, Long> {

    public void updateTradeNo(Long id, String tradeNo);
}
