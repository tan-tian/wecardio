package com.borsam.repository.dao.org;

import com.borsam.repository.entity.org.OrganizationDoctorImage;
import com.hiteam.common.base.repository.dao.BaseDao;

/**
 * Dao - 机构医生证书
 * Created by Sebarswee on 2015/8/18.
 */
public interface OrganizationDoctorImageDao extends BaseDao<OrganizationDoctorImage, Long> {

    /**
     * 删除所有图片
     * @param oid 机构标识
     */
    public void removeImages(Long oid);
}
