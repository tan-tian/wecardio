package com.borsam.repository.dao.doctor.impl;

import com.borsam.repository.dao.doctor.DoctorTokenDao;
import com.borsam.repository.entity.doctor.DoctorToken;
import com.hiteam.common.base.repository.dao.impl.BaseDaoImpl;
import org.springframework.stereotype.Repository;

/**
 * Dao - 医生Token
 * Created by tantian on 2015/6/27.
 */
@Repository("doctorTokenDaoImpl")
public class DoctorTokenDaoImpl extends BaseDaoImpl<DoctorToken, Long> implements DoctorTokenDao {
}
