package com.borsam.service.patient;

import com.borsam.repository.entity.patient.PatientProfile;
import com.hiteam.common.base.pojo.search.Pageable;
import com.hiteam.common.base.service.BaseService;
import com.hiteam.common.util.pojo.EnumBean;

import java.util.List;

/**
 * Service - 患者信息
 * Created by Sebarswee on 2015/7/20.
 */
public interface PatientProfileService extends BaseService<PatientProfile, Long> {

    /**
     * 判断mobile是否存在
     * @param mobile mobile
     * @return boolean
     */
    public boolean mobileExists(String mobile);

    /**
     * 判断eMail是否存在
     * @param email
     * @return boolean
     */
    public boolean emailExists(String email);

    /**
     * 新增患者
     * @param patientProfile 患者信息
     * @param account 系统账号
     */
    public void create(PatientProfile patientProfile, String account);

    /**
     * 编辑患者
     * @param patientProfile 患者信息
     * @param account 系统账号
     */
    public void reCreate(PatientProfile patientProfile, String account);

    /**
     * 禁用账号信息
     * @param ids 患者ID
     */
    public void disable(Long[] ids);


    /**
     * VIP患者列表（for 下拉框）
     * @param name 患者名称
     * @return VIP患者列表
     */
    public List<EnumBean> sel(Long iOrgId,Long iDoctorId,Integer iType,String name);

    /**
     * 通过多条件匹配查询
     * @param name
     * @param email
     * @param mobile
     * @param isWalletActive
     * @param doctor
     * @param iType
     * @param pageable
     * @return boolean
     */
    public List<PatientProfile> queryPatient(String name, String email, String mobile, Integer[] isWalletActive, Integer[] doctor, Long[] org, String iType, Pageable pageable);
}
