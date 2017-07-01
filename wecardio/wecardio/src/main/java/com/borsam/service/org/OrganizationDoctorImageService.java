package com.borsam.service.org;

import com.borsam.repository.entity.org.OrganizationDoctorImage;
import com.hiteam.common.base.service.BaseService;

/**
 * Service - 机构医生证书
 * Created by Sebarswee on 2015/7/1.
 */
public interface OrganizationDoctorImageService extends BaseService<OrganizationDoctorImage, Long> {

    /**
     * 生成医生证书图片
     * @param doctorImage 医生证书图片
     */
    public void build(OrganizationDoctorImage doctorImage);

    /**
     * 删除所有图片
     * @param oid 机构标识
     */
    public void removeImages(Long oid);
}
