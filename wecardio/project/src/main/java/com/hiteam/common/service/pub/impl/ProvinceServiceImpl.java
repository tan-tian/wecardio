package com.hiteam.common.service.pub.impl;

import com.hiteam.common.base.service.impl.BaseServiceImpl;
import com.hiteam.common.repository.dao.pub.ProvinceDao;
import com.hiteam.common.repository.entity.pub.Province;
import com.hiteam.common.service.pub.ProvinceService;
import com.hiteam.common.util.pojo.EnumBean;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;

/**
 * 省份 - Service
 */
@Service("provinceServiceImpl")
public class ProvinceServiceImpl extends BaseServiceImpl<Province, Long> implements ProvinceService {

    @Resource(name = "provinceDaoImpl")
    private ProvinceDao provinceDao;

    @Resource(name = "provinceDaoImpl")
    public void setBaseDao(ProvinceDao provinceDao) {
        super.setBaseDao(provinceDao);
    }

    @Transactional(readOnly = true)
    @Override
    public List<EnumBean> provinceSel(Long countryId, Long areaId, String name) {
        return provinceDao.provinceSel(countryId, areaId, name);
    }
}
