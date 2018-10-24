package com.borsam.service.patient.impl;

import com.borsam.repository.dao.patient.PatientProfileDao;
import com.borsam.repository.dao.patient.PatientWalletDao;
import com.borsam.repository.dao.patient.PatientWalletVerifyDao;
import com.borsam.repository.entity.patient.PatientAccount;
import com.borsam.repository.entity.patient.PatientProfile;
import com.borsam.repository.entity.patient.PatientWallet;
import com.borsam.repository.entity.patient.PatientWalletVerify;
import com.borsam.service.patient.PatientWalletVerifyService;
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
 * Service - 患者钱包验证
 * Created by tantian on 2015/7/23.
 */
@Service("patientWalletVerifyServiceImpl")
public class PatientWalletVerifyServiceImpl extends BaseServiceImpl<PatientWalletVerify, Long> implements PatientWalletVerifyService {
    @Resource(name = "rsaServiceImpl")
    private RSAService rsaService;

    @Resource(name = "patientProfileDaoImpl")
    private PatientProfileDao patientProfileDao;

    @Resource(name = "patientWalletDaoImpl")
    private PatientWalletDao patientWalletDao;

    @Resource(name = "patientWalletVerifyDaoImpl")
    public void setBaseDao(PatientWalletVerifyDao patientWalletVerifyDao) {
        super.setBaseDao(patientWalletVerifyDao);
    }

    @Override
    @Transactional
    public void activate(Long uid, String password) {
        PatientWalletVerify patientWalletVerify = new PatientWalletVerify();
        patientWalletVerify.setPassword(DigestUtils.md5Hex(password));
        patientWalletVerify.setId(uid);
        patientWalletVerify.setFailureNum(0);
        patientWalletVerify.setLockTime(0L);
        patientWalletVerify.setToken("");
        patientWalletVerify.setTokenTime(0L);
        this.save(patientWalletVerify);

        // 设置钱包激活状态
        PatientProfile patientProfile = patientProfileDao.find(uid);
        patientProfile.setIsWalletActive(true);
        patientProfileDao.merge(patientProfile);

        // 生成钱包
        PatientWallet patientWallet = patientWalletDao.find(uid);
        if (patientWallet == null) {
            patientWallet = new PatientWallet();
            patientWallet.setId(uid);
            patientWallet.setTotal(new BigDecimal(0));
            patientWalletDao.persist(patientWallet);
        }
    }

    @Override
    public Message verify(PatientAccount patientAccount) {
        // 支付验证
        PatientWalletVerify patientWalletVerify = super.find(patientAccount.getId());
//        PatientWalletVerify patientWalletVerify = super.find(10009L);
        // 未设置钱包密码
        if (patientWalletVerify == null) {
            return Message.warn("patient.record.message.wallet.noset");
        }
        // 是否锁定
        if (patientWalletVerify.isLock()) {
            // 获取配置的锁定时长，0为永久锁定
            int walletLockTime = Integer.parseInt(ConfigUtils.config.getProperty("walletLockTime", "0"));
            if (walletLockTime == 0) {
                return Message.warn("patient.record.message.wallet.lock");
            }
            Date lockedDate = new Date(patientWalletVerify.getLockTime() * 1000);
            Date unlockDate = DateUtils.addMinutes(lockedDate, walletLockTime);
            // 超过锁定时长自动解锁
            if (new Date().after(unlockDate)) {
                patientWalletVerify.setFailureNum(0);
                patientWalletVerify.setLockTime(0L);
                super.update(patientWalletVerify);
            } else {
                return Message.warn("patient.record.message.wallet.lock");
            }
        }
        // 验证密码
        String password = rsaService.decryptParameter("enPassword");
        if (!DigestUtils.md5Hex(password).equals(patientWalletVerify.getPassword())) {
            int failureCount = patientWalletVerify.getFailureNum() + 1;
            // 获取配置的连续登录失败次数
            int walletLockCount = Integer.parseInt(ConfigUtils.config.getProperty("walletLockCount", "1"));
            // 登录失败超过配置次数则锁定账号
            if (failureCount >= walletLockCount) {
                patientWalletVerify.setLockTime(new Date().getTime() / 1000);
            }
            patientWalletVerify.setFailureNum(failureCount);
            super.update(patientWalletVerify);
            return Message.warn("patient.record.message.wallet.lockCount", walletLockCount);
        }
        patientWalletVerify.setFailureNum(0);
        super.update(patientWalletVerify);

        return Message.success("");
    }
}
