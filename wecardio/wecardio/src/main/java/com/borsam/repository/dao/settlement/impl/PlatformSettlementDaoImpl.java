package com.borsam.repository.dao.settlement.impl;

import com.borsam.pojo.statistics.SettlementOrgSumData;
import com.borsam.pojo.wallet.PlatformSummary;
import com.borsam.repository.dao.settlement.PlatformSettlementDao;
import com.borsam.repository.entity.settlement.PlatformSettlement;
import com.hiteam.common.base.pojo.search.Filter;
import com.hiteam.common.base.pojo.search.Page;
import com.hiteam.common.base.pojo.search.Pageable;
import com.hiteam.common.base.repository.dao.impl.BaseDaoImpl;
import com.hiteam.common.util.collections.CollectionUtil;
import org.springframework.stereotype.Repository;

import javax.persistence.FlushModeType;
import javax.persistence.LockModeType;
import java.math.BigDecimal;
import java.util.*;

/**
 * <pre>
 * @Description:
 * @author :Zhang zhongtao
 * @version: Ver 1.0
 * @Date: 2015-08-12 17:35
 * </pre>
 */
@Repository(value = "platformSettlementDaoImpl")
public class PlatformSettlementDaoImpl extends BaseDaoImpl<PlatformSettlement, Long> implements PlatformSettlementDao {

    @Override
    public List<PlatformSettlement> findCurrentYearData(Calendar calendar) {
        String jql =
                "Select t from PlatformSettlement t where t.year = :year order by t.year,t.month asc";

        Map params = new HashMap<>();
        params.put("year", calendar.get(Calendar.YEAR));

        List<PlatformSettlement> list = this.findListByJql(jql, params, PlatformSettlement.class);
        List<PlatformSettlement> result = new ArrayList<>();
        //从12个月中找出未统计的月份数据，并用空对象填充
        for (int i = 1; i <= 12; i++) {
            final int j = i;
            Optional<PlatformSettlement> first =
                    list.stream().filter(orgSettlement -> orgSettlement.getMonth().equals(j)).findFirst();

            if (first.equals(Optional.empty())) {
                PlatformSettlement platformSettlement = new PlatformSettlement();
                platformSettlement.setMonth(j);
                platformSettlement.setYear(calendar.get(Calendar.YEAR));
                result.add(platformSettlement);
            } else {
                result.add(first.get());
            }
        }

        return result;
    }

    @Override
    public BigDecimal findGrossSalesTotal() {
        String jql = "Select sum(t.saleAmount) from PlatformSettlement t";
        List<BigDecimal> bigDecimals =
                this.entityManager.createQuery(jql, BigDecimal.class).getResultList();

        BigDecimal result = CollectionUtil.getFirst(bigDecimals, new BigDecimal(0));

        return result;
    }

    @Override
    public Page<PlatformSettlement> findPageByUnSynch(Integer pageNo) {
        List<Filter> filters = new ArrayList<>();
        filters.add(Filter.eq("isSynch", false));
        Pageable pageable = new Pageable();
        pageable.setFilters(filters);
        pageable.setPageNo(pageNo);
        pageable.setPageSize(100);

        return this.findPage(pageable);
    }

    @Override
    public PlatformSummary getPlatformSummary() {
        String jpql = "select new com.borsam.pojo.wallet.PlatformSummary(sum(p.saleAmount), sum(p.payAmount), sum(p.alreadyPayAmount), sum(p.notPayAmount), sum(p.rateAmount)) "
                + " from PlatformSettlement p";
        return this.entityManager.createQuery(jpql, PlatformSummary.class).setFlushMode(FlushModeType.COMMIT).getSingleResult();
    }

    @Override
    public List<PlatformSettlement> getExistsData(List<SettlementOrgSumData> datas) {
        if (CollectionUtil.isEmpty(datas)) {
            return null;
        }

        String jql = "select t from PlatformSettlement t where t.oid in(:oid) and t.year in(:year) and t.month in(:month)";
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

        List<PlatformSettlement> settlements = this.findListByJql(jql, params, PlatformSettlement.class);

        return settlements;
    }

    @Override
    public PlatformSettlement getByYearAndMonth(Integer year, Integer month) {
        String jql = "select t from PlatformSettlement t where t.year = :year and t.month =:month";

        List<PlatformSettlement> settlements =
                this.entityManager.createQuery(jql, PlatformSettlement.class)
                        .setFlushMode(FlushModeType.COMMIT)
                        .setLockMode(LockModeType.PESSIMISTIC_WRITE)
                        .setParameter("year", year)
                        .setParameter("month", month)
                        .getResultList();
        return CollectionUtil.getFirst(settlements);
    }

    @Override
    public List<PlatformSettlement> getUnSyncData() {
        String jql = "select t from PlatformSettlement t where t.synch = :synch";

        List<PlatformSettlement> settlements =
                this.entityManager.createQuery(jql, PlatformSettlement.class)
                        .setFlushMode(FlushModeType.COMMIT)
                        .setLockMode(LockModeType.PESSIMISTIC_WRITE)
                        .setParameter("synch", false) //未同步完成的数据
                        .getResultList();
        return settlements;
    }
}
