package com.borsam.service.doctor.impl;

import com.borsam.pojo.security.Principal;
import com.borsam.repository.dao.doctor.DoctorAccountDao;
import com.borsam.repository.entity.doctor.DoctorAccount;
import com.borsam.service.doctor.DoctorAccountService;
import com.hiteam.common.base.service.impl.BaseServiceImpl;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;

/**
 * Service - 医生账号
 * Created by Sebarswee on 2015/6/18.
 */
@Service("doctorAccountServiceImpl")
public class DoctorAccountServiceImpl extends BaseServiceImpl<DoctorAccount, Long> implements DoctorAccountService {

    @Resource(name = "doctorAccountDaoImpl")
    private DoctorAccountDao doctorAccountDao;

    @Resource(name = "doctorAccountDaoImpl")
    public void setBaseDao(DoctorAccountDao doctorAccountDao) {
        super.setBaseDao(doctorAccountDao);
    }

    @Transactional(readOnly = true)
    @Override
    public DoctorAccount findByUsername(String username) {
        return doctorAccountDao.findByUsername(username);
    }

    @Transactional(readOnly = true)
    @Override
    public boolean emailExists(String email) {
        return doctorAccountDao.emailExists(email);
    }

    @Transactional(readOnly = true)
    @Override
    public DoctorAccount getCurrent() {
        Subject subject = SecurityUtils.getSubject();
        if (subject != null) {
            Principal principal = (Principal) subject.getPrincipal();
            if (principal != null) {
                return doctorAccountDao.find(principal.getId());
            }
        }
        return null;
    }
}
