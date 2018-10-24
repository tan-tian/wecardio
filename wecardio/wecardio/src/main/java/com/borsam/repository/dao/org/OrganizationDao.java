package com.borsam.repository.dao.org;

import com.borsam.repository.entity.org.Organization;
import com.hiteam.common.base.repository.dao.BaseDao;
import com.hiteam.common.util.pojo.EnumBean;

import java.util.List;

/**
 * Dao - 机构信息
 * Created by tantian on 2015/7/1.
 */
public interface OrganizationDao extends BaseDao<Organization, Long> {

    /**
     * 机构列表（for 下拉框）
     * @param name 机构名称
     * @return 机构列表
     */
    public List<EnumBean> sel(String name);
    
    /**
     * 设备类型列表（for 下拉框）
     * @param name 设备类型
     * @return 类型列表
     */
    public List<EnumBean> selType(String tblName, String colName,String notIn);
}
