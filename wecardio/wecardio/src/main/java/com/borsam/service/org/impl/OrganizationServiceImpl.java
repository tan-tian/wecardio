package com.borsam.service.org.impl;

import com.borsam.pojo.wf.CreateProcessParam;
import com.borsam.pojo.wf.WfCode;
import com.borsam.repository.dao.doctor.DoctorProfileDao;
import com.borsam.repository.dao.org.OrganizationDao;
import com.borsam.repository.dao.wf.ActDao;
import com.borsam.repository.dao.wf.WfInstDao;
import com.borsam.repository.entity.doctor.DoctorAccount;
import com.borsam.repository.entity.doctor.DoctorProfile;
import com.borsam.repository.entity.org.Organization;
import com.borsam.repository.entity.org.OrganizationDoctorImage;
import com.borsam.repository.entity.org.OrganizationImage;
import com.borsam.repository.entity.wf.Act;
import com.borsam.repository.entity.wf.WfInst;
import com.borsam.service.doctor.DoctorAccountService;
import com.borsam.service.org.OrganizationDoctorImageService;
import com.borsam.service.org.OrganizationImageService;
import com.borsam.service.org.OrganizationService;
import com.borsam.service.wf.ProcessEngine;
import com.hiteam.common.base.service.impl.BaseServiceImpl;
import com.hiteam.common.util.ConfigUtils;
import com.hiteam.common.util.Multimedia;
import com.hiteam.common.util.io.FileUtil;
import com.hiteam.common.util.pojo.EnumBean;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang3.ArrayUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;

/**
 * Service - 鏈烘瀯淇℃伅
 * Created by tantian on 2015/7/1.
 */
@Service("organizationServiceImpl")
public class OrganizationServiceImpl extends BaseServiceImpl<Organization, Long> implements OrganizationService {

    @Resource(name = "organizationImageServiceImpl")
    private OrganizationImageService organizationImageService;

    @Resource(name = "organizationDoctorImageServiceImpl")
    private OrganizationDoctorImageService organizationDoctorImageService;

    @Resource(name = "doctorAccountServiceImpl")
    private DoctorAccountService doctorAccountService;

    @Resource(name = "processEngineImpl")
    private ProcessEngine processEngine;

    @Resource(name = "wfInstDaoImpl")
    private WfInstDao wfInstDao;

    @Resource(name = "actDaoImpl")
    private ActDao actDao;

    @Resource(name = "doctorProfileDaoImpl")
    private DoctorProfileDao doctorProfileDao;

    @Resource(name = "organizationDaoImpl")
    private OrganizationDao organizationDao;

    @Resource(name = "organizationDaoImpl")
    public void setBaseDao(OrganizationDao organizationDao) {
        super.setBaseDao(organizationDao);
    }

    @Transactional
    @Override
    public void create(Organization organization, Boolean isSubmit) {
        // 鍚姩瀹℃壒娴佺▼
        CreateProcessParam processParam = new CreateProcessParam();
        processParam.setWfCode(WfCode.ORG);
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

        if (isSubmit) {
            organization.setAuditState(0);  // 寰呭鏍�
        } else {
            organization.setAuditState(1);  // 寰呮彁浜�
        }
        if (organization.getIsSign() == null) {
            organization.setIsSign(false);
        }
        organization.setIsWalletActive(false);
        organization.setFaceTime(0L);
        organization.setPic("");
        organization.setDoctorPic("");
        organization.setRate(new BigDecimal(1));
        organization.setDoctorNum(0);
        organization.setOrderMoney(new BigDecimal(0));
        organization.setPatientNum(0);
        organization.setOrderNum(0);
        organization.setServiceNum(0);
        organization.setCommentNum(0);
        organization.setCommentScore(0);
        organization.setGuid(guid);
        // 淇濆瓨鏈烘瀯淇℃伅
        this.save(organization);

        // 鐢熸垚鏈烘瀯鍥剧墖
        for (OrganizationImage organizationImage : organization.getOrgImages()) {
            organizationImageService.build(organizationImage);
            organizationImage.setOrg(organization);
            organizationImageService.save(organizationImage);
        }
        // 鐢熸垚鍖荤敓璇佷功鍥剧墖
        for (OrganizationDoctorImage doctorImage : organization.getDoctorImages()) {
            organizationDoctorImageService.build(doctorImage);
            doctorImage.setOrg(organization);
            organizationDoctorImageService.save(doctorImage);
        }

        // 璁剧疆鏈烘瀯绠＄悊鍛樻墍灞炴満鏋�
        DoctorAccount doctorAccount = doctorAccountService.getCurrent();
        DoctorProfile doctorProfile = doctorAccount.getDoctorProfile();
        doctorProfile.setOrg(organization);
        // 涓汉鏈烘瀯锛岄渶瑕佸鍒跺熀鏈俊鎭埌鍖荤敓淇℃伅涓�
        if (Organization.Type.personal.equals(organization.getType())) {
            this.copyProfile(organization, doctorProfile);
        }
        doctorProfileDao.merge(doctorProfile);

        // 鏇存柊灏侀潰鍥剧墖
        if (organization.getOrgImages() != null && !organization.getOrgImages().isEmpty()) {
            organization.setHeadpidId(organization.getOrgImages().get(0).getId());
            this.update(organization);
        }
    }

    @Transactional
    @Override
    public void reCreate(Organization organization, Boolean isSubmit) {
        // 鎻愪氦瀹℃壒娴佺▼
        if (isSubmit) {
            processEngine.pushProcessInstance(organization.getGuid(), null, new HashMap<String, String>() {
                {
                    put("[WorkItem].result", "common.wf.result.reCreate");
                }
            });
        }

        String[] ignores = { "faceTime", "pic", "doctorPic", "isWalletActive", "rate", "doctorNum", "orderMoney",
                "patientNum", "orderNum", "serviceNum", "commentNum", "commentScore", "guid", "created", "modify" };
        if (isSubmit) {
            organization.setAuditState(0);  // 寰呭鏍�
        } else {
            ignores = ArrayUtils.addAll(ignores, "auditState");
        }
        if (organization.getIsSign() == null) {
            organization.setIsSign(false);
        }

        // 淇濆瓨鏈烘瀯淇℃伅
        organization = this.update(organization, ignores);

        // 鐢熸垚鏈烘瀯鍥剧墖
        organizationImageService.removeImages(organization.getId());
        for (OrganizationImage organizationImage : organization.getOrgImages()) {
            organizationImageService.build(organizationImage);
            organizationImage.setOrg(organization);
            organizationImageService.save(organizationImage);
        }
        // 鐢熸垚鍖荤敓璇佷功鍥剧墖
        organizationDoctorImageService.removeImages(organization.getId());
        for (OrganizationDoctorImage doctorImage : organization.getDoctorImages()) {
            organizationDoctorImageService.build(doctorImage);
            doctorImage.setOrg(organization);
            organizationDoctorImageService.save(doctorImage);
        }
        // 鏇存柊灏侀潰鍥剧墖
        if (organization.getOrgImages() != null && !organization.getOrgImages().isEmpty()) {
            organization.setHeadpidId(organization.getOrgImages().get(0).getId());
            this.update(organization);
        }

        // 涓汉鏈烘瀯锛岄渶瑕佸鍒跺熀鏈俊鎭埌鍖荤敓淇℃伅涓�
        if (Organization.Type.personal.equals(organization.getType())) {
            DoctorAccount doctorAccount = doctorAccountService.getCurrent();
            DoctorProfile doctorProfile = doctorAccount.getDoctorProfile();
            this.copyProfile(organization, doctorProfile);
            doctorProfileDao.merge(doctorProfile);
        }
    }

    /**
     * 澶嶅埗鏈烘瀯娉曚汉淇℃伅鍒板尰鐢熶俊鎭�
     * @param org 鏈烘瀯
     * @param doctor 鍖荤敓
     */
    private void copyProfile(Organization org, DoctorProfile doctor) {
        doctor.setFirstName(org.getFirstName());
        doctor.setSecondName(org.getSecondName());
        doctor.setFullName(org.getFirstName() + " " + org.getSecondName());
        doctor.setIc(org.getIc());
        doctor.setSex(DoctorProfile.Gender.valueOf(org.getSex().name()));
        doctor.setBirthday(org.getBirthdate());
        doctor.setIntro(org.getIntro());
        doctor.setNationCode(org.getNationCode());
        doctor.setNationName(org.getNationName());
        doctor.setProvinceCode(org.getProvinceCode());
        doctor.setProvinceName(org.getProvinceName());
        doctor.setCityCode(org.getCityCode());
        doctor.setCityName(org.getCityName());
        doctor.setRegionCode(org.getRegionCode());
        doctor.setRegionName(org.getRegionName());
        doctor.setPostCode(org.getPostCode());
        doctor.setZoneCode(org.getZoneCode());
        doctor.setAddress(org.getAddress());
        doctor.setRoles(3);
        // 璁剧疆澶村儚锛屽彇鏈烘瀯鐨勭涓�寮犲浘鐗�
        if (org.getOrgImages() != null && !org.getOrgImages().isEmpty()) {
            OrganizationImage organizationImage = org.getOrgImages().get(0);
            doctor.setHeadPath(organizationImage.getLarge());
            // 娉ㄦ剰锛氱敱浜庢満鏋勫浘鐗囩殑鐢熸垚鏄娇鐢⊿pring task鍦ㄥ悗鍙扮敓鎴愮殑锛屼細瀛樺湪鍥剧墖璺緞鎵句笉鍒扮殑闂锛堝凡鍙栨秷task锛侊紒锛�
            this.saveHeadImg(doctor);
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
             * 鍖荤敓id鎵╁厖鎴�12浣嶅瓧绗︽偅锛屽10000聽->聽000000010000
             * 鏂囦欢璺緞锛毬�/doctor/00/00/00/01/00/00/10000
             * 缂╃暐璺緞锛�/doctor/00/00/00/01/00/00/10000.thumb  (80X80)
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
            // 澶村儚鏃犲彉鏇�
            if (doctorProfile.getHeadPath().equals(headPath)) {
                doctorProfile.setThumbPath(thumbPath);
                return;
            }
            try {
                File source = new File(basePath, doctorProfile.getHeadPath());
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
            doctorProfile.setHeadPath(headPath);
            doctorProfile.setThumbPath(thumbPath);
        }
    }

    @Transactional
    @Override
    public void audit(Long oid, Boolean isPass, String remark) {
        Organization organization = this.find(oid);
        Long guid = organization.getGuid();

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
                // 褰掓。鍚庤缃満鏋勫鏍哥姸鎬佷负婵�娲�
                organization.setAuditState(2);
                // 璁剧疆鏈烘瀯绠＄悊鍛樺鏍哥姸鎬佷负婵�娲�
                List<DoctorProfile> adminDoctors = doctorProfileDao.findAdminDoctor(oid);
                if (adminDoctors != null && !adminDoctors.isEmpty()) {
                    for (DoctorProfile adminDoctor : adminDoctors) {
                        adminDoctor.setAuditState(2);
                    }
                    doctorProfileDao.merge(adminDoctors);
                }
            }
        } else {
            // 鏃犺鍒濆杩樻槸缁堝锛屽洖閫�鐩存帴鎵撳洖鏈烘瀯鐢宠鐜妭
            Act act = actDao.getAct(WfCode.ORG, "create");
            processEngine.pushProcessInstance(guid, act, null, new HashMap<String, String>() {
                {
                    put("[WorkItem].result", "common.wf.result.noPass");
                    put("[WorkItem].remark", remark);
                }
            });
            // 璁剧疆瀹℃牳鐘舵�佷负寰呮彁浜�
            organization.setAuditState(1);
        }

        this.update(organization);
    }

    @Transactional(readOnly = true)
    @Override
    public List<EnumBean> sel(String name) {
        return organizationDao.sel((StringUtils.isNotEmpty(name) ? "%" + name + "%" : name));
    }
    
    @Override
    public String getAccount(Long oid) {
        List<DoctorProfile> doctorProfiles = doctorProfileDao.findAdminDoctor(oid);
        if (doctorProfiles != null && !doctorProfiles.isEmpty()) {
            DoctorProfile doctorProfile = doctorProfiles.get(0);
            return doctorProfile.getAccount();
        } else {
            return null;
        }
    }

	
}
