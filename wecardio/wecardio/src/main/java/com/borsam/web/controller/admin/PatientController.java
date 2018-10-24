package com.borsam.web.controller.admin;

import com.borsam.plugin.PaymentPlugin;

import com.borsam.plugin.alipayDirect.AlipayDirectPlugin;
import com.borsam.pojo.security.Principal;
import com.borsam.pub.UserType;
import com.borsam.repository.dao.patient.PatientWalletDao;
import com.borsam.repository.entity.doctor.DoctorAccount;
import com.borsam.repository.entity.doctor.DoctorProfile;
import com.borsam.repository.entity.message.MessageInfo;
import com.borsam.repository.entity.org.Organization;
import com.borsam.repository.entity.patient.PatientAccount;
import com.borsam.repository.entity.patient.PatientActivity;
import com.borsam.repository.entity.patient.PatientDoctorOpinion;
import com.borsam.repository.entity.patient.PatientProfile;
import com.borsam.repository.entity.patient.PatientWalletHistory;
import com.borsam.repository.entity.patient.PatientProfile.BindType;
import com.borsam.repository.entity.record.Record.Stoplight;
import com.borsam.repository.entity.service.ServicePackage;
import com.borsam.service.device.GprsService;
import com.borsam.service.doctor.DoctorProfileService;
import com.borsam.service.message.MessageService;
import com.borsam.service.org.OrganizationService;
import com.borsam.service.patient.PatientAccountService;
import com.borsam.service.patient.PatientActivityService;
import com.borsam.service.patient.PatientDoctorOpinionService;
import com.borsam.service.patient.PatientProfileService;
import com.borsam.service.patient.PatientWalletHistoryService;
import com.borsam.service.patient.PatientWalletVerifyService;
import com.borsam.service.pub.PluginService;
import com.borsam.service.pub.SnService;
import com.borsam.service.service.ServicePackageService;
import com.hiteam.common.base.pojo.search.Filter;
import com.hiteam.common.base.pojo.search.Order;
import com.hiteam.common.base.pojo.search.Page;
import com.hiteam.common.base.pojo.search.Pageable;
import com.hiteam.common.service.enums.EnumService;
import com.hiteam.common.util.ConfigUtils;
import com.hiteam.common.util.collections.MapUtil;
import com.hiteam.common.util.http.HttpClientUtils;
import com.hiteam.common.util.json.JsonUtils;
import com.hiteam.common.util.lang.DateUtil;
import com.hiteam.common.util.lang.StringUtil;
import com.hiteam.common.util.pojo.EnumBean;
import com.hiteam.common.web.Message;
import com.hiteam.common.web.WebUtil;
import com.hiteam.common.web.controller.BaseController;
import com.hiteam.common.web.template.freemarker.FreemarkerUtils;

import freemarker.template.TemplateException;

import org.apache.commons.lang.StringUtils;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.persistence.LockModeType;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.math.BigDecimal;
import java.net.URL;
import java.net.URLConnection;
import java.util.*;

/**
 * Controller - 患者管理
 * Created by tantian on 2015/7/7.
 */
@Controller("adminPatientController")
@RequestMapping("/admin/patient")
public class PatientController extends BaseController {
    private Logger log = LoggerFactory.getLogger(PatientController.class);
    
    @Resource(name="gprsServiceImpl")
    private GprsService gprsService;
    
    @Resource(name = "patientProfileServiceImpl")
    private PatientProfileService patientProfileService;

    @Resource(name = "doctorProfileServiceImpl")
    private DoctorProfileService doctorProfileService;

    @Resource(name = "organizationServiceImpl")
    private OrganizationService organizationService;

    @Resource(name = "enumServiceImpl")
    private EnumService enumService;

    @Resource(name = "patientActivityServiceImpl")
    private PatientActivityService patientActivityService;

    @Resource(name = "patientDoctorOpinionServiceImpl")
    private PatientDoctorOpinionService patientDoctorOpinionService;

    @Resource(name = "messageServiceImpl")
    private MessageService messageService;
    
    @Resource(name = "pluginServiceImpl")
    private PluginService pluginService;

    @Resource(name = "patientAccountServiceImpl")
    private PatientAccountService patientAccountService;

    @Resource(name = "patientWalletHistoryServiceImpl")
    private PatientWalletHistoryService patientWalletHistoryService;
    
    @Resource(name = "snServiceImpl")
    private SnService snService;
    
    @Resource(name = "servicePackageServiceImpl")
    private ServicePackageService servicePackageService;
    
    @Resource(name = "patientWalletVerifyServiceImpl")
    private PatientWalletVerifyService patientWalletVerifyService;
    
    @Resource(name = "patientWalletDaoImpl")
    private PatientWalletDao patientWalletDao;
    
    /**
     * 管理主页，返回机构列表页面
     */
    @RequestMapping(method = RequestMethod.GET)
    public String index(Integer iType, Model model) {
        model.addAttribute("iType", iType);
        return "/admin/patient/list";
    }

    /**
     * 详情页面
     */
    @RequestMapping(value = "/{iId}/detail", method = RequestMethod.GET)
    public String detail(@PathVariable Long iId, Integer iType, Model model) {
        Principal principal = getLoginId();
        String userType = principal.getUserType().name();

        model.addAttribute("iType", iType);
        model.addAttribute("userType", userType);
        model.addAttribute("entity", patientProfileService.find(iId));
        return "/admin/patient/detail";
    }

    /**
     * 患者详情页面图表
     * @param patientId 当前患者ID
     * @param type 患者图表数据类型
     * @param timeType 时间类型：1,3,6,12
     * @param selectTime 页面根据时间控件进行选择
     * @param startTime 开始时间
     * @param endTime 结束时间
     * @return
     */
    @RequestMapping(value = "/getData", method = RequestMethod.POST)
    @ResponseBody
    public Map getData(Long patientId, Integer type, Integer timeType,Boolean selectTime,Date startTime,Date endTime) {
        Assert.isTrue(timeType <= 12 && timeType >= 1);
        Assert.isTrue(type >= 1 && type <= 7);
        Assert.notNull(patientId);

        Calendar endCal = Calendar.getInstance();
        //在页面中选择时间段
        if (selectTime) {

            if (endTime == null) {
                endTime = Calendar.getInstance().getTime();
            }

            if (startTime == null) {
                Calendar instance = Calendar.getInstance();
                instance.set(Calendar.MONTH, endTime.getMonth() - 1);
                instance.set(Calendar.DAY_OF_MONTH, 1);
                startTime = instance.getTime();
            }

            endCal.setTime(endTime);
        }

        //结束时间
        Long end = endCal.getTimeInMillis() / 1000;

        Calendar startCal = Calendar.getInstance();
        startCal.setTimeInMillis(endCal.getTimeInMillis());

        //开始时间
        startCal.add(Calendar.MONTH, -timeType);

        if (selectTime) {
            startCal.setTime(startTime);
            Integer monthsBetween = DateUtil.getMonthsBetween(endCal.getTime(), startCal.getTime());
            //根据页面选择的时间月份间隔，设置时间类型
            timeType = monthsBetween <= 1 ? 1 : 6;
        }

        Calendar startCal1 = Calendar.getInstance();
        startCal1.setTimeInMillis(startCal.getTimeInMillis());
//        patientId = 10304L;
        Long start = startCal.getTimeInMillis() / 1000;
        String url = ConfigUtils.config.getProperty("server.url") + "/getpatienttype" + type + "?uid=" + patientId;
        url += ("&start=" + start + "&end=" + end);

        log.info("调用图表数据接口：{}", url);
        System.out.println("url = " + url);

        Map resultMap = new HashMap<>();
        Map data = new HashMap<>();

        try {
            String result = HttpClientUtils.invokeGet(url, null, "UTF-8", 2000);
            resultMap = JsonUtils.toObject(Objects.toString(result,"{}"), Map.class);
            data = JsonUtils.toObject(MapUtil.getString(resultMap, "data", ""), Map.class);
        } catch (Exception e) {
            e.printStackTrace();
        }

        List<Long> dateTime = new ArrayList<>();
        Integer firstMonth = 0;

        switch (timeType) {
            case 1:
                Integer daysBetween = DateUtil.getDaysBetween(endCal.getTime(), startCal.getTime()) + 2;

                for (Integer i = -1; i < daysBetween; i++) {
                    DateUtil.clearTime(startCal);
                    startCal.add(Calendar.DAY_OF_MONTH, i == -1 ? i : 1);
                    dateTime.add(startCal.getTimeInMillis());
                }
                Calendar s1 = Calendar.getInstance();
                s1.setTimeInMillis(startCal.getTimeInMillis());
                break;
            case 3:
            case 6:
            case 12:
                Integer monthsBetween = DateUtil.getMonthsBetween(endCal.getTime(), startCal.getTime()) + 2;
                for (Integer i = -1; i < monthsBetween; i++) {
                    startCal.set(Calendar.DAY_OF_MONTH, 1);
                    startCal.add(Calendar.MONTH, i == -1 ? i : 1);
                    dateTime.add(startCal.getTimeInMillis());
                }

                Calendar s2 = Calendar.getInstance();
                s2.setTimeInMillis(startCal.getTimeInMillis());
                break;
            default:
                break;
        }

        resultMap.put("data", data);
        resultMap.put("timeType", timeType);
        resultMap.put("dateTime", dateTime);

        return resultMap;
    }

    /**
     * 分页查询
     */
    @RequestMapping(value = "/page", method = RequestMethod.POST)
    @ResponseBody
    public Page<PatientProfile> page(String name, String email, String mobile, Integer[] isWalletActive, Integer[] doctor, Long[] org, String iType, Pageable pageable) {
        Page<PatientProfile> page = null;

        List<Filter> filters = new ArrayList<Filter>();
        filters.add(Filter.eq("isDelete", false));  // 不查删除的

        if (StringUtils.isNotEmpty(name)) {
            filters.add(Filter.like("fullName", name));
        }
        if (StringUtils.isNotEmpty(email)) {
            filters.add(Filter.eq("email", email));
        }
        if (StringUtils.isNotEmpty(mobile)) {
            filters.add(Filter.eq("mobile", mobile));
        }
        if (isWalletActive != null && isWalletActive.length > 0) {
            filters.add(Filter.in("isWalletActive", isWalletActive));
        }

        PatientProfile.BindType[] bindTypeArr = new PatientProfile.BindType[2];
        bindTypeArr[0] = PatientProfile.BindType.org;
        bindTypeArr[1] = PatientProfile.BindType.self;
        if (iType != null && iType.equals("1")) {//散户--非机构创建
            filters.add(Filter.ni("bindType", bindTypeArr));
        }
        if (iType != null && iType.equals("2")) {//VIP患者--0-机构创建 1-用户选择
            filters.add(Filter.in("bindType", bindTypeArr));
        }

        if (iType != null && iType.equals("3")) {//VIP患者--邀请页面查询 --不是机构创建并且可以查询已被邀请
            Organization organization = organizationService.find(0l);
            filters.add(Filter.eq("org", organization));
            filters.add(Filter.ne("bindType", PatientProfile.BindType.org));
            filters.add(Filter.ne("bindType", PatientProfile.BindType.self));
        }

        if (doctor != null && doctor.length > 0) {
            filters.add(Filter.in("doctor", doctor));
        }
        if (org != null && org.length > 0) {
            filters.add(Filter.in("org", org));
        }

        if (iType != null && !iType.equals("3")) {//VIP患者--邀请页面查询 --不是机构创建并且没有被邀请
            Principal principal = getLoginId();
            switch (principal.getUserType()) {
                case org:
                    Organization nowOrg = doctorProfileService.find(principal.getId()).getOrg();
                    filters.add(Filter.eq("org", nowOrg));
                    break;
                case doctor:
                	Organization nowOrg_1 = doctorProfileService.find(principal.getId()).getOrg();
                    filters.add(Filter.eq("org", nowOrg_1));
                    DoctorProfile nowDoctorProfile = doctorProfileService.find(principal.getId());
                    if ("2".equals(iType)) {
                        // 高总：审核医生和操作医生能看到所有患者数据，主治医生只能看到自己的VIP患者数据
                        if ((nowDoctorProfile.getRoles() & 2)==2) {
                            filters.add(Filter.eq("doctor", nowDoctorProfile));
                        }
                    } else {
                        filters.add(Filter.eq("doctor", nowDoctorProfile));
                    }
                    break;
                default:
                    break;
            }
        }

        pageable.setFilters(filters);

        List<Order> orders = new ArrayList<Order>();
        orders.add(Order.desc("id"));
        pageable.setOrders(orders);

        page = patientProfileService.findPage(pageable);
        enumService.transformEnum(page.getContent());
        return page;
    }

    /**
     * 分页查询
     */
    @RequestMapping(value = "/queryPatient", method = RequestMethod.POST)
    @ResponseBody
    public List<PatientProfile> queryPatient(String name, String email, String mobile, Integer[] isWalletActive, Integer[] doctor, Long[] org, String iType, Pageable pageable) {
        List<PatientProfile> patientList = null;
        patientList = patientProfileService.queryPatient(name,email,mobile,isWalletActive,doctor,org,iType, pageable);
        enumService.transformEnum(patientList);
        return patientList;
    }

    /**
     * 跳转到客户新增、编辑页
     *
     * @return
     */
    @RequestMapping(value = "/addEdit/{id}", method = RequestMethod.GET)
    public String addEdit(@PathVariable("id") Long id, Integer iType, ModelMap model) {
        PatientProfile entity = patientProfileService.find(id);

        if (entity == null) {
            entity = new PatientProfile();
            Principal principal = getLoginId();
            switch (principal.getUserType()) {
                case org:
                    Organization nowOrg = doctorProfileService.find(principal.getId()).getOrg();
                    entity.setOrg(nowOrg);
                    break;
                case doctor:
                    DoctorProfile nowDoctorProfile = doctorProfileService.find(principal.getId());
                    entity.setOrg(nowDoctorProfile.getOrg());
                    entity.setDoctor(nowDoctorProfile);
                    break;
                default:
                    break;
            }
        }

        model.addAttribute("id", id);
        model.addAttribute("iType", iType);
        model.addAttribute("entity", entity);
        if (entity.getBindType()==BindType.other || entity.getBindType()==BindType.unknow)
        	model.addAttribute("isempty",true);
        else
        	model.addAttribute("isempty",false);
        return "/admin/patient/addEdit";
    }

    /**
     * 检查联系电话是否存在
     */
    @RequestMapping(value = "/checkMobile", method = RequestMethod.GET)
    @ResponseBody
    public boolean checkMobile(String mobile,String iId) {
        boolean result = true;
        if (StringUtils.isEmpty(iId)){
            boolean flag = StringUtils.isNotEmpty(mobile) && patientProfileService.mobileExists(mobile);
            if (flag){
                result = false;
            }
        }
        return result;
    }

    /**
     * 检查Email是否存在
     */
    @RequestMapping(value = "/checkEmail", method = RequestMethod.GET)
    @ResponseBody
    public boolean checkEmail(String emailV,String iId) {
        boolean result = true;
        if (StringUtils.isEmpty(iId)){
            boolean flag = StringUtils.isNotEmpty(emailV) && patientProfileService.emailExists(emailV);
            if (flag){
                result = false;
            }
        }
        return result;
    }
    
    /**
     * 检查Email或电话是否存在 
     */
    @RequestMapping(value = "/checkEmail_Mobile", method = RequestMethod.GET)
    @ResponseBody
    public boolean checkEmail_Mobile(String emailV,String iId) {
        boolean result = true;
        System.out.println("+++++++++++++++"+emailV);
        if (StringUtils.isEmpty(iId)){
            boolean flag = StringUtils.isNotEmpty(emailV) && (patientProfileService.emailExists(emailV)
            		|| patientProfileService.mobileExists(emailV));
            if (flag){
                result = false;
            }
        }
        return result;
    }
    
    /**
     * 编辑医生提交
     */
    @RequestMapping(value = "/save", method = RequestMethod.POST)
    @ResponseBody
    public Message save(PatientProfile patientProfile, Date dateBirthday, Long iId, Long iOrgId, Long iDoctorId, String emailV) {
        if (!StringUtil.checkObj(patientProfile)) {
            patientProfile = new PatientProfile();
        }
        DoctorProfile doctor = doctorProfileService.find(iDoctorId);
        patientProfile.setDoctor(doctor);

        Organization org = organizationService.find(iOrgId);
        Principal principal = getLoginId();
        patientProfile.setOrg(org);

        switch (principal.getUserType()) {//机构创建和医生创建的，bindType都是0-机构创建
            case org:
                patientProfile.setBindType(PatientProfile.BindType.org);
                break;
            case doctor:
                patientProfile.setBindType(PatientProfile.BindType.org);
                break;
            default:
                patientProfile.setBindType(PatientProfile.BindType.org);
                break;
        }

        if (dateBirthday != null) {
            patientProfile.setBirthday(dateBirthday.getTime() / 1000);
        }
        patientProfile.setFullName(patientProfile.getFirstName() + patientProfile.getSecondName());
        patientProfile.setEmail(emailV);
        if (iId != null && iId > 0) {
            patientProfile.setId(iId);

            //先把原来关联的医生患者数还原回去，然后再更新
            DoctorProfile old_DoctorProfile = patientProfileService.find(iId).getDoctor();
            if (old_DoctorProfile != null) {
                Integer patientNum = old_DoctorProfile.getPatientNum();
                patientNum--;
                if (patientNum < 0) {
                    patientNum = 0;
                }
                old_DoctorProfile.setPatientNum(patientNum);
                doctorProfileService.update(old_DoctorProfile);

                DoctorProfile new_DoctorProfile = patientProfile.getDoctor();
                if (new_DoctorProfile != null) {
                    patientNum = new_DoctorProfile.getPatientNum();
                    patientNum++;
                    new_DoctorProfile.setPatientNum(patientNum);
                    doctorProfileService.update(new_DoctorProfile);
                }
            }

            //先把原来关联的机构患者数还原回去，然后再更新
            Organization old_Organization = patientProfileService.find(iId).getOrg();
            if (old_Organization != null) {
                Integer patientNum = old_Organization.getPatientNum();
                patientNum--;
                if (patientNum < 0) {
                    patientNum = 0;
                }
                old_Organization.setPatientNum(patientNum);
                organizationService.update(old_Organization);

                Organization new_Organization = patientProfile.getOrg();
                if (new_Organization != null) {
                    patientNum = new_Organization.getPatientNum();
                    patientNum++;
                    new_Organization.setPatientNum(patientNum);
                    organizationService.update(new_Organization);
                }
            }
            patientProfileService.reCreate(patientProfile, emailV);
        } else {
            //直接更新患者数 start
            Integer patientNum = 0;
            DoctorProfile new_DoctorProfile = patientProfile.getDoctor();
            if (new_DoctorProfile != null) {
                patientNum = new_DoctorProfile.getPatientNum();
                patientNum++;
                new_DoctorProfile.setPatientNum(patientNum);
                doctorProfileService.update(new_DoctorProfile);
            }

            Organization new_Organization = patientProfile.getOrg();
            if (new_Organization != null) {
                patientNum = new_Organization.getPatientNum();
                patientNum++;
                new_Organization.setPatientNum(patientNum);
                organizationService.update(new_Organization);
            }
            //直接更新患者数 end

            patientProfileService.create(patientProfile, emailV);
        }
        return SUCCESS_MSG;
    }

    /**
     * 删除
     */
    @RequestMapping(value = "/delete", method = RequestMethod.POST)
    @ResponseBody
    public Message delete(Long[] ids) {
        if (ids == null || ids.length == 0) {
            return Message.warn("common.message.delete.select");
        }
        patientProfileService.disable(ids);

        for (int i = 0; i < ids.length; i++) {
            DoctorProfile doctorProfile = patientProfileService.find(ids[i]).getDoctor();
            if (doctorProfile != null) {
                Integer patientNum = doctorProfile.getPatientNum();
                patientNum--;
                if (patientNum < 0) {
                    patientNum = 0;
                }
                doctorProfile.setPatientNum(patientNum);
                doctorProfileService.update(doctorProfile);
            }

            Organization org = patientProfileService.find(ids[i]).getOrg();
            if (org != null) {
                Integer patientNum = org.getPatientNum();
                patientNum--;
                if (patientNum < 0) {
                    patientNum = 0;
                }
                org.setPatientNum(patientNum);
                organizationService.update(org);
            }
        }
        return SUCCESS_MSG;
    }

    /**
     * 医嘱页面
     */
    @RequestMapping(value = "/addAdvice", method = RequestMethod.GET)
    public String addAdvice(Long iId, Model model) {
        model.addAttribute("iId", iId);
        PatientProfile patientProfile = patientProfileService.find(iId);
        model.addAttribute("patientProfile", patientProfile);

        return "/admin/patient/addAdvice";
    }

    /**
     * 活动记录页面
     */
    @RequestMapping(value = "/actRecord", method = RequestMethod.GET)
    public String actRecord(Long iId, Model model) {
        model.addAttribute("iId", iId);
        model.addAttribute("dateStartTime", StringUtil.getNowDate());

        PatientProfile patientProfile = patientProfileService.find(iId);
        model.addAttribute("patientProfile", patientProfile);
        return "/admin/patient/actRecord";
    }

    /**
     * 保存医嘱
     */
    @RequestMapping(value = "/saveAdvice", method = RequestMethod.POST)
    @ResponseBody
    public Message saveAdvice(PatientDoctorOpinion patientDoctorOpinion, Long iId) {
        PatientProfile patientProfile = patientProfileService.find(iId);

        if (patientDoctorOpinion == null) {
            patientDoctorOpinion = new PatientDoctorOpinion();
        }
        patientDoctorOpinion.setReceive_id(patientProfile);

        DoctorProfile doctorProfile = null;
        Principal principal = getLoginId();
        switch (principal.getUserType()) {
            case doctor:
                doctorProfile = doctorProfileService.find(principal.getId());
                patientDoctorOpinion.setSend_id(doctorProfile);
                break;
            default:
//                临时设置
                doctorProfile = doctorProfileService.find(10017L);
                patientDoctorOpinion.setSend_id(doctorProfile);
                break;
        }
        patientDoctorOpinionService.save(patientDoctorOpinion);
        return SUCCESS_MSG;
    }

    /**
     * 保存活动记录
     */
    @RequestMapping(value = "/saveRecord", method = RequestMethod.POST)
    @ResponseBody
    public Message saveRecord(PatientActivity patientActivity, Date dateStartTime, Long iId) {
        PatientProfile patientProfile = patientProfileService.find(iId);
        if (patientActivity == null) {
            patientActivity = new PatientActivity();
        }
        patientActivity.setUid(patientProfile);
        if (dateStartTime != null) {
            patientActivity.setStart_time(dateStartTime.getTime() / 1000);
        } else {
            patientActivity.setStart_time(new Date().getTime() / 1000);
        }
        UserType recordType0=UserType.admin;//患者创建 0，医生创建 1；
        UserType recordType1=UserType.org;
        Principal principal = getLoginId();
        switch (principal.getUserType()) {
            case org:
                Organization nowOrg = doctorProfileService.find(principal.getId()).getOrg();
                patientActivity.setCreate_name(principal.getName());
                patientActivity.setCreate_id(nowOrg.getId());
                patientActivity.setType(recordType1);
                break;
            case doctor:
                DoctorProfile doctorProfile = doctorProfileService.find(principal.getId());
                patientActivity.setCreate_name(principal.getName());
                patientActivity.setCreate_id(doctorProfile.getId());
                patientActivity.setHeadPath(doctorProfile.getHeadPath());
                patientActivity.setType(recordType1);
                break;
            case patient:
                PatientProfile createPatientProfile = patientProfileService.find(principal.getId());
                patientActivity.setCreate_name(principal.getName());
                patientActivity.setCreate_id(createPatientProfile.getId());
                patientActivity.setHeadPath(createPatientProfile.getHeadPath());
                patientActivity.setType(recordType0);
                break;
            default:
                patientActivity.setCreate_name(principal.getName());
                patientActivity.setCreate_id(principal.getId());
                patientActivity.setType(recordType1);
                break;
        }
        patientActivityService.save(patientActivity);
        return SUCCESS_MSG;
    }

    /**
     * 当前登录人
     *
     * @return Long
     */
    public Principal getLoginId() {
        Subject subject = SecurityUtils.getSubject();
        Principal principal = (Principal) subject.getPrincipal();
        return principal;
    }

    /**
     * 分页查询
     */
    @RequestMapping(value = "/queryAdvice", method = RequestMethod.POST)
    @ResponseBody
    public Page<PatientDoctorOpinion> queryAdvice(Long iId, Pageable pageable) {
        Page<PatientDoctorOpinion> page = null;

        List<Filter> filters = new ArrayList<Filter>();
        PatientProfile patientProfile = patientProfileService.find(iId);
        if (patientProfile != null) {
            filters.add(Filter.eq("receive_id", patientProfile));
        } else {
            filters.add(Filter.isNull("receive_id"));
        }
        pageable.setFilters(filters);

        List<Order> orders = new ArrayList<Order>();
        orders.add(Order.desc("id"));
        pageable.setOrders(orders);

        page = patientDoctorOpinionService.findPage(pageable);
        return page;
    }

    /**
     * 分页查询
     */
    @RequestMapping(value = "/queryRecord", method = RequestMethod.POST)
    @ResponseBody
    public Page<PatientActivity> queryRecord(Long iId, Pageable pageable) {
        Page<PatientActivity> page = null;

        List<Filter> filters = new ArrayList<Filter>();
        PatientProfile patientProfile = patientProfileService.find(iId);
        if (patientProfile != null) {
            filters.add(Filter.eq("uid", patientProfile));
        } else {
            filters.add(Filter.isNull("uid"));
        }
        pageable.setFilters(filters);

        List<Order> orders = new ArrayList<Order>();
        orders.add(Order.desc("id"));
        pageable.setOrders(orders);

        page = patientActivityService.findPage(pageable);
        return page;
    }

    /**
     * 患者列表（for 下拉框）
     */
    @RequestMapping(value = "/sel", method = RequestMethod.POST)
    @ResponseBody
    public List<EnumBean> sel(Integer iType, String name) {
        return patientProfileService.sel(null,null,iType, name);
    }

    /**
     * 跳转到客户新增、编辑页
     *
     * @return
     */
    @RequestMapping(value = "/invite")
    public String invite(ModelMap model) {
        return "/admin/patient/invite";
    }

    /**
     * 解除绑定
     */
    @RequestMapping(value = "/releaseBinding", method = RequestMethod.POST)
    @ResponseBody
    @Transactional
    public Message releaseBinding(Long[] ids) {
        for (int i = 0; i < ids.length; i++) {
            DoctorProfile doctorProfile = patientProfileService.find(ids[i]).getDoctor();
            if (doctorProfile != null) {
                Integer patientNum = doctorProfile.getPatientNum();
                patientNum--;
                if (patientNum < 0) {
                    patientNum = 0;
                }
                doctorProfile.setPatientNum(patientNum);
                doctorProfileService.update(doctorProfile);
            }

            Organization org = patientProfileService.find(ids[i]).getOrg();
            if (org != null) {
                Integer patientNum = org.getPatientNum();
                patientNum--;
                if (patientNum < 0) {
                    patientNum = 0;
                }
                org.setPatientNum(patientNum);
                organizationService.update(org);
            }

            MessageInfo newMessage = new MessageInfo();
            //添加解除绑定的消息 start
            Principal principal = getLoginId();

            switch (principal.getUserType()) {
                case org:
                    Organization nowOrg = doctorProfileService.find(principal.getId()).getOrg();
                    newMessage.setFrom_name(principal.getName());
                    newMessage.setFrom_id(nowOrg.getId());
                    newMessage.setFrom_type(principal.getUserType());
                    newMessage.setType(MessageInfo.MessageType.doctorRelievePatientMessage);
                    newMessage.setContent("message.template.key8");
                    break;
                case doctor:
                    DoctorProfile doctor = doctorProfileService.find(principal.getId());
                    newMessage.setFrom_name(principal.getName());
                    newMessage.setFrom_id(doctor.getId());
                    newMessage.setFrom_type(principal.getUserType());
                    newMessage.setType(MessageInfo.MessageType.doctorRelievePatientMessage);
                    newMessage.setContent("message.template.key8");
                    break;
                case patient:
                    newMessage.setFrom_name(principal.getName());
                    newMessage.setFrom_id(principal.getId());
                    newMessage.setFrom_type(principal.getUserType());
                    newMessage.setType(MessageInfo.MessageType.patientRelievePatientMessage);
                    newMessage.setContent("message.template.key9");
                    break;
                default:
                    newMessage.setFrom_name(principal.getName());
                    newMessage.setFrom_id(principal.getId());
                    newMessage.setFrom_type(principal.getUserType());
                    newMessage.setType(MessageInfo.MessageType.doctorRelievePatientMessage);
                    newMessage.setContent("message.template.key8");
                    break;
            }

            //更新绑定状态
            PatientProfile patientProfile = patientProfileService.find(ids[i]);
            patientProfile.setBindType(PatientProfile.BindType.other);
            patientProfile.setOrg(null);
            patientProfile.setDoctor(null);
            patientProfileService.update(patientProfile);

            newMessage.setTo_id(patientProfile.getId());
            newMessage.setTo_name(patientProfile.getFullName());
            newMessage.setTo_type(UserType.patient);
            newMessage.setRecevied(0);
            newMessage.setRecevied_time(-1l);
            newMessage.setiDeal(0);
            messageService.save(newMessage);
            //添加解除绑定的消息 end
        }

        return SUCCESS_MSG;
    }
    
    /**
     * 患者绑定设备holter620下载
     */
    @RequestMapping(value = "/bind620/download")
    public String download(Long pId, HttpServletRequest request, HttpServletResponse response) throws Exception {
        System.out.println(pId);
    	String bindPath = bindUrl(pId);
        String filename;
        filename = "PATLABEL.TXT";
        URL url = new URL(bindPath);
        try {
            URLConnection conn = url.openConnection();
            InputStream is = conn.getInputStream();
            OutputStream out = response.getOutputStream();

            byte[] buf = new byte[1024];
            int len;
            response.reset();
            response.setContentType("APPLICATION/OCTET-STREAM");
            response.addHeader("Content-Disposition", "attachment; filename=" + WebUtil.encodingFileName(filename, request));
//            response.addHeader("Content-Length", "" + file.length());
            response.setContentType("application/octet-stream;charset=UTF-8");
            while ((len = is.read(buf)) > 0) {
                out.write(buf, 0, len);
                response.flushBuffer();
            }
            is.close();
            out.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
    /**
     * 620设备绑定页面
     */
    @RequestMapping(value = "/bind620", method = RequestMethod.GET)
     public String bind620(Long pId,Model model){
    	//查询后台是否已经绑定
    	boolean isBind=true;
    	System.out.println("1111111"+pId);
    	isBind=gprsService.isBind(patientProfileService.find(pId));
    	model.addAttribute("pId", pId);
    	model.addAttribute("isBind",isBind);
    	return "/admin/patient/bind620";
    }
    
    /**
     * 获取绑定设备下载地址holter620
     */
    @ResponseBody
     public String bindUrl(Long pid){
    	String uid=String.valueOf(pid);
    	Map<String,String> params=new HashMap<String,String>();
    	params.put("user_id", uid);
    	params.put("type", "6");
    	String fileUrl=null;
		fileUrl = ConfigUtils.config.getProperty("server.url") + "/web/device/file";
		String result=HttpClientUtils.invokeGet(fileUrl,params,"utf-8", 5000);
    	Map json = JsonUtils.toObject(result, Map.class);
     	//获取端口返回json数据中的网址
     	return  (String) ((Map) json).get("file_url");
    }
    
    /**
     * 三导设备绑定页面
     */
    @RequestMapping(value = "/lead3bind", method = RequestMethod.GET)
     public String lead3bind(Long pId,Model model){
    	System.out.println("1111111"+pId);
    	model.addAttribute("pId", pId);
    	return "/admin/patient/lead3bind";
    }
    
    /**
     * 三导设备绑定提交
     */
    @RequestMapping(value="/lead3bind/submit",method=RequestMethod.POST)
    @ResponseBody
     public Message toeditSubmit(Long pId,Long code){
    	Map<String, Object> params = new HashMap<String, Object>();
		params.put("code", code);
		String result = HttpClientUtils.postJsonBody(
				ConfigUtils.config.getProperty("server.url") + "/web/device/bind?user_id="+pId, 5000, params, "utf-8");
		Map<String, Object> json = JsonUtils.toObject(result, Map.class);
		// 获取端口返回json数据中的网址
		int datajson = (int) json.get("code");
		if (datajson==0)
			return SUCCESS_MSG;
		Message msgerro=new Message(null,(String) json.get("msg"));
		return msgerro;
    } 
    
    /**
     * holter上传文件页面
     */
    @RequestMapping(value = "/holter", method = RequestMethod.GET)
     public String holterUpload(Long pId,Long imeId,Model model){
    	if (pId!=null)
    		model.addAttribute("pId", pId);
    	if(imeId!=null && gprsService.find(imeId).getPatient()!=null)
    		model.addAttribute("pId", gprsService.find(imeId).getPatient().getId());
    	return "/admin/patient/holter";
    }
    
    /**
     * 代充服务包页面
     */
    @RequestMapping(value = "/daichong", method = RequestMethod.GET)
    public String dcPackage(Long pId,Model model){
   	model.addAttribute("pId", pId);
   	PatientProfile patientProfile=patientProfileService.find(pId);
   	model.addAttribute("patientName",patientProfile.getName());
   	return "/admin/patient/daichong";
    }
    
    /**
     * 代充支付页面(先充值到患者用户，在从用户账户扣费)
     */
    @Transactional
    @RequestMapping(value = "/payment/submit", method = RequestMethod.POST)
    public String submit(Long sid,Long pId,PatientWalletHistory.Type type, String paymentPluginId, BigDecimal amount, HttpServletRequest request, HttpServletResponse response, ModelMap model){
    	   PatientAccount patientAccount = patientAccountService.find(pId);
    	   DoctorProfile doctorProfile=patientProfileService.find(pId).getDoctor();
    	   ServicePackage servicePackage=servicePackageService.find(sid);
           if (patientAccount == null) {
               return ERROR_VIEW;
           }
           PaymentPlugin paymentPlugin = pluginService.getPaymentPlugin(paymentPluginId);
           if (paymentPlugin == null || !paymentPlugin.isEnabled()) {
               return ERROR_VIEW;
           }
           PatientWalletHistory history = new PatientWalletHistory();
           String description = null;
           String returnUrl = null;
           String notifyUrl = null;
           //生成充值记录
           if (type == PatientWalletHistory.Type.recharge) {
               if (amount == null || amount.compareTo(new BigDecimal(0)) <= 0 || amount.precision() > 15 || amount.scale() > 2) {
                   return ERROR_VIEW;
               }
               history.setPayNo("");
               history.setType(PatientWalletHistory.Type.daichong);
               history.setSuccess(0);  // 0-等待返回结果
               history.setPayStyle(1); // TODO 支付类型枚举
               history.setMoney(paymentPlugin.calculateAmount(amount));
               history.setUid(patientAccount.getId());
               history.setVerdict(sid+"");
               history.setOid(0L);
               history.setTradeNo("");
               history.setDcDid(doctorProfile.getId());
               history.setDefinition(servicePackage.getTitle());
               
               patientWalletHistoryService.save(history);
               // 生成交易号
               history.setTradeNo(snService.generate(history.getId()));
               patientWalletHistoryService.update(history);
               description = message("patient.wallet.recharge.title");

               returnUrl = ConfigUtils.config.getProperty("siteUrl") + "/doctor/patient/payment/notify/" + PaymentPlugin.NotifyMethod.sync + "/" + history.getTradeNo();
               notifyUrl = ConfigUtils.config.getProperty("siteUrl") + "/doctor/patient/payment/notify/" + PaymentPlugin.NotifyMethod.async + "/" + history.getTradeNo();
               
               request.setAttribute("return_url", returnUrl);
               request.setAttribute("notify_url", notifyUrl);
           } else {
               return ERROR_VIEW;
           }

           Map<String, Object> parameterMap = paymentPlugin.getParameterMap(history.getTradeNo(), history.getMoney(), description, request);
           model.addAttribute("requestUrl", paymentPlugin.getRequestUrl());
           model.addAttribute("requestMethod", paymentPlugin.getRequestMethod());
           model.addAttribute("requestCharset", paymentPlugin.getRequestCharset());
           model.addAttribute("parameterMap", parameterMap);

           if (StringUtils.isNotEmpty(paymentPlugin.getRequestCharset())) {
               response.setContentType("text/html; charset=" + paymentPlugin.getRequestCharset());
           }
         //判断患者钱包是否激活（默认激活：密码123456）
           if (patientWalletDao.find(pId, LockModeType.PESSIMISTIC_WRITE)==null)
        	   patientWalletVerifyService.activate(patientAccount.getId(), "123456");
           
           return "/admin/patient/payment/submit";
    }

          /**
            * 通知(同步）
            */
    @RequestMapping("/payment/notify/sync/{sn}")
    public String syncNotify( @PathVariable String sn, HttpServletRequest request, ModelMap model) {
       PatientWalletHistory history = patientWalletHistoryService.findBySn(sn);
           if (history != null) {
                  // 支付宝交易号
                  history.setPayNo(request.getParameter("trade_no"));
                  patientWalletHistoryService.update(history);
                  // TODO 历史表应该添加字段保存支付插件的ID，这里先默认使用支付宝即时交易，待后续扩展
                  PaymentPlugin paymentPlugin = pluginService.getPaymentPlugin(AlipayDirectPlugin.class.getAnnotation(Component.class).value());
                  if (paymentPlugin != null) {
                       if (paymentPlugin.verifyNotify(sn, history.getMoney(), PaymentPlugin.NotifyMethod.sync, request)) {
                           patientWalletHistoryService.handle(history);
                       }
                       model.addAttribute("notifyMessage", paymentPlugin.getNotifyMessage(sn, PaymentPlugin.NotifyMethod.sync, request));
                   }
                  model.addAttribute("payment", history);
                  //判断是否为医生代充
                  Long sid=Long.parseLong(history.getVerdict());
                  if (history.getDcDid()!=null && sid!=null){
                	  ServicePackage servicePackage=servicePackageService.find(sid);
                	  Message message = servicePackageService.pay(servicePackage, history.getUid(),history.getDcDid());
                  }
               }
        return "/admin/patient/payment/notify";
    }
        
        /**
         * 通知(异步)
         */
     @RequestMapping("/payment/notify/async/{sn}")
       public String asyncNotify( @PathVariable String sn, HttpServletRequest request, ModelMap model) {
          PatientWalletHistory history = patientWalletHistoryService.findBySn(sn);
          if (history != null) {
               // 支付宝交易号
               history.setPayNo(request.getParameter("trade_no"));
               patientWalletHistoryService.update(history);
               // TODO 历史表应该添加字段保存支付插件的ID，这里先默认使用支付宝即时交易，待后续扩展
               PaymentPlugin paymentPlugin = pluginService.getPaymentPlugin(AlipayDirectPlugin.class.getAnnotation(Component.class).value());
               if (paymentPlugin != null) {
                    if (paymentPlugin.verifyNotify(sn, history.getMoney(), PaymentPlugin.NotifyMethod.async, request)) {
                        patientWalletHistoryService.handle(history);
                    }
                    model.addAttribute("notifyMessage", paymentPlugin.getNotifyMessage(sn, PaymentPlugin.NotifyMethod.async, request));
                }
               model.addAttribute("payment", history);
               //判断是否为医生代充
               Long sid=Long.parseLong(history.getVerdict());
               if (history.getDcDid()!=null && sid!=null){
             	  ServicePackage servicePackage=servicePackageService.find(sid);
             	  Message message = servicePackageService.pay(servicePackage, history.getUid(),history.getDcDid());
               }
            }
            return "success";
 	}
}
