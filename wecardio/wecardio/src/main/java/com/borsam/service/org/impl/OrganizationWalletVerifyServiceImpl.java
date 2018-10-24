package com.borsam.service.org.impl;

import com.borsam.repository.dao.org.OrganizationDao;
import com.borsam.repository.dao.org.OrganizationWalletDao;
import com.borsam.repository.dao.org.OrganizationWalletVerifyDao;
import com.borsam.repository.entity.org.Organization;
import com.borsam.repository.entity.org.OrganizationWallet;
import com.borsam.repository.entity.org.OrganizationWalletVerify;
import com.borsam.service.org.OrganizationWalletVerifyService;
import com.hiteam.common.base.service.impl.BaseServiceImpl;
import com.hiteam.common.service.RSAService;
import com.hiteam.common.util.ConfigUtils;
import com.hiteam.common.web.Message;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang.time.DateUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.Date;

/**
 * Service - 机构钱包认证
 * Created by tantian on 2015/8/10.
 */
@Service("organizationWalletVerifyServiceImpl")
public class OrganizationWalletVerifyServiceImpl extends BaseServiceImpl<OrganizationWalletVerify, Long> implements OrganizationWalletVerifyService {

    @Resource(name = "rsaServiceImpl")
    private RSAService rsaService;

    @Resource(name = "organizationDaoImpl")
    private OrganizationDao organizationDao;

    @Resource(name = "organizationWalletDaoImpl")
    private OrganizationWalletDao organizationWalletDao;

    @Resource(name = "organizationWalletVerifyDaoImpl")
    public void setBaseDao(OrganizationWalletVerifyDao organizationWalletVerifyDao) {
        super.setBaseDao(organizationWalletVerifyDao);
    }

    @Override
    @Transactional
    public Message verify(Long oid) {
        // 支付验证
        OrganizationWalletVerify organizationWalletVerify = this.find(oid);
        // 未设置钱包密码
        if (organizationWalletVerify == null) {
            return Message.warn("org.message.wallet.noset");
        }
        // 是否锁定
        if (organizationWalletVerify.isLock()) {
            // 获取配置的锁定时长，0为永久锁定
            int walletLockTime = Integer.parseInt(ConfigUtils.config.getProperty("walletLockTime", "0"));
            if (walletLockTime == 0) {
                return Message.warn("org.message.wallet.lock");
            }
            Date lockedDate = new Date(organizationWalletVerify.getLockTime() * 1000);
            Date unlockDate = DateUtils.addMinutes(lockedDate, walletLockTime);
            // 超过锁定时长自动解锁
            if (new Date().after(unlockDate)) {
                organizationWalletVerify.setFailureNum(0);
                organizationWalletVerify.setLockTime(0L);
                super.update(organizationWalletVerify);
            } else {
                return Message.warn("org.message.wallet.lock");
            }
        }
        // 验证密码
        String password = rsaService.decryptParameter("enPassword");
        if (!DigestUtils.md5Hex(password).equals(organizationWalletVerify.getPassword())) {
            int failureCount = organizationWalletVerify.getFailureNum() + 1;
            // 获取配置的连续登录失败次数
            int walletLockCount = Integer.parseInt(ConfigUtils.config.getProperty("walletLockCount", "1"));
            // 登录失败超过配置次数则锁定账号
            if (failureCount >= walletLockCount) {
                organizationWalletVerify.setLockTime(new Date().getTime() / 1000);
            }
            organizationWalletVerify.setFailureNum(failureCount);
            super.update(organizationWalletVerify);
            return Message.warn("org.message.wallet.lockCount", walletLockCount);
        }
        organizationWalletVerify.setFailureNum(0);
        this.update(organizationWalletVerify);

        return Message.success("");
    }

    @Override
    @Transactional
    public void activate(Long oid, String password) {
        OrganizationWalletVerify organizationWalletVerify = new OrganizationWalletVerify();
        organizationWalletVerify.setPassword(DigestUtils.md5Hex(password));
        organizationWalletVerify.setId(oid);
        organizationWalletVerify.setFailureNum(0);
        organizationWalletVerify.setLockTime(0L);
        organizationWalletVerify.setToken("");
        organizationWalletVerify.setTokenTime(0L);
        this.save(organizationWalletVerify);

        // 设置钱包激活状态
        Organization organization = organizationDao.find(oid);
        organization.setIsWalletActive(true);
        organizationDao.merge(organization);

        // 生成钱包
        OrganizationWallet organizationWallet = organizationWalletDao.find(oid);
        if (organizationWallet == null) {
            organizationWallet = new OrganizationWallet();
            organizationWallet.setId(oid);
            organizationWallet.setTotal(new BigDecimal(0));
            organizationWallet.setRealTotal(new BigDecimal(0));
            organizationWallet.setGrandTotal(new BigDecimal(0));
            organizationWallet.setLastTime(new Date().getTime() / 1000);
            organizationWallet.setBankBranch("");
            organizationWallet.setBankUsername("");
            organizationWallet.setBankNo("");
            organizationWalletDao.persist(organizationWallet);
        }
    }
}
