package com.borsam.repository.dao.settlement.impl;

import com.borsam.pojo.statistics.SettlementOrgSumData;
import com.borsam.pojo.statistics.YearCountData;
import com.borsam.pojo.wallet.OrgSummary;
import com.borsam.repository.dao.settlement.OrgSettlementDao;
import com.borsam.repository.entity.settlement.OrgSettlement;
import com.borsam.repository.entity.settlement.PlatformSettlement;
import com.hiteam.common.base.pojo.search.Filter;
import com.hiteam.common.base.repository.dao.impl.BaseDaoImpl;
import com.hiteam.common.util.collections.CollectionUtil;
import com.hiteam.common.util.lang.NumberUtil;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Repository;

import javax.persistence.FlushModeType;
import javax.persistence.TypedQuery;
import java.math.BigDecimal;
import java.util.*;

/**
 * <pre>
 * @Description:
 * @author :tantian
 * @version: Ver 1.0
 * @Date: 2015-08-12 17:32
 * </pre>
 */
@Repository("orgSettlementDaoImpl")
public class OrgSettlementDaoImpl extends BaseDaoImpl<OrgSettlement, Long> implements OrgSettlementDao {
    @Override
    public List<OrgSettlement> findCurrentYearData(Long oid, Calendar calendar) {
        String jql =
                "Select t from OrgSettlement t where t.oid = :oid and t.year = :year";

        Map params = new HashMap<>();
        params.put("oid", oid);
        params.put("year", calendar.get(Calendar.YEAR));

        List<OrgSettlement> list = this.findListByJql(jql, params, OrgSettlement.class);
        List<OrgSettlement> result = new ArrayList<>();
        //从12个月中找出未统计的月份数据，并用空对象填充
        for (int i = 1; i <= 12; i++) {
            final int j = i;
            Optional<OrgSettlement> first =
                    list.stream().filter(orgSettlement -> orgSettlement.getMonth().equals(j)).findFirst();

            if (first.equals(Optional.empty())) {
                OrgSettlement orgSettlement = new OrgSettlement();
                orgSettlement.setOid(oid);
                orgSettlement.setMonth(j);
                orgSettlement.setYear(calendar.get(Calendar.YEAR));
                result.add(orgSettlement);
            } else {
                result.add(first.get());
            }

        }

        return result;
    }

    @Override
    public BigDecimal findGrossSalesTotal(Long oid) {
        String jql = "Select sum(t.saleAmount) from OrgSettlement t where t.oid = :oid ";

        List<BigDecimal> bigDecimals =
                this.entityManager.createQuery(jql, BigDecimal.class).setParameter("oid", oid).getResultList();

        BigDecimal result = CollectionUtil.getFirst(bigDecimals,new BigDecimal(0));

        return result;
    }

    @Override
    public SettlementOrgSumData findMoneyTotalByPlat(Integer year, Integer month, Boolean isSynch) {
        Map params = new HashMap<>();

        StringBuilder jql = new StringBuilder("" +
                "Select " +
                "   new com.borsam.pojo.statistics.SettlementOrgSumData(t.year,t.month,sum(t.money) as moneyTotal," +
                "       sum(t.rate * t.money) as moneyRateTotal) " +
                " from OrgSettlement t  " +
                "   Where t.year = :year and t.month = :month and t.synch = :isSynch " +
                "   group by t.year, t.month");

        params.put("year", year);
        params.put("month", month);
        params.put("isSynch", isSynch);

        List<SettlementOrgSumData> datas = this.findListByJql(jql.toString(), params, SettlementOrgSumData.class);

        return CollectionUtil.getFirst(datas);
    }

    @Override
    public List<OrgSummary> getOrgSummary(String orgName) {
        String jpql = "select new com.borsam.pojo.wallet.OrgSummary(o.name, sum(p.saleAmount), sum(p.alreadyReceivedAmount), sum(p.notReceivedAmount)) "
                + " from OrgSettlement p, Organization o where p.oid = o.id";
        if (StringUtils.isNotEmpty(orgName)) {
            jpql += " and o.name like :orgName";
        }
        jpql += " group by p.oid";
        TypedQuery<OrgSummary> query = this.entityManager.createQuery(jpql, OrgSummary.class);
        query.setFlushMode(FlushModeType.COMMIT);
        if (StringUtils.isNotEmpty(orgName)) {
            query.setParameter("orgName", "%" + orgName + "%");
        }
        return query.getResultList();
    }



    @Override
    public List<OrgSettlement> getExistsData(List<SettlementOrgSumData> datas) {
        if (CollectionUtil.isEmpty(datas)) {
            return null;
        }

        String jql = "select t from OrgSettlement t where t.oid in(:oid) and t.year in(:year) and t.month in(:month)";
        Map params = new HashMap<>();

        List<Long> oids = new ArrayList<>();
        List<Integer> months = new ArrayList<>();
        List<Integer> years = new ArrayList<>();

        for (SettlementOrgSumData data : datas) {
            oids.add(data.getOid());
            months.add(data.getMonth());
            years.add(data.getYear());
        }

        params.put("oid", oids);
        params.put("year", years);
        params.put("month", months);

        List<OrgSettlement> settlements = this.findListByJql(jql, params, OrgSettlement.class);

        return settlements;
    }

    @Override
    public List<OrgSettlement> getUnSyncData(Integer year) {
        String jql = "select t from OrgSettlement t where t.year >= :year and t.synch = :isSynch";
        Map params = new HashMap<>();
        params.put("year", year);
        params.put("isSynch", false);

        List<OrgSettlement> settlements = this.findListByJql(jql, params, OrgSettlement.class);

        return settlements;
    }

    @Override
    public SettlementOrgSumData getSumByTime(Calendar now, Integer year, Integer month) {
        String jql = "" +
                "select new com.borsam.pojo.statistics.SettlementOrgSumData(" +
                "t.year,t.month,sum(t.saleAmount)," +
                "sum(t.receiveAmount),sum(t.notReceivedAmount),sum(t.alreadyReceivedAmount)" +
                ") " +
        "   from OrgSettlement t " +
                "   where t.year = :year and t.month = :month group by t.year,t.month";

        Map params = new HashMap<>();
        params.put("year", year);
        params.put("month", month);

        List<SettlementOrgSumData> settlements = this.findListByJql(jql, params, SettlementOrgSumData.class);
        SettlementOrgSumData sumData = CollectionUtil.getFirst(settlements, null);

        if (sumData == null) {
            return null;
        }

        if (Objects.equals(now.get(Calendar.YEAR), year) && Objects.equals(now.get(Calendar.MONTH) + 1, month)) {
            //相同年月，则为当前月份统计，则置为IsExistUnSettle 未结算
            settlements.parallelStream().forEach(data -> {
                data.setIsExistUnSettle(1);
            });
        } else {
            Long count = this.count(Filter.eq("year", year), Filter.eq("month", month),Filter.eq("synch",false));
            //记录此次统计是否都已经同步完成
            sumData.setIsExistUnSettle(count == null ? 0 : count.intValue());
        }

        return sumData;
    }
}
