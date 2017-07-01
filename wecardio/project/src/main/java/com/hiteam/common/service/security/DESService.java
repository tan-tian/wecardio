package com.hiteam.common.service.security;

/**
 * <pre>
 * Description:
 * Author: Zhang zhongtao
 * Version:
 * Since: Ver 1.1
 * Date: 2014-10-28 13:33
 * </pre>
 */
public interface DESService {
    /***
     * 明文加密
     * @param val 明文
     * @return 加密后结果
     */
    public String encrypt(String val);

    /**
     * 密文解密
     * @param val 密文
     * @return 解密结果
     */
    public String decrypt(String val);
}
