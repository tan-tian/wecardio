package com.hiteam.common.service;

import java.awt.image.BufferedImage;

/**
 * 验证码服务类
 */
public abstract interface CaptchaService {
	
	/**
	 * 生成验证码图片
	 * @param captchaId 验证ID
	 * @return 验证码图片
	 */
	public abstract BufferedImage buildImage(String captchaId);

	/**
	 * 验证验证码
	 * @param captchaId 验证ID
	 * @param captcha	验证码（不分大小写）
	 * @return
	 */
	public abstract boolean isValid(String captchaId, String captcha);

	/**
	 * 获取验证码ID
	 * @return 验证码ID
	 */
	public String getCaptchaId();
}
