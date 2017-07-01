package com.borsam.repository.dao.org.impl;

import com.borsam.pojo.statistics.SettlementOrgSumData;
import com.borsam.pojo.wallet.Settlement;
import com.borsam.repository.dao.org.OrganizationWalletHistoryDao;
import com.borsam.repository.entity.org.Organization;
import com.borsam.repository.entity.org.OrganizationWalletHistory;
import com.hiteam.common.base.pojo.search.Page;
import com.hiteam.common.base.pojo.search.Pageable;
import com.hiteam.common.base.repository.dao.impl.BaseDaoImpl;
import com.hiteam.common.util.bean.BeanUtil;
import com.hiteam.common.util.collections.CollectionUtil;
import org.springframework.stereotype.Repository;

import javax.persistence.FlushModeType;
import javax.persistence.TypedQuery;
import java.math.BigDecimal;
import java.util.*;

/**
 * Dao - 机构钱包流水
 * Created by Sebarswee on 2015/8/10.
 */
@Repository("organizationWalletHistoryDaoImpl")
public class OrganizationWalletHistoryDaoImpl extends BaseDaoImpl<OrganizationWalletHistory, Long> implements OrganizationWalletHistoryDao {

    @Override
    public void addLogInSellService(Organization organization, BigDecimal price, Long uid, String fromTradeNo, String verdict) {
        OrganizationWalletHistory history = new OrganizationWalletHistory();

        Calendar now = Calendar.getInstance();

        history.setOid(organization.getId());
        //销售金额
        history.setMoney(price);
        //患者ID
        history.setFromUid(uid);
        //单据流水号
        history.setFromTradeNo(fromTradeNo);
        //单据类型:0-购买服务单据
        history.setTicketType(0);
        //未结算
        history.setStatus(1);
        history.setVerdict(verdict);
        //0 收入
        history.setType(0);
        //分成比例
        history.setRate(organization.getRate());
        //单据年月
        history.setYear(now.get(Calendar.YEAR));
        history.setMonth(now.get(Calendar.MONTH) + 1);
        history.setCreated(now.getTimeInMillis() / 1000L);

        this.persist(history);
    }

    @Override
    public void addLogInConsultation(Organization organization, BigDecimal price, Long uid, String fromTradeNo, String verdict) {
        OrganizationWalletHistory history = new OrganizationWalletHistory();

        Calendar now = Calendar.getInstance();

        history.setOid(organization.getId());
        // 销售金额
        history.setMoney(price);
        // 患者ID
        history.setFromUid(uid);
        // 单据流水号
        history.setFromTradeNo(fromTradeNo);
        // 单据类型:1-诊单服务
        history.setTicketType(1);
        // 未结算
        history.setStatus(1);
        history.setVerdict(verdict);
        // 0 收入
        history.setType(0);
        // 分成比例
        history.setRate(organization.getRate());
        // 单据年月
        history.setYear(now.get(Calendar.YEAR));
        history.setMonth(now.get(Calendar.MONTH) + 1);
        history.setCreated(now.getTimeInMillis() / 1000L);

        this.persist(history);
    }

    @Override
    public List<Settlement> findUnSettlementList(Long oid, Long startTime, Long endTime) {
        String jpql = "select new com.borsam.pojo.wallet.Settlement(history.year, history.month, sum(history.rate * history.money)) from OrganizationWalletHistory history"
                + " where history.type = 0 and history.status = 1"
                + " and history.oid = :oid";
        if (startTime != null) {
            jpql += " and history.created >= " + startTime;
        }
        if (endTime != null) {
            jpql += " and history.created < " + endTime;
        }
        jpql += " group by history.year, history.month order by history.year, history.month asc";
        return this.entityManager.createQuery(jpql, Settlement.class).setFlushMode(FlushModeType.COMMIT)
                .setParameter("oid", oid).getResultList();
    }

    @Override
    public void updateState(Long oid, Integer year, Integer month, Integer oldState, Integer newState) {
        String jpql = "update OrganizationWalletHistory history set history.status = :newState"
                + " where history.oid = :oid and history.year = :year and history.month = :month and history.status = :oldState";
        this.entityManager.createQuery(jpql).setFlushMode(FlushModeType.COMMIT)
                .setParameter("oid", oid)
                .setParameter("year", year)
                .setParameter("month", month)
                .setParameter("oldState", oldState)
                .setParameter("newState", newState)
                .executeUpdate();
    }

    @Override
    public Settlement findUnSettlement(Long oid, Integer year, Integer month) {
        String jpql = "select new com.borsam.pojo.wallet.Settlement(history.year, history.month, sum(history.rate * history.money)) from OrganizationWalletHistory history"
                + " where history.type = 0 and history.status = 1"
                + " and history.oid = :oid and history.year = :year and history.month = :month"
                + " group by history.year, history.month";
        return this.entityManager.createQuery(jpql, Settlement.class).setFlushMode(FlushModeType.COMMIT)
                .setParameter("oid", oid)
                .setParameter("year", year)
                .setParameter("month", month)
                .getSingleResult();
    }

    @Override
    public List<SettlementOrgSumData> findMoneyTotalByOrg(Calendar now, Integer year, Integer month, List<Long> oids) {
        StringBuilder jql = new StringBuilder("select " +
                "new com.borsam.pojo.statistics.SettlementOrgSumData(" +
                "t.oid,t.year,t.month,t.status," +
                "sum(t.money)," + //销售总额
                "sum(t.money * t.rate)," + //机构应收金额也就是应拿金额
                "(case " +
                "   when t.status >= 1 then sum(t.money * t.rate) " +
                "   else 0 " +
                "end)," + //机构未收金额
                "(case " +
                "   when t.status = 0 then sum(t.money * t.rate) " +
                "   else 0 " + //机构已收金额
                "end)" +
                ") from OrganizationWalletHistory t where 1 = 1 ");

        if (CollectionUtil.isNotEmpty(oids)) {
            jql.append(" and t.oid in(:oids) ");
        }

        jql.append("  and t.year = :year and t.month = :month group by t.oid,t.year,t.month,t.status ");

        TypedQuery<SettlementOrgSumData> query =
                this.entityManager.createQuery(jql.toString(), SettlementOrgSumData.class);
        query.setParameter("year", year).setParameter("month", month);

        if (CollectionUtil.isNotEmpty(oids)) {
            query.setParameter("oids", oids);
        }

        List<SettlementOrgSumData> datas = query.getResultList();

        List<SettlementOrgSumData> resultDatas = new ArrayList<>();

        //根据机构ID、年、月 合并数据
        for (SettlementOrgSumData data : datas) {

            Optional<SettlementOrgSumData> first = resultDatas.parallelStream().filter(resData ->
                    Objects.equals(resData.getOid(), data.getOid())
                            && Objects.equals(resData.getYear(), data.getYear())
                            && Objects.equals(resData.getMonth(), data.getMonth())).findFirst();

            if (first.equals(Optional.empty())) {
                resultDatas.add(data);
            } else {
                SettlementOrgSumData sumData = first.get();

                sumData.setAlreadyReceivedAmountTotal(
                        sumData.getAlreadyReceivedAmountTotal().add(data.getAlreadyReceivedAmountTotal()));

                sumData.setNotReceivedAmountTotal(
                        sumData.getNotReceivedAmountTotal().add(data.getNotReceivedAmountTotal()));

                sumData.setReceiveAmountTotal(sumData.getReceiveAmountTotal().add(data.getReceiveAmountTotal()));

                sumData.setSaleAmountTotal(sumData.getSaleAmountTotal().add(data.getSaleAmountTotal()));

                //TODO
                if (data.getIsExistUnSettle() >= 1) {
                    sumData.setIsExistUnSettle(data.getIsExistUnSettle());
                }
            }

            //同年同月的应该在此次统计中，设置为未统计完成状态
            if (Objects.equals(now.get(Calendar.YEAR), year) && Objects.equals(now.get(Calendar.MONTH) + 1, month)) {
                data.setIsExistUnSettle(1);
            }
        }
        return resultDatas;
    }

    @Override
    @SuppressWarnings("unchecked")
    public Page<OrganizationWalletHistory> detailList(Long oid, Integer[] status, int pageNo, int pageSize) {
        // 普通SQL查询
        StringBuilder sql = new StringBuilder();
        sql.append("select ");
        sql.append("oid, ");
        sql.append("rate, ");
        sql.append("`type`, ");
        sql.append("from_trade_no as fromTradeNo, ");
        sql.append("created, ");
        sql.append("verdict, ");
        sql.append("ticket_type as ticketType, ");
        sql.append("`year`, ");
        sql.append("`month`, ");
        sql.append("`status`, ");
        sql.append("(case `status` when 0 then -money else money end) as money, ");
        sql.append("(");
        sql.append("select sum((case `status` when 0 then -money else money end) * rate)");
        sql.append("from organization_wallet_history where t.created >= created and oid = ").append(oid).append(" and `type` = 0");
        sql.append(") as sumMoney ");
        sql.append("from organization_wallet_history t ");
        sql.append("where t.oid = ").append(oid);
        sql.append(" and t.`type` = 0 ");
        if (status != null && status.length > 0) {
            sql.append("and t.`status` in :status ");
        }
        sql.append("order by t.created desc ");

        Map<String, Object> params = new HashMap<>();
        if (status != null && status.length > 0) {
            params.put("status", Arrays.asList(status));
        }

        Page<Map> result = this.findPageBySql(sql.toString(), params, new Pageable(pageNo, pageSize), Map.class);
        List<OrganizationWalletHistory> histories = new ArrayList<>();

        for (Map<String, Object> map : result.getContent()) {
            OrganizationWalletHistory history = BeanUtil.populate(OrganizationWalletHistory.class, map);
            histories.add(history);
        }

        return new Page<>(histories, result.getTotal(), result.getPageable());
    }
}
