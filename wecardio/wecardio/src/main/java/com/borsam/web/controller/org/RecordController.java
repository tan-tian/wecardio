package com.borsam.web.controller.org;

import com.borsam.repository.entity.doctor.DoctorAccount;
import com.borsam.repository.entity.doctor.DoctorProfile;
import com.borsam.repository.entity.patient.PatientProfile;
import com.borsam.repository.entity.record.Record;
import com.borsam.service.doctor.DoctorAccountService;
import com.borsam.service.patient.PatientProfileService;
import com.borsam.service.record.RecordService;
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

/**
 * Controller - 检查记录
 * Created by tantian on 2015/7/26.
 */
@Controller("orgRecorderController")
@RequestMapping(value = "/org/record")
public class RecordController extends BaseController {

    @Resource(name = "doctorAccountServiceImpl")
    private DoctorAccountService doctorAccountService;

    @Resource(name = "recordServiceImpl")
    private RecordService recordService;

    @Resource(name = "patientProfileServiceImpl")
    private PatientProfileService patientProfileService;

    /**
     * 主页面
     */
    @RequestMapping(method = RequestMethod.GET)
    public String index(Long pId, Model model) {
        model.addAttribute("pId", pId);
        return "/org/record/list";
    }

    /**
     * 详情页面
     */
    @RequestMapping(value = "/{id}/view", method = RequestMethod.GET)
    public String view(@PathVariable Long id, Model model) {
        Record record = recordService.find(id);
        model.addAttribute("record", record);
        return "/org/record/view";
    }

    /**
     * 分页查询
     */
    @RequestMapping(value = "/page", method = RequestMethod.POST)
    @ResponseBody
    public Page<Record> page(String serviceName, Long serviceType, Date startDate, Date endDate, Integer[] state,
                             Integer[] stoplight, Long pId, Pageable pageable) {
        DoctorAccount doctorAccount = doctorAccountService.getCurrent();
        List<Filter> filters = new ArrayList<Filter>();

        // 只查绑定当前机构绑的患者的记录
        DoctorProfile doctorProfile = doctorAccount.getDoctorProfile();
        if (doctorProfile.getOrg() != null && doctorProfile.getOrg().getId() != 0L) {
            filters.add(Filter.eq("patient.org", doctorProfile.getOrg()));
        } else {
            filters.add(Filter.isNull("patient.org"));
        }

        // 服务名称
        if (StringUtils.isNotEmpty(serviceName)) {
            filters.add(Filter.like("serviceType.name", serviceName));
        }

        // 服务类型
        if (serviceType != null) {
            filters.add(Filter.eq("serviceType.id", serviceType));
        }

        // 测试时间
        if (startDate != null) {
            filters.add(Filter.ge("tested", startDate.getTime() / 1000));
        }
        if (endDate != null) {
            filters.add(Filter.le("tested", DateUtils.addDays(endDate, 1).getTime() / 1000));
        }

        if(pId!=null)
        {
            PatientProfile patientProfile =  patientProfileService.find(pId);
            filters.add(Filter.eq("patient", patientProfile));
        }

        // 记录状态
        if (state != null && state.length > 0) {
            filters.add(Filter.in("isCommit", state));
        }

        // 红绿灯
        if (stoplight != null && stoplight.length > 0) {
            filters.add(Filter.in("stoplight", stoplight));
        }

        pageable.setFilters(filters);

        pageable.setOrderProperty("created");
        pageable.setOrderDirection(Order.Direction.desc);
        return recordService.findPage(pageable);
    }

    /**
     * 分页查询
     */
    @RequestMapping(value = "/pageByPatient", method = RequestMethod.POST)
    @ResponseBody
    public Page<Record> pageByPatient(String serviceName, Long serviceType, Date startDate, Date endDate, Integer[] state,
                                      Integer[] stoplight,Long pId, Pageable pageable) {
        List<Filter> filters = new ArrayList<Filter>();
        filters.add(Filter.eq("patient", pId));      // 只查当前患者的记录

        // 服务名称
        if (StringUtils.isNotEmpty(serviceName)) {
            filters.add(Filter.like("serviceType.name", serviceName));
        }

        // 服务类型
        if (serviceType != null) {
            filters.add(Filter.eq("serviceType.id", serviceType));
        }

        // 测试时间
        if (startDate != null) {
            filters.add(Filter.ge("tested", startDate.getTime() / 1000));
        }
        if (endDate != null) {
            filters.add(Filter.le("tested", DateUtils.addDays(endDate, 1).getTime() / 1000));
        }

        // 记录状态
        if (state != null && state.length > 0) {
            filters.add(Filter.in("isCommit", state));
        }

        // 红绿灯
        if (stoplight != null && stoplight.length > 0) {
            filters.add(Filter.in("stoplight", stoplight));
        }

        pageable.setFilters(filters);

        pageable.setOrderProperty("created");
        pageable.setOrderDirection(Order.Direction.desc);
        return recordService.findPage(pageable);
    }

    /**
     * 获取pdf报告地址
     */
    @RequestMapping(value = "/pdf", method = RequestMethod.POST)
    @ResponseBody
    public String getPdfReport(Long rid) {
//        Record record = recordService.find(rid);
//        if (!recordService.localFileExist(record)) {
//            // 调用接口获取pdf文件
//            recordService.saveFileToLocal(record);
//        }
        return recordService.getRemoteFilePath(rid);
    }

    /**
     * 下载pdf报告
     */
    @RequestMapping(value = "/pdf/download")
    public String downloadPdfReport(Long rid, HttpServletRequest request, HttpServletResponse response) throws Exception {
        String pdfPath = recordService.getRemoteFilePath(rid);

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
}
