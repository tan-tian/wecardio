package com.borsam.repository.dao.doctor;

import com.borsam.repository.entity.doctor.DoctorImage;
import com.hiteam.common.base.repository.dao.BaseDao;

/**
 * Dao - 医生证书
 * Created by Sebarswee on 2015/8/18.
 */
public interface DoctorImageDao extends BaseDao<DoctorImage, Long> {

    /**
     * 删除所有图片
     * @param did 医生标识
     */
    public void removeImages(Long did);
}
