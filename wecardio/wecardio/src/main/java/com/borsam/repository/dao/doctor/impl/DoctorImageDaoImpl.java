package com.borsam.repository.dao.doctor.impl;

import com.borsam.repository.dao.doctor.DoctorImageDao;
import com.borsam.repository.entity.doctor.DoctorImage;
import com.hiteam.common.base.repository.dao.impl.BaseDaoImpl;
import org.springframework.stereotype.Repository;

import javax.persistence.FlushModeType;

/**
 * Dao - 医生图片
 * Created by tantian on 2015/8/18.
 */
@Repository("doctorImageDaoImpl")
public class DoctorImageDaoImpl extends BaseDaoImpl<DoctorImage, Long> implements DoctorImageDao {

    @Override
    public void removeImages(Long did) {
        String jpql = "delete from DoctorImage o where o.doctor.id = :did";
        this.entityManager.createQuery(jpql).setFlushMode(FlushModeType.COMMIT).setParameter("did", did).executeUpdate();
    }
}
