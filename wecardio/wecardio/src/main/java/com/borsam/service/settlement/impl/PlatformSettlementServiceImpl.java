package com.borsam.service.settlement.impl;

import com.borsam.pojo.wallet.PlatformSummary;
import com.borsam.repository.dao.settlement.PlatformSettlementDao;
import com.borsam.repository.entity.settlement.PlatformSettlement;
import com.borsam.service.settlement.PlatformSettlementService;
import com.hiteam.common.base.service.impl.BaseServiceImpl;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;

/**
 * <pre>
 * @Description:
 * @author :Zhang zhongtao
 * @version: Ver 1.0
 * @Date: 2015-08-12 17:36
 * </pre>
 */
@Service(value = "platformSettlementServiceImpl")
public class PlatformSettlementServiceImpl extends BaseServiceImpl<PlatformSettlement, Long> implements PlatformSettlementService {
    @Resource(name = "platformSettlementDaoImpl")
    private PlatformSettlementDao platformSettlementDao;

    @Resource(name = "platformSettlementDaoImpl")
    public void setBaseDao(PlatformSettlementDao platformSettlementDao) {
        super.setBaseDao(platformSettlementDao);
    }

    @Override
    @Transactional(readOnly = true)
    public PlatformSummary getPlatformSummary() {
        return platformSettlementDao.getPlatformSummary();
    }
}
