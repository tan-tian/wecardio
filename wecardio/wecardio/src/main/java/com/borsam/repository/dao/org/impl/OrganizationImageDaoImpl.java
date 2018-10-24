package com.borsam.repository.dao.org.impl;

import com.borsam.repository.dao.org.OrganizationImageDao;
import com.borsam.repository.entity.org.OrganizationImage;
import com.hiteam.common.base.repository.dao.impl.BaseDaoImpl;
import org.springframework.stereotype.Repository;

import javax.persistence.FlushModeType;

/**
 * Dao - 机构图片
 * Created by tantian on 2015/8/18.
 */
@Repository("organizationImageDaoImpl")
public class OrganizationImageDaoImpl extends BaseDaoImpl<OrganizationImage, Long> implements OrganizationImageDao {

    @Override
    public void removeImages(Long oid) {
        String jpql = "delete from OrganizationImage o where o.org.id = :oid";
        this.entityManager.createQuery(jpql).setFlushMode(FlushModeType.COMMIT).setParameter("oid", oid).executeUpdate();
    }
}
