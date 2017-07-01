package com.borsam.web.controller.doctor;

import com.borsam.pojo.security.Principal;
import com.borsam.pojo.wf.WfCode;
import com.borsam.pub.UserType;
import com.borsam.repository.entity.consultation.Consultation;
import com.borsam.repository.entity.doctor.DoctorAccount;
import com.borsam.repository.entity.doctor.DoctorProfile;
import com.borsam.repository.entity.patient.PatientProfile;
import com.borsam.repository.entity.record.Record.Stoplight;
import com.borsam.service.consultation.ConsultationService;
import com.borsam.service.doctor.DoctorAccountService;
import com.borsam.service.doctor.DoctorProfileService;
import com.borsam.service.patient.PatientProfileService;
import com.borsam.service.wf.QueryService;
import com.hiteam.common.base.pojo.search.Filter;
import com.hiteam.common.base.pojo.search.Order;
import com.hiteam.common.base.pojo.search.Page;
import com.hiteam.common.base.pojo.search.Pageable;
import com.hiteam.common.util.ConfigUtils;
import com.hiteam.common.util.http.HttpClientUtils;
import com.hiteam.common.util.json.JsonUtils;
import com.hiteam.common.web.Message;
import com.hiteam.common.web.WebUtil;
import com.hiteam.common.web.controller.BaseController;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.time.DateUtils;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Controller - 诊单管理
 * Created by Sebarswee on 2015/7/27.
 */
@Controller("doctorConsultationController")
@RequestMapping(value = "/doctor/consultation")
public class ConsultationController extends BaseController {
	static HashMap cidtotal;
    @Resource(name = "consultationServiceImpl")
    private ConsultationService consultationService;

    @Resource(name = "doctorAccountServiceImpl")
    private DoctorAccountService doctorAccountService;

    @Resource(name = "patientProfileServiceImpl")
    private PatientProfileService patientProfileService;

    @Resource(name = "queryServiceImpl")
    private QueryService queryService;

    @Resource(name = "doctorProfileServiceImpl")
    private DoctorProfileService doctorProfileService;
    
    /**
     * 待办页面
     */
    @RequestMapping(value = "/todo", method = RequestMethod.GET)
    public String todo(Long pId, Model model) {
        model.addAttribute("pId", pId);
        return "/doctor/consultation/todo";
    }

    
    /**
     * 查询页面
     */
    @RequestMapping(value = "/list", method = RequestMethod.GET)
    public String list(Long pId, Model model) {
        model.addAttribute("pId", pId);
        return "/doctor/consultation/list";
    }

    /**
     * 详情页面（流程）
     */
    @RequestMapping(value = "/{cid}/view", method = RequestMethod.GET)
    public String view(@PathVariable Long cid, Model model) {
        Consultation consultation = consultationService.find(cid);
        model.addAttribute("wfCode", WfCode.CONSULTATION);
        model.addAttribute("consultation", consultation);
        return "/doctor/consultation/view";
    }

    /**
     * 详情页面
     */
    @RequestMapping(value = "/{cid}/detail", method = RequestMethod.GET)
    public String detail(@PathVariable Long cid, Model model) {
        Consultation consultation = consultationService.find(cid);
        DoctorAccount doctorAccount=doctorAccountService.getCurrent();
        DoctorProfile doctor = doctorProfileService.find(doctorAccount.getId());
        model.addAttribute("wfCode", WfCode.CONSULTATION);
        model.addAttribute("consultation", consultation);
        model.addAttribute("doctor",doctor);
        return "/doctor/consultation/detail";
    }

    /**
     * 编辑页面
     */
    @RequestMapping(value = "/{cid}/edit", method = RequestMethod.GET)
    public String edit(@PathVariable Long cid, Model model) {
        model.addAttribute("cid", cid);
        return "/doctor/consultation/edit";
    }

    /**
     * 审核页面
     */
    @RequestMapping(value = "/{cid}/audit", method = RequestMethod.GET)
    public String audit(@PathVariable Long cid, Model model) {
        model.addAttribute("cid", cid);
        return "/doctor/consultation/audit";
    }

    /**
     * 待办列表
     */
    @RequestMapping(value = "/todoList", method = RequestMethod.POST)
    @ResponseBody
    public Page<Consultation> todoList(String serviceName, Long serviceType, Date startDate, Date endDate, Integer[] state,
                                       String doctor, String editDoctor, String auditDoctor, String org, Pageable pageable) {
        DoctorAccount doctorAccount = doctorAccountService.getCurrent();
        List<Long> guidList = queryService.queryTodoGuids(WfCode.CONSULTATION, UserType.doctor, doctorAccount.getId());
        List<Filter> filters = new ArrayList<Filter>();

        if (guidList != null && !guidList.isEmpty()) {
            filters.add(Filter.in("guid", guidList.toArray()));
        } else {
            return new Page<Consultation>(null, 0, pageable);
        }
        	
        // 服务名称
        if (StringUtils.isNotEmpty(serviceName)) {
            filters.add(Filter.like("type.name", serviceName));
        }

        // 服务类型
        if (serviceType != null) {
            filters.add(Filter.eq("type.id", serviceType));
        }

        // 下单时间
        if (startDate != null) {
            filters.add(Filter.ge("created", startDate.getTime() / 1000));
        }
        if (endDate != null) {
            filters.add(Filter.le("created", DateUtils.addDays(endDate, 1).getTime() / 1000));
        }

        // 诊单状态
        if (state != null && state.length > 0) {
            filters.add(Filter.in("state", state));
        }

        // 主治医生
        if (StringUtils.isNotEmpty(doctor)) {
            filters.add(Filter.like("doctor.fullName", doctor));
        }
        // 编辑医生
        if (StringUtils.isNotEmpty(editDoctor)) {
            filters.add(Filter.like("editDoctor.fullName", editDoctor));
        }
        // 审核医生
        if (StringUtils.isNotEmpty(auditDoctor)) {
            filters.add(Filter.like("auditDoctor.fullName", auditDoctor));
        }

        // 机构
        if (StringUtils.isNotEmpty(org)) {
            filters.add(Filter.like("patient.org.name", org));
        }

        pageable.setFilters(filters);

        pageable.setOrderProperty("created");
        pageable.setOrderDirection(Order.Direction.desc);
        return consultationService.findPage(pageable);
    }

    /**
     * 查询列表
     */
    @RequestMapping(value = "/page", method = RequestMethod.POST)
    @ResponseBody
    public Page<Consultation> page(String serviceName, Long serviceType, Date startDate, Date endDate, Integer[] state,
                                   String doctor, String editDoctor, String auditDoctor, String orgName, Long pId, Pageable pageable) {
        List<Filter> filters = new ArrayList<Filter>();

        DoctorAccount doctorAccount = doctorAccountService.getCurrent();
        DoctorProfile doctorProfile = doctorAccount.getDoctorProfile();
        // 只查当前机构
        filters.add(Filter.eq("org", doctorProfile.getOrg()));

        // 审核医生和操作医生能看到所有患者数据，主治医生只能看到自己的VIP患者数据
        if ((doctorProfile.getRoles() & 2)==2) {
            filters.add(Filter.eq("doctor", doctorProfile));
        }

        // 服务名称
        if (StringUtils.isNotEmpty(serviceName)) {
            filters.add(Filter.like("type.name", serviceName));
        }

        // 服务类型
        if (serviceType != null) {
            filters.add(Filter.eq("type.id", serviceType));
        }

        // 下单时间
        if (startDate != null) {
            filters.add(Filter.ge("created", startDate.getTime() / 1000));
        }
        if (endDate != null) {
            filters.add(Filter.le("created", DateUtils.addDays(endDate, 1).getTime() / 1000));
        }

        // 诊单状态
        if (state != null && state.length > 0) {
            filters.add(Filter.in("state", state));
        }

        // 主治医生
        if (StringUtils.isNotEmpty(doctor)) {
            filters.add(Filter.like("doctor.fullName", doctor));
        }
        // 编辑医生
        if (StringUtils.isNotEmpty(editDoctor)) {
            filters.add(Filter.like("editDoctor.fullName", editDoctor));
        }
        // 审核医生
        if (StringUtils.isNotEmpty(auditDoctor)) {
            filters.add(Filter.like("auditDoctor.fullName", auditDoctor));
        }

        // 机构
        if (StringUtils.isNotEmpty(orgName)) {
            filters.add(Filter.like("org.name", orgName));
        }

        if (pId != null) {
            PatientProfile patientProfile = patientProfileService.find(pId);
            filters.add(Filter.eq("patient", patientProfile));
        }

        setFilterByRole(filters);

        pageable.setFilters(filters);

        pageable.setOrderProperty("created");
        pageable.setOrderDirection(Order.Direction.desc);
        return consultationService.findPage(pageable);
    }

    /**
     * 根据权限设置特有的查询条件
     * @param filters
     */
    private void setFilterByRole(List<Filter> filters) {
        Principal principal = getPrincipal();

        switch (principal.getUserType()) {
            case org:
            	DoctorProfile doctorProfile = doctorProfileService.find(principal.getId());
                //针对没初始化机构数据的机构，设置为-1默认值
                Long oid = -1L;

                if (doctorProfile.getOrg() != null) {
                    oid = doctorProfile.getOrg().getId();
                }

                filters.add(Filter.eq("org.id", oid));
                break;
            default:
                break;
        }
    }

    private Principal getPrincipal() {
        Subject subject = SecurityUtils.getSubject();
        return (Principal) subject.getPrincipal();
    }

    /**
     * 分析编辑
     */
    @RequestMapping(value = "/edit", method = RequestMethod.POST)
    @ResponseBody
    public Message edit(Long cid, String qt, String qtc, String pr, String qrs, String rr, String opinion) {
        Assert.notNull(cid);
        consultationService.edit(cid, qt, qtc, pr, qrs, rr, opinion);
        return SUCCESS_MSG;
    }

    /**
     * 审核
     */
    @RequestMapping(value = "/audit", method = RequestMethod.POST)
    @ResponseBody
    public Message audit(Long cid, Boolean isPass, String remark) {
        Assert.notNull(cid);
        Assert.notNull(isPass);
        consultationService.audit(cid, isPass, remark);
        return SUCCESS_MSG;
    }

    /**
     * 获取pdf报告地址
     */
    @RequestMapping(value = "/pdf", method = RequestMethod.POST)
    @ResponseBody
    public String getPdfReport(Long id) {
        return consultationService.getRemoteFilePath(id);
    }

    /**
     * 
     * 
     * 下载pdf报告
     */
    @RequestMapping(value = "/pdf/download")
    public String downloadPdfReport(Long cid, HttpServletRequest request, HttpServletResponse response) throws Exception {
        String pdfPath = consultationService.getRemoteFilePath(cid);

        String filename;
        if (pdfPath.lastIndexOf("/") > -1) {
            filename = pdfPath.substring(pdfPath.lastIndexOf("/") + 1);
        } else {
            filename = pdfPath;
        }

        URL url = new URL(pdfPath);

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
        //编辑pdf报告
        @RequestMapping(value="/{cid}/pdf/edit",method=RequestMethod.GET)
        public String editPdf(@PathVariable Long cid,Model model){
        	Consultation consultation = consultationService.find(cid);
        	model.addAttribute("cid",cid);
        	model.addAttribute("consultation", consultation);
        	return "/doctor/consultation/pdf/edit";
        } 	
        
        @RequestMapping(value="/pdf/edit/submit",method=RequestMethod.POST)
        @ResponseBody
         public Message editPdfSubmit(Long cid,String opinion,Stoplight stoplight,
        		String qt, String qtc, String pr, String qrs, String rr){
        	 DoctorAccount doctorAccount=doctorAccountService.getCurrent();
        	 Map<String,Object> params=new HashMap<String,Object>();
        	 params.put("cid", cid);
        	 params.put("opinion", opinion);
        	 params.put("user_id", doctorAccount.getId());
        	 params.put("stoplight", stoplight.ordinal());
        	 String result=HttpClientUtils.postJsonBody(ConfigUtils.config.getProperty("server.url") + "/web/consultation/edit", 5000, params, "utf-8");
        	 Map<String, Object> json = JsonUtils.toObject(result, Map.class);
         	 //获取端口返回json数据中的网址
         	 Map<String,Object> datajson=JsonUtils.toObject((String)json.get("data"), Map.class);
         	 //return  (String) datajson.get("report_url");
         	return SUCCESS_MSG;
        }
        
        /**
         * 汇总页面 
         */
        @RequestMapping(value = "/total", method = RequestMethod.GET)
        public String total(Long pId, Model model) {
        	model.addAttribute("pId", pId);
            return "/doctor/consultation/total";
        }
        
        /**
         * 编辑汇总报告页面
         */
        @RequestMapping(value = "/{pId}/toedit", method = RequestMethod.GET)
        public String toedit(@PathVariable Long pId,String ids, Model model,Date startDate, Date endDate) {
            model.addAttribute("ids", ids);
            model.addAttribute("pId",pId);
            if (startDate==null) startDate=DateUtils.addMonths(new Date(), -1);
            if (endDate==null) endDate=DateUtils.addDays(new Date(), 1);
            model.addAttribute("startDate",startDate.getTime()/1000);
            model.addAttribute("endDate",endDate.getTime()/1000);
            DoctorAccount doctorAccount = doctorAccountService.getCurrent();
			Map<String, String> params = new HashMap<String, String>();
			params.put("uid", pId+"");
			params.put("start", startDate.getTime()/1000+"");
			params.put("end", endDate.getTime()/1000+"");
			String url=ConfigUtils.config.getProperty("server.url") + "/web/multi/analysis"
					+ "?uid="+
					pId+"&start="+startDate.getTime()/1000+"&end="+endDate.getTime()/1000;
			String result = HttpClientUtils.invokeGet(url, null, "UTF-8", 5000);
			Map<String, Object> json = JsonUtils.toObject(result, Map.class);
			// 获取端口返回json数据中的网址
			Map<String, Object> datajson = JsonUtils.toObject((String) json.get("data"), Map.class);
            model.addAttribute("autoOpinion",(String) datajson.get("analysis"));
            return "/doctor/consultation/toedit";
        }
        
        /**
         * 汇总报告提交按钮
         */
        @RequestMapping(value="/toedit/submit",method=RequestMethod.POST)
        @ResponseBody
         public Map<String, Object> toeditSubmit(Long pId,Long[] ids,String startDate,String endDate,String opinion){
        	String cids="";
        	 for (int i = 0; i < ids.length; i++) {
				if(i!=(ids.length-1))
					cids+=ids[i]+"|";
				else
					cids+=ids[i];
			}
			DoctorAccount doctorAccount = doctorAccountService.getCurrent();
			Map<String, Object> params = new HashMap<String, Object>();
			Map<String, Object> data = new HashMap<String, Object>();
			params.put("did", doctorAccount.getId());
			params.put("cids", cids);
			params.put("uid", pId);
			params.put("start", startDate);
			params.put("end", endDate);
			params.put("opinion", opinion);
			String result = HttpClientUtils.postJsonBody(
					ConfigUtils.config.getProperty("server.url") + "/web/consultation/multi", 5000, params, "utf-8");
			Map<String, Object> json = JsonUtils.toObject(result, Map.class);
			// 获取端口返回json数据中的网址
			Map<String, Object> datajson = JsonUtils.toObject((String) json.get("data"), Map.class);
			String ids2=datajson.get("id")+"";
			data.put("cid", ids2);
			data.put("msg",SUCCESS_MSG);
			WebUtil.setSessionData("TotalCid", ids2);
			return data;
         	 //return SUCCESS_MSG;
        }
        
        /**
         * 汇总报告查询列表
         */
        @RequestMapping(value = "/pageTotal", method = RequestMethod.POST)
        @ResponseBody
        public Page<Consultation> pageTotal(String serviceName, Long serviceType, Date startDate, Date endDate, Integer[] state,
                                       String doctor, String editDoctor, String auditDoctor, String orgName, Long pId, Pageable pageable) {
            List<Filter> filters = new ArrayList<Filter>();

            DoctorAccount doctorAccount = doctorAccountService.getCurrent();
            DoctorProfile doctorProfile = doctorAccount.getDoctorProfile();
            
            //诊单状态
            if (state != null && state.length > 0) {
                filters.add(Filter.in("state", state));
            }
            
            // 只查当前机构
            filters.add(Filter.eq("org", doctorProfile.getOrg()));
            
            // 审核医生和操作医生能看到所有患者数据，主治医生只能看到自己的VIP患者数据
            if ((doctorProfile.getRoles() & 4) != 4 && (doctorProfile.getRoles() & 8) != 8) {
                filters.add(Filter.eq("patient.doctor", doctorProfile));
            }

            // 服务名称
            if (StringUtils.isNotEmpty(serviceName)) {
                filters.add(Filter.like("type.name", serviceName));
            }

            // 服务类型，不查汇总报告
            if (serviceType != null) {
                filters.add(Filter.eq("type.id", serviceType));
            }else{
            	filters.add(Filter.lt("type.id", 100L));
            }
            
            // 下单时间
            if (startDate != null) {
                filters.add(Filter.ge("created", startDate.getTime() / 1000));
            } else {
            	filters.add(Filter.ge("created",DateUtils.addMonths(new Date(), -1).getTime()/1000 ));
            }
           
            if (endDate != null) {
                filters.add(Filter.le("created", DateUtils.addDays(endDate, 1).getTime() / 1000));
            } else {
            	filters.add(Filter.le("created", DateUtils.addDays(new Date(), 1).getTime() / 1000));
            }

            // 诊单状态
            if (state != null && state.length > 0) {
                filters.add(Filter.in("state", state));
            }

            // 主治医生
            if (StringUtils.isNotEmpty(doctor)) {
                filters.add(Filter.like("doctor.fullName", doctor));
            }
            // 编辑医生
            if (StringUtils.isNotEmpty(editDoctor)) {
                filters.add(Filter.like("editDoctor.fullName", editDoctor));
            }
            // 审核医生
            if (StringUtils.isNotEmpty(auditDoctor)) {
                filters.add(Filter.like("auditDoctor.fullName", auditDoctor));
            }

            // 机构
            if (StringUtils.isNotEmpty(orgName)) {
                filters.add(Filter.like("org.name", orgName));
            }
            
            // 诊单类型
            
            if (pId != null) {
                PatientProfile patientProfile = patientProfileService.find(pId);
                filters.add(Filter.eq("patient", patientProfile));
            }

            setFilterByRole(filters);

            pageable.setFilters(filters);

            pageable.setOrderProperty("created");
            pageable.setOrderDirection(Order.Direction.desc);
            return consultationService.findPage(pageable);
        }
        
        /**
         * 返回汇总报告详情页面
         */
        @RequestMapping(value = "/totaldetail", method = RequestMethod.GET)
        public String totaldetail(Model model) {
        	Long cid=Long.parseLong(WebUtil.getSessionData("TotalCid"));
        	return detail(cid,model);
        }
}
