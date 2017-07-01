package com.borsam.repository.dao.service;

import com.borsam.pojo.service.QueryServiceData;
import com.borsam.repository.entity.service.Service;
import com.borsam.repository.entity.service.ServiceKey;
import com.hiteam.common.base.pojo.search.Page;
import com.hiteam.common.base.repository.dao.BaseDao;

import java.util.Map;

/**
 * Dao - 服务项
 * Created by Sebarswee on 2015/7/20.
 */
public interface ServiceDao extends BaseDao<Service, ServiceKey> {
    /**
     * 分页查询服务
     * @param data 查询参数
     * @return page
     */
    public Page<Map> queryService(QueryServiceData data);
}
