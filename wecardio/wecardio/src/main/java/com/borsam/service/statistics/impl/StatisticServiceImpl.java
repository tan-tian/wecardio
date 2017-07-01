package com.borsam.service.statistics.impl;

import com.borsam.pojo.security.Principal;
import com.borsam.pojo.statistics.*;
import com.borsam.repository.dao.consultation.ConsultationDao;
import com.borsam.repository.dao.doctor.DoctorProfileDao;
import com.borsam.repository.dao.org.OrganizationDao;
import com.borsam.repository.dao.org.OrganizationWalletHistoryDao;
import com.borsam.repository.dao.patient.PatientProfileDao;
import com.borsam.repository.dao.service.ServicePackageDao;
import com.borsam.repository.dao.settlement.OrgSettlementDao;
import com.borsam.repository.dao.settlement.PlatformSettlementDao;
import com.borsam.repository.entity.doctor.DoctorProfile;
import com.borsam.repository.entity.org.Organization;
import com.borsam.repository.entity.org.OrganizationWalletHistory;
import com.borsam.repository.entity.patient.PatientProfile;
import com.borsam.repository.entity.settlement.OrgSettlement;
import com.borsam.repository.entity.settlement.PlatformSettlement;
import com.borsam.service.statistics.StatisticService;
import com.hiteam.common.base.pojo.search.Filter;
import com.hiteam.common.base.pojo.search.Page;
import com.hiteam.common.base.pojo.search.Pageable;
import com.hiteam.common.base.repository.entity.BaseEntity;
import com.hiteam.common.util.collections.CollectionUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.*;
import java.util.concurrent.TimeUnit;

/**
 * <pre>
 * @Description:
 * @author :Zhang zhongtao
 * @version: Ver 1.0
 * @Date: 2015-08-11 16:15
 * </pre>
 */
@CacheConfig(cacheNames = {"homeCountCache"})
@Service(value = "statisticServiceImpl")
public class StatisticServiceImpl implements StatisticService {
    private Logger logger = LoggerFactory.getLogger(StatisticServiceImpl.class);

    @Resource(name = "servicePackageDaoImpl")
    private ServicePackageDao servicePackageDao;

    @Resource(name = "doctorProfileDaoImpl")
    private DoctorProfileDao doctorProfileDao;

    @Resource(name = "organizationDaoImpl")
    private OrganizationDao organizationDao;

    @Resource(name = "organizationWalletHistoryDaoImpl")
    private OrganizationWalletHistoryDao organizationWalletHistoryDao;

    @Resource(name = "patientProfileDaoImpl")
    private PatientProfileDao patientProfileDao;

    @Resource(name = "consultationDaoImpl")
    private ConsultationDao consultationDao;

    @Resource(name = "platformSettlementDaoImpl")
    private PlatformSettlementDao platformSettlementDao;

    @Resource(name = "orgSettlementDaoImpl")
    private OrgSettlementDao orgSettlementDao;

    /**
     * 获取统计数据
     * 平台管理员统计：机构数量、医生数量、患者数量、诊单数量
     * 机构管理员统计：医生数量、患者数量、诊单数量、服务包数量
     *
     * @param principal 当前登录身份
     * @return CountData
     */
    @Override
    @Cacheable(key = "'count_' + #principal.userType.name + '_' + #key")
    public CountData getCount(Principal principal, Long key) {
        Assert.notNull(principal);

        CountData data = new CountData();
        Long oid = null;

        switch (principal.getUserType()) {
            case admin:
                //机构数量
                data.setOrgCount(organizationDao.count(Filter.in("auditState", new Integer[]{2, 3})));
                oid = -1L;
                break;
            case org:
                //服务包数量
                DoctorProfile doctorProfile = doctorProfileDao.find(principal.getId());

                if (doctorProfile.getOrg() != null) {
                    oid = doctorProfile.getOrg().getId();
                    data.setServicePackageCount(servicePackageCouont(doctorProfile.getOrg()));
                } else {
                    data.setServicePackageCount(0L);
                }

                break;
        }

        //医生数量
        data.setDoctorCount(doctorCount(oid));
        //患者数量
        data.setPatientCouont(patientCount(oid));
        //诊单数量
        data.setConsultationCount(consultationCount(oid));

        return data;
    }

    @Override
    @Transactional
    @Cacheable(key = "'yearCount_' + #principal.userType.name + '_' + #key")
    public YearCountData getYearCount(Principal principal, Long key) {

        Calendar now = Calendar.getInstance();

        switch (principal.getUserType()) {
            case admin:

                sumAllByWallet(now, now.get(Calendar.YEAR), now.get(Calendar.MONTH) + 1, null);

                PaltformYearCountData data = new PaltformYearCountData();

                //总销售额
                data.setGrossSalesTotal(platformSettlementDao.findGrossSalesTotal());

                //今年12个月销售数据
                List<PlatformSettlement> datas = platformSettlementDao.findCurrentYearData(now);

                //今年总销售额
                BigDecimal salesTotal =
                        datas.stream().map(PlatformSettlement::getSaleAmount).reduce((b1, b2) -> b1.add(b2)).get();
                data.setSalesTotal(salesTotal);

                //今年应付金额
                BigDecimal payAmountTotal =
                        datas.stream().map(PlatformSettlement::getPayAmount).reduce((b1, b2) -> b1.add(b2)).get();
                data.setPayAmount(payAmountTotal);

                //今年未付金额
                BigDecimal notPayAmountTotal =
                        datas.stream().map(PlatformSettlement::getNotPayAmount).reduce((b1, b2) -> b1.add(b2)).get();
                data.setNotPayAmount(notPayAmountTotal);

                //今年所得金额
                BigDecimal rateAmountTotal =
                        datas.stream().map(PlatformSettlement::getRateAmount).reduce((b1, b2) -> b1.add(b2)).get();
                data.setRateAmount(rateAmountTotal);

                //今年已付金额 alreadyPayAmount
                BigDecimal alreadyPayAmountTotal =
                        datas.stream().map(PlatformSettlement::getAlreadyPayAmount).reduce((b1, b2) -> b1.add(b2)).get();
                data.setAlreadyPayAmount(alreadyPayAmountTotal);

                data.setDatas(datas);

                return data;
            case org:
                Long oid = -1L;

                DoctorProfile doctorProfile = doctorProfileDao.find(principal.getId());
                if (doctorProfile.getOrg() != null) {
                    oid = doctorProfile.getOrg().getId();
                }

                sumAllByWallet(now, now.get(Calendar.YEAR), now.get(Calendar.MONTH) + 1, Arrays.asList(oid));

                OrgYearCountData orgData = new OrgYearCountData();
                //总销售额
                orgData.setGrossSalesTotal(orgSettlementDao.findGrossSalesTotal(oid));

                //今年12个月销售数据
                List<OrgSettlement> orgDatas = orgSettlementDao.findCurrentYearData(oid, now);

                //今年总销售额
                BigDecimal orgSalesTotal =
                        orgDatas.stream().map(OrgSettlement::getSaleAmount).reduce((b1, b2) -> b1.add(b2)).get();
                orgData.setSalesTotal(orgSalesTotal);

                //今年应收金额
                BigDecimal orgPayAmountTotal =
                        orgDatas.stream().map(OrgSettlement::getReceiveAmount).reduce((b1, b2) -> b1.add(b2)).get();
                orgData.setReceiveAmount(orgPayAmountTotal);

                //今年未收金额
                BigDecimal orgNotReceivedAmount =
                        orgDatas.stream().map(OrgSettlement::getNotReceivedAmount).reduce((b1, b2) -> b1.add(b2)).get();
                orgData.setNotReceivedAmount(orgNotReceivedAmount);

                //今年已收金额 alreadyPayAmount
                BigDecimal alreadyReceivedTotal =
                        orgDatas.stream().map(OrgSettlement::getAlreadyReceivedAmount).reduce((b1, b2) -> b1.add(b2)).get();
                orgData.setAlreadyReceivedAmount(alreadyReceivedTotal);

                orgData.setDatas(orgDatas);

                return orgData;
        }

        return null;
    }

    @Transactional
    @Async
    @Override
    public void sumByWallet() {
        Calendar time = Calendar.getInstance();
        //当前时间，过程中不变
        Calendar now = Calendar.getInstance();
        //上一个月
        time.set(Calendar.MONTH, time.get(Calendar.MONTH) - 1);

        Integer year = time.get(Calendar.YEAR);
        Integer month = time.get(Calendar.MONTH) + 1;

        sumForOrg(now, year, month, null);
        sumForPlat(now, year, month);
    }

    @Override
    @Transactional
    public void sumAllByWallet(Calendar now, Integer year, Integer month, List<Long> oid) {
        sumForOrg(now, year, month, oid);
        sumForPlat(now, year, month);
    }

    /***
     * 统计平台数据
     *
     * @param year  年
     * @param month 月
     * @param oid   机构ID，空则为统计所有机构
     */
    private void sumForOrg(Calendar now, Integer year, Integer month, List<Long> oid) {
        List<SettlementOrgSumData> datas = null;
        //找到所有未同步完成的数据，再进行统计一次(处理2年以内的数据)
        List<OrgSettlement> orgSettlements = orgSettlementDao.getUnSyncData(year - 2);

        if (CollectionUtil.isNotEmpty(orgSettlements)) {
            for (OrgSettlement orgSettlement : orgSettlements) {
                datas =
                        organizationWalletHistoryDao
                                .findMoneyTotalByOrg(now, orgSettlement.getYear(), orgSettlement.getMonth(),
                                        new ArrayList<Long>() {{
                                            add(orgSettlement.getOid());
                                        }});
                modifyForOrg(datas);
            }
        }

        datas = organizationWalletHistoryDao.findMoneyTotalByOrg(now, year, month, oid);

        //方法里会删除datas里存在的元素，保证datas里都是新增
        modifyForOrg(datas);
        //入库
        newForOrg(datas);
    }

    /**
     * 平台数据统计
     *
     * @param year
     * @param month
     */
    private void sumForPlat(Calendar now, Integer year, Integer month) {
        //处理所有未同步完成的数据
        List<PlatformSettlement> settlements = platformSettlementDao.getUnSyncData();
        if (CollectionUtil.isNotEmpty(settlements)) {
            for (PlatformSettlement settlement : settlements) {
                SettlementOrgSumData sumData =
                        orgSettlementDao.getSumByTime(now, settlement.getYear(), settlement.getMonth());
                modifyOrNewForPlat(sumData);
            }
        }

        //处理当前时间的统计数据
        SettlementOrgSumData sumData = orgSettlementDao.getSumByTime(now, year, month);

        //更新或入库
        modifyOrNewForPlat(sumData);
    }

    /**
     * 根据统计的数据修改或新增一条记录到平台统计表中
     *
     * @param sumData 统计数据
     */
    private void modifyOrNewForPlat(SettlementOrgSumData sumData) {
        if (sumData == null) {
            return;
        }

        PlatformSettlement settlement = platformSettlementDao.getByYearAndMonth(sumData.getYear(), sumData.getMonth());

        if (settlement == null) {
            settlement = new PlatformSettlement();
        }

        settlement.setYear(sumData.getYear());
        settlement.setMonth(sumData.getMonth());
        //平台总销售额
        settlement.setSaleAmount(sumData.getSaleAmountTotal());
        //平台应付金额
        settlement.setPayAmount(sumData.getReceiveAmountTotal());
        //平台未付金额
        settlement.setNotPayAmount(sumData.getNotReceivedAmountTotal());
        //平台已付金额
        settlement.setAlreadyPayAmount(sumData.getAlreadyReceivedAmountTotal());
        //平台应得金额 = 机构总销售金额-机构应收金额
        settlement.setRateAmount(sumData.getSaleAmountTotal().subtract(sumData.getReceiveAmountTotal()));
        //标记下次是否需要再次统计
        settlement.setSynch(sumData.getIsExistUnSettle() == 0);

        if (settlement.getId() == null) {
            platformSettlementDao.persist(settlement);
        } else {
            platformSettlementDao.merge(settlement);
        }
    }

    private void newForOrg(List<SettlementOrgSumData> datas) {
        List<OrgSettlement> list = new ArrayList<>();

        for (SettlementOrgSumData data : datas) {
            OrgSettlement settlement = new OrgSettlement();
            settlement.setOid(data.getOid());
            settlement.setYear(data.getYear());
            settlement.setMonth(data.getMonth());
            settlement.setSaleAmount(data.getSaleAmountTotal());
            settlement.setReceiveAmount(data.getReceiveAmountTotal());
            settlement.setNotReceivedAmount(data.getNotReceivedAmountTotal());
            settlement.setAlreadyReceivedAmount(data.getAlreadyReceivedAmountTotal());
            //TODO [zzt] 0:全部完成，也就是下次不需要再次统计
            settlement.setSynch(data.getIsExistUnSettle() == 0);
            list.add(settlement);
        }

        orgSettlementDao.persist(list);
    }

    /**
     * 检查数据库中是否存在指定的记录，如果存在，则更新统计值，并同步到数据库
     * 注意：针对数据库已经存在的值会删除datas中的原始
     *
     * @param datas
     */
    private void modifyForOrg(List<SettlementOrgSumData> datas) {
        List<OrgSettlement> dbData = orgSettlementDao.getExistsData(datas);
        List<OrgSettlement> modifyData = new ArrayList<>();

        if (CollectionUtil.isNotEmpty(dbData) && CollectionUtil.isNotEmpty(datas)) {
            for (OrgSettlement orgSettlement : dbData) {
                Optional<SettlementOrgSumData> first = datas.stream().parallel().filter(sumData ->
                                Objects.equals(sumData.getOid(), orgSettlement.getOid())
                                        && Objects.equals(sumData.getYear(), orgSettlement.getYear())
                                        && Objects.equals(sumData.getMonth(), orgSettlement.getMonth())
                ).findFirst();

                if (first.equals(Optional.empty())) {
                    continue;
                }

                SettlementOrgSumData sumData = first.get();

                orgSettlement.setAlreadyReceivedAmount(sumData.getAlreadyReceivedAmountTotal());

                orgSettlement.setNotReceivedAmount(sumData.getNotReceivedAmountTotal());

                orgSettlement.setReceiveAmount(sumData.getReceiveAmountTotal());

                orgSettlement.setSaleAmount(sumData.getSaleAmountTotal());
                //TODO [zzt] 0:全部完成，也就是下次不需要再次统计
                orgSettlement.setSynch(sumData.getIsExistUnSettle()!=null && sumData.getIsExistUnSettle() == 0);

                modifyData.add(orgSettlement);
                datas.remove(sumData);
            }
        }

        if (CollectionUtil.isNotEmpty(modifyData)) {
            orgSettlementDao.merge(modifyData);
        }

    }

    /**
     * 服务包数量
     *
     * @param organization 机构
     * @return Long
     */
    private Long servicePackageCouont(Organization organization) {
        Long count = 0L;

        if (organization == null) {
            return count;
        }

        //查询当前机构下的未删除的服务包数量
        count = servicePackageDao.count(Filter.eq("org", organization), Filter.eq("isDelete", false));

        return count;
    }

    /**
     * 患者数量
     *
     * @param id 机构ID
     * @return Long
     */
    private Long patientCount(Long id) {
        Long count = 0L;
        if (id == null) {
            return count;
        }else if (id == -1L) { //查询所有患者数量
            count = patientProfileDao.count(Filter.eq("isDelete", false), Filter.in("bindType", new Integer[]{0, 1}));
        } else if (id > 0L) { //查询属于某个机构的所有患者
            count = patientProfileDao.count(Filter.eq("org", id), Filter.eq("isDelete", false), Filter.in("bindType", new Integer[]{0, 1}));
        }

        return count;
    }

    /**
     * 医生数量
     *
     * @param id 机构ID
     * @return Long
     */
    private Long doctorCount(Long id) {
        Long count = 0L;
        if (id == null) {
            return count;
        }else if (id == -1L) { //查询所有医生数量
            count = doctorProfileDao.count(Filter.ne("roles", 1), Filter.eq("isDelete", false), Filter.eq("auditState",2));
        } else if (id > 0L) { //查询属于某个机构的所有医生
            count = doctorProfileDao.count(
                    Filter.eq("org", id), Filter.ne("roles", 1), Filter.eq("isDelete", false), Filter.eq("auditState",2));
        }

        return count;
    }

    /**
     * 查询诊单数量
     *
     * @param id 机构ID
     * @return
     */
    private Long consultationCount(Long id) {
        Long count = 0L;
        if (id == null) {
            return count;
        }else if (id == -1L) { //查询所有诊单数量
            count = consultationDao.count();
        } else if (id > 0L) { //查询属于某个机构的所有诊单
            count = consultationDao.count(Filter.eq("org", id));
        }

        return count;
    }
}
