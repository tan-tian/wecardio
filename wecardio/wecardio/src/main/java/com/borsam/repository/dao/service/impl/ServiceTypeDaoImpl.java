package com.borsam.repository.dao.service.impl;

import com.borsam.repository.dao.service.ServiceTypeDao;
import com.borsam.repository.entity.service.ServiceType;
import com.hiteam.common.base.repository.dao.impl.BaseDaoImpl;
import org.springframework.stereotype.Repository;

/**
 * Dao - 服务配置
 * Created by tantian on 2015/7/20.
 */
@Repository("serviceTypeDaoImpl")
public class ServiceTypeDaoImpl extends BaseDaoImpl<ServiceType, Long> implements ServiceTypeDao {
}
