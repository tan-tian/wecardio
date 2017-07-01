package com.hiteam.common.service.pub.impl;

import com.hiteam.common.base.service.impl.BaseServiceImpl;
import com.hiteam.common.repository.dao.pub.CountryDao;
import com.hiteam.common.repository.entity.pub.Country;
import com.hiteam.common.service.pub.CountryService;
import com.hiteam.common.util.pojo.EnumBean;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;

/**
 * 国家 - Service
 */
@Service("countryServiceImpl")
public class CountryServiceImpl extends BaseServiceImpl<Country, Long> implements CountryService {

    @Resource(name = "countryDaoImpl")
    private CountryDao countryDao;

    @Resource(name = "countryDaoImpl")
    public void setBaseDao(CountryDao countryDao) {
        super.setBaseDao(countryDao);
    }

    @Transactional(readOnly = true)
    @Override
    public List<EnumBean> countrySel(String name) {
        return countryDao.countrySel(name);
    }
}
