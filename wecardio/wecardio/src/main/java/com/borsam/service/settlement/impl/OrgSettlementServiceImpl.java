package com.borsam.service.settlement.impl;

import com.borsam.pojo.wallet.OrgSummary;
import com.borsam.repository.dao.settlement.OrgSettlementDao;
import com.borsam.repository.entity.settlement.OrgSettlement;
import com.borsam.service.settlement.OrgSettlementService;
import com.hiteam.common.base.service.impl.BaseServiceImpl;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;

/**
 * <pre>
 * @Description:
 * @author :Zhang zhongtao
 * @version: Ver 1.0
 * @Date: 2015-08-12 17:37
 * </pre>
 */
@Service(value = "orgSettlementServiceImpl")
public class OrgSettlementServiceImpl extends BaseServiceImpl<OrgSettlement,Long> implements OrgSettlementService {
    @Resource(name = "orgSettlementDaoImpl")
    private OrgSettlementDao orgSettlementDao;

    @Resource(name = "orgSettlementDaoImpl")
    public void setBaseDao(OrgSettlementDao orgSettlementDao) {
        super.setBaseDao(orgSettlementDao);
    }

    @Override
    @Transactional(readOnly = true)
    public List<OrgSummary> getOrgSummary(String orgName) {
        return orgSettlementDao.getOrgSummary(orgName);
    }
}

