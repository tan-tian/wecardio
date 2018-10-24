package com.borsam.service.service;

import com.borsam.repository.entity.patient.PatientProfile;
import com.borsam.repository.entity.service.ServiceOrder;
import com.hiteam.common.base.pojo.search.Page;
import com.hiteam.common.base.pojo.search.Pageable;
import com.hiteam.common.base.service.BaseService;

/**
 * <pre>
 * @Description:
 * @author :tantian
 * @version: Ver 1.0
 * @Date: 2015-08-06 15:00
 * </pre>
 */
public interface ServiceOrderService extends BaseService<ServiceOrder, Long> {
    /**
     * 分页查询
     * @param packageId 包ID
     * @param pageable 分页参数
     * @return page
     */
    public Page<PatientProfile> query(Long packageId, Pageable pageable);
}
