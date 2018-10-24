package com.borsam.repository.dao.org.impl;

import com.borsam.repository.dao.org.OrganizationDoctorImageDao;
import com.borsam.repository.entity.org.OrganizationDoctorImage;
import com.hiteam.common.base.repository.dao.impl.BaseDaoImpl;
import org.springframework.stereotype.Repository;

import javax.persistence.FlushModeType;

/**
 * Dao - 机构医生证书
 * Created by tantian on 2015/8/18.
 */
@Repository("organizationDoctorImageDaoImpl")
public class OrganizationDoctorImageDaoImpl extends BaseDaoImpl<OrganizationDoctorImage, Long> implements OrganizationDoctorImageDao {

    @Override
    public void removeImages(Long oid) {
        String jpql = "delete from OrganizationDoctorImage o where o.org.id = :oid";
        this.entityManager.createQuery(jpql).setFlushMode(FlushModeType.COMMIT).setParameter("oid", oid).executeUpdate();
    }
}
