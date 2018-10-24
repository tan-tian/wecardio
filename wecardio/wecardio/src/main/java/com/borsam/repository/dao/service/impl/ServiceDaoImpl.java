package com.borsam.repository.dao.service.impl;

import com.borsam.pojo.service.QueryServiceData;
import com.borsam.repository.dao.service.ServiceDao;
import com.borsam.repository.entity.service.Service;
import com.borsam.repository.entity.service.ServiceKey;
import com.hiteam.common.base.pojo.search.Page;
import com.hiteam.common.base.repository.dao.impl.BaseDaoImpl;
import com.hiteam.common.util.lang.ArrayUtil;
import com.hiteam.common.util.lang.StringUtil;
import org.springframework.stereotype.Repository;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

/**
 * Dao - 服务项
 * Created by tantian on 2015/7/20.
 */
@Repository("serviceDaoImpl")
public class ServiceDaoImpl extends BaseDaoImpl<Service, ServiceKey> implements ServiceDao {
    @Override
    public Page<Map> queryService(QueryServiceData data) {

        StringBuilder jql = new StringBuilder("" +
                "SELECT s.i_is_enabled as enabled,s.price as price,s.created as created,'' as orgName," +
                " s.all_day as isAllDay,s.oid as oid,s.type as type,0 as serviceCount," +
                "s.s_remark as remark,s.s_code as code," +
                " st.type_name as name FROM service s " +
                " LEFT JOIN service_type st on st.type = s.type ");

        jql.append(" Where 1=1 ");

        Map params = new HashMap<>();

        if (data.getStaffId() != null) {
            jql.append(" And s.oid = :oid ");
            params.put("oid", data.getStaffId());
        }

        if (StringUtil.isNotBlank(data.getCode())) {
            jql.append(" And s.s_code like :code ");
            params.put("code" , "%" + data.getCode() + "%");
        }

        if (ArrayUtil.isNotEmpty(data.getIsEnableds())) {
            jql.append(" And s.i_is_enabled in (:enableds) ");
            params.put("enableds" , Arrays.asList(data.getIsEnableds()));
        }

        if(StringUtil.isNotBlank(data.getName())){
            jql.append(" AND st.type_name like :typeName ");
            params.put("typeName","%" + data.getName() + "%");
        }

        if(data.getItemId() != null){
            jql.append(" AND st.type = :type ");
            params.put("type",data.getItemId());
        }

        if (ArrayUtil.isNotEmpty(data.getItemIds())) {
            jql.append(" AND st.type IN (:types) ");
            params.put("types" , Arrays.asList(data.getItemIds()));
        }

        Page<Map> mapPage = this.findPageBySql(jql.toString(), params, data, Map.class);
        return mapPage;
    }
}
