package com.borsam.service.settlement;

import com.borsam.pojo.wallet.OrgSummary;
import com.borsam.repository.entity.settlement.OrgSettlement;
import com.hiteam.common.base.service.BaseService;

import java.util.List;

/**
 * <pre>
 * @Description:
 * @author :tantian
 * @version: Ver 1.0
 * @Date: 2015-08-12 17:37
 * </pre>
 */
public interface OrgSettlementService extends BaseService<OrgSettlement,Long> {

    /**
     * 获取机构汇总信息
     * @param orgName 机构汇总信息
     * @return 机构汇总信息
     */
    public List<OrgSummary> getOrgSummary(String orgName);
}
