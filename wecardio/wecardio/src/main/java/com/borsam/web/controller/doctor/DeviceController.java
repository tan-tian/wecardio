package com.borsam.web.controller.doctor;

import com.borsam.pojo.security.Principal;
import com.borsam.pub.UserType;
import com.borsam.repository.entity.device.Gprs;
import com.borsam.repository.entity.device.GprsHistory;
import com.borsam.repository.entity.doctor.DoctorProfile;
import com.borsam.repository.entity.org.Organization;
import com.borsam.repository.entity.patient.PatientActivity;
import com.borsam.repository.entity.patient.PatientDoctorOpinion;
import com.borsam.repository.entity.patient.PatientProfile;
import com.borsam.service.device.GprsHistoryService;
import com.borsam.service.device.GprsService;
import com.borsam.service.doctor.DoctorAccountService;
import com.borsam.service.doctor.DoctorProfileService;
import com.borsam.service.org.OrganizationService;
import com.borsam.service.patient.PatientActivityService;
import com.borsam.service.patient.PatientDoctorOpinionService;
import com.borsam.service.patient.PatientProfileService;
import com.borsam.service.settlement.OrgSettlementService;
import com.hiteam.common.base.pojo.search.Filter;
import com.hiteam.common.base.pojo.search.Order;
import com.hiteam.common.base.pojo.search.Page;
import com.hiteam.common.base.pojo.search.Pageable;
import com.hiteam.common.service.enums.EnumService;
import com.hiteam.common.util.lang.StringUtil;
import com.hiteam.common.util.pojo.EnumBean;
import com.hiteam.common.web.Message;
import com.hiteam.common.web.WebUtil;
import com.hiteam.common.web.controller.BaseController;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.time.DateUtils;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;


/**
 * Controller - 患者管理
 * Created by tantian on 2015/7/7.
 */
@Controller("doctorDeviceController")
@RequestMapping("/doctor/device")
public class DeviceController extends BaseController{

@Resource(name="gprsServiceImpl")
private GprsService gprsService;

@Resource(name="gprsHistoryServiceImpl")
private GprsHistoryService gprsHistoryService;

@Resource(name = "enumServiceImpl")
private EnumService enumService;

@Resource(name = "patientProfileServiceImpl")
private PatientProfileService patientProfileService;

@Resource(name = "doctorProfileServiceImpl")
private DoctorProfileService doctorProfileService;

@Resource(name = "doctorAccountServiceImpl")
private DoctorAccountService doctorAccountService;

@Resource(name = "organizationServiceImpl")
private OrganizationService organizationService;

@Resource(name = "orgSettlementServiceImpl")
private OrgSettlementService orgSettlementService;


/**
 * 卫星holter页面
 */
@RequestMapping(value = "/holter620",method = RequestMethod.GET)
public String holter620(Integer iType, Model model) {
    model.addAttribute("iType", iType);
    return "/doctor/device/holter620";
}

/**
 * 设备编号页面
 */
@RequestMapping(value = "/setMark",method = RequestMethod.GET)
public String setMark(Long imeId,Model model){
	model.addAttribute("imeId",imeId );
	return "/doctor/device/setMark";
}

/**
 * 绑定患者页面
 */
@RequestMapping(value = "/bindPatient",method = RequestMethod.GET)
public String bindPatient(Long imeId,Model model){
	model.addAttribute("imeId",imeId );
	//医生只能操作VIP患者（散户不能）
	model.addAttribute("iType",2);
	return "/doctor/device/bindPatient";
}

/**
 * 绑定患者确认页面
 */
@RequestMapping(value = "/bindSubmitPage",method = RequestMethod.GET)
public String bindSubmitPage(Long imeId,Long pId,Model model){
	
	model.addAttribute("imeId",imeId );
	model.addAttribute("pId",pId);
	model.addAttribute("patientName",patientProfileService.find(pId).getName());
	String ime=gprsService.find(imeId).getIme();
	model.addAttribute("ime",ime);
	return "/doctor/device/bindSubmitPage";
}

/**
 * 绑定患者提交
 */
@RequestMapping(value = "/bindSubmit",method = RequestMethod.POST)
@ResponseBody
public Message bindSubmit(Long imeId,Long pId,String mark){
	Gprs gprs=gprsService.find(imeId);
	PatientProfile patientProfile=patientProfileService.find(pId);
	gprs.setPatient(patientProfile);
	gprsService.update(gprs);
	GprsHistory gprsHistory=new GprsHistory();
	gprsHistory.setDoctor(doctorProfileService.find(doctorAccountService.getCurrent().getId()));
	gprsHistory.setGprs(gprs);
	gprsHistory.setNote(GprsHistory.Type.bind);
	gprsHistory.setOperateTime(null);
	System.out.println("-----"+gprsHistory);
	gprsHistoryService.save(gprsHistory);
	return SUCCESS_MSG;
}

/**
 * 解除绑定页面
 */
@RequestMapping(value = "/removeBindPage",method = RequestMethod.GET)
public String removeBindPage(Long imeId,Model model){
	model.addAttribute("imeId",imeId );
	Gprs gprs=gprsService.find(imeId);
	model.addAttribute("ime", gprs.getIme());
	model.addAttribute("patientName",gprs.getPatientName());
	return "/doctor/device/removeBind";
}

/**
 * 解除绑定提交
 */
@RequestMapping(value = "/removeBindSubmit",method = RequestMethod.POST)
@ResponseBody
public Message removeBindSubmit(Long imeId,String mark){
	Gprs gprs=gprsService.find(imeId);
	gprs.setPatient(null);
	gprsService.update(gprs);
	GprsHistory gprsHistory=new GprsHistory();
	gprsHistory.setDoctor(doctorProfileService.find(doctorAccountService.getCurrent().getId()));
	gprsHistory.setGprs(gprs);
	gprsHistory.setNote(GprsHistory.Type.remove);
	gprsHistory.setOperateTime(null);
	gprsHistoryService.save(gprsHistory);
	return SUCCESS_MSG;
}

/**
 * 查看记录页面
 */
@RequestMapping(value = "/logPage",method = RequestMethod.GET)
public String logPage(Long imeId,Model model){
	model.addAttribute("imeId",imeId );
	return "/doctor/device/log";
}

/**
 * 分页查询（设备操作记录） 
 */
@RequestMapping(value = "/bindLog",method = RequestMethod.POST)
@ResponseBody
public Page<GprsHistory> bindLog(Long imeId,Date startDate, Date endDate,Pageable pageable){
	Page<GprsHistory> page = null;

    List<Filter> filters = new ArrayList<Filter>();
    filters.add(Filter.eq("gprs", gprsService.find(imeId)));

    if (startDate != null) {
        filters.add(Filter.ge("operateTime", startDate.getTime() / 1000));
    }
    if (endDate != null) {
        filters.add(Filter.le("operateTime", DateUtils.addDays(endDate, 1).getTime() / 1000));
    }
    pageable.setFilters(filters);
    pageable.setOrderProperty("operateTime");
    pageable.setOrderDirection(Order.Direction.desc);
    return gprsHistoryService.findPage(pageable);
}
/**
 * 设置编号
 */
@RequestMapping(value = "/submit",method = RequestMethod.POST)
@ResponseBody
public Message submit(Long imeId,String mark){
	Gprs gprs=gprsService.find(imeId);
	gprs.setMark(mark);
	gprsService.update(gprs);
	GprsHistory gprsHistory=new GprsHistory();
	gprsHistory.setDoctor(doctorProfileService.find(doctorAccountService.getCurrent().getId()));
	gprsHistory.setGprs(gprs);
	gprsHistory.setNote(GprsHistory.Type.mark);
	gprsHistory.setOperateTime(null);
	gprsHistoryService.save(gprsHistory);
	return SUCCESS_MSG;
}

/**
 * admin新增设备页面
 */
@RequestMapping(value = "/addDevicePage",method = RequestMethod.GET)
public String addDevicePage(Model model){
	return "/doctor/device/addDevice";
}
/**
 * admin新增设备（提交）
 */
@RequestMapping(value = "/addDeviceSubmit",method = RequestMethod.POST)
@ResponseBody
public Message addDeviceSubmit(String ime,Long org, Long type){
	if(gprsService.isImeExist(ime)){
		return IMEI_MSG;
		}
	//增加设备时默认选择上一次选中的机构
	if(org!=null)
		WebUtil.setSessionData("oidDefault", org);
	else
		WebUtil.setSessionData("oidDefault", null);
	Gprs gprs=new Gprs();
	gprs.setOrganization(organizationService.find(org));
	gprs.setIme(ime);
	gprs.setCreated(System.currentTimeMillis()/1000);
	gprs.setIsDelete(false);
	gprs.setReceiveTotal(0L);
	gprs.setType(type);
	gprsService.save(gprs);
	return SUCCESS_MSG;
}

 /**
 * 分页查询(设备)
 */
@RequestMapping(value = "/page", method = RequestMethod.POST)
@ResponseBody
public Page<Gprs> page(String name, String email, String mobile, Long org,Pageable pageable,Long type) {
    Page<Gprs> page = null;
    
    List<Filter> filters = new ArrayList<Filter>();
    filters.add(Filter.eq("isDelete", false));  // 不查删除的

    if (StringUtils.isNotEmpty(name)) {
        filters.add(Filter.like("patient.fullName", name));
    }
    
    if (type!=null) {
        filters.add(Filter.eq("type", type));
    }
    
    Principal principal = getPrincipal();
    if (org != null) {
        filters.add(Filter.eq("organization", organizationService.find(org)));
    }
    else if(principal.getUserType()!=UserType.admin){
    	
    	DoctorProfile doctorProfile = doctorProfileService.find(principal.getId());
    	 //针对没初始化机构数据的机构，设置
        Organization oid = null;
	    switch (principal.getUserType()) {
	    case org:
	
	       if (doctorProfile.getOrg() != null) {
	            oid = doctorProfile.getOrg();
	        }
	
	        filters.add(Filter.eq("organization", oid));
	        break;
	    case doctor:
	    	if (doctorProfile.getOrg() != null) {
	            oid = doctorProfile.getOrg();
	        }
	    	filters.add(Filter.eq("organization", doctorProfile.getOrg()));
	    	break;
	    default:
	        break;
	    }
    }
    
    
   
	
    pageable.setFilters(filters);

    List<Order> orders = new ArrayList<Order>();
    orders.add(Order.desc("id"));
    pageable.setOrders(orders);

    page = gprsService.findPage(pageable);
    enumService.transformEnum(page.getContent());
    return page;
	}

private Principal getPrincipal() {
    Subject subject = SecurityUtils.getSubject();
    return (Principal) subject.getPrincipal();
}

/**
* 分页查询(记录)
*/
@RequestMapping(value = "/logPage", method = RequestMethod.POST)
@ResponseBody
public Page<GprsHistory> logPage(String name, String email, String mobile, Integer[] doctor, Long[] org, String iType, Pageable pageable) {
   Page<GprsHistory> page = null;

   List<Filter> filters = new ArrayList<Filter>();
   filters.add(Filter.eq("isDelete", false));  // 不查删除的

   if (StringUtils.isNotEmpty(name)) {
       filters.add(Filter.like("patient.fullName", name));
   }
   
   if (org != null && org.length > 0) {
       filters.add(Filter.in("org", org));
   }

   pageable.setFilters(filters);

   List<Order> orders = new ArrayList<Order>();
   orders.add(Order.desc("id"));
   pageable.setOrders(orders);

   page = gprsHistoryService.findPage(pageable);
   enumService.transformEnum(page.getContent());
   return page;
	}
}
