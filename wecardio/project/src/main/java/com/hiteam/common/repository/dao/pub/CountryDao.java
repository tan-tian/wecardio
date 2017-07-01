package com.hiteam.common.repository.dao.pub;

import com.hiteam.common.base.repository.dao.BaseDao;
import com.hiteam.common.repository.entity.pub.Country;
import com.hiteam.common.util.pojo.EnumBean;

import java.util.List;

/**
 * 国家 - Dao
 */
public interface CountryDao extends BaseDao<Country, Long> {

    /**
     * 国家枚举下拉框
     *
     * @param name 国家名称
     * @return 国家枚举列表
     */
    public List<EnumBean> countrySel(String name);
}
