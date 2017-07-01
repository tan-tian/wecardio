package com.hiteam.common.service.impl;

import com.hiteam.common.service.CaptchaService;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.awt.image.BufferedImage;
import java.util.UUID;

/**
 * 验证码服务类
 */
@Service("captchaServiceImpl")
public class CaptchaServiceImpl implements CaptchaService {

	@Resource(name = "imageCaptchaService")
	private com.octo.captcha.service.CaptchaService captchaService;

	@Override
	public BufferedImage buildImage(String captchaId) {
		return (BufferedImage) captchaService.getChallengeForID(captchaId);
	}

	@Override
	public boolean isValid(String captchaId, String captcha) {
		if ((StringUtils.isNotEmpty(captchaId)) && (StringUtils.isNotEmpty(captcha))) {
			try {
				return captchaService.validateResponseForID(captchaId, captcha.toUpperCase()).booleanValue();
			} catch (Exception e) {
				e.printStackTrace();
				return false;
			}
		}
		return false;
	}

	@Override
	public String getCaptchaId() {
		return UUID.randomUUID().toString();
	}

}
