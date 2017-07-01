package com.borsam.service.service.impl;

import com.borsam.pojo.service.QueryServiceData;
import com.borsam.repository.dao.org.OrganizationDao;
import com.borsam.repository.dao.service.ServiceDao;
import com.borsam.repository.entity.org.Organization;
import com.borsam.repository.entity.service.Service;
import com.borsam.repository.entity.service.ServiceKey;
import com.borsam.service.service.ServiceService;
import com.hiteam.common.base.pojo.search.Filter;
import com.hiteam.common.base.pojo.search.Page;
import com.hiteam.common.base.service.impl.BaseServiceImpl;
import com.hiteam.common.util.collections.CollectionUtil;
import com.hiteam.common.util.collections.MapUtil;
import org.springframework.util.Assert;

import javax.annotation.Resource;
import javax.transaction.Transactional;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * Created by Zhang zhongtao on 2015/7/25.
 */
@org.springframework.stereotype.Service("serviceServiceImpl")
public class ServiceServiceImpl extends BaseServiceImpl<Service, ServiceKey> implements ServiceService {
    @Resource(name = "serviceDaoImpl")
    private ServiceDao serviceDao;

    @Resource(name = "organizationDaoImpl")
    private OrganizationDao organizationDao;

    @Resource(name = "serviceDaoImpl")
    public void setBaseDao(ServiceDao serviceDao) {
        super.setBaseDao(serviceDao);
    }

    @Override
    public List<Service> editServices(List<Service> services, Long oid) {

        List<ServiceKey> serviceKeys = new ArrayList<ServiceKey>();

        services.stream().parallel().forEach(service1 -> {
            //设置默认值
            if (service1.getIsAllDay() == null) {
                service1.setIsAllDay(false);
            }

            if (service1.getOid() == null) {
                service1.setOid(oid);
            }

            if (service1.getPrice() == null) {
                service1.setPrice(new BigDecimal(0));
            }

            //组装主键
            serviceKeys.add(new ServiceKey(service1.getOid(), service1.getType()));
        });

        //根据主键数组获取数据库记录
        List<Service> dbServices = this.findList(serviceKeys.toArray(new ServiceKey[serviceKeys.size()]));

        List<Service> listModify = new ArrayList<Service>();
        List<Service> listadd = new ArrayList<Service>();

        //根据新增或修改分别存储到不同的list中
        services.stream().parallel().forEach(service -> {
            Optional<Service> first = dbServices.stream().parallel()
                    .filter(service1 ->
                                    service.getOid().equals(service1.getOid()) && service.getType().equals(service1.getType())
                    ).findFirst();

            if (first.equals(Optional.empty())) {
                listadd.add(service);
            } else {
                Service serviceTemp = first.get();
                serviceTemp.setIsAllDay(service.getIsAllDay());
                serviceTemp.setPrice(service.getPrice());
                serviceTemp.setRemark(service.getRemark());
                listModify.add(serviceTemp);
            }
        });

        //当前机构下所有的服务数量
        Long count = this.count(Filter.eq("oid",oid));

        //处理数据是新增还是修改
        if (CollectionUtil.isNotEmpty(listadd)) {
            for (Service service : listadd) {
                this.save(service);
            }

            count += listadd.size();
        }

        modifyOrgServiceCount(oid, count.intValue());

        return services;
    }

    /**
     * 修改机构中的服务统计数
     * @param oid 机构ID
     * @param count 服务条数
     */
    public void modifyOrgServiceCount(Long oid,Integer count) {

        Organization organization = organizationDao.find(oid);

        if (organization != null) {
            organization.setServiceNum(count);
            organizationDao.saveOrUpdate(organization);
        }
    }

    @Override
    public Page<Map> queryService(QueryServiceData data) {
        Page<Map> mapPage = serviceDao.queryService(data);

        //获取机构名称
        if (mapPage.getTotal() > 0) {
            long[] orgIds =
                    mapPage.getContent().stream().parallel()
                            .mapToLong(value -> MapUtil.getLongValue(value, "oid")).toArray();
            List<Filter> filters = new ArrayList<>();
            filters.add(Filter.in("", orgIds));

            List<Organization> organizations = organizationDao.findList(null, null, filters, null);

            if (CollectionUtil.isNotEmpty(organizations)) {
                mapPage.getContent().stream().parallel().forEach(map -> {
                    if (MapUtil.getInteger(map, "isAllDay", 0) == 0) {
                        map.put("isAllDay", false);
                    } else {
                        map.put("isAllDay", true);
                    }
                    organizations.stream().parallel()
                            .filter(organization -> organization.getId().equals(MapUtil.getLong(map, "oid")))
                            .forEach(organization1 -> map.put("orgName", organization1.getName()));
                });
            }
        }

        return mapPage;
    }


    @Override
    @Transactional
    public void setEnableVal(String[] ids, Boolean isEnable) {
        Assert.noNullElements(ids);

        //分解机构ID及服务项ID
        List<Long> oids = new ArrayList<>();
        List<Long> types = new ArrayList<>();

        for (String id : ids) {
            String[] oid_type = id.split("-");

            Assert.noNullElements(oid_type);
            Assert.notNull(oid_type[0]);
            Assert.notNull(oid_type[1]);

            //机构ID
            oids.add(Long.valueOf(oid_type[0]));
            //服务项ID
            types.add(Long.valueOf(oid_type[1]));
        }

        List<Filter> filters = new ArrayList<Filter>();
        filters.add(Filter.in("oid", oids.toArray()));
        filters.add(Filter.in("type", types.toArray()));

        //查询出需要禁用的服务项
        List<Service> services = this.findList(null, null, filters, null);

        Long oid = null;

        for (Service service : services) {
            if (service.getOid() != null) {
                oid = service.getOid();
            }
            service.setIsEnabled(isEnable);
            this.update(service);
        }
    }
}
