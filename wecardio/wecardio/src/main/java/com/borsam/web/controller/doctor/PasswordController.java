package com.borsam.web.controller.doctor;

import com.borsam.repository.entity.doctor.DoctorAccount;
import com.borsam.service.doctor.DoctorAccountService;
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
@Controller("doctorPasswordController")
@RequestMapping(value = "/doctor/password")
public class PasswordController extends BaseController {

    @Resource(name = "doctorAccountServiceImpl")
    private DoctorAccountService doctorAccountService;

    /**
     * 修改密码页面
     */
    @RequestMapping(value = "/reset", method = RequestMethod.GET)
    public String reset() {
        return "/doctor/password/reset";
    }

    /**
     * 修改密码提交
     */
    @RequestMapping(value = "/reset", method = RequestMethod.POST)
    @ResponseBody
    public Message reset(String oldPassword, String newPassword) {
        DoctorAccount doctorAccount = doctorAccountService.getCurrent();
        if (!DigestUtils.md5Hex(oldPassword).equals(doctorAccount.getPassword())) {
            return Message.warn("common.message.oldPassword");
        }
        doctorAccount.setPassword(DigestUtils.md5Hex(newPassword));
        doctorAccountService.update(doctorAccount);
        return SUCCESS_MSG;
    }
}
