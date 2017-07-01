package com.hiteam.common.service;

import java.security.interfaces.RSAPublicKey;
import java.util.Map;

/**
 * RSA算法加密
 */
public interface RSAService {
	
	/**
	 * 生成密钥
	 * 添加私钥到session并返回公钥
	 * @return
	 */
	public abstract RSAPublicKey generateKey();

	/***
	 *@param publicKey 密钥
	 * @return Map
	 */
	public abstract Map<String, String> getModulusAndExponent(RSAPublicKey publicKey);

	public abstract Map getModulusAndExponent();

	/**
	 * 移除私钥
	 */
	public abstract void removePrivateKey();

	/**
	 * 解密参数
	 * @param name
	 * @return
	 */
	public abstract String decryptParameter(String name);
}
