package com.borsam.service.patient.impl;

import com.borsam.pojo.security.Principal;
import com.borsam.repository.dao.patient.PatientAccountDao;
import com.borsam.repository.entity.patient.PatientAccount;
import com.borsam.service.patient.PatientAccountService;
import com.hiteam.common.base.service.impl.BaseServiceImpl;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;

/**
 * Service - 患者账号
 * Created by tantian on 2015/6/18.
 */
@Service("patientAccountServiceImpl")
public class PatientAccountServiceImpl extends BaseServiceImpl<PatientAccount, Long> implements PatientAccountService {

    @Resource(name = "patientAccountDaoImpl")
    private PatientAccountDao patientAccountDao;

    @Resource(name = "patientAccountDaoImpl")
    public void setBaseDao(PatientAccountDao patientAccountDao) {
        super.setBaseDao(patientAccountDao);
    }

    @Transactional(readOnly = true)
    @Override
    public PatientAccount findByUsername(String username) {
        return patientAccountDao.findByUsername(username);
    }

    @Transactional(readOnly = true)
    @Override
    public PatientAccount getCurrent() {
        Subject subject = SecurityUtils.getSubject();
        if (subject != null) {
            Principal principal = (Principal) subject.getPrincipal();
            if (principal != null) {
                return patientAccountDao.find(principal.getId());
            }
        }
        return null;
    }
}
