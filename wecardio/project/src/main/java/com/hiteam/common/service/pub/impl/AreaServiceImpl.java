package com.hiteam.common.service.pub.impl;

import com.hiteam.common.base.service.impl.BaseServiceImpl;
import com.hiteam.common.repository.dao.pub.AreaDao;
import com.hiteam.common.repository.entity.pub.Area;
import com.hiteam.common.service.pub.AreaService;
import com.hiteam.common.util.pojo.EnumBean;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;

/**
 * 地区 - Service
 */
@Service("areaServiceImpl")
public class AreaServiceImpl extends BaseServiceImpl<Area, Long> implements AreaService {

    @Resource(name = "areaDaoImpl")
    private AreaDao areaDao;

    @Resource(name = "areaDaoImpl")
    public void setBaseDao(AreaDao areaDao) {
        super.setBaseDao(areaDao);
    }

    @Transactional(readOnly = true)
    @Override
    public List<EnumBean> areaSel(Long countryId, String name) {
        return areaDao.areaSel(countryId, name);
    }
}
