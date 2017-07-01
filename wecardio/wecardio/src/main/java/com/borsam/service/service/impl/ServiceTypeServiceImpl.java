package com.borsam.service.service.impl;

import com.borsam.repository.dao.service.ServiceTypeDao;
import com.borsam.repository.entity.service.ServiceType;
import com.borsam.service.service.ServiceTypeService;
import com.hiteam.common.base.pojo.search.Filter;
import com.hiteam.common.base.service.impl.BaseServiceImpl;
import com.hiteam.common.util.lang.StringUtil;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

/**
 * Service - 服务配置
 * Created by Sebarswee on 2015/7/20.
 */
@Service("serviceTypeServiceImpl")
public class ServiceTypeServiceImpl extends BaseServiceImpl<ServiceType, Long> implements ServiceTypeService {

    @Resource(name = "serviceTypeDaoImpl")
    private ServiceTypeDao serviceTypeDao;

    @Resource(name = "serviceTypeDaoImpl")
    public void setBaseDao(ServiceTypeDao serviceTypeDao) {
        super.setBaseDao(serviceTypeDao);
    }

    @Override
    public Boolean isExistType(ServiceType serviceType) {
        List<Filter> filters = new ArrayList<>();

        if (StringUtil.isNotBlank(serviceType.getUuid())) {
            filters.add(Filter.ne("uuid", serviceType.getUuid()));
        }

        filters.add(Filter.eq("id", serviceType.getId()));

        Long count = this.count(filters.toArray(new Filter[filters.size()]));

        return (count == null ? 0 : count) > 0;
    }
}
