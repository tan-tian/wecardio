package com.borsam.service.doctor.impl;

import com.borsam.repository.dao.doctor.DoctorTokenDao;
import com.borsam.repository.entity.doctor.DoctorToken;
import com.borsam.service.doctor.DoctorTokenService;
import com.hiteam.common.base.service.impl.BaseServiceImpl;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * Service - 医生Token
 * Created by tantian on 2015/6/27.
 */
@Service("doctorTokenServiceImpl")
public class DoctorTokenServiceImpl extends BaseServiceImpl<DoctorToken, Long> implements DoctorTokenService {

    @Resource(name = "doctorTokenDaoImpl")
    public void setBaseDao(DoctorTokenDao doctorTokenDao) {
        super.setBaseDao(doctorTokenDao);
    }
}
