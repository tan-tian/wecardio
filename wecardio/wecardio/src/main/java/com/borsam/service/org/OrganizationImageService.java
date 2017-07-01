package com.borsam.service.org;

import com.borsam.repository.entity.org.OrganizationImage;
import com.hiteam.common.base.service.BaseService;

/**
 * Service - 机构图片
 * Created by Sebarswee on 2015/7/1.
 */
public interface OrganizationImageService extends BaseService<OrganizationImage, Long> {

    /**
     * 生成机构图片
     * @param organizationImage 机构图片
     */
    public void build(OrganizationImage organizationImage);

    /**
     * 删除所有图片记录
     * @param oid 机构标识
     */
    public void removeImages(Long oid);
}
