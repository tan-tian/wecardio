package com.borsam.service.doctor.impl;

import com.borsam.pojo.wf.CreateProcessParam;
import com.borsam.pojo.wf.WfCode;
import com.borsam.repository.dao.doctor.DoctorProfileDao;
import com.borsam.repository.dao.org.OrganizationDao;
import com.borsam.repository.dao.wf.ActDao;
import com.borsam.repository.dao.wf.WfInstDao;
import com.borsam.repository.entity.doctor.DoctorAccount;
import com.borsam.repository.entity.doctor.DoctorImage;
import com.borsam.repository.entity.doctor.DoctorProfile;
import com.borsam.repository.entity.doctor.DoctorToken;
import com.borsam.repository.entity.org.Organization;
import com.borsam.repository.entity.wf.Act;
import com.borsam.repository.entity.wf.WfInst;
import com.borsam.service.doctor.DoctorAccountService;
import com.borsam.service.doctor.DoctorImageService;
import com.borsam.service.doctor.DoctorProfileService;
import com.borsam.service.doctor.DoctorTokenService;
import com.borsam.service.pub.MailService;
import com.borsam.service.wf.ProcessEngine;
import com.hiteam.common.base.service.impl.BaseServiceImpl;
import com.hiteam.common.util.ConfigUtils;
import com.hiteam.common.util.Multimedia;
import com.hiteam.common.util.io.FileUtil;
import com.hiteam.common.util.pojo.EnumBean;
import com.hiteam.common.web.WebUtil;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.time.DateUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.io.File;
import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

/**
 * Service - 鍖荤敓淇℃伅
 * Created by Sebarswee on 2015/6/23.
 */
@Service("doctorProfileServiceImpl")
public class DoctorProfileServiceImpl extends BaseServiceImpl<DoctorProfile, Long> implements DoctorProfileService {

    @Resource(name = "processEngineImpl")
    private ProcessEngine processEngine;

    @Resource(name = "wfInstDaoImpl")
    private WfInstDao wfInstDao;

    @Resource(name = "actDaoImpl")
    private ActDao actDao;

    @Resource(name = "doctorAccountServiceImpl")
    private DoctorAccountService doctorAccountService;

    @Resource(name = "doctorImageServiceImpl")
    private DoctorImageService doctorImageService;

    @Resource(name = "doctorTokenServiceImpl")
    private DoctorTokenService doctorTokenService;

    @Resource(name = "mailServiceImpl")
    private MailService mailService;

    @Resource(name = "organizationDaoImpl")
    private OrganizationDao organizationDao;

    @Resource(name = "doctorProfileDaoImpl")
    private DoctorProfileDao doctorProfileDao;

    @Resource(name = "doctorProfileDaoImpl")
    public void setBaseDao(DoctorProfileDao doctorProfileDao) {
        super.setBaseDao(doctorProfileDao);
    }

    @Transactional
    @Override
    public void register(String email, String password, String mobile) {
        // 淇濆瓨璐﹀彿淇℃伅
        DoctorAccount doctorAccount = new DoctorAccount();
        doctorAccount.setEmail(email);
        doctorAccount.setPassword(DigestUtils.md5Hex(password));
        doctorAccount.setMobile(mobile);
        doctorAccount.setIsActive(false);
        doctorAccount.setIsDelete(false);
        doctorAccountService.save(doctorAccount);

        // 淇濆瓨璧勬枡淇℃伅
        DoctorProfile doctorProfile = new DoctorProfile();
        doctorProfile.setId(doctorAccount.getId());
        doctorProfile.setDoctorAccount(doctorAccount);
        doctorProfile.setFirstName("");
        doctorProfile.setSecondName("");
        doctorProfile.setBirthday(0L);
        doctorProfile.setIc("");
        doctorProfile.setSex(DoctorProfile.Gender.other);
        doctorProfile.setMobile(doctorAccount.getMobile());
        doctorProfile.setEmail(doctorAccount.getEmail());
        doctorProfile.setRoles(1);      // 瑙掕壊涓哄钩鍙扮鐞嗗憳
        doctorProfile.setFaceTime(0L);
        doctorProfile.setAddress("");
        doctorProfile.setIntro("");
        doctorProfile.setAuditState(0); // 骞冲彴绠＄悊鏄惁闇?瑕佸鏍?
        doctorProfile.setIsDelete(false);
        doctorProfile.setLastTime(0L);
        doctorProfile.setLoginState(DoctorProfile.LoginState.offLine);
        doctorProfile.setZoneCode("");
        doctorProfile.setPatientNum(0);
        doctorProfile.setOrderNum(0);
        doctorProfile.setCommentNum(0);
        doctorProfile.setCommentScore(0);
        this.save(doctorProfile);

        WebUtil.addCookie("org.username", doctorAccount.getEmail());

        // 鍙戦?佹縺娲婚偖浠?
        DoctorToken token = new DoctorToken();
        token.setId(doctorAccount.getId());
        token.setToken(UUID.randomUUID().toString());
        token.setUpdated((DateUtils.addDays(new Date(), 7).getTime()) / 1000);   // 7澶╁唴鏈夋晥
        doctorTokenService.save(token);
        mailService.sendActiveMail(doctorAccount.getEmail(), doctorAccount.getEmail(), token);
    }

    @Transactional
    @Override
    public void create(DoctorProfile doctorProfile, String account, String password, Boolean isSubmit) {
        // 鍚姩瀹℃壒娴佺▼
        CreateProcessParam processParam = new CreateProcessParam();
        processParam.setWfCode(WfCode.DOCTOR);
        processParam.setIsPush(isSubmit);
        processParam.setDatas(new HashMap<String, String>() {
            {
                if (isSubmit) {
                    put("[WorkItem].result", "common.wf.result.create");
                } else {
                    put("[WorkItem].result", "common.wf.result.save");
                }
            }
        });
        Long guid = processEngine.startProcessInstance(processParam);

        // 鏂板鍖荤敓璐﹀彿淇℃伅
        DoctorAccount doctorAccount = new DoctorAccount();
        doctorAccount.setEmail(account);
        doctorAccount.setMobile(doctorProfile.getMobile());
        doctorAccount.setPassword(DigestUtils.md5Hex(password));
        doctorAccount.setIsDelete(false);
        doctorAccount.setIsActive(false);
        doctorAccountService.save(doctorAccount);

        doctorProfile.setId(doctorAccount.getId());
        doctorProfile.setFullName(doctorProfile.getFirstName() + " " + doctorProfile.getSecondName());
        doctorProfile.setDoctorAccount(doctorAccount);

        if (isSubmit) {
            doctorProfile.setAuditState(1); // 1-寰呭鏍?
        } else {
            doctorProfile.setAuditState(0);  // 寰呮彁浜?
        }
        doctorProfile.setFaceTime(new Date().getTime() / 1000);
        doctorProfile.setIsDelete(false);
        doctorProfile.setLastTime(0L);
        doctorProfile.setLoginState(DoctorProfile.LoginState.offLine);
        doctorProfile.setZoneCode("");
        doctorProfile.setPatientNum(0);
        doctorProfile.setOrderNum(0);
        doctorProfile.setCommentScore(0);
        doctorProfile.setCommentNum(0);
        doctorProfile.setGuid(guid);

        // 璁剧疆鎵?灞炴満鏋?
        DoctorAccount current = doctorAccountService.getCurrent();
        doctorProfile.setOrg(current.getDoctorProfile().getOrg());


        // 淇濆瓨鍖荤敓澶村儚
        if (StringUtils.isNotEmpty(doctorProfile.getHeadPath())) {
            this.saveHeadImg(doctorProfile);
        }
        // 淇濆瓨鍖荤敓璧勬枡
        this.save(doctorProfile);

        // 鐢熸垚鏈烘瀯鍥剧墖
        for (DoctorImage doctorImage : doctorProfile.getDoctorImages()) {
            doctorImageService.build(doctorImage);
            doctorImage.setDoctor(doctorProfile);
            doctorImageService.save(doctorImage);
        }
    }

    @Transactional
    @Override
    public void reCreate(DoctorProfile doctorProfile, String account, String password, Boolean isSubmit) {
        if (isSubmit) {
            // 鎻愪氦娴佺▼
            processEngine.pushProcessInstance(doctorProfile.getGuid(), null, new HashMap<String, String>() {
                {
                    put("[WorkItem].result", "common.wf.result.reCreate");
                }
            });
            doctorProfile.setAuditState(1); // 1-寰呭鏍?
        } else {
            doctorProfile.setAuditState(0); // 0-寰呬慨鏀?
        }

        // 淇濆瓨鍖荤敓澶村儚
        if (StringUtils.isNotEmpty(doctorProfile.getHeadPath())) {
            this.saveHeadImg(doctorProfile);
        }

        doctorProfile.setFullName(doctorProfile.getFirstName() + " " + doctorProfile.getSecondName());
        // 鏇存柊鍖荤敓璐﹀彿淇℃伅
        this.update(doctorProfile, "doctorAccount", "faceTime", "created", "org", "isDelete", "lastTime", "loginState",
                "zoneCode", "patientNum", "orderNum", "commentNum", "commentScore", "guid");

        // 淇敼璐﹀彿淇℃伅
        DoctorAccount doctorAccount = doctorAccountService.find(doctorProfile.getId());
        if (StringUtils.isNotEmpty(password) && !doctorAccount.getPassword().equals(password)) {
            doctorAccount.setPassword(DigestUtils.md5Hex(password));
            doctorAccountService.update(doctorAccount);
        }

        // 鐢熸垚鏈烘瀯鍥剧墖
        doctorImageService.removeImages(doctorProfile.getId());
        for (DoctorImage doctorImage : doctorProfile.getDoctorImages()) {
            doctorImageService.build(doctorImage);
            doctorImage.setDoctor(doctorProfile);
            doctorImageService.save(doctorImage);
        }
    }

    /**
     * 淇濆瓨澶村儚
     * @param doctorProfile 鍖荤敓淇℃伅
     */
    private void saveHeadImg(DoctorProfile doctorProfile) {
        // 淇濆瓨鍖荤敓澶村儚
        if (StringUtils.isNotEmpty(doctorProfile.getHeadPath())) {
            /**
             * 鍖荤敓id鎵╁厖鎴?12浣嶅瓧绗︽偅锛屽10000聽->聽000000010000
             * 鏂囦欢璺緞锛毬?/doctor/00/00/00/01/00/00/10000
             * 缂╃暐璺緞锛?/doctor/00/00/00/01/00/00/10000.thumb  (80X80)
             */
            String leftPad = StringUtils.leftPad(doctorProfile.getId() + "", 12, "0");
            String uploadPath = "/doctor/";
            int start = 0, end = 2;
            while (end <= leftPad.length()) {
                uploadPath += StringUtils.substring(leftPad, start, end) + "/";
                start += 2;
                end += 2;
            }
            String basePath = ConfigUtils.config.getProperty("uploadPath");
            String headPath = uploadPath + doctorProfile.getId();
            String thumbPath  = uploadPath + doctorProfile.getId() + ".thumb";
            // 澶村儚鏃犲彉鏇?
            if (doctorProfile.getHeadPath().equals(headPath)) {
                doctorProfile.setThumbPath(thumbPath);
                return;
            }
            try {
                File source = new File(basePath, doctorProfile.getHeadPath());
                // 澶村儚
                FileUtil.copyFile(source, new File(basePath, headPath));
                // 缂╃暐鍥?
                Multimedia.saveImage(source, basePath + headPath + "-thumb.jpg", 80, 80);
                File thumb = new File(basePath + headPath + "-thumb.jpg");
                FileUtil.copyFile(thumb, new File(basePath, thumbPath));
                FileUtil.deleteQuietly(thumb);
            } catch (IOException e) {
                e.printStackTrace();
                throw new RuntimeException("淇濆瓨澶村儚澶辫触锛?");
            }
            doctorProfile.setHeadPath(headPath);
            doctorProfile.setThumbPath(thumbPath);
        }
    }

    @Transactional
    @Override
    public void audit(Long did, Boolean isPass, String remark) {
        DoctorProfile doctorProfile = this.find(did);
        Long guid = doctorProfile.getGuid();

        if (isPass) {
            processEngine.pushProcessInstance(guid, null, new HashMap<String, String>() {
                {
                    put("[WorkItem].result", "common.wf.result.pass");
                    put("[WorkItem].remark", remark);
                }
            });
            // 鍒ゆ柇娴佺▼鏄惁褰掓。
            WfInst wfInst = wfInstDao.find(guid);
            if (wfInst.getStatus().equals(WfInst.Status.finished)) {
                // 褰掓。鍚庤缃満鏋勫鏍哥姸鎬佷负婵?娲?
                doctorProfile.setAuditState(2);
                // 婵?娲昏处鍙?
                DoctorAccount doctorAccount = doctorProfile.getDoctorAccount();
                doctorAccount.setIsActive(true);
                doctorAccountService.update(doctorAccount);
                // 鏇存柊鏈烘瀯鍖荤敓鏁伴噺
                Organization org = doctorProfile.getOrg();
                org.setDoctorNum(org.getDoctorNum() + 1);
                organizationDao.merge(org);
            }
        } else {
            // 鏃犺鍒濆杩樻槸缁堝锛屽洖閫?鐩存帴鎵撳洖鏈烘瀯鐢宠鐜妭
            Act act = actDao.getAct(WfCode.DOCTOR, "create");
            processEngine.pushProcessInstance(guid, act, null, new HashMap<String, String>() {
                {
                    put("[WorkItem].result", "common.wf.result.noPass");
                    put("[WorkItem].remark", remark);
                }
            });
            // 璁剧疆瀹℃牳鐘舵?佷负寰呮彁浜?
            doctorProfile.setAuditState(0);
        }

        this.update(doctorProfile);
    }

    @Transactional
    @Override
    public void disable(Long[] did) {
        for (Long id : did) {
            // 璁剧疆鍖荤敓淇℃伅涓哄垹闄?
            DoctorProfile doctorProfile = this.find(id);
            doctorProfile.setIsDelete(true);
            this.update(doctorProfile);

            // 璁剧疆鍖荤敓璐﹀彿涓哄垹闄?
            DoctorAccount doctorAccount = doctorAccountService.find(id);
            doctorAccount.setIsDelete(true);
            doctorAccountService.update(doctorAccount);

            // 鍑忓皯鏈烘瀯鍖荤敓鏁伴噺
            Organization org = doctorProfile.getOrg();
            org.setDoctorNum(org.getDoctorNum() - 1);
            organizationDao.merge(org);
        }
    }

    @Transactional
    @Override
    public void devolve(Long[] from, Long to) {
        doctorProfileDao.devolve(from, to);
    }


    @Transactional(readOnly = true)
    @Override
    public List<EnumBean> sel(String name,Long iOrgId,Integer iType) {
        return doctorProfileDao.sel((StringUtils.isNotEmpty(name) ? "%" + name + "%" : name),iOrgId,iType);
    }
}
