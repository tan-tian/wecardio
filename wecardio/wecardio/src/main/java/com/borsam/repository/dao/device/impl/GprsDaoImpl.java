package com.borsam.repository.dao.device.impl;

import javax.persistence.FlushModeType;

import org.springframework.stereotype.Repository;

import com.borsam.repository.dao.consultation.ConsultationDao;
import com.borsam.repository.dao.device.GprsDao;
import com.borsam.repository.entity.consultation.Consultation;
import com.borsam.repository.entity.device.Gprs;
import com.borsam.repository.entity.patient.PatientProfile;
import com.hiteam.common.base.repository.dao.impl.BaseDaoImpl;

@Repository("GprsDaoImpl")
public class GprsDaoImpl extends BaseDaoImpl<Gprs, Long> implements GprsDao {

	@Override
	public boolean isBind(PatientProfile patientProfile) {
		 if (patientProfile == null) {
	            return false;
	        }
	        String jpql = "select count(gprs) from Gprs gprs where lower(gprs.patient)= lower(:patient)";
	        Long count = entityManager.createQuery(jpql, Long.class).setFlushMode(FlushModeType.COMMIT).setParameter("patient", patientProfile).getSingleResult();
	        return count > 0;
	}
	
	public boolean isImeExist(String ime){
		if(ime==null){
			return false;
		}
		String jpql="select count(gprs) from Gprs gprs where lower(gprs.ime)=lower(:ime)";
		Long count=entityManager.createQuery(jpql,Long.class).setFlushMode(FlushModeType.COMMIT).setParameter("ime", ime).getSingleResult();
		return count>0;
	}
}