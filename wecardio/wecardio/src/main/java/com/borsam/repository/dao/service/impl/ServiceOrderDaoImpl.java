package com.borsam.repository.dao.service.impl;

import com.borsam.repository.dao.service.ServiceOrderDao;
import com.borsam.repository.entity.patient.PatientProfile;
import com.borsam.repository.entity.service.ServiceOrder;
import com.borsam.repository.entity.service.ServicePackage;
import com.hiteam.common.base.pojo.search.Page;
import com.hiteam.common.base.pojo.search.Pageable;
import com.hiteam.common.base.repository.dao.impl.BaseDaoImpl;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.Map;

/**
 * <pre>
 * @Description:
 * @author :Zhang zhongtao
 * @version: Ver 1.0
 * @Date: 2015-08-06 14:59
 * </pre>
 */
@Repository("serviceOrderDaoImpl")
public class ServiceOrderDaoImpl extends BaseDaoImpl<ServiceOrder, Long> implements ServiceOrderDao {
    @Override
    public Page<PatientProfile> query(Long packageId, Pageable pageable) {
        StringBuilder jql =
                new StringBuilder("select DISTINCT t.patient from ServiceOrder t where t.packageId = :packageId group by t.patient");

        Map params = new HashMap() {{
            put("packageId", packageId);
        }};

        return this.findPageByJql(jql.toString(), params, pageable, PatientProfile.class);
    }

    @Override
    public ServiceOrder addLogInSellService(ServicePackage servicePackage, String tradeNo, PatientProfile patient,Long did) {
        ServiceOrder serviceOrder = new ServiceOrder();
        //0 有效
        serviceOrder.setInvalid(0);
        serviceOrder.setOid(servicePackage.getOrg().getId());
        serviceOrder.setPackageId(servicePackage.getId());
        serviceOrder.setPrice(servicePackage.getPrice());
        serviceOrder.setTitle(servicePackage.getTitle());
        //订单编号
        serviceOrder.setTradeNo(tradeNo);
        serviceOrder.setTypes(servicePackage.getTypes());
        serviceOrder.setPatient(patient);
        serviceOrder.setDid(did);

        //记录入库
        this.persist(serviceOrder);

        return serviceOrder;
    }
}
