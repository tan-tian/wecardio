package com.borsam.repository.dao.service.impl;

import com.borsam.pojo.service.QueryServicePackageData;
import com.borsam.repository.dao.service.ServicePackageDao;
import com.borsam.repository.entity.service.ServicePackage;
import com.hiteam.common.base.pojo.search.Page;
import com.hiteam.common.base.repository.dao.impl.BaseDaoImpl;
import com.hiteam.common.util.lang.DateUtil;
import com.hiteam.common.util.lang.StringUtil;
import org.springframework.stereotype.Repository;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

/**
 * Dao - 服务包
 * Created by tantian on 2015/7/20.
 */
@Repository("servicePackageDaoImpl")
public class ServicePackageDaoImpl extends BaseDaoImpl<ServicePackage, Long> implements ServicePackageDao {
    @Override
    public Page<ServicePackage> queryServicePackage(QueryServicePackageData data) {
        StringBuilder jql = new StringBuilder("select sp from ServicePackage sp join fetch sp.org o where 1=1 ");
        Map params = new HashMap();

        if (StringUtil.isNotBlank(data.getTitle())) {
            jql.append(" and sp.title like :title ");
            params.put("title", "%" + data.getTitle() + "%");
        }

        if (data.getOrgId() != null && data.getOrgId() != 0L) {
            jql.append(" and o.id = :orgId ");
            params.put("orgId", data.getOrgId());
        }

        if (data.getStartTime() != null) {
            jql.append(" and sp.created >= :startTime ");
            params.put("startTime", data.getStartTime().getTime() / 1000);
        }

        if (data.getEndTime() != null) {
            jql.append(" and sp.created <= :endTime ");
            Calendar calendar = DateUtil.toCalendar(data.getEndTime());
            calendar.set(Calendar.HOUR_OF_DAY, 23);
            calendar.set(Calendar.MINUTE, 59);
            calendar.set(Calendar.SECOND, 59);
            params.put("endTime", calendar.getTimeInMillis() / 1000);
        }

        if (data.getDel() != null) {
            jql.append(" and sp.isDelete = :isDelete ");
            params.put("isDelete", data.getDel());
        }

        if (data.getTypeId() != null) {
            jql.append(" and sp.types like :types ");
            params.put("types", "[" + data.getTypeId() + ",%");
        }

        //屏蔽有效计算方法，服务包只有有效天数，而无具体时间点
//        if (CollectionUtil.isNotEmpty(data.getStatus()) && data.getStatus().size() == 1) {
//            Boolean status = CollectionUtil.getFirst(data.getStatus());
//            Calendar calendar = Calendar.getInstance();
//
//            calendar.set(Calendar.HOUR_OF_DAY, 23);
//            calendar.set(Calendar.MINUTE, 59);
//            calendar.set(Calendar.SECOND, 59);
//
//            if (status) {
//                jql.append(" and sp.expired >= :time ");
//            } else {
//                jql.append(" and sp.expired < :time ");
//            }
//
//            params.put("time", calendar.getTimeInMillis() / 1000);
//        }

        Page<ServicePackage> page = findPageByJql(jql.toString(), params, data, ServicePackage.class);

        return page;
    }
}
