package com.borsam.service.doctor;

import com.borsam.repository.entity.doctor.DoctorProfile;
import com.hiteam.common.base.service.BaseService;
import com.hiteam.common.util.pojo.EnumBean;

import java.util.List;

/**
 * Service - 医生信息
 * Created by Sebarswee on 2015/6/23.
 */
public interface DoctorProfileService extends BaseService<DoctorProfile, Long> {

    /**
     * 机构管理员注册
     * @param email 用户账号
     * @param password 密码
     * @param mobile 手机
     */
    public void register(String email, String password, String mobile);

    /**
     * 新增医生
     * @param doctorProfile 医生信息
     * @param account 系统账号
     * @param password 账号密码
     * @param isSubmit 是否提交
     */
    public void create(DoctorProfile doctorProfile, String account, String password, Boolean isSubmit);

    /**
     * 编辑医生
     * @param doctorProfile 医生信息
     * @param account 系统账号
     * @param password 账号密码
     * @param isSubmit 是否提交
     */
    public void reCreate(DoctorProfile doctorProfile, String account, String password, Boolean isSubmit);

    /**
     * 医生审核
     * @param did 医生ID
     * @param isPass 是否通过
     * @param remark 备注信息
     */
    public void audit(Long did, Boolean isPass, String remark);

    /**
     * 禁用账号信息
     * @param did 医生ID
     */
    public void disable(Long[] did);

    /**
     * 患者转移
     * @param from 源医生
     * @param to 目标医生
     */
    public void devolve(Long[] from, Long to);

    /**
     * 医生列表（for 下拉框）
     * @param name 医生名称
     * @return 医生列表
     */
    public List<EnumBean> sel(String name,Long iOrgId,Integer iType);
}
