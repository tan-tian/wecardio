package com.borsam.web.security.shiro.token;

import com.borsam.pub.UserType;
import org.apache.shiro.authc.UsernamePasswordToken;

/**
 * 含用户类型的登录令牌
 * Created by tantian on 2015/6/17.
 */
public class UserTypeToken extends UsernamePasswordToken {

    private String captchaId;	        // 验证ID
    private String captcha;		        // 验证码
    private UserType userType;          // 用户类型

    /**
     * 构造方法
     */
    public UserTypeToken(String username, String password, String captchaId, String captcha, UserType userType, boolean rememberMe) {
        super(username, password, rememberMe);
        this.captchaId = captchaId;
        this.captcha = captcha;
        this.userType = userType;
    }

    /**
     * 获取验证码ID
     * @return 验证码ID
     */
    public String getCaptchaId() {
        return captchaId;
    }

    /**
     * 设置验证码ID
     * @param captchaId 验证码ID
     */
    public void setCaptchaId(String captchaId) {
        this.captchaId = captchaId;
    }

    /**
     * 获取验证码
     * @return 验证码
     */
    public String getCaptcha() {
        return captcha;
    }

    /**
     * 设置验证码
     * @param captcha 验证码
     */
    public void setCaptcha(String captcha) {
        this.captcha = captcha;
    }

    /**
     * 获取用户类型
     * @return 用户类型
     */
    public UserType getUserType() {
        return userType;
    }

    /**
     * 设置用户类型
     * @param userType 用户类型
     */
    public void setUserType(UserType userType) {
        this.userType = userType;
    }
}
