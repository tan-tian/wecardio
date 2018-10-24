package com.borsam.service.doctor;

import com.borsam.repository.entity.doctor.DoctorImage;
import com.hiteam.common.base.service.BaseService;

/**
 * Service - 医生资质证书
 * Created by tantian on 2015/7/14.
 */
public interface DoctorImageService extends BaseService<DoctorImage, Long> {

    /**
     * 生成证书图片
     * @param doctorImage 证书图片
     */
    public void build(DoctorImage doctorImage);

    /**
     * 删除所有图片
     * @param did 医生标识
     */
    public void removeImages(Long did);
}
