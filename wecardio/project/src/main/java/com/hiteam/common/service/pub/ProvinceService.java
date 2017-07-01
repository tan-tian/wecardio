package com.hiteam.common.service.pub;

import com.hiteam.common.base.service.BaseService;
import com.hiteam.common.repository.entity.pub.Province;
import com.hiteam.common.util.pojo.EnumBean;

import java.util.List;

/**
 * 省份 - Service
 */
public interface ProvinceService extends BaseService<Province, Long> {

    /**
     * 省份枚举下拉框
     * @param countryId 国家ID
     * @param areaId    地区ID
     * @param name      省份名称
     * @return 省份枚举列表
     */
    public List<EnumBean> provinceSel(Long countryId, Long areaId, String name);

}
