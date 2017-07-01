package com.borsam.web.controller.admin;

import com.borsam.repository.entity.admin.Admin;
import com.borsam.service.admin.AdminService;
import com.hiteam.common.web.Message;
import com.hiteam.common.web.controller.BaseController;
import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;

/**
 * Controller - 密码
 * Created by Sebarswee on 2015/9/2.
 */
@Controller("adminPasswordController")
@RequestMapping(value = "/admin/password")
public class PasswordController extends BaseController {

    @Resource(name = "adminServiceImpl")
    private AdminService adminService;

    /**
     * 修改密码页面
     */
    @RequestMapping(value = "/reset", method = RequestMethod.GET)
    public String reset() {
        return "/admin/password/reset";
    }

    /**
     * 修改密码提交
     */
    @RequestMapping(value = "/reset", method = RequestMethod.POST)
    @ResponseBody
    public Message reset(String oldPassword, String newPassword) {
        Admin admin = adminService.getCurrent();
        if (!DigestUtils.md5Hex(oldPassword).equals(admin.getPassword())) {
            return Message.warn("common.message.oldPassword");
        }
        admin.setPassword(DigestUtils.md5Hex(newPassword));
        adminService.update(admin);
        return SUCCESS_MSG;
    }
}
