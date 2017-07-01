package com.borsam.repository.dao.extract;

import com.borsam.repository.entity.extract.MonthClear;
import com.hiteam.common.base.repository.dao.BaseDao;

/**
 * Dao - 月结信息
 * Created by Sebarswee on 2015/8/10.
 */
public interface MonthClearDao extends BaseDao<MonthClear, Long> {

    /**
     * 根据年月查找月结信息
     * @param orderId 单据标识
     * @param year 年份
     * @param month 月份
     * @return 月结信息
     */
    public MonthClear findByYearMonth(Long orderId, int year, int month);
}
