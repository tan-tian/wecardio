package com.borsam.web.controller.admin;

import com.borsam.pojo.wf.WfCode;

import com.borsam.repository.entity.consultation.Consultation;
import com.borsam.repository.entity.patient.PatientProfile;
import com.borsam.service.consultation.ConsultationService;
import com.borsam.service.patient.PatientProfileService;
import com.hiteam.common.base.pojo.search.Filter;
import com.hiteam.common.base.pojo.search.Order;
import com.hiteam.common.base.pojo.search.Page;
import com.hiteam.common.base.pojo.search.Pageable;
import com.hiteam.common.web.WebUtil;
import com.hiteam.common.web.controller.BaseController;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.time.DateUtils;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Controller - 诊单管理
 * Created by Sebarswee on 2015/7/27.
 */
@Controller("adminConsultationController")
@RequestMapping(value = "/admin/consultation")
public class ConsultationController extends BaseController {

    @Resource(name = "consultationServiceImpl")
    private ConsultationService consultationService;

    @Resource(name = "patientProfileServiceImpl")
    private PatientProfileService patientProfileService;
    /**
     * 查询页面
     */
    @RequestMapping(value = "/list", method = RequestMethod.GET)
    public String todo(Long pId, Model model) {
        model.addAttribute("pId", pId);
        return "/admin/consultation/list";
    }

    /**
     * 详情页面
     */
    @RequestMapping(value = "/{cid}/view", method = RequestMethod.GET)
    public String view(@PathVariable Long cid, Model model) {
        Consultation consultation = consultationService.find(cid);
        model.addAttribute("wfCode", WfCode.CONSULTATION);
        model.addAttribute("consultation", consultation);
        return "/admin/consultation/view";
    }

    /**
     * 查询列表
     */
    @RequestMapping(value = "/page", method = RequestMethod.POST)
    @ResponseBody
    public Page<Consultation> page(String serviceName, Long serviceType, Date startDate, Date endDate, Integer[] state,Long patientId,
                                       String doctor, String editDoctor, String auditDoctor, Long pId, String orgName,
                                       String xw_searchListInput,Long oid, Pageable pageable) {
        List<Filter> filters = new ArrayList<Filter>();
        if(xw_searchListInput!=null)
        System.out.println("+++++++++++++"+xw_searchListInput+"++"+call_email_name(xw_searchListInput));
        // 输入搜索栏，判断输入的是电话0,姓名2,邮箱1
        if (StringUtils.isNotEmpty(xw_searchListInput)) {
        	if(call_email_name(xw_searchListInput)==0)
        		filters.add(Filter.like("patient.mobile", xw_searchListInput));
        	else if (call_email_name(xw_searchListInput)==1)
        		filters.add(Filter.like("patient.email", xw_searchListInput));
        	else
        		filters.add(Filter.like("patient.fullName", xw_searchListInput));
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

        // 患者
        if (patientId != null) {
            PatientProfile patient = patientProfileService.find(patientId);
            filters.add(Filter.eq("patient", patient));
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
        if (oid != null) {
            filters.add(Filter.eq("org.id", oid));
        }

        if(pId!=null)
        {
            PatientProfile patientProfile =  patientProfileService.find(pId);
            filters.add(Filter.eq("patient", patientProfile));
        }

        pageable.setFilters(filters);

        pageable.setOrderProperty("created");
        pageable.setOrderDirection(Order.Direction.desc);
        return consultationService.findPage(pageable);
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
    /**
     * 判断字符串是电话0，邮箱1，还是名字2
     */
    public int call_email_name(String inp)
    {
    	 Pattern pattern = Pattern.compile("[0-9]*"); 
    	   Matcher isNum = pattern.matcher(inp);
    	   if( isNum.matches() ){
    	       return 0; 
    	   } 
    	   if(inp.contains("@")){
    		   return 1;
    	   }
    	    return 2;
    }
}
