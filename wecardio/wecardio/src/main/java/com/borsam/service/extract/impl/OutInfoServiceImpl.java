package com.borsam.service.extract.impl;

import com.borsam.repository.dao.extract.OutInfoDao;
import com.borsam.repository.entity.extract.OutInfo;
import com.borsam.service.extract.OutInfoService;
import com.hiteam.common.base.service.impl.BaseServiceImpl;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * Service - 出账信息
 * Created by tantian on 2015/8/10.
 */
@Service("outInfoServiceImpl")
public class OutInfoServiceImpl extends BaseServiceImpl<OutInfo, Long> implements OutInfoService {

    @Resource(name = "outInfoDaoImpl")
    public void setBaseDao(OutInfoDao outInfoDao) {
        super.setBaseDao(outInfoDao);
    }
}
