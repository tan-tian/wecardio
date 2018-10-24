package com.borsam.service.patient.impl;

import com.borsam.pojo.security.Principal;
import com.borsam.repository.dao.patient.PatientAccountDao;
import com.borsam.repository.dao.patient.PatientProfileDao;
import com.borsam.repository.entity.common.Language;
import com.borsam.repository.entity.patient.PatientAccount;
import com.borsam.repository.entity.patient.PatientProfile;
import com.borsam.service.patient.PatientProfileService;
import com.hiteam.common.base.pojo.search.Pageable;
import com.hiteam.common.base.service.impl.BaseServiceImpl;
import com.hiteam.common.util.ConfigUtils;
import com.hiteam.common.util.Multimedia;
import com.hiteam.common.util.io.FileUtil;
import com.hiteam.common.util.pojo.EnumBean;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.io.File;
import java.io.IOException;
import java.util.List;

/**
 * Service - 鎮ｈ�呬俊鎭�
 * Created by tantian on 2015/7/20.
 */
@Service("patientProfileServiceImpl")
public class PatientProfileServiceImpl extends BaseServiceImpl<PatientProfile, Long> implements PatientProfileService {

    @Resource(name = "patientProfileDaoImpl")
    private PatientProfileDao patientProfileDao;

    @Resource(name = "patientAccountDaoImpl")
    private PatientAccountDao patientAccountDao;

    @Resource(name = "patientProfileDaoImpl")
    public void setBaseDao(PatientProfileDao patientProfileDao) {
        super.setBaseDao(patientProfileDao);
    }

    @Transactional(readOnly = true)
    @Override
    public boolean mobileExists(String mobile) {
        return patientProfileDao.mobileExists(mobile);
    }

    @Transactional(readOnly = true)
    @Override
    public boolean emailExists(String email) {
        return patientProfileDao.emailExists(email);
    }

    @Transactional
    @Override
    public void create(PatientProfile patientProfile, String account) {
        // 鏂板鍖荤敓璐﹀彿淇℃伅
        PatientAccount patientAccount = new PatientAccount();
        //鍒ゆ柇account鏄偖绠卞湴鍧�杩樻槸鎵嬫満濂藉悧
        boolean flag=true;
        for (int i = 0; i < account.length(); i++){
        	   if (!Character.isDigit(account.charAt(i))){
        	    flag=false;
        	   }
        }
        if (flag) 
        {
        	patientAccount.setMobile(account);
        	patientAccount.setEmail("");
        }
        else
        {
        	patientAccount.setEmail(account);
        	patientAccount.setMobile("");
        }
        	
        //patientAccount.setMobile(patientProfile.getMobile());
        patientAccount.setPassword(DigestUtils.md5Hex(ConfigUtils.config.getProperty("defaultPassword")));
        patientAccount.setIsDelete(false);
        patientAccountDao.saveOrUpdate(patientAccount);

        patientProfile.setId(patientAccount.getId());
        patientProfile.setPatientAccount(patientAccount);
        patientProfile.setFaceTime(0L);
        patientProfile.setIsDelete(false);
        patientProfile.setBindTime(0L);
        patientProfile.setLoginTime(0L);
        patientProfile.setLastConsultation("");
        patientProfile.setIsWalletActive(false);

        Principal principal = getLoginId();
        switch (principal.getUserType()) {
            case org:
                patientProfile.setBindType(PatientProfile.BindType.org);
                break;
            case doctor:
                patientProfile.setBindType(PatientProfile.BindType.org);
                break;
            default:
                patientProfile.setBindType(PatientProfile.BindType.self);
                break;
        }

        Language language = new Language();
        language.setId(1002L);
        language.setName("English");
        patientProfile.setLanguage(language);

        // 淇濆瓨澶村儚
        if (StringUtils.isNotEmpty(patientProfile.getHeadPath())) {
            this.saveHeadImg(patientProfile);
        }
        this.save(patientProfile);
    }

    @Transactional
    @Override
    public void reCreate(PatientProfile patientProfile, String account) {
//        patientProfile.setFaceTime(0L);
//        patientProfile.setIsDelete(false);
//        patientProfile.setBindTime(0L);
//        patientProfile.setLoginTime(0L);
//        patientProfile.setBindType(PatientProfile.BindType.self);
//        patientProfile.setLastConsultation("");
//        patientProfile.setIsWalletActive(false);
//
//        Language language = new Language();
//        language.setId(1002L);
//        language.setName("English");
//        patientProfile.setLanguage(language);

        // 淇濆瓨澶村儚
        if (StringUtils.isNotEmpty(patientProfile.getHeadPath())) {
            this.saveHeadImg(patientProfile);
        }
        this.update(patientProfile,"patientAccount","faceTime","isDelete","created","bindTime","loginTime","bindType","lastConsultation","isWalletActive","language");
    }

    /**
     * 淇濆瓨澶村儚
     * @param patientProfile 鎮ｈ�呬俊鎭�
     */
    private void saveHeadImg(PatientProfile patientProfile) {
        // 淇濆瓨鍖荤敓澶村儚
        if (StringUtils.isNotEmpty(patientProfile.getHeadPath())) {
            /**
             * 鎮ｈ�卛d鎵╁厖鎴�12浣嶅瓧绗︽偅锛屽10000聽->聽000000010000
             * 鏂囦欢璺緞锛毬�/face/00/00/00/01/00/00/10000
             * 缂╃暐璺緞锛�/face/00/00/00/01/00/00/10000.thumb  (80X80)
             */
            String leftPad = StringUtils.leftPad(patientProfile.getId() + "", 12, "0");
            String uploadPath = "/face/";
            int start = 0, end = 2;
            while (end <= leftPad.length()) {
                uploadPath += StringUtils.substring(leftPad, start, end) + "/";
                start += 2;
                end += 2;
            }
            String basePath = ConfigUtils.config.getProperty("uploadPath");
            String headPath = uploadPath + patientProfile.getId();
            String thumbPath  = uploadPath + patientProfile.getId() + ".thumb";
            // 澶村儚鏃犲彉鏇�
            if (patientProfile.getHeadPath().equals(headPath)) {
                patientProfile.setThumbPath(thumbPath);
                return;
            }
            try {
                File source = new File(basePath, patientProfile.getHeadPath());
                // 澶村儚
                FileUtil.copyFile(source, new File(basePath, headPath));
                // 缂╃暐鍥�
                Multimedia.saveImage(source, basePath + headPath + "-thumb.jpg", 80, 80);
                File thumb = new File(basePath + headPath + "-thumb.jpg");
                FileUtil.copyFile(thumb, new File(basePath, thumbPath));
                FileUtil.deleteQuietly(thumb);
            } catch (IOException e) {
                e.printStackTrace();
                throw new RuntimeException("淇濆瓨澶村儚澶辫触锛�");
            }
            patientProfile.setHeadPath(headPath);
            patientProfile.setThumbPath(thumbPath);
        }
    }

    @Transactional
    @Override
    public void disable(Long[] ids) {
        for (Long id : ids) {
            // 璁剧疆鎮ｈ�呬俊鎭负鍒犻櫎
            PatientProfile patientProfile = this.find(id);
            patientProfile.setIsDelete(true);
            this.update(patientProfile);

            // 璁剧疆鎮ｈ�呰处鍙蜂负鍒犻櫎
            PatientAccount patientAccount = patientAccountDao.find(id);
            patientAccount.setIsDelete(true);
            patientAccountDao.saveOrUpdate(patientAccount);
        }
    }

    /**
     * 褰撳墠鐧诲綍浜�
     *
     * @return Long
     */
    public Principal getLoginId() {
        Subject subject = SecurityUtils.getSubject();
        Principal principal = (Principal) subject.getPrincipal();
        return principal;
    }

    @Transactional(readOnly = true)
    @Override
    public List<EnumBean> sel(Long iOrgId,Long iDoctorId,Integer iType,String name) {
        return patientProfileDao.sel(iOrgId,iDoctorId,iType,(StringUtils.isNotEmpty(name) ? "%" + name + "%" : name));
    }

    /**
     * 閫氳繃澶氭潯浠跺尮閰嶆煡璇�
     *
     * @param name
     * @param email
     * @param mobile
     * @param isWalletActive
     * @param doctor
     * @param org
     * @param iType
     * @param pageable       @return boolean
     */
    @Transactional(readOnly = true)
    @Override
    public List<PatientProfile> queryPatient(String name, String email, String mobile, Integer[] isWalletActive, Integer[] doctor, Long[] org, String iType, Pageable pageable) {
        return patientProfileDao.queryPatient(name,email,mobile,isWalletActive,doctor,org,iType, pageable);
    }

}
