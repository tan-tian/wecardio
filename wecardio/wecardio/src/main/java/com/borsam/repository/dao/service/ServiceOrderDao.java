package com.borsam.repository.dao.service;

import com.borsam.repository.entity.patient.PatientProfile;
import com.borsam.repository.entity.service.ServiceOrder;
import com.borsam.repository.entity.service.ServicePackage;
import com.hiteam.common.base.pojo.search.Page;
import com.hiteam.common.base.pojo.search.Pageable;
import com.hiteam.common.base.repository.dao.BaseDao;

/**
 * <pre>
 * @Description:
 * @author :tantian
 * @version: Ver 1.0
 * @Date: 2015-08-06 14:58
 * </pre>
 */
public interface ServiceOrderDao extends BaseDao<ServiceOrder, Long> {
    /**
     * 查询单据
     * @param packageId 包ID
     * @param pageable 分页参数
     * @return
     */
    public Page<PatientProfile> query(Long packageId, Pageable pageable);

    /**
     * 购买服务包时，新增记录
     * @param servicePackage 服务包
     * @param tradeNo 交易流水号
     * @param uid 患者ID
     */
    public ServiceOrder addLogInSellService(ServicePackage servicePackage, String tradeNo, PatientProfile patient,Long did);
}
