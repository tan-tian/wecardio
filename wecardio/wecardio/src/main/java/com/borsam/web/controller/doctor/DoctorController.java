package com.borsam.web.controller.doctor;

import com.borsam.pojo.security.Principal;
import com.borsam.repository.entity.doctor.DoctorProfile;
import com.borsam.service.doctor.DoctorProfileService;
import com.hiteam.common.util.ConfigUtils;
import com.hiteam.common.util.Multimedia;
import com.hiteam.common.util.io.FileUtil;
import com.hiteam.common.util.pojo.EnumBean;
import com.hiteam.common.web.Message;
import com.hiteam.common.web.controller.BaseController;
import org.apache.commons.lang.StringUtils;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import java.io.File;
import java.io.IOException;
import java.util.Date;
import java.util.List;

/**
 * Controller - 鍖荤敓绠＄悊
 * Created by Sebarswee on 2015/7/15.
 */
@Controller("doctorDoctorController")
@RequestMapping(value = "/doctor/doctor")
public class DoctorController extends BaseController {

    @Resource(name = "doctorProfileServiceImpl")
    private DoctorProfileService doctorProfileService;
    
    /**
     * 鍖荤敓鍒楄〃锛坒or 涓嬫媺妗嗭級
     */
    @RequestMapping(value = "/sel", method = RequestMethod.POST)
    @ResponseBody
    public List<EnumBean> sel(String name,Long iOrgId,Integer iType) {
    	Principal principal = getLoginId();
    	DoctorProfile doctorProfile2 = doctorProfileService.find(principal.getId());
    	iOrgId = doctorProfile2.getOrg().getId();
    	return doctorProfileService.sel(name,iOrgId,iType);
    }

    /**
     * 缂栬緫椤甸潰
     */
    @RequestMapping(value = "/{did}/edit", method = RequestMethod.GET)
    public String edit(@PathVariable Long did, Model model) {
        model.addAttribute("doctor", doctorProfileService.find(did));
        return "/doctor/doctor/edit";
    }

    /**
     * 鏇存柊
     */
    @RequestMapping(value = "/update", method = RequestMethod.POST)
    @ResponseBody
    public Message update(DoctorProfile doctorProfile) {
        DoctorProfile doctor = doctorProfileService.find(doctorProfile.getId());
        doctor.setEmail(doctorProfile.getEmail());
        doctor.setMobile(doctorProfile.getMobile());
        doctor.setIntro(doctorProfile.getIntro());
        doctor.setAddress(doctorProfile.getAddress());
        doctor.setNationCode(doctorProfile.getNationCode());
        doctor.setNationName(doctorProfile.getNationName());
        doctor.setProvinceCode(doctorProfile.getProvinceCode());
        doctor.setProvinceName(doctorProfile.getProvinceName());
        doctor.setCityCode(doctorProfile.getCityCode());
        doctor.setCityName(doctorProfile.getCityName());
        if (StringUtils.isNotEmpty(doctorProfile.getHeadPath())) {
            doctor.setHeadPath(doctorProfile.getHeadPath());
            this.saveHeadImg(doctor);
        }
        doctorProfileService.update(doctor);
        return SUCCESS_MSG;
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
            doctorProfile.setFaceTime(new Date().getTime() / 1000);
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
}
