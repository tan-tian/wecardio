package com.borsam.repository.dao.org;

import com.borsam.pojo.statistics.SettlementOrgSumData;
import com.borsam.pojo.wallet.Settlement;
import com.borsam.repository.entity.org.Organization;
import com.borsam.repository.entity.org.OrganizationWalletHistory;
import com.hiteam.common.base.pojo.search.Page;
import com.hiteam.common.base.repository.dao.BaseDao;

import java.math.BigDecimal;
import java.util.Calendar;
import java.util.List;

/**
 * Dao - 机构钱包流水
 * Created by Sebarswee on 2015/8/10.
 */
public interface OrganizationWalletHistoryDao extends BaseDao<OrganizationWalletHistory, Long> {

    /**
     * 购买服务包时，新增流水记录
     *
     * @param organization 销售机构
     * @param price        销售金额
     * @param uid          患者ID
     * @param fromTradeNo  单据编号
     */
    public void addLogInSellService(Organization organization, BigDecimal price, Long uid, String fromTradeNo, String verdict);

    /**
     * 诊单服务审核通过时，新增流水记录
     * @param organization 销售机构
     * @param price 金额
     * @param uid 患者ID
     * @param fromTradeNo 单据编号
     */
    public void addLogInConsultation(Organization organization, BigDecimal price, Long uid, String fromTradeNo, String verdict);

    /**
     * 查询未结算列表
     *
     * @param oid       机构标识
     * @param startTime 开始时间点
     * @param endTime   结束时间点
     * @return 未结算列表
     */
    public List<Settlement> findUnSettlementList(Long oid, Long startTime, Long endTime);

    /**
     * 更新流水记录的状态
     *
     * @param oid      机构标识
     * @param year     年份
     * @param month    月份
     * @param oldState 原状态
     * @param newState 新状态
     */
    public void updateState(Long oid, Integer year, Integer month, Integer oldState, Integer newState);

    /**
     * 根据年月查询未结算记录
     *
     * @param oid   机构标识
     * @param year  年份
     * @param month 月份
     * @return 未结算记录
     */
    public Settlement findUnSettlement(Long oid, Integer year, Integer month);

    /**
     * 统计机构某年某月的销售金额
     *
     * @param year  年
     * @param month 月
     * @param oids  机构ID集合
     * @return SettlementOrgSumData
     */
    public List<SettlementOrgSumData> findMoneyTotalByOrg(Calendar now ,Integer year, Integer month, List<Long> oids);

    /**
     * 钱包流水详情
     */
    Page<OrganizationWalletHistory> detailList(Long oid, Integer[] status, int pageNo, int pageSize);
}
