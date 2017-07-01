package com.hiteam.common.web.security.shiro.token;

import org.apache.shiro.authc.UsernamePasswordToken;

/**
 * shiro登录令牌
 */
public class ExtAuthenticationToken extends UsernamePasswordToken {

	/**
	 * 
	 */
	private static final long serialVersionUID = -6764367468165785696L;

	private String captchaId;	// 验证ID
	private String captcha;		// 验证码
	private String sBrowserType;		// 浏览器类型与版本 
	/**
	 * 忽略验证码（用于内部代码自动登陆)
	 */
	private boolean isIgnoreCaptcha = false;

	public boolean isIgnoreCaptcha() {
		return isIgnoreCaptcha;
	}

	public void setIgnoreCaptcha(boolean isIgnoreCaptcha) {
		this.isIgnoreCaptcha = isIgnoreCaptcha;
	}

	public ExtAuthenticationToken(){

	}

	/**
	 * 
	 * @param username		用户名
	 * @param password		密码
	 * @param captchaId		验证ID
	 * @param captcha		验证码
	 * @param rememberMe	记住我
	 * @param host			登录ip
	 */
	public ExtAuthenticationToken(String username, String password, String captchaId, String captcha, boolean rememberMe, String host, String sBrowserType) {
		super(username, password, rememberMe);
		this.captchaId = captchaId;
		this.captcha = captcha;
		this.sBrowserType = sBrowserType;
	}

	public ExtAuthenticationToken(String username, String password, String captchaId, String captcha, boolean rememberMe, String host,String sBrowserType,boolean isIgnoreCaptcha) {
		this(username,password,captchaId,captcha,rememberMe,host,sBrowserType);
		this.isIgnoreCaptcha = isIgnoreCaptcha;
	}

	public String getCaptchaId() {
		return this.captchaId;
	}

	public void setCaptchaId(String captchaId) {
		this.captchaId = captchaId;
	}

	public String getCaptcha() {
		return this.captcha;
	}

	public void setCaptcha(String captcha) {
		this.captcha = captcha;
	}
	
	public String getsBrowserType() {
		return sBrowserType;
	}

	public void setsBrowserType(String sBrowserType) {
		this.sBrowserType = sBrowserType;
	}
}
