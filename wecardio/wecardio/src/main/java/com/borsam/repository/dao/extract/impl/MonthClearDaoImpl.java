package com.borsam.repository.dao.extract.impl;

import com.borsam.repository.dao.extract.MonthClearDao;
import com.borsam.repository.entity.extract.MonthClear;
import com.hiteam.common.base.repository.dao.impl.BaseDaoImpl;
import org.springframework.stereotype.Repository;

import javax.persistence.FlushModeType;

/**
 * Dao - 月结信息
 * Created by Sebarswee on 2015/8/10.
 */
@Repository("monthClearDaoImpl")
public class MonthClearDaoImpl extends BaseDaoImpl<MonthClear, Long> implements MonthClearDao {

    @Override
    public MonthClear findByYearMonth(Long orderId, int year, int month) {
        String jpql = "select m from MonthClear m where m.order.id = :orderId and m.year = :year and m.month = :month";
        try {
            return this.entityManager.createQuery(jpql, MonthClear.class).setFlushMode(FlushModeType.COMMIT)
                    .setParameter("orderId", orderId)
                    .setParameter("year", year)
                    .setParameter("month", month)
                    .getSingleResult();
        } catch (Exception e) {
            return null;
        }
    }
}
