package com.borsam.repository.dao.admin;

import com.borsam.repository.entity.admin.Admin;
import com.hiteam.common.base.repository.dao.BaseDao;

/**
 * Dao - 平台管理员账号
 * Created by tantian on 2015/6/19.
 */
public interface AdminDao extends BaseDao<Admin, Long> {

    /**
     * 根据用户名查找账号
     * @param username 用户名
     * @return 平台管理员账号
     */
    public Admin findByUsername(String username);
}
