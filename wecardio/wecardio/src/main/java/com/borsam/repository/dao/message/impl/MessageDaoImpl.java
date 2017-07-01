package com.borsam.repository.dao.message.impl;

import com.borsam.pub.UserType;
import com.borsam.repository.dao.message.MessageDao;
import com.borsam.repository.entity.doctor.DoctorProfile;
import com.borsam.repository.entity.message.MessageInfo;
import com.borsam.repository.entity.patient.PatientProfile;
import com.hiteam.common.base.pojo.search.Filter;
import com.hiteam.common.base.pojo.search.Page;
import com.hiteam.common.base.pojo.search.Pageable;
import com.hiteam.common.base.repository.dao.impl.BaseDaoImpl;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.time.DateUtils;
import org.springframework.stereotype.Repository;

import javax.persistence.FlushModeType;
import javax.persistence.TypedQuery;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

/**
 * Dao - 消息管理
 * Created by liujieming on 2015/7/20.
 */
@Repository("messageDaoImpl")
public class MessageDaoImpl extends BaseDaoImpl<MessageInfo, Long> implements MessageDao {

    /**
     * 通过多条件匹配查询
     *
     * @param name
     * @param startDate
     * @param endDate
     * @param type
     * @param org
     * @param pageable
     * @return
     */
    @Override
    public Page<MessageInfo> queryMessage(String name, Date startDate, Date endDate, MessageInfo.MessageType[] type, Long org,Long objId,UserType objType, Pageable pageable) {
        String jpql = "select message from MessageInfo message"
                + " where 1=1 ";

        if (StringUtils.isNotEmpty(name)) {
            jpql += " and (message.to_name like :name or message.from_name like :name)";
        }

        if (startDate != null) {
            jpql += " and message.created >= :startDate ";
        }
        if (endDate != null) {
            jpql += " and message.created <= :endDate ";
        }

        if (type != null&&type.length>0) {
            jpql += " and message.type in :type ";
        }

        if (objType != null) {
            //能看到自己接收或者发送的消息
            if (objType.equals(UserType.admin))
            {
                if(org!=null)
                {
                    jpql += " and ((message.to_id = :orgId and  message.to_type ="+UserType.org.ordinal()+") or (message.from_id = :orgId and  message.from_type ="+UserType.org.ordinal()+"))";
                    jpql += " or ((message.to_id in :docId and  message.to_type ="+UserType.doctor.ordinal()+") or (message.from_id in :docId and  message.from_type ="+UserType.doctor.ordinal()+"))";
                    jpql += " or ((message.to_id in :patientId and  message.to_type ="+UserType.patient.ordinal()+") or (message.from_id in :patientId and  message.from_type ="+UserType.patient.ordinal()+"))";
                }
            }

            if (objType.equals(UserType.org)||objType.equals(UserType.doctor)||objType.equals(UserType.patient))
            {
                jpql += " and ((message.to_id = :objId and  message.to_type =:objType) or (message.from_id = :objId and  message.from_type =:objType))";
            }
        }

        jpql += " order by message.id desc";

        TypedQuery<MessageInfo> query = this.entityManager.createQuery(jpql, MessageInfo.class);
        query.setFlushMode(FlushModeType.COMMIT);

        if (StringUtils.isNotEmpty(name)) {
            query.setParameter("name", "%"+name+"%");
        }

        if (startDate!=null) {
            query.setParameter("startDate", startDate.getTime() / 1000);
        }

        if (endDate!=null) {
            query.setParameter("endDate", DateUtils.addDays(endDate, 1).getTime() / 1000);
        }

        if (type != null&&type.length>0) {
            query.setParameter("type", Arrays.asList(type));
        }

        if (objType != null) {
            //能看到自己接收或者发送的消息
            if (objType.equals(UserType.admin))
            {
                if(org!=null)
                {
                    query.setParameter("orgId", org);
                    String doctorJpql = "select doctor from DoctorProfile doctor"
                            + " where doctor.org.id="+org;
                    TypedQuery<DoctorProfile> doctorQuery = this.entityManager.createQuery(doctorJpql, DoctorProfile.class);
                    List<DoctorProfile> doctorList = doctorQuery.getResultList();
                    Long[] doctorArr = null;
                    if (doctorList != null && doctorList.size()>0) {
                        doctorArr = new Long[doctorList.size()];
                        for (int i = 0; i < doctorList.size(); i++) {
                            doctorArr[i] = doctorList.get(i).getId();
                        }
                    }else{
                        doctorArr = new Long[1];
                        doctorArr[0] = -1l;
                    }

                    String patientJpql = "select patient from PatientProfile patient"
                            + " where patient.org.id="+org;
                    TypedQuery<PatientProfile> patientQuery = this.entityManager.createQuery(patientJpql, PatientProfile.class);
                    List<PatientProfile> patientList = patientQuery.getResultList();

                    Long[] patientArr = null;
                    if (patientList!=null&&patientList.size()>0){
                        patientArr = new Long[patientList.size()];
                        for (int i = 0; i < patientList.size(); i++) {
                            patientArr[i] = patientList.get(i).getId();
                        }
                    }
                    else{
                        patientArr = new Long[1];
                        patientArr[0] = -1l;
                    }

                    query.setParameter("docId", Arrays.asList(doctorArr));
                    query.setParameter("patientId", Arrays.asList(patientArr));
                }
            }

            if (objType.equals(UserType.org)||objType.equals(UserType.doctor)||objType.equals(UserType.patient))
            {
                query.setParameter("objId", objId);
                query.setParameter("objType", objType);
            }
        }

        Integer total = query.getResultList().size();
        query.setFirstResult((pageable.getPageNo() - 1) * pageable.getPageSize());
        query.setMaxResults(pageable.getPageSize());
        return new Page<MessageInfo>(query.getResultList(), total, pageable);
    }
}
