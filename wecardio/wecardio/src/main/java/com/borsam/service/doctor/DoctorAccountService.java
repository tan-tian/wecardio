package com.borsam.service.doctor;

import com.borsam.repository.entity.doctor.DoctorAccount;
import com.hiteam.common.base.service.BaseService;

/**
 * Service - 医生账号
 * Created by Sebarswee on 2015/6/18.
 */
public interface DoctorAccountService extends BaseService<DoctorAccount, Long> {

    /**
     * 根据用户名查找医生账号
     * @param username 用户名
     * @return 医生账号
     */
    public DoctorAccount findByUsername(String username);

    /**
     * 判断Email是否存在
     * @param email Email(忽略大小写)
     * @return Email是否存在
     */
    public boolean emailExists(String email);

    /**
     * 获取当前登录账号
     * @return 当前登录账号，若不存在返回null
     */
    public DoctorAccount getCurrent();
}
