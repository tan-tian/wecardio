package com.borsam.service.statistics;

import com.borsam.pojo.security.Principal;
import com.borsam.pojo.statistics.CountData;
import com.borsam.pojo.statistics.YearCountData;

import java.util.Calendar;
import java.util.List;

/**
 * <pre>
 * @Description: 统计
 * @author :Zhang zhongtao
 * @version: Ver 1.0
 * @Date: 2015-08-11 16:14
 * </pre>
 */
public interface StatisticService {
    /**
     * 获取统计数据
     * 平台管理员统计：机构数量、医生数量、患者数量、诊单数量
     * 机构管理员统计：医生数量、患者数量、诊单数量、服务包数量
     *
     * @param principal 当前登录身份
     * @param key       缓存KEY
     * @return
     */
    public CountData getCount(Principal principal, Long key);

    /**
     * 首页年度统计
     *
     * @param principal 当前登录身份
     * @param key       缓存Key
     * @return 平台管理员：PaltformYearCountData，机构管理员：OrgYearCountData
     */
    public YearCountData getYearCount(Principal principal, Long key);

    /***
     * 根据organization_wallet_history 表汇聚t_platform_settlement、orgainization_settlement数据
     */
    public void sumByWallet();

    /**
     * 根据organization_wallet_history 表汇聚t_platform_settlement、orgainization_settlement数据
     *
     * @param now   当前时间
     * @param year  年
     * @param month 月
     * @param oid   需要统计的机构数据，空：则统计所有
     */
    public void sumAllByWallet(Calendar now, Integer year, Integer month, List<Long> oid);
}
