package com.hiteam.common.service.pub;

import com.hiteam.common.base.service.BaseService;
import com.hiteam.common.repository.entity.pub.Area;
import com.hiteam.common.util.pojo.EnumBean;

import java.util.List;

/**
 * 地区 - Service.
 */
public interface AreaService extends BaseService<Area, Long> {

    /**
     * 地区枚举下拉框
     * @param countryId 国家ID
     * @param name 地区名称
     * @return 地区枚举列表
     */
    public List<EnumBean> areaSel(Long countryId, String name);

}
