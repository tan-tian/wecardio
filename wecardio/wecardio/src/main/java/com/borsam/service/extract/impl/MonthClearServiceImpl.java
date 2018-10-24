package com.borsam.service.extract.impl;

import com.borsam.repository.dao.extract.MonthClearDao;
import com.borsam.repository.entity.extract.MonthClear;
import com.borsam.service.extract.MonthClearService;
import com.hiteam.common.base.service.impl.BaseServiceImpl;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * Service - 月结信息
 * Created by tantian on 2015/8/10.
 */
@Service("monthClearServiceImpl")
public class MonthClearServiceImpl extends BaseServiceImpl<MonthClear, Long> implements MonthClearService {

    @Resource(name = "monthClearDaoImpl")
    public void setBaseDao(MonthClearDao monthClearDao) {
        super.setBaseDao(monthClearDao);
    }
}
