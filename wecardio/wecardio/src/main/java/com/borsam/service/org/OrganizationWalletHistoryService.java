package com.borsam.service.org;

import com.borsam.pojo.wallet.Settlement;
import com.borsam.repository.entity.org.OrganizationWalletHistory;
import com.hiteam.common.base.pojo.search.Page;
import com.hiteam.common.base.service.BaseService;

import java.util.Date;
import java.util.List;

/**
 * Service - 机构钱包流水
 * Created by Sebarswee on 2015/8/10.
 */
public interface OrganizationWalletHistoryService extends BaseService<OrganizationWalletHistory, Long> {

    /**
     * 查询未结算列表
     * @param oid 机构标识
     * @param startDate 开始时间
     * @param endDate 结束时间
     * @return 未结算列表
     */
    public List<Settlement> findUnSettlementList(Long oid, Date startDate, Date endDate);

    /**
     * 钱包流水详情
     */
    Page<OrganizationWalletHistory> detailList(Long oid, Integer[] status, int pageNo, int pageSize);
}
