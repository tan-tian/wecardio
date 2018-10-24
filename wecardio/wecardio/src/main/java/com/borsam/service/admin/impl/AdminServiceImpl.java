package com.borsam.service.admin.impl;

import com.borsam.pojo.security.Principal;
import com.borsam.repository.dao.admin.AdminDao;
import com.borsam.repository.entity.admin.Admin;
import com.borsam.service.admin.AdminService;
import com.hiteam.common.base.service.impl.BaseServiceImpl;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;

/**
 * Service - 平台管理员账号
 * Created by tantian on 2015/6/19.
 */
@Service("adminServiceImpl")
public class AdminServiceImpl extends BaseServiceImpl<Admin, Long> implements AdminService {

    @Resource(name = "adminDaoImpl")
    private AdminDao adminDao;

    @Resource(name = "adminDaoImpl")
    public void setBaseDao(AdminDao adminDao) {
        super.setBaseDao(adminDao);
    }

    @Transactional(readOnly = true)
    @Override
    public Admin getCurrent() {
        Subject subject = SecurityUtils.getSubject();
        if (subject != null) {
            Principal principal = (Principal) subject.getPrincipal();
            if (principal != null) {
                return this.adminDao.find(principal.getId());
            }
        }
        return null;
    }

    @Transactional(readOnly = true)
    @Override
    public Admin findByUsername(String username) {
        return adminDao.findByUsername(username);
    }
}
