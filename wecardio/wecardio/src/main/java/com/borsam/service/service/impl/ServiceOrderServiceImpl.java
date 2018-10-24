package com.borsam.service.service.impl;

import com.borsam.repository.dao.service.ServiceOrderDao;
import com.borsam.repository.entity.patient.PatientProfile;
import com.borsam.repository.entity.service.ServiceOrder;
import com.borsam.service.service.ServiceOrderService;
import com.hiteam.common.base.pojo.search.Page;
import com.hiteam.common.base.pojo.search.Pageable;
import com.hiteam.common.base.service.impl.BaseServiceImpl;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * <pre>
 * @Description:
 * @author :tantian
 * @version: Ver 1.0
 * @Date: 2015-08-06 15:02
 * </pre>
 */
@Service("serviceOrderServiceImpl")
public class ServiceOrderServiceImpl extends BaseServiceImpl<ServiceOrder, Long> implements ServiceOrderService {
    @Resource(name = "serviceOrderDaoImpl")
    private ServiceOrderDao serviceOrderDao;

    @Resource(name = "serviceOrderDaoImpl")
    public void setBaseDao(ServiceOrderDao serviceOrderDao) {
        super.setBaseDao(serviceOrderDao);
    }

    @Override
    public Page<PatientProfile> query(Long packageId, Pageable pageable) {
        return serviceOrderDao.query(packageId,pageable);
    }
}
