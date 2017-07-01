package com.hiteam.common.service.impl;

import com.hiteam.common.service.RSAService;
import com.hiteam.common.util.crypto.RSAUtils;
import com.hiteam.common.web.WebUtil;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import java.security.KeyPair;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.util.HashMap;
import java.util.Map;

@Service("rsaServiceImpl")
public class RSAServiceImpl implements RSAService {

	/**
	 * 私钥参数名称
	 */
	private static final String PRIVATE_KEY_NAME = "privateKey";

	@Override
	public RSAPublicKey generateKey() {
		Assert.notNull(WebUtil.getSession());
		KeyPair keyPair = RSAUtils.generateKeyPair();
		RSAPublicKey publicKey = (RSAPublicKey) keyPair.getPublic();
		RSAPrivateKey privateKey = (RSAPrivateKey) keyPair.getPrivate();
		WebUtil.getSession().setAttribute(PRIVATE_KEY_NAME, privateKey);
		return publicKey;
	}

	@Override
	public Map<String, String> getModulusAndExponent(RSAPublicKey publicKey) {
		String modulus = Base64.encodeBase64String(publicKey.getModulus().toByteArray());
		String exponent = Base64.encodeBase64String(publicKey.getPublicExponent().toByteArray());
		Map<String, String> modelMap = new HashMap<String, String>();
		modelMap.put("modulus", modulus);
		modelMap.put("exponent", exponent);
		return modelMap;
	}

    @Override
    public Map getModulusAndExponent() {
        return getModulusAndExponent(generateKey());
    }

    @Override
	public void removePrivateKey() {
		Assert.notNull(WebUtil.getSession());
		WebUtil.getSession().removeAttribute(PRIVATE_KEY_NAME);
	}

	@Override
	public String decryptParameter(String name) {
		Assert.notNull(WebUtil.getSession());
		if (name != null) {
			RSAPrivateKey privateKey = (RSAPrivateKey) WebUtil.getSession().getAttribute("privateKey");
			String value = WebUtil.getRequest().getParameter(name);
			if ((privateKey != null) && (StringUtils.isNotEmpty(value))) {
				return RSAUtils.decrypt(privateKey, value);
			}
		}
		return null;
	}

}
