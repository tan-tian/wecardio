package com.borsam.repository.dao.org.impl;

import com.borsam.repository.dao.org.OrganizationDao;
import com.borsam.repository.entity.org.Organization;
import com.hiteam.common.base.repository.dao.impl.BaseDaoImpl;
import com.hiteam.common.util.pojo.EnumBean;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Repository;

import javax.persistence.FlushModeType;
import javax.persistence.TypedQuery;
import java.util.Arrays;
import java.util.List;

/**
 * Dao - 机构信息
 * Created by tantian on 2015/7/1.
 */
@Repository("organizationDaoImpl")
public class OrganizationDaoImpl extends BaseDaoImpl<Organization, Long> implements OrganizationDao {

    @Override
    public List<EnumBean> sel(String name) {
        String jpql = "select new com.hiteam.common.util.pojo.EnumBean(org.id, org.name) from Organization org"
                + " where org.auditState in :auditState";
        if (StringUtils.isNotEmpty(name)) {
            jpql += " and org.name like :name";
        }
        TypedQuery<EnumBean> query = this.entityManager.createQuery(jpql, EnumBean.class);
        query.setFlushMode(FlushModeType.COMMIT);
        query.setParameter("auditState", Arrays.asList(2, 3));
        if (StringUtils.isNotEmpty(name)) {
            query.setParameter("name", name);
        }
        return query.getResultList();
    }
    
    @Override
    public List<EnumBean> selType(String tblName, String colName,String notIn) {
        String jpql = "select new com.hiteam.common.util.pojo.EnumBean(enum.value, enum.text) from Enum enum"
            + " where enum.tblName = :tblName and enum.colName = :colName";
        if(notIn!=null&&!notIn.equals(""))
        {
            jpql+="enum.value not in ("+notIn+")";
        }

        return this.entityManager.createQuery(jpql, EnumBean.class).setFlushMode(FlushModeType.COMMIT).setParameter("tblName", tblName).setParameter("colName", colName).getResultList();
    }
}
