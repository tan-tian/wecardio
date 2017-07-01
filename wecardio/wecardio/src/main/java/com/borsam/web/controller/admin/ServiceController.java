package com.borsam.web.controller.admin;

import com.borsam.plugin.PaymentPlugin;
import com.borsam.pojo.security.Principal;
import com.borsam.pojo.service.EditServiceData;
import com.borsam.pojo.service.EditServicePackageData;
import com.borsam.pojo.service.QueryServiceData;
import com.borsam.pojo.service.QueryServicePackageData;
import com.borsam.pub.UserType;
import com.borsam.repository.entity.doctor.DoctorProfile;
import com.borsam.repository.entity.org.Organization;
import com.borsam.repository.entity.patient.PatientAccount;
import com.borsam.repository.entity.patient.PatientProfile;
import com.borsam.repository.entity.patient.PatientProfile.BindType;
import com.borsam.repository.entity.patient.PatientWallet;
import com.borsam.repository.entity.patient.PatientWalletHistory;
import com.borsam.repository.entity.service.Service;
import com.borsam.repository.entity.service.ServiceOrder;
import com.borsam.repository.entity.service.ServicePackage;
import com.borsam.repository.entity.service.ServiceType;
import com.borsam.service.doctor.DoctorProfileService;
import com.borsam.service.org.OrganizationService;
import com.borsam.service.patient.PatientAccountService;
import com.borsam.service.patient.PatientProfileService;
import com.borsam.service.patient.PatientWalletHistoryService;
import com.borsam.service.patient.PatientWalletService;
import com.borsam.service.patient.PatientWalletVerifyService;
import com.borsam.service.pub.PluginService;
import com.borsam.service.service.ServiceOrderService;
import com.borsam.service.service.ServicePackageService;
import com.borsam.service.service.ServiceService;
import com.borsam.service.service.ServiceTypeService;
import com.hiteam.common.base.pojo.search.Filter;
import com.hiteam.common.base.pojo.search.Order;
import com.hiteam.common.base.pojo.search.Page;
import com.hiteam.common.base.pojo.search.Pageable;
import com.hiteam.common.service.RSAService;
import com.hiteam.common.util.collections.CollectionUtil;
import com.hiteam.common.util.lang.ArrayUtil;
import com.hiteam.common.util.lang.StringUtil;
import com.hiteam.common.util.pojo.EnumBean;
import com.hiteam.common.util.spring.SpringUtils;
import com.hiteam.common.web.I18Util;
import com.hiteam.common.web.Message;
import com.hiteam.common.web.controller.BaseController;

import org.apache.commons.lang.time.DateUtils;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authz.annotation.Logical;
import org.apache.shiro.authz.annotation.RequiresRoles;
import org.apache.shiro.authz.annotation.RequiresUser;
import org.apache.shiro.subject.Subject;
import org.springframework.ui.Model;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;
import java.util.*;

/**
 * Created by Zhang zhongtao on 2015/7/22.
 */
@RequiresUser
@RestController("adminServiceController")
@RequestMapping("/admin/service")
public class ServiceController extends BaseController {

    @Resource(name = "serviceTypeServiceImpl")
    private ServiceTypeService serviceTypeService;

    @Resource(name = "serviceServiceImpl")
    private ServiceService serviceService;

    @Resource(name = "servicePackageServiceImpl")
    private ServicePackageService servicePackageService;

    @Resource(name = "organizationServiceImpl")
    private OrganizationService organizationService;

    @Resource(name = "doctorProfileServiceImpl")
    private DoctorProfileService doctorProfileService;

    @Resource(name = "patientWalletVerifyServiceImpl")
    private PatientWalletVerifyService patientWalletVerifyService;

    @Resource(name = "patientAccountServiceImpl")
    private PatientAccountService patientAccountService;

    @Resource(name = "patientWalletServiceImpl")
    private PatientWalletService patientWalletService;
    
    @Resource(name = "patientWalletHistoryServiceImpl")
    private PatientWalletHistoryService patientWalletHistoryService;

    @Resource(name = "serviceOrderServiceImpl")
    private ServiceOrderService serviceOrderService;

    @Resource(name = "patientProfileServiceImpl")
    private PatientProfileService patientProfileService;
    
    @Resource(name = "pluginServiceImpl")
    private PluginService pluginService;

    /**
     * 服务项查询
     *
     * @return Page
     */
    @RequiresRoles(value = {"admin", "org"}, logical = Logical.OR)
    @RequestMapping(value = "/query", method = RequestMethod.POST)
    public Page<ServiceType> query(String name, Boolean[] isEnableds, Pageable pageable) {
        List<Filter> filterList = new ArrayList<Filter>();

        if (StringUtil.isNotBlank(name)) {
            filterList.add(Filter.like("name", name));
        }

        if (ArrayUtil.isNotEmpty(isEnableds)) {
            filterList.add(Filter.in("enabled", isEnableds));
        }

        pageable.setFilters(filterList);
        pageable.setOrders(new ArrayList<Order>() {{
            add(Order.asc("created"));
        }});

        return serviceTypeService.findPage(pageable);
    }

    /**
     * 保存服务项
     *
     * @return Message
     */
    @RequiresRoles(value = {"admin"}, logical = Logical.OR)
    @RequestMapping(value = "/edit", method = RequestMethod.POST)
    public Message edit(ServiceType serviceType) {
        Assert.notNull(serviceType);
        Assert.notNull(serviceType.getId());

        if (!isValid(serviceType)) {
            return Message.error();
        }

        if (serviceTypeService.isExistType(serviceType)) {
            return Message.warn("admin.service.msg.warn.existType");
        }

        if (serviceType.getFrom().compareTo(serviceType.getTo()) >= 1) {
            return Message.warn("admin.service.form.price.compare");
        }

        if (serviceType.getId() == null) {
            serviceTypeService.save(serviceType);
        } else {
            serviceTypeService.update(serviceType, "created");
        }

        return SUCCESS_MSG;
    }

    /**
     * 新增服务
     *
     * @param services
     * @return Message
     */
    @RequestMapping(value = "/addService", method = RequestMethod.POST)
    public Message addService(Service[] services) {
        return SUCCESS_MSG;
    }

    /**
     * 删除服务项
     *
     * @param ids 服务项ID数组
     * @return Message
     */
    @RequestMapping(value = "/delItem", method = RequestMethod.POST)
    public Message delItem(Long[] ids) {
        return SUCCESS_MSG;
    }

    /**
     * 禁用服务项
     *
     * @param ids 标识ID数组
     * @return Message
     */
    @RequiresRoles(value = {"admin"}, logical = Logical.OR)
    @RequestMapping(value = "/disableItem", method = RequestMethod.POST)
    public Message disableItem(Long[] ids) {
        Assert.noNullElements(ids);

        List<ServiceType> serviceTypes = serviceTypeService.findList(ids);
        for (ServiceType serviceType : serviceTypes) {
            serviceType.setEnabled(false);
        }

        serviceTypeService.saveOrUpdate(serviceTypes);

        return SUCCESS_MSG;
    }

    /**
     * 启用服务项
     *
     * @param ids 标识ID数组
     * @return Message
     */
    @RequiresRoles(value = {"admin"}, logical = Logical.OR)
    @RequestMapping(value = "/enableItem", method = RequestMethod.POST)
    public Message enableItem(Long[] ids) {
        Assert.noNullElements(ids);

        List<ServiceType> serviceTypes = serviceTypeService.findList(ids);
        for (ServiceType serviceType : serviceTypes) {
            serviceType.setEnabled(true);
        }

        serviceTypeService.saveOrUpdate(serviceTypes);
        return SUCCESS_MSG;
    }

    /**
     * 服务编辑（新增、修改）
     *
     * @param data 服务集合
     * @return Message
     */
    @RequiresRoles(value = {"org"}, logical = Logical.OR)
    @RequestMapping(value = "/editService", method = RequestMethod.POST)
    public Message editService(EditServiceData data) {
        Assert.notNull(data);
        Assert.notEmpty(data.getServices());
        Principal principal = getLoginId();
        Long oid = getOidByRole(principal);

        if (oid == null) {
            return Message.warn("admin.service.msg.org.bug");
        }

        serviceService.editServices(data.getServices(), oid);
        return SUCCESS_MSG;
    }

    /**
     * 服务查询
     *
     * @return Page
     */
    @RequestMapping(value = "/queryService", method = RequestMethod.POST)
    public Page<Map> queryService(QueryServiceData data) {

        Principal principal = getLoginId();
        Long oid = getOidByRole(principal);
        oid = (oid == null ? -1L : oid);

        if (principal.getUserType() == UserType.admin) {
        	return serviceService.queryService(data);
        }

        data.setStaffId(oid);
        return serviceService.queryService(data);
    }

    private Long getOidByRole(Principal principal) {
        Long oid = null;

        //根据医生、机构角色获取机构对象
        switch (principal.getUserType()) {
            case doctor:
            case org:
                DoctorProfile doctorProfile = doctorProfileService.find(principal.getId());
                if (doctorProfile.getOrg() != null) {
                    oid = doctorProfile.getOrg().getId();
                }
                break;
            default:
                break;
        }

        return oid;
    }

    @RequestMapping(value = "/package/service/{packageId}", method = RequestMethod.POST)
    public Page<Map> queryServiceByPackId(@PathVariable Long packageId) {
        return servicePackageService.queryServiceByPackId(packageId);
    }

    /**
     * 支付
     *
     * @param id 服务包ID
     * @return
     */
    @RequiresRoles(value = {"patient"}, logical = Logical.OR)
    @RequestMapping(value = "/package/service/pay", method = RequestMethod.POST)
    public Message pay(Long id, String enPassword) {
        Assert.notNull(id);
        Assert.hasLength(enPassword);

        ServicePackage servicePackage = servicePackageService.find(id);
        Assert.notNull(servicePackage);

        Message verify = patientWalletVerifyService.verify(patientAccountService.getCurrent());

        if (verify.getType() != Message.Type.success) {
            return verify;
        }

        Message message = servicePackageService.pay(servicePackage, getLoginId().getId(),null);

        if (message.getType() != Message.Type.success) {
            return message;
        }

        return SUCCESS_MSG;
    }

    /**
     * 禁用服务
     *
     * @param ids 标识数组（机构ID——服务项ID）
     * @return Message
     */
    @RequiresRoles(value = {"admin", "org"}, logical = Logical.OR)
    @RequestMapping(value = "/disable", method = RequestMethod.POST)
    public Message disable(String[] ids) {
        serviceService.setEnableVal(ids, false);
        return SUCCESS_MSG;
    }


    /**
     * 启用服务
     *
     * @param ids 标识数组（机构ID——服务项ID）
     * @return Message
     */
    @RequiresRoles(value = {"admin", "org"}, logical = Logical.OR)
    @RequestMapping(value = "/enable", method = RequestMethod.POST)
    public Message enable(String[] ids) {
        serviceService.setEnableVal(ids, true);
        return SUCCESS_MSG;
    }

    @RequiresRoles(value = {"admin", "org"}, logical = Logical.OR)
    @RequestMapping(value = "/delPackage", method = RequestMethod.POST)
    public Message delPackage(Long[] ids) {
        Assert.noNullElements(ids);
        servicePackageService.delPackage(ids);

        return SUCCESS_MSG;
    }

    /**
     * 查询服务名称
     *
     * @return
     */
    @RequestMapping(value = "/queryServiceName", method = RequestMethod.POST)
    public List<Map> queryServiceName(String name) {

        List<Filter> filters = new ArrayList<>();

        if (StringUtil.isNotBlank(name)) {
            filters.add(Filter.like("name", name));
        }

        List<Order> orders = new ArrayList<>();
        orders.add(Order.asc("name"));

        List<ServiceType> list = serviceTypeService.findList(50, filters, orders);

        List<Map> maps = new ArrayList<>();

        if (CollectionUtil.isNotEmpty(list)) {
            list.stream().parallel().forEach(serviceType -> maps.add(new HashMap<String, Object>() {{
                put("name", serviceType.getName());
                put("id", serviceType.getId());
            }}));
        }

        return maps;
    }

    /**
     * 服务包编辑（新增、修改）
     *
     * @param data 服务集合
     * @return Message
     */
    @RequiresRoles(value = {"org"}, logical = Logical.OR)
    @RequestMapping(value = "/editServicePackage", method = RequestMethod.POST)
    public Message editServicePackage(EditServicePackageData data) {
        Assert.notNull(data);
        Assert.notEmpty(data.getTypes());
        Principal principal = getLoginId();

        Organization organization = null;
        DoctorProfile doctorProfile = null;

        //根据医生、机构角色获取机构对象
        switch (principal.getUserType()) {
            case doctor:
            case org:
                doctorProfile = doctorProfileService.find(principal.getId());
                organization = doctorProfile.getOrg();
                if (organization != null && !organization.getIsWalletActive()) {
                    return Message.warn("org.wallet.noactivate.title");
                }
                break;
            default:
                break;
        }

        Assert.notNull(organization);

        servicePackageService.editServicePackages(data, organization);
        return SUCCESS_MSG;
    }

    /**
     * 查询服务包
     *
     * @param data 查询参数
     * @return Page
     */
    @RequestMapping(value = "/queryServicePackage", method = RequestMethod.POST)
    public Page<ServicePackage> queryServicePackage(QueryServicePackageData data,Model model) {
        data.setDel(false);

        Principal principal = getLoginId();

        //根据医生、机构角色获取机构对象
        switch (principal.getUserType()) {
            case doctor:
            case org:
                if (data.getOrgId() == null) {
                    DoctorProfile doctorProfile = doctorProfileService.find(principal.getId());
                    Organization organization = doctorProfile.getOrg();
                    data.setOrgId(organization == null ? null : organization.getId());
                }
                break;
            case patient:
                if (data.getOrgId() == null) {
                    PatientProfile patientProfile = patientProfileService.find(principal.getId());
                    Organization organization1 = patientProfile.getOrg();
                    data.setOrgId(organization1 == null ? null : organization1.getId());
                }
                break;
            default:
                break;
        }
        if(patientProfileService.find(principal.getId())!=null)
        model.addAttribute("bindType",patientProfileService.find(principal.getId()).getBindType());
         Page<ServicePackage> page = servicePackageService.queryServicePackage(data);
        return page;
    }

    /**
     * 机构列表（for 下拉框）
     */
    @RequestMapping(value = "/sel", method = RequestMethod.POST)
    public List<EnumBean> sel(String name) {
    	Principal principal = getLoginId();
    	//如果不是属于patient就显示所以的机构名称
    	if (principal.getId()==null || patientProfileService.find(principal.getId())==null)
			return organizationService.sel(name);
    	PatientProfile patientProfile = patientProfileService.find(principal.getId());
    	BindType bind=patientProfileService.find(principal.getId()).getBindType();
    	int bin0=bind.ordinal();
    	if(bin0==0 || bin0==1)
    		return organizationService.sel(patientProfile.getOrgName());
        return organizationService.sel(name);
    }

    /**
     * 服务包有效期及已失效
     *
     * @return
     */
    @RequestMapping(value = "/packageStatus", method = RequestMethod.POST)
    public List<EnumBean> packageStatus() {
        List<EnumBean> enumBeans = new ArrayList<EnumBean>() {{
            add(new EnumBean(0, I18Util.getMessage("admin.service.lbl.pack.status.pass")));
            add(new EnumBean(1, I18Util.getMessage("admin.service.lbl.pack.status.valid")));
        }};

        return enumBeans;
    }

    /**
     * 服务包详情
     *
     * @param id
     * @return
     */
    @RequestMapping(value = "/package/detail/{id}", method = RequestMethod.GET)
    public ServicePackage packageDetail(@PathVariable Long id) {
        Assert.notNull(id);
        ServicePackage servicePackage = servicePackageService.find(id);
        return servicePackage;
    }

    /***
     * 查询购买服务包的患者列表
     *
     * @param packageId
     */
    @RequestMapping(value = "/package/queryConsumer", method = RequestMethod.POST)
    public Page<PatientProfile> queryConsumer(Long packageId, Pageable pageable) {
        List<Filter> filters = new ArrayList<Filter>();
        filters.add(Filter.eq("packageId", packageId));

        pageable.setFilters(filters);
        //服务订单分页数据
        Page<PatientProfile> orderPage = serviceOrderService.query(packageId, pageable);
        
        	return orderPage;
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
     * 服务包详情页面
     * 
     */
    @RequestMapping(value = "/packDetailBuy", method = RequestMethod.GET)
    public ModelAndView packDetailBuy(Long id, Long pId, Model model) {
        model.addAttribute("sid", id);
        model.addAttribute("id",id);
        model.addAttribute("pId",pId);
       return new ModelAndView("/admin/service/packDetailBuy");
    }
    
    /**
     * 代充服务包页面
     * 
     */
    @RequestMapping(value = "/daichongBuy", method = RequestMethod.GET)
    public ModelAndView daichongBuy(Long sid, Long pId, Model model) {
      //如果服务包价格为零，就直接购买。
    	ServicePackage servicePackage=servicePackageService.find(sid);
	 	if(servicePackage.getPrice()==null || servicePackage.getPrice().equals(new BigDecimal("0.00"))){
	 		   DoctorProfile doctorProfile=patientProfileService.find(pId).getDoctor();
	 		   PatientWalletHistory history = new PatientWalletHistory();
	 		   		  history.setPayNo("");
		              history.setType(PatientWalletHistory.Type.daichong);
		              history.setSuccess(1);  // 
		              history.setPayStyle(1); // TODO 支付类型枚举
		              history.setMoney(new BigDecimal("0.00"));
		              history.setUid(pId);
		              history.setVerdict(sid+"");
		              history.setOid(0L);
		              history.setTradeNo("");
		              history.setDcDid(doctorProfile.getId());
		              history.setDefinition(servicePackage.getTitle());
		              history.setCreated(System.currentTimeMillis()/1000-1);
		       patientWalletHistoryService.save(history);
		       model.addAttribute("payment", history);
		       Message message= servicePackageService.pay(servicePackage, pId,doctorProfile.getId());
		       return new ModelAndView("/admin/patient/payment/notify1");
	 	   }
        model.addAttribute("sid", sid);
        model.addAttribute("pId",pId);
        model.addAttribute("paymentPlugins", pluginService.getPaymentPlugins(true));
        model.addAttribute("data", servicePackage);
        //服务包价格
        model.addAttribute("price",servicePackage.getPrice());
 	   return new ModelAndView("/admin/service/daichongBuy");
    }

    /**
     * 代充记录（分页）
     * 
     */
    @RequestMapping(value = "/daichongLogPage", method = RequestMethod.POST)
    @ResponseBody
    public Page<ServiceOrder> daichongLogPage(Date startDate, Date endDate, Pageable pageable) {
        List<Filter> filters = new ArrayList<Filter>();
        Principal principal=getLoginId();
        filters.add(Filter.eq("did", principal.getId()));

        if (startDate != null) {
            filters.add(Filter.ge("created", startDate.getTime() / 1000));
        }
        if (endDate != null) {
            filters.add(Filter.le("created", DateUtils.addDays(endDate, 1).getTime() / 1000));
        }

        pageable.setFilters(filters);
        pageable.setOrderProperty("created");
        pageable.setOrderDirection(Order.Direction.desc);
        return serviceOrderService.findPage(pageable);
    }
    
    
    @ModelAttribute
    public void initData(@RequestParam(value = "id", defaultValue = "-1") Long id,
                         HttpServletRequest request, Model model) {
        String url = request.getRequestURI();
        String from = request.getHeader("Referer");
        Principal principal = getLoginId();

        if (id != -1) {

            //服务项编辑页面-编辑
            if (url.contains("/service/toPage/edit")) {
                ServiceType serviceType = serviceTypeService.find(id);
                model.addAttribute("data", serviceType);
            }

            //服务包编辑页面
            if (url.contains("/service/toPage/package1")) {
                //服务包信息
                ServicePackage servicePackage = servicePackageService.find(id);
                model.addAttribute("data", servicePackage);
            } else if (url.contains("/service/toPage/buy")) {//打开购买页面
                //服务包信息
                ServicePackage servicePackage = servicePackageService.find(id);
                model.addAttribute("data", servicePackage);

                PatientWallet wallet = patientWalletService.find(principal.getId());

                Boolean isEnoughPay = false;
                BigDecimal accountBalance = new BigDecimal(0);

                if (wallet != null) {
                    isEnoughPay = (wallet.getTotal().compareTo(servicePackage.getPrice()) >= 0);
                    accountBalance = wallet.getTotal();
                }

                Map params = new HashMap<>();
                //是否足够去支付
                params.put("isEnoughPay", isEnoughPay);
                //账户余额
                params.put("accountBalance", accountBalance);
                //当前钱包信息
                model.addAttribute("wallet", params);

            } else if (url.contains("/service/toPage/pay")) {//打开支付页面
                //存放密钥
                model.addAllAttributes(((RSAService) SpringUtils.getBean("rsaServiceImpl")).getModulusAndExponent());
            }

        } else {
            //服务发布、服务包页面，需要检验机构账号、钱包是否已经激
            if ((url.contains("/service/toPage/package1") || url.contains("/service/toPage/publish")) && principal.getUserType() == UserType.org) {
                DoctorProfile doctorProfile = doctorProfileService.find(principal.getId());

                //机构未初始化未完成
                if (doctorProfile.getOrg() == null) {
                    model.addAttribute("orgInit", false);
                    model.addAttribute("orgWalletInit", false);
                } else {
                    model.addAttribute("orgInit", true);
                    model.addAttribute("orgWalletInit", doctorProfile.getOrg().getIsWalletActive());
                }
            }
        }
    }
}
