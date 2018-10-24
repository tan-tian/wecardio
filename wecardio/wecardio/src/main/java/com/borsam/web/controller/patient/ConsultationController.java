package com.borsam.web.controller.patient;

import com.borsam.pojo.wf.WfCode;
import com.borsam.repository.entity.consultation.Consultation;
import com.borsam.repository.entity.forum.ForumInfo;
import com.borsam.service.consultation.ConsultationService;
import com.borsam.service.patient.PatientAccountService;
import com.hiteam.common.base.pojo.search.Filter;
import com.hiteam.common.base.pojo.search.Order;
import com.hiteam.common.base.pojo.search.Page;
import com.hiteam.common.base.pojo.search.Pageable;
import com.hiteam.common.web.Message;
import com.hiteam.common.web.WebUtil;
import com.hiteam.common.web.controller.BaseController;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.time.DateUtils;
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
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Controller - 诊单管理
 * Created by tantian on 2015/7/27.
 */
@Controller("patientConsultationController")
@RequestMapping(value = "/patient/consultation")
public class ConsultationController extends BaseController {

    @Resource(name = "patientAccountServiceImpl")
    private PatientAccountService patientAccountService;

    @Resource(name = "consultationServiceImpl")
    private ConsultationService consultationService;

    /**
     * 查询页面
     */
    @RequestMapping(value = "/list", method = RequestMethod.GET)
    public String todo(Long pId, Model model) {
        model.addAttribute("pId", pId);
        return "/patient/consultation/list";
    }

    /**
     * 详情页面
     */
    @RequestMapping(value = "/{cid}/view", method = RequestMethod.GET)
    public String view(@PathVariable Long cid, Model model) {
        Consultation consultation = consultationService.find(cid);
        model.addAttribute("wfCode", WfCode.CONSULTATION);
        model.addAttribute("consultation", consultation);
        return "/patient/consultation/view";
    }

    /**
     * 评价页面
     */
    @RequestMapping(value = "/{cid}/evaluate", method = RequestMethod.GET)
    public String evaluate(@PathVariable Long cid, Model model) {
        model.addAttribute("cid", cid);
        return "/patient/consultation/evaluate";
    }

    /**
     * 查询列表
     */
    @RequestMapping(value = "/page", method = RequestMethod.POST)
    @ResponseBody
    public Page<Consultation> page(String serviceName, Long serviceType, Long[] org,Date startDate, Date endDate, Integer[] state, Pageable pageable) {
        List<Filter> filters = new ArrayList<Filter>();
        // 只查自己
        filters.add(Filter.eq("patient", patientAccountService.getCurrent().getId()));

        // 服务名称
        if (StringUtils.isNotEmpty(serviceName)) {
            filters.add(Filter.like("type.name", serviceName));
        }

        // 服务类型
        if (serviceType != null) {
            filters.add(Filter.eq("type.id", serviceType));
        }
        
        //机构
        if (org != null && org.length > 0) {
            filters.add(Filter.in("org", org));
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
     * 评价
     */
    @RequestMapping(value = "/{cid}/evaluate", method = RequestMethod.POST)
    @ResponseBody
    public Message evaluate(@PathVariable Long cid, ForumInfo forumInfo) {
        Assert.notNull(cid);
        consultationService.evaluate(cid, forumInfo);
        return SUCCESS_MSG;
    }
}
