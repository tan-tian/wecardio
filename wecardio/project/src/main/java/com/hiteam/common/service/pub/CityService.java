package com.hiteam.common.service.pub;

import com.hiteam.common.base.service.BaseService;
import com.hiteam.common.repository.entity.pub.City;
import com.hiteam.common.util.pojo.EnumBean;

import java.util.List;

/**
 * 城市 - Service
 */
public interface CityService extends BaseService<City, Long> {

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
