package com.borsam.repository.dao.patient.impl;

import com.borsam.repository.dao.patient.PatientProfileDao;
import com.borsam.repository.entity.org.Organization;
import com.borsam.repository.entity.patient.PatientProfile;
import com.hiteam.common.base.pojo.search.Filter;
import com.hiteam.common.base.pojo.search.Pageable;
import com.hiteam.common.base.repository.dao.impl.BaseDaoImpl;
import com.hiteam.common.util.pojo.EnumBean;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Repository;

import javax.persistence.FlushModeType;
import javax.persistence.TypedQuery;
import java.util.Arrays;
import java.util.List;

/**
 * Dao - 患者信息
 * Created by tantian on 2015/7/20.
 */
@Repository("patientProfileDaoImpl")
public class PatientProfileDaoImpl extends BaseDaoImpl<PatientProfile, Long> implements PatientProfileDao {

    @Override
    public boolean mobileExists(String mobile) {
        if (mobile == null) {
            return false;
        }
        String jpql = "select count(account) from PatientProfile account where lower(account.mobile) = lower(:mobile)";
        Long count = entityManager.createQuery(jpql, Long.class).setFlushMode(FlushModeType.COMMIT).setParameter("mobile", mobile).getSingleResult();
        return count > 0;
    }

    @Override
    public boolean emailExists(String email) {
        if (email == null) {
            return false;
        }
        String jpql = "select count(account) from PatientProfile account where lower(account.email) = lower(:email)";
        Long count = entityManager.createQuery(jpql, Long.class).setFlushMode(FlushModeType.COMMIT).setParameter("email", email).getSingleResult();
        return count > 0;
    }

    @Override
    public List<EnumBean> sel(Long iOrgId,Long iDoctorId,Integer iType,String name) {
        String jpql = "select new com.hiteam.common.util.pojo.EnumBean(org.id, org.fullName) from PatientProfile org"
                + " where org.bindType in :bindType";
        if (StringUtils.isNotEmpty(name)) {
            jpql += " and org.fullName like :name";
        }
        if (iOrgId!=null) {
            jpql += " and org.org.id = :iOrgId";
        }
        if (iDoctorId!=null) {
            jpql += " and org.doctor.id = :iDoctorId";
        }
        TypedQuery<EnumBean> query = this.entityManager.createQuery(jpql, EnumBean.class);
        query.setFlushMode(FlushModeType.COMMIT);
        if(iType==1)//散户
        {
            query.setParameter("bindType", Arrays.asList(PatientProfile.BindType.self,PatientProfile.BindType.other,PatientProfile.BindType.unknow));
        }else if(iType==2)//VIP
        {
            query.setParameter("bindType", Arrays.asList(PatientProfile.BindType.org));
        }

        if (StringUtils.isNotEmpty(name)) {
            query.setParameter("name", name);
        }
        if (iOrgId!=null) {
            query.setParameter("iOrgId", iOrgId);
        }
        if (iDoctorId!=null) {
            query.setParameter("iDoctorId", iDoctorId);
        }
        return query.getResultList();
    }

    @Override
    public List<PatientProfile> queryPatient(String name, String email, String mobile, Integer[] isWalletActive, Integer[] doctor, Long[] org, String iType, Pageable pageable) {
        String jpql = "select patient from PatientProfile patient"
                + " where patient.isDelete = 0 ";

        if (StringUtils.isNotEmpty(email)) {
            jpql += " and (patient.patientAccount.email = :email or patient.patientAccount.mobile = :email)";
        }
        if (StringUtils.isNotEmpty(iType)) {
            if(iType.equals("3"))//VIP患者--邀请页面查询 --不是机构创建并且可以查询已被邀请
            {
                jpql += " and (patient.bindType <> :bindTypeOrg)";
                jpql += " and (patient.bindType <> :bindTypeSelf)";
            }
        }

        jpql += " order by patient.id desc";

        TypedQuery<PatientProfile> query = this.entityManager.createQuery(jpql, PatientProfile.class);
        query.setFlushMode(FlushModeType.COMMIT);

        if (StringUtils.isNotEmpty(email)) {
            query.setParameter("email", email);
        }

        if (StringUtils.isNotEmpty(iType)) {
            if (iType.equals("3"))//VIP患者--邀请页面查询 --不是机构创建并且可以查询已被邀请
            {
                query.setParameter("bindTypeOrg", PatientProfile.BindType.org);
                query.setParameter("bindTypeSelf", PatientProfile.BindType.self);
            }
        }

        return query.getResultList();
    }
}
