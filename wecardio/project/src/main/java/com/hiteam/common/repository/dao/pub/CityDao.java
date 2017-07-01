package com.hiteam.common.repository.dao.pub;

import com.hiteam.common.base.repository.dao.BaseDao;
import com.hiteam.common.repository.entity.pub.City;
import com.hiteam.common.util.pojo.EnumBean;

import java.util.List;

/**
 * 城市 - Dao
 */
public interface CityDao extends BaseDao<City, Long> {

    /**
     * 城市枚举下拉框
     * @param countryId  国家ID
     * @param areaId     地区ID
     * @param provinceId 省份ID
     * @param name       城市名称
     * @return 城市枚举列表
     */
    public List<EnumBean> citySel(Long countryId, Long areaId, Long provinceId, String name);

    /**
     * 获取城市邮编
     * @param cityId 城市ID
     * @return 邮编
     */
    public String getCityZipCode(Long cityId);

}
