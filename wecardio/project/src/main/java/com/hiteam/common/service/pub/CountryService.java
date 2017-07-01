package com.hiteam.common.service.pub;

import com.hiteam.common.base.service.BaseService;
import com.hiteam.common.repository.entity.pub.Country;
import com.hiteam.common.util.pojo.EnumBean;

import java.util.List;

/**
 * 国家 - Service
 */
public interface CountryService extends BaseService<Country, Long> {

    /**
     * 国家枚举下拉框
     * @param name 国家名称
     * @return 国家枚举列表
     */
    public List<EnumBean> countrySel(String name);

}
