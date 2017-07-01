package com.borsam.repository.dao.device.impl;

import javax.persistence.FlushModeType;

import org.springframework.stereotype.Repository;

import com.borsam.repository.dao.consultation.ConsultationDao;
import com.borsam.repository.dao.device.GprsDao;
import com.borsam.repository.dao.device.GprsHistoryDao;
import com.borsam.repository.entity.consultation.Consultation;
import com.borsam.repository.entity.device.Gprs;
import com.borsam.repository.entity.device.GprsHistory;
import com.borsam.repository.entity.patient.PatientProfile;
import com.hiteam.common.base.repository.dao.impl.BaseDaoImpl;

@Repository("GprsHistoryDaoImpl")
public class GprsHistoryDaoImpl extends BaseDaoImpl<GprsHistory, Long> implements GprsHistoryDao {
	
}