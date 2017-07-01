package com.borsam.repository.dao.org;

import com.borsam.repository.entity.org.OrganizationImage;
import com.hiteam.common.base.repository.dao.BaseDao;

/**
 * Dao - 机构图片
 * Created by Sebarswee on 2015/8/18.
 */
public interface OrganizationImageDao extends BaseDao<OrganizationImage, Long> {

    /**
     * 删除所有图片
     * @param oid 机构标识
     */
    public void removeImages(Long oid);
}
