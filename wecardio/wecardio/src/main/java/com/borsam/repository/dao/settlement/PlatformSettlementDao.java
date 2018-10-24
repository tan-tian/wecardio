package com.borsam.repository.dao.settlement;

import com.borsam.pojo.statistics.SettlementOrgSumData;
import com.borsam.pojo.wallet.PlatformSummary;
import com.borsam.repository.entity.settlement.PlatformSettlement;
import com.hiteam.common.base.pojo.search.Page;
import com.hiteam.common.base.repository.dao.BaseDao;

import java.math.BigDecimal;
import java.util.Calendar;
import java.util.List;

/**
 * <pre>
 * @Description:
 * @author :tantian
 * @version: Ver 1.0
 * @Date: 2015-08-12 17:34
 * </pre>
 */
public interface PlatformSettlementDao extends BaseDao<PlatformSettlement,Long> {
    /**
     * 获取今年所有的数据，最多12条
     * @return
     */
    public List<PlatformSettlement> findCurrentYearData(Calendar calendar);

    /**
     * 查询所有的销售额
     * @return BigDecimal
     */
    public BigDecimal findGrossSalesTotal();

    /**
     * 分页查询未同步的数据
     * @param pageNo 页码
     * @return Page
     */
    public Page<PlatformSettlement> findPageByUnSynch(Integer pageNo);

    /**
     * 获取平台汇总信息
     * @return 平台汇总信息
     */
    public PlatformSummary getPlatformSummary();

    /**
     * 获取已经存在的数据
     * @param datas
     */
    public List<PlatformSettlement> getExistsData(List<SettlementOrgSumData> datas);

    /**
     * 获取指定年月的统计数据（只会有一条记录）
     * @param year 年
     * @param month 月
     * @return PlatformSettlement
     */
    public PlatformSettlement getByYearAndMonth(Integer year, Integer month);

    /**
     * 获取所有未同步完成的数据
     * @return List
     */
    public List<PlatformSettlement> getUnSyncData();
}
