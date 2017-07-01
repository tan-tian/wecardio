package com.hiteam.common.repository.dao.pub;

import com.hiteam.common.base.repository.dao.BaseDao;
import com.hiteam.common.repository.entity.pub.Area;
import com.hiteam.common.util.pojo.EnumBean;

import java.util.List;

/**
 * 地区 - Dao
 */
public interface AreaDao extends BaseDao<Area, Long> {

    /**
     * 地区枚举下拉框
     * @param countryId 国家ID
     * @param name 地区名称
     * @return 地区枚举列表
     */
    public List<EnumBean> areaSel(Long countryId, String name);

}
