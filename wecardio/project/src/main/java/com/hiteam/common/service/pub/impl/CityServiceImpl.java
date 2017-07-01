package com.hiteam.common.service.pub.impl;

import com.hiteam.common.base.service.impl.BaseServiceImpl;
import com.hiteam.common.repository.dao.pub.CityDao;
import com.hiteam.common.repository.entity.pub.City;
import com.hiteam.common.service.pub.CityService;
import com.hiteam.common.util.pojo.EnumBean;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;

/**
 * 城市 - Service
 */
@Service("cityServiceImpl")
public class CityServiceImpl extends BaseServiceImpl<City, Long> implements CityService {

    @Resource(name = "cityDaoImpl")
    private CityDao cityDao;

    @Resource(name = "cityDaoImpl")
    public void setBaseDao(CityDao cityDao) {
        super.setBaseDao(cityDao);
    }

    @Transactional(readOnly = true)
    @Override
    public List<EnumBean> citySel(Long countryId, Long areaId, Long provinceId, String name) {
        return cityDao.citySel(countryId, areaId, provinceId, name);
    }

    @Transactional(readOnly = true)
    @Override
    public String getCityZipCode(Long cityId) {
        return cityDao.getCityZipCode(cityId);
    }
}
