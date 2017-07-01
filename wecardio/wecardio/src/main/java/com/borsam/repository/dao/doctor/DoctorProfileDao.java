package com.borsam.repository.dao.doctor;

import com.borsam.repository.entity.doctor.DoctorProfile;
import com.hiteam.common.base.repository.dao.BaseDao;
import com.hiteam.common.util.pojo.EnumBean;

import java.util.List;


/**
 * Dao - 医生信息
 * Created by Sebarswee on 2015/6/23.
 */
public interface DoctorProfileDao extends BaseDao<DoctorProfile, Long> {

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

    /**
     * 查找机构下面的所有技师
     * @param oid 机构
     * @return 医生列表
     */
    public List<DoctorProfile> findEditDoctor(Long oid);

    /**
     * 查找机构下面的所有审核医生
     * @param oid 机构
     * @return 医生列表
     */
    public List<DoctorProfile> findAuditDoctor(Long oid);

    /**
     * 查找机构下面的所有主治医生
     * @param oid 机构
     * @return 医生列表
     */
    public List<DoctorProfile> findMainDoctor(Long oid);

    /**
     * 查找机构下面的所有机构管理员
     * @param oid 机构
     * @return 管理员列表
     */
    public List<DoctorProfile> findAdminDoctor(Long oid);
}
