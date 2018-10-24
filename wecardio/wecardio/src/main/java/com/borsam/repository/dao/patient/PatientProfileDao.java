package com.borsam.repository.dao.patient;

import com.borsam.repository.entity.patient.PatientProfile;
import com.hiteam.common.base.pojo.search.Pageable;
import com.hiteam.common.base.repository.dao.BaseDao;
import com.hiteam.common.util.pojo.EnumBean;

import java.util.List;

/**
 * Dao - 患者信息
 * Created by tantian on 2015/7/20.
 */
public interface PatientProfileDao extends BaseDao<PatientProfile, Long> {

    /**
     * 判断联系电话是否存在
     * @param mobile mobile(忽略大小写)
     * @return mobile
     */
    public boolean mobileExists(String mobile);

    /**
     * 患者列表（for 下拉框）
     * @param name 患者名称
     * @return 患者列表
     */
    public List<EnumBean> sel(Long iOrgId,Long iDoctorId,Integer iType,String name);

    /**
     * 判断eMail是否存在
     * @param email
     * @return boolean
     */
    public boolean emailExists(String email);

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
