package com.borsam.repository.dao.settlement;

import com.borsam.pojo.statistics.SettlementOrgSumData;
import com.borsam.pojo.wallet.OrgSummary;
import com.borsam.repository.entity.settlement.OrgSettlement;
import com.hiteam.common.base.repository.dao.BaseDao;

import java.math.BigDecimal;
import java.util.Calendar;
import java.util.List;

/**
 * <pre>
 * @Description:
 * @author :Zhang zhongtao
 * @version: Ver 1.0
 * @Date: 2015-08-12 17:32
 * </pre>
 */
public interface OrgSettlementDao extends BaseDao<OrgSettlement,Long> {
    /**
     * 获取今年所有的数据，最多12条
     * @param oid 机构ID
     * @return List
     */
    public List<OrgSettlement> findCurrentYearData(Long oid, Calendar calendar);

    /***
     * 统计当前机构总销售额
     * @param oid 机构ID
     * @return BigDecimal
     */
    public BigDecimal findGrossSalesTotal(Long oid);

    /**
     * 统计某年某月的销售金额
     * @param year 年
     * @param month 月
     * @param isSynch 同步状态
     * @return  SettlementOrgSumData
     */
    public SettlementOrgSumData findMoneyTotalByPlat(Integer year, Integer month, Boolean isSynch);

    /**
     * 获取机构汇总信息
     * @param orgName 机构汇总信息
     * @return 机构汇总信息
     */
    public List<OrgSummary> getOrgSummary(String orgName);

    /**
     * 获取已经存在的统计数据
     * @param datas 参数
     * @return List
     */
    public List<OrgSettlement> getExistsData(List<SettlementOrgSumData> datas);

    /**
     * 获取所有未同步完成的数据
     * @param year
     * @return List
     */
    public List<OrgSettlement> getUnSyncData(Integer year);

    /**
     * 获取指定月份的机构汇总信息
     * @param now 当前时间，用于判断当前时间是否和指定的年月相同，相同则表示不是统计上一个月的，而是实时统计的
     * @param year 年
     * @param month 月
     * @return List
     */
    public SettlementOrgSumData getSumByTime(Calendar now, Integer year, Integer month);

}
