package com.borsam.service.patient;

import com.borsam.repository.entity.patient.PatientAccount;
import com.hiteam.common.base.service.BaseService;

/**
 * Service - 患者账号
 * Created by tantian on 2015/6/18.
 */
public interface PatientAccountService extends BaseService<PatientAccount, Long> {

    /**
     * 根据用户名查找患者账号
     * @param username 用户名
     * @return 患者账号
     */
    public PatientAccount findByUsername(String username);

    /**
     * 获取当前登录账号
     * @return 当前登录账号
     */
    public PatientAccount getCurrent();
}
