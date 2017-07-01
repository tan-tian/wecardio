package com.borsam.service.service;

import com.borsam.repository.entity.service.ServiceType;
import com.hiteam.common.base.service.BaseService;

/**
 * Service - 服务配置
 * Created by Sebarswee on 2015/7/20.
 */
public interface ServiceTypeService extends BaseService<ServiceType, Long> {
    /***
     * 判断创建或编辑的服务类型是否存在
     * @param serviceType
     * @return boolean：true-存在；false-不存在
     */
    public Boolean isExistType(ServiceType serviceType);
}
