package com.hiteam.common.service.security.impl;

import com.hiteam.common.service.security.DESService;
import com.hiteam.common.util.ConfigUtils;
import com.hiteam.common.util.crypto.DESUtil;
import org.springframework.stereotype.Service;

/**
 * <pre>
 * Description:
 * Author: tantian
 * Version:
 * Since: Ver 1.1
 * Date: 2014-10-28 13:35
 * </pre>
 */
@Service("desServiceImpl")
public class DESServiceImpl implements DESService {

    private String getKey(){
        return ConfigUtils.config.getProperty("des.key","cattsoft");
    }
    @Override
    public String encrypt(String val) {
        return DESUtil.encrypt(getKey(), val);
    }

    @Override
    public String decrypt(String val) {
        return DESUtil.decrypt(getKey(),val);
    }
}
