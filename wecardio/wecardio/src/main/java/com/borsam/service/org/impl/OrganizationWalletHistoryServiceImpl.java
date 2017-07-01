package com.borsam.service.org.impl;

import com.borsam.pojo.wallet.Settlement;
import com.borsam.repository.dao.org.OrganizationWalletHistoryDao;
import com.borsam.repository.entity.org.OrganizationWalletHistory;
import com.borsam.service.org.OrganizationWalletHistoryService;
import com.hiteam.common.base.pojo.search.Page;
import com.hiteam.common.base.service.impl.BaseServiceImpl;
import org.apache.commons.lang.time.DateUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * Service - 机构钱包流水
 * Created by Sebarswee on 2015/8/10.
 */
@Service("organizationWalletHistoryServiceImpl")
public class OrganizationWalletHistoryServiceImpl extends BaseServiceImpl<OrganizationWalletHistory, Long> implements OrganizationWalletHistoryService {

    @Resource(name = "organizationWalletHistoryDaoImpl")
    private OrganizationWalletHistoryDao organizationWalletHistoryDao;

    @Resource(name = "organizationWalletHistoryDaoImpl")
    public void setBaseDao(OrganizationWalletHistoryDao organizationWalletHistoryDao) {
        super.setBaseDao(organizationWalletHistoryDao);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Settlement> findUnSettlementList(Long oid, Date startDate, Date endDate) {
        Long startTime = null;
        Long endTime = null;
        if (startDate != null) {
            startTime = startDate.getTime() / 1000;
        }
        if (endDate != null) {
            endTime = DateUtils.addMonths(endDate, 1).getTime() / 1000;
        } else {
            Calendar now = Calendar.getInstance();
            now.set(Calendar.DAY_OF_MONTH, 1);
            now.set(Calendar.HOUR_OF_DAY, 0);
            now.set(Calendar.MINUTE, 0);
            now.set(Calendar.SECOND, 0);
            endTime = now.getTime().getTime() / 1000;
        }
        return organizationWalletHistoryDao.findUnSettlementList(oid, startTime, endTime);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<OrganizationWalletHistory> detailList(Long oid, Integer[] status, int pageNo, int pageSize) {
        return organizationWalletHistoryDao.detailList(oid, status, pageNo, pageSize);
    }
}
