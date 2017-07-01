package com.borsam.service.service.impl;

import com.borsam.pojo.service.EditServicePackageData;

import com.borsam.pojo.service.QueryServiceData;
import com.borsam.pojo.service.QueryServicePackageData;
import com.borsam.repository.dao.org.OrganizationDao;
import com.borsam.repository.dao.org.OrganizationWalletDao;
import com.borsam.repository.dao.org.OrganizationWalletHistoryDao;
import com.borsam.repository.dao.patient.PatientProfileDao;
import com.borsam.repository.dao.patient.PatientServiceDao;
import com.borsam.repository.dao.patient.PatientWalletDao;
import com.borsam.repository.dao.patient.PatientWalletHistoryDao;
import com.borsam.repository.dao.service.ServiceDao;
import com.borsam.repository.dao.service.ServiceOrderDao;
import com.borsam.repository.dao.service.ServicePackageDao;
import com.borsam.repository.dao.service.ServiceTypeDao;
import com.borsam.repository.entity.org.Organization;
import com.borsam.repository.entity.patient.*;
import com.borsam.repository.entity.service.ServicePackage;
import com.borsam.service.service.ServicePackageService;
import com.hiteam.common.base.pojo.search.Filter;
import com.hiteam.common.base.pojo.search.Page;
import com.hiteam.common.base.service.impl.BaseServiceImpl;
import com.hiteam.common.util.collections.MapUtil;
import com.hiteam.common.util.json.JsonUtils;
import com.hiteam.common.util.lang.ArrayUtil;
import com.hiteam.common.web.Message;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import javax.annotation.Resource;
import javax.persistence.LockModeType;
import javax.transaction.Transactional;
import java.util.*;

/**
 * Created by Zhang zhongtao on 2015/7/28.
 */
@Service(value = "servicePackageServiceImpl")
public class ServicePackageServiceImpl extends BaseServiceImpl<ServicePackage, Long> implements ServicePackageService {
    @Resource(name = "servicePackageDaoImpl")
    private ServicePackageDao servicePackageDao;

    @Resource(name = "serviceDaoImpl")
    private ServiceDao serviceDao;

    @Resource(name = "organizationDaoImpl")
    private OrganizationDao organizationDao;

    @Resource(name = "patientWalletDaoImpl")
    private PatientWalletDao patientWalletDao;

    @Resource(name = "patientWalletHistoryDaoImpl")
    private PatientWalletHistoryDao patientWalletHistoryDao;

    @Resource(name = "patientServiceDaoImpl")
    private PatientServiceDao patientServiceDao;

    @Resource(name = "patientProfileDaoImpl")
    private PatientProfileDao patientProfileDao;

    @Resource(name = "serviceTypeDaoImpl")
    private ServiceTypeDao serviceTypeDao;

    @Resource(name = "serviceOrderDaoImpl")
    private ServiceOrderDao serviceOrderDao;

    @Resource(name = "organizationWalletDaoImpl")
    private OrganizationWalletDao organizationWalletDao;

    @Resource(name = "organizationWalletHistoryDaoImpl")
    private OrganizationWalletHistoryDao organizationWalletHistoryDao;

    @Resource(name = "servicePackageDaoImpl")
    public void setBaseDao(ServicePackageDao servicePackageDao) {
        super.setBaseDao(servicePackageDao);
    }

    @Override
    public ServicePackage editServicePackages(EditServicePackageData data, Organization organization) {
        ServicePackage servicePackage = null;

        if (data.getId() != null) {
            servicePackage = super.find(data.getId());
        } else {
            servicePackage = new ServicePackage();
        }

        servicePackage.setContent(data.getContent());
        servicePackage.setExpired(data.getExpired());
        servicePackage.setIsDelete(false);
        servicePackage.setOrg(organization);
        servicePackage.setPrice(data.getPrice());
        servicePackage.setTitle(data.getTitle());
        servicePackage.setType(data.getType());

        List<EditServicePackageData.Types> types = data.getTypes();
        //组装[[1,100],[2,200]] [[类型,数目],..] 结构
        StringBuilder sTypes = new StringBuilder("[");

        for (int i = 0; i < types.size(); i++) {
            EditServicePackageData.Types types1 = types.get(i);

            sTypes.append("[").append(types1.getType()).append(",").append(types1.getTimes()).append("]");

            if (i != types.size() - 1) {
                sTypes.append(",");
            }
        }

        sTypes.append("]");

        servicePackage.setTypes(sTypes.toString());

        this.saveOrUpdate(servicePackage);

        return servicePackage;
    }

    @Override
    public Page<ServicePackage> queryServicePackage(QueryServicePackageData data) {
        return servicePackageDao.queryServicePackage(data);
    }

    @Override
    @Transactional
    public void delPackage(Long[] ids) {
        List<ServicePackage> packages = this.findList(ids);
        for (ServicePackage aPackage : packages) {
            aPackage.setIsDelete(true);

            this.update(aPackage);
        }
    }

    @Override
    public Page<Map> queryServiceByPackId(Long packageId) {
        Assert.notNull(packageId);

        QueryServiceData data = new QueryServiceData();
        data.setPageNo(0);
        data.setPageSize(500);

        ServicePackage servicePackage = this.find(packageId);

        Assert.notNull(servicePackage);

        data.setStaffId(servicePackage.getOrg().getId());
        //[[1,3],[10011,3]]
        String types = servicePackage.getTypes();

        //分解出服务项ID
        TypesData typesData = toServiceTypeIds(types);
        Long[] ids = typesData.getTypeIds();

        data.setItemIds(ids);

        Page<Map> page = serviceDao.queryService(data);

        //设置服务对应的次数
        if (page.getTotalPages() > 0) {

            for (Map map : page.getContent()) {

                Long type = MapUtil.getLong(map, "type");

                if (type == null) {
                    continue;
                }

                map.put("serviceCount", MapUtil.getLong(typesData.getTypeCount(), type, 0L));
            }
        }

        return page;
    }

    /**
     * 分解出服务项ID
     *
     * @param types
     * @return
     */
    private TypesData toServiceTypeIds(String types) {
        //分解出服务项ID
        List<List<Integer>> list = JsonUtils.toList(types, List.class);
        Long[] ids = new Long[list.size()];
        Map<Long, Integer> longMap = new HashMap<>();

        for (int i = 0; i < list.size(); i++) {
            Integer val = list.get(i).get(0);
            ids[i] = Long.valueOf(val);
            longMap.put(ids[i], list.get(i).get(1));
        }

        TypesData data = new TypesData();
        data.setTypeCount(longMap);
        data.setTypeIds(ids);

        return data;
    }

    @Override
    @Transactional
    public Message pay(ServicePackage servicePackage, Long patientId,Long did) {
        Assert.notNull(patientId);
        Assert.notNull(servicePackage);

        Calendar now = Calendar.getInstance();

        //region 00-减掉消费金额(PatientWallet)

        PatientWallet wallet = patientWalletDao.find(patientId, LockModeType.PESSIMISTIC_WRITE);
        
        PatientProfile patient=patientProfileDao.find(patientId, LockModeType.PESSIMISTIC_WRITE);

        if (servicePackage.getPrice().compareTo(wallet.getTotal()) > 0) {
            //余额不够
            return Message.warn("admin.service.msg.payTip0");
        }

        wallet.setTotal(wallet.getTotal().subtract(servicePackage.getPrice()));
        //更新入库
        patientWalletDao.merge(wallet);
        //endregion

        //region 01-新增患者消费历史记录(PatientWalletHistory)

        PatientWalletHistory history = patientWalletHistoryDao.addLogInSellService(servicePackage, patientId, now);

        //endregion

        //region 02-新增服务订单记录(ServiceOrder)
        serviceOrderDao.addLogInSellService(servicePackage, history.getTradeNo(), patient,did);
        //endregion

        //region 03-患者服务记录修改及新增(PatientService)
        TypesData data = toServiceTypeIds(servicePackage.getTypes());
        Long[] serviceTypesIds = data.getTypeIds();
        List<PatientService> patientServiceList = findPatientService(servicePackage, patientId, serviceTypesIds);

        Long time = now.getTimeInMillis() / 1000;

        //region A-更新已有数据
        patientServiceList.stream().parallel()
                .filter(ps -> ArrayUtil.contains(serviceTypesIds, ps.getType()))
                .forEach(ps1 -> {
                    //获取当前服务项的次数
                    Integer count = MapUtil.getInteger(data.getTypeCount(), ps1.getType(), 0);
                    //删除原值
                    data.getTypeCount().remove(ps1.getType());

                    ps1.setTotalCount(ps1.getTotalCount() + count);
                    ps1.setCount(ps1.getCount() + count);

                    //已经过期的时间，设置为当前时间
                    if (ps1.getExpiredTime() < time) {
                        ps1.setExpiredTime(time);
                    }

                    Calendar date = Calendar.getInstance();
                    date.setTimeInMillis(ps1.getExpiredTime() * 1000);
                    date.getTime();
                    //增加天数
                    date.add(Calendar.DAY_OF_MONTH, servicePackage.getExpired());
                    ps1.setExpiredTime(date.getTimeInMillis() / 1000);
                });

        patientServiceDao.merge(patientServiceList);
        //endregion

        //region B-新增数据
        List<PatientService> patientServiceList1 = new ArrayList<PatientService>();
        PatientProfile patientProfile = patientProfileDao.find(patientId);

        //根据有效天数，设置过期时间点
        now = Calendar.getInstance();
        now.add(Calendar.DAY_OF_YEAR, servicePackage.getExpired());
        Long exTime = now.getTimeInMillis() / 1000;
        data.getTypeCount().forEach((typeId, count) -> {
            //患者服务
            PatientService patientService = new PatientService();
            //剩余次数
            patientService.setCount(count);
            //总次数
            patientService.setTotalCount(count);

            PatientServiceKey serviceKey = new PatientServiceKey();
            //当前服务包对应的机构
            serviceKey.setOrg(servicePackage.getOrg());
            //服务项
            serviceKey.setServiceType(serviceTypeDao.find(typeId));
            //患者
            serviceKey.setPatient(patientProfile);

            patientService.setKey(serviceKey);
            //到期时间
            patientService.setExpiredTime(exTime);

            patientServiceList1.add(patientService);
        });

        patientServiceDao.persist(patientServiceList1);
        //endregion

        //endregion

        //region 04-销售机构钱包调整及新增钱包流水记录(OrganizationWallet、OrganizationWalletHistory)
        Organization org = servicePackage.getOrg();
        org = organizationDao.find(org.getId());
        //调整机构收入及分成后的收入
        organizationWalletDao.adjustTotalInSellService(org, servicePackage.getPrice());
        //新增机构销售流水记录
        organizationWalletHistoryDao.addLogInSellService(org, servicePackage.getPrice(), patientId, history.getTradeNo(),servicePackage.getTitle());
        //endregion

        return Message.success("");
    }

    /**
     * 获取患者服务
     *
     * @param servicePackage
     * @param patientId
     * @return
     */
    private List<PatientService> findPatientService(ServicePackage servicePackage, Long patientId, Long[] serviceTypeIds) {
        List<Filter> filters = new ArrayList<>();
        filters.add(Filter.eq("key.org", servicePackage.getOrg()));
        filters.add(Filter.in("key.serviceType", serviceTypeIds));
        filters.add(Filter.eq("key.patient", patientId));

        List<PatientService> services = patientServiceDao.findList(null, null, filters, null);
        return services;
    }


    public static class TypesData {
        /**服务项ID数组*/
        private Long[] typeIds;
        /**
         * key:服务项ID,value:服务次数
         */
        private Map<Long, Integer> typeCount = null;

        public Map<Long, Integer> getTypeCount() {
            return typeCount;
        }

        public void setTypeCount(Map<Long, Integer> typeCount) {
            this.typeCount = typeCount;
        }

        public Long[] getTypeIds() {
            return typeIds;
        }

        public void setTypeIds(Long[] typeIds) {
            this.typeIds = typeIds;
        }
    }
}
