package com.borsam.service.admin;

import com.borsam.repository.entity.admin.Admin;
import com.hiteam.common.base.service.BaseService;

/**
 * Service - 平台管理员账号
 * Created by tantian on 2015/6/19.
 */
public interface AdminService extends BaseService<Admin, Long> {

    /**
     * 获取当前用户
     * @return 当前用户
     */
    public Admin getCurrent();

    /**
     * 根据用户名查找账号
     * @param username 用户名
     * @return 平台管理员账号
     */
    public Admin findByUsername(String username);
}
