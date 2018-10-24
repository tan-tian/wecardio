package com.borsam.repository.dao.doctor.impl;

import com.borsam.repository.dao.doctor.DoctorProfileDao;
import com.borsam.repository.entity.doctor.DoctorProfile;
import com.hiteam.common.base.repository.dao.impl.BaseDaoImpl;
import com.hiteam.common.util.pojo.EnumBean;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Repository;

import javax.persistence.FlushModeType;
import javax.persistence.TypedQuery;
import java.util.Arrays;
import java.util.List;

/**
 * Dao - 医生信息
 * Created by tantian on 2015/6/23.
 */
@Repository("doctorProfileDaoImpl")
public class DoctorProfileDaoImpl extends BaseDaoImpl<DoctorProfile, Long> implements DoctorProfileDao {

    @Override
    public void devolve(Long[] from, Long to) {
        String jpql = "update PatientProfile p set p.doctor.id = :toDoctor where p.doctor.id in:fromDoctor";
        this.entityManager.createQuery(jpql).setFlushMode(FlushModeType.COMMIT)
                .setParameter("fromDoctor", Arrays.asList(from))
                .setParameter("toDoctor", to)
                .executeUpdate();
    }

    @Override
    public List<EnumBean> sel(String name,Long iOrgId,Integer iType) {
        String jpql = "select new com.hiteam.common.util.pojo.EnumBean(org.id, concat(org.firstName,org.secondName)) from DoctorProfile org"
                + " where org.auditState in :auditState";
        if (StringUtils.isNotEmpty(name)) {
            jpql += " and org.firstName like :name";
        }
        if (iOrgId!=null) {
            jpql += " and org.org.id = :iOrgId";
        }
        if (iType!=null) {
            if (iType==2)//1：管理员 +2 主治医生 +3 审核医生 +4 操作医生
            {
                jpql += " and org.roles in :roles";
            }
        }

        TypedQuery<EnumBean> query = this.entityManager.createQuery(jpql, EnumBean.class);
        query.setFlushMode(FlushModeType.COMMIT);
        query.setParameter("auditState", Arrays.asList(2, 3));
        if (StringUtils.isNotEmpty(name)) {
            query.setParameter("name", name);
        }
        if (iOrgId!=null) {
            query.setParameter("iOrgId", iOrgId);
        }
        if (iType!=null) {
            if (iType==2)//1：管理员 +2 主治医生 +3 审核医生 +4 操作医生
            {
                query.setParameter("roles", Arrays.asList(2, 3, 6, 7, 10, 11, 14, 15));
            }
        }
        return query.getResultList();
    }

    @Override
    public List<DoctorProfile> findEditDoctor(Long oid) {
        String jpql = "select doctor from DoctorProfile doctor where doctor.org.id = :oid and doctor.roles in :roles"
                + " and doctor.auditState = 2";
        return this.entityManager.createQuery(jpql, DoctorProfile.class).setFlushMode(FlushModeType.COMMIT)
                .setParameter("oid", oid)
                .setParameter("roles", Arrays.asList(8, 9, 10, 11, 12, 13, 14, 15))
                .getResultList();
    }

    @Override
    public List<DoctorProfile> findAuditDoctor(Long oid) {
        String jpql = "select doctor from DoctorProfile doctor where doctor.org.id = :oid and doctor.roles in :roles"
                + " and doctor.auditState = 2";
        return this.entityManager.createQuery(jpql, DoctorProfile.class).setFlushMode(FlushModeType.COMMIT)
                .setParameter("oid", oid)
                .setParameter("roles", Arrays.asList(4, 5, 6, 7, 8, 13, 14, 15))
                .getResultList();
    }

    @Override
    public List<DoctorProfile> findMainDoctor(Long oid) {
        String jpql = "select doctor from DoctorProfile doctor where doctor.org.id = :oid and doctor.roles in :roles"
                + " and doctor.auditState = 2";
        return this.entityManager.createQuery(jpql, DoctorProfile.class).setFlushMode(FlushModeType.COMMIT)
                .setParameter("oid", oid)
                .setParameter("roles", Arrays.asList(2, 3, 6, 7, 10, 11, 14, 15))
                .getResultList();
    }

    @Override
    public List<DoctorProfile> findAdminDoctor(Long oid) {
        String jpql = "select doctor from DoctorProfile doctor where doctor.org.id = :oid and doctor.roles in :roles";
        return this.entityManager.createQuery(jpql, DoctorProfile.class).setFlushMode(FlushModeType.COMMIT)
                .setParameter("oid", oid)
                .setParameter("roles", Arrays.asList(1, 3, 5, 7, 9, 11, 13, 14))
                .getResultList();
    }
}
