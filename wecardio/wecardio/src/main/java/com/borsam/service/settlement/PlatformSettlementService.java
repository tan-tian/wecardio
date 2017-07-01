package com.borsam.service.settlement;

import com.borsam.pojo.wallet.PlatformSummary;
import com.borsam.repository.entity.settlement.PlatformSettlement;
import com.hiteam.common.base.service.BaseService;

/**
 * <pre>
 * @Description:
 * @author :Zhang zhongtao
 * @version: Ver 1.0
 * @Date: 2015-08-12 17:36
 * </pre>
 */
public interface PlatformSettlementService extends BaseService<PlatformSettlement,Long> {

    /**
     * 获取平台汇总信息
     * @return 平台汇总信息
     */
    public PlatformSummary getPlatformSummary();
}
