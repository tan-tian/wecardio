package com.borsam.service.org;

import com.borsam.repository.entity.org.OrganizationWalletVerify;
import com.hiteam.common.base.service.BaseService;
import com.hiteam.common.web.Message;

/**
 * Service - 机构钱包认证
 * Created by Sebarswee on 2015/8/10.
 */
public interface OrganizationWalletVerifyService extends BaseService<OrganizationWalletVerify, Long> {

    /***
     * 支付校验
     * @param oid 机构标识
     * @return Message
     */
    public Message verify(Long oid);

    /**
     * 钱包激活
     * @param oid 机构标识
     * @param password 支付密码
     */
    public void activate(Long oid, String password);
}
