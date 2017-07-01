package com.borsam.web.controller.patient;

import com.borsam.pojo.service.AvailableService;
import com.borsam.repository.entity.patient.*;
import com.borsam.repository.entity.record.Record;
import com.borsam.repository.entity.service.Service;
import com.borsam.repository.entity.service.ServiceKey;
import com.borsam.service.org.OrganizationService;
import com.borsam.service.patient.PatientAccountService;
import com.borsam.service.patient.PatientServiceService;
import com.borsam.service.patient.PatientWalletService;
import com.borsam.service.patient.PatientWalletVerifyService;
import com.borsam.service.record.RecordService;
import com.borsam.service.service.ServiceService;
import com.borsam.service.service.ServiceTypeService;
import com.hiteam.common.base.pojo.search.Filter;
import com.hiteam.common.base.pojo.search.Order;
import com.hiteam.common.base.pojo.search.Page;
import com.hiteam.common.base.pojo.search.Pageable;
import com.hiteam.common.service.RSAService;
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
import java.security.interfaces.RSAPublicKey;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * Controller - 检查记录
 * Created by Sebarswee on 2015/7/21.
 */
@Controller("patientRecorderController")
@RequestMapping(value = "/patient/record")
public class RecordController extends BaseController {

    @Resource(name = "patientAccountServiceImpl")
    private PatientAccountService patientAccountService;

    @Resource(name = "recordServiceImpl")
    private RecordService recordService;

    @Resource(name = "patientServiceServiceImpl")
    private PatientServiceService patientServiceService;

    @Resource(name = "rsaServiceImpl")
    private RSAService rsaService;

    @Resource(name = "organizationServiceImpl")
    private OrganizationService organizationService;

    @Resource(name = "serviceTypeServiceImpl")
    private ServiceTypeService serviceTypeService;

    @Resource(name = "serviceServiceImpl")
    private ServiceService serviceService;

    @Resource(name = "patientWalletVerifyServiceImpl")
    private PatientWalletVerifyService patientWalletVerifyService;

    @Resource(name = "patientWalletServiceImpl")
    private PatientWalletService patientWalletService;

    /**
     * 主页面
     */
    @RequestMapping(method = RequestMethod.GET)
    public String index(Long pId, Model model) {
        model.addAttribute("pId", pId);
        return "/patient/record/list";
    }

    /**
     * 详情页面
     */
    @RequestMapping(value = "/{id}/view", method = RequestMethod.GET)
    public String view(@PathVariable Long id, Model model) {
        Record record = recordService.find(id);
        model.addAttribute("record", record);
        // 通过接口获取报告需要花费一定时间，会导致页面加载缓慢，改由异步获取
//        if (recordService.localFileExist(record)) {
//            model.addAttribute("pdf", recordService.getLocalFilePath(record));
//        } else {
//            // 调用接口获取pdf文件
//            recordService.saveFileToLocal(record);
//            model.addAttribute("pdf", recordService.getLocalFilePath(record));
//        }
        return "/patient/record/view";
    }

    /**
     * 设置标识页面
     */
    @RequestMapping(value = "/flag", method = RequestMethod.GET)
    public String flag(String ids, Model model) {
        model.addAttribute("ids", ids);
        return "/patient/record/flag";
    }

    /**
     * 选择服务页面
     */
    @RequestMapping(value = "/{rid}/choose", method = RequestMethod.GET)
    public String choose(@PathVariable Long rid, Model model) {
        model.addAttribute("rid", rid);
        return "/patient/record/choose";
    }

    /**
     * 支付密码页面
     */
    @RequestMapping(value = "/password", method = RequestMethod.GET)
    public String passsword(Long rid, Long oid, Integer type, Long serviceType, Model model) {
        model.addAttribute("rid", rid);
        model.addAttribute("oid", oid);
        model.addAttribute("type", type);
        model.addAttribute("serviceType", serviceType);

        RSAPublicKey publicKey = rsaService.generateKey();
        Map<String, String> map = rsaService.getModulusAndExponent(publicKey);
        model.addAttribute("modulus", map.get("modulus"));
        model.addAttribute("exponent", map.get("exponent"));
        return "/patient/record/password";
    }

    /**
     * 分页查询
     */
    @RequestMapping(value = "/page", method = RequestMethod.POST)
    @ResponseBody
    public Page<Record> page(String serviceName, Long serviceType, Date startDate, Date endDate, Integer[] state,
                             Integer[] stoplight, Pageable pageable) {
        PatientAccount patientAccount = patientAccountService.getCurrent();
        List<Filter> filters = new ArrayList<Filter>();
        filters.add(Filter.eq("patient", patientAccount.getId()));      // 只查患者本人的记录

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
     * 设置标识
     */
    @RequestMapping(value = "/flag/set", method = RequestMethod.POST)
    @ResponseBody
    public Message setFlag(Long[] ids, Record.Flag flag) {
        Assert.notNull(ids);
        Assert.notNull(flag);
        recordService.setFlag(ids, flag);
        return SUCCESS_MSG;
    }

    /**
     * 可使用服务列表
     */
    @RequestMapping(value = "/service/available", method = RequestMethod.POST)
    @ResponseBody
    public List<AvailableService> availableService(Long rid) {
        Record record = recordService.find(rid);
        PatientProfile patientProfile = record.getPatient();
        List<Filter> filters1 = new ArrayList<Filter>();
        List<Filter> filters2 = new ArrayList<Filter>();

        // bindtype=0时，才只有这一家机构名称，其他情况，要出来所有能提供这种服务的所有机构的名称
        if (patientProfile.getBindType().equals(PatientProfile.BindType.org)) {
            filters1.add(Filter.eq("key.org", patientProfile.getOrg()));
            filters2.add(Filter.eq("oid", patientProfile.getOrg().getId()));
        }

        // 购买的服务
        filters1.add(Filter.eq("key.patient", patientProfile));
        filters1.add(Filter.eq("key.serviceType", record.getServiceType()));
        filters1.add(Filter.gt("count", 0));
        List<PatientService> buyList = patientServiceService.findList(null, filters1, null);

        // 机构发布的服务
        filters2.add(Filter.eq("type", record.getServiceType().getId()));
        filters2.add(Filter.eq("isEnabled", true));
        List<Service> serviceList = serviceService.findList(null, filters2, null);

        // 合并结果
        List<AvailableService> result = new ArrayList<>();
        for (PatientService patientService : buyList) {
            AvailableService availableService = new AvailableService();
            availableService.setType(0);
            availableService.setOid(patientService.getOid());
            availableService.setOrgName(patientService.getOrgName());
            availableService.setServiceType(patientService.getType());
            availableService.setServiceName(patientService.getServiceName());
            availableService.setCount(patientService.getCount());
            result.add(availableService);
        }
        for (Service service : serviceList) {
            AvailableService availableService = new AvailableService();
            availableService.setType(1);
            availableService.setOid(service.getOid());
            availableService.setOrgName(organizationService.find(service.getOid()).getName());
            availableService.setServiceType(service.getType());
            availableService.setServiceName(serviceTypeService.find(service.getType()).getName());
            availableService.setPrice(service.getPrice());
            result.add(availableService);
        }
        return result;
    }

    /**
     * 支付
     */
    @RequestMapping(value = "/pay", method = RequestMethod.POST)
    @ResponseBody
    public Message pay(Long rid, Long oid, Integer type, Long serviceType) {
        PatientAccount patientAccount = patientAccountService.getCurrent();

        Message message = patientWalletVerifyService.verify(patientAccount);

        if(message.getType() != Message.Type.success) {
            return message;
        }

        // 扣除购买的服务次数
        if (type == 0) {
            // 根据机构和服务类型，查询患者购买的服务
            PatientServiceKey key = new PatientServiceKey();
            key.setPatient(patientAccount.getPatientProfile());
            key.setOrg(organizationService.find(oid));
            key.setServiceType(serviceTypeService.find(serviceType));
            PatientService patientService = patientServiceService.find(key);

            // 服务不存在
            if (patientService == null) {
                return Message.warn("patient.record.message.service.notexist");
            }
            // 服务次数不足
            if (patientService.getCount() <= 0) {
                return Message.warn("patient.record.message.service.low");
            }
            // 服务已过期
            if (patientService.hasExpired()) {
                return Message.warn("patient.record.message.service.expired");
            }
        } else if (type == 1) {
            ServiceKey key = new ServiceKey();
            key.setOid(oid);
            key.setType(serviceType);
            Service service = serviceService.find(key);
            // 服务不存在
            if (service == null) {
                return Message.warn("patient.record.message.service.notexist");
            }
            PatientWallet patientWallet = patientWalletService.getWallet(patientAccount.getId());
            // 余额不足
            if (service.getPrice().compareTo(patientWallet.getTotal()) > 0) {
                return Message.warn("patient.record.message.wallet.low");
            }
        }

        // 创建诊单
        recordService.createConsultation(patientAccount.getId(), rid, oid, type, serviceType);

        // 移除私钥
        rsaService.removePrivateKey();
        return SUCCESS_MSG;
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
