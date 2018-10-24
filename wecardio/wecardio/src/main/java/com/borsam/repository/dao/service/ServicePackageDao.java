package com.borsam.repository.dao.service;

import com.borsam.pojo.service.QueryServicePackageData;
import com.borsam.repository.entity.service.ServicePackage;
import com.hiteam.common.base.pojo.search.Page;
import com.hiteam.common.base.repository.dao.BaseDao;

/**
 * Dao - 服务包
 * Created by tantian on 2015/7/20.
 */
public interface ServicePackageDao extends BaseDao<ServicePackage, Long> {
    /**
     * 分页查询服务包信息
     * @param data 查询参数
     * @return Page
     */
    public Page<ServicePackage> queryServicePackage(QueryServicePackageData data);
}
