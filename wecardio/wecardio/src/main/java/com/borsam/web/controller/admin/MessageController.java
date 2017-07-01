package com.borsam.web.controller.admin;

import com.borsam.pojo.security.Principal;
import com.borsam.pub.UserType;
import com.borsam.repository.entity.doctor.DoctorProfile;
import com.borsam.repository.entity.message.MessageInfo;
import com.borsam.repository.entity.org.Organization;
import com.borsam.repository.entity.patient.PatientProfile;
import com.borsam.service.doctor.DoctorProfileService;
import com.borsam.service.message.MessageService;
import com.borsam.service.org.OrganizationService;
import com.borsam.service.patient.PatientProfileService;
import com.hiteam.common.base.pojo.search.Filter;
import com.hiteam.common.base.pojo.search.Order;
import com.hiteam.common.base.pojo.search.Page;
import com.hiteam.common.base.pojo.search.Pageable;
import com.hiteam.common.service.enums.EnumService;
import com.hiteam.common.util.lang.DateUtil;
import com.hiteam.common.util.lang.StringUtil;
import com.hiteam.common.util.pojo.EnumBean;
import com.hiteam.common.web.I18Util;
import com.hiteam.common.web.Message;
import com.hiteam.common.web.controller.BaseController;
import com.hiteam.common.web.template.freemarker.FreemarkerUtils;
import freemarker.template.TemplateException;
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
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

/**
 * Controller - 消息管理
 * Created by liujieming on 2015/7/7.
 */
@Controller("adminMessageController")
@RequestMapping("/admin/message")
public class MessageController extends BaseController {

    @Resource(name = "messageServiceImpl")
    private MessageService messageService;

    @Resource(name = "enumServiceImpl")
    private EnumService enumService;

    @Resource(name = "patientProfileServiceImpl")
    private PatientProfileService patientProfileService;

    @Resource(name = "doctorProfileServiceImpl")
    private DoctorProfileService doctorProfileService;

    @Resource(name = "organizationServiceImpl")
    private OrganizationService organizationService;

    /**
     * 管理主页，返回消息列表页面
     */
    @RequestMapping(method = RequestMethod.GET)
    public String index(Integer iType, Model model) {
        model.addAttribute("iType", iType);
        return "/admin/message/list";
    }

    /**
     * 详情页面
     */
    @RequestMapping(value = "/{iId}/detail", method = RequestMethod.GET)
    public String detail(@PathVariable Long iId, Model model) {
        Principal principal = getLoginId();
        String userType = principal.getUserType().name();
        model.addAttribute("userType", userType);
        model.addAttribute("entity", messageService.find(iId));
        return "/admin/message/detail";
    }

    /**
     * 分页查询
     */
    @RequestMapping(value = "/page", method = RequestMethod.POST)
    @ResponseBody
    public List<MessageInfo> page(String name,Date startDate, Date endDate, MessageInfo.MessageType[] type, Long org, Pageable pageable) {
        Page<MessageInfo> page = null;

        Long objId = null;
        UserType objType = null;

        Principal principal = getLoginId();
        switch (principal.getUserType()) {
            case org:
                Organization nowOrg = doctorProfileService.find(principal.getId()).getOrg();
                objId = nowOrg == null ? -1L : nowOrg.getId();
                objType = principal.getUserType();
                break;
            case admin:
                objType = principal.getUserType();
                break;
            default:
                objId = principal.getId();
                objType = principal.getUserType();
                break;
        }

        page = messageService.queryMessage(name, startDate, endDate, type, org,objId,objType, pageable);
        enumService.transformEnum(page.getContent());

        List<MessageInfo> messageInfoList = page.getContent();
        MessageInfo messageInfo = null;
        List<MessageInfo> newMessageInfoList = new ArrayList<MessageInfo>();
        if (messageInfoList!=null&&messageInfoList.size()>0) {
            for (int i = 0; i < messageInfoList.size(); i++) {
                messageInfo = messageInfoList.get(i);

                processTemplate(messageInfo);

                messageInfo.setTotal(page.getTotal());
                if (messageInfo.getFrom_type().equals(UserType.org)){
                    if(messageInfo.getFrom_id()!=null)
                    {
                        Organization organization = organizationService.find(messageInfo.getFrom_id());
                        if (organization!=null){
                            messageInfo.setFrom_HeadPath(organization.getDoctorPic());
                        }
                    }
                }
                if (messageInfo.getFrom_type().equals(UserType.doctor)){
                    messageInfo.setFrom_HeadPath(doctorProfileService.find(messageInfo.getFrom_id()).getHeadImg());
                }
                if (messageInfo.getFrom_type().equals(UserType.patient)){
                    messageInfo.setFrom_HeadPath(patientProfileService.find(messageInfo.getFrom_id()).getHeadPath());
                }

                if (messageInfo.getTo_type().equals(UserType.org)){
                    messageInfo.setTo_HeadPath(organizationService.find(messageInfo.getTo_id()).getDoctorPic());
                }
                if (messageInfo.getTo_type().equals(UserType.doctor)){
                    messageInfo.setTo_HeadPath(doctorProfileService.find(messageInfo.getTo_id()).getHeadPath());
                }
                if (messageInfo.getTo_type().equals(UserType.patient)){
                    messageInfo.setTo_HeadPath(patientProfileService.find(messageInfo.getTo_id()).getHeadPath());
                }
                newMessageInfoList.add(messageInfo);
            }
        }

        return newMessageInfoList;
    }

    /**
     * 处理消息中的模板,会修改messageInfo中的Content属性
     * @param messageInfo
     */
    private void processTemplate(MessageInfo messageInfo) {
        try {

            if (messageInfo == null || messageInfo.getType() == null) {
                return;
            }

            if (Arrays.binarySearch(MessageInfo.TEMPLATE_TYPE_IDS, messageInfo.getType().ordinal()) < 0) {
                return;
            }

            String messageTemplate = I18Util.getMessage(messageInfo.getContent());

            if (StringUtil.isNotBlank(messageTemplate)) {
                String tempVal = FreemarkerUtils.process2(messageTemplate, messageInfo);
                if (StringUtil.isNotBlank(tempVal)) {
                    messageInfo.setContent(tempVal);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (TemplateException e) {
            e.printStackTrace();
        }
    }

    /**
     * 跳转到客户新增、编辑页
     * @return
     */
    @RequestMapping(value = "/addEdit/{id}", method = RequestMethod.GET)
    public String addEdit(@PathVariable("id") Long id,ModelMap model) throws Exception{
        MessageInfo entity = messageService.find(id);

        String nowTime = "";
        if (entity==null) {
            entity = new MessageInfo();

            nowTime = StringUtil.getNowTime();
            model.addAttribute("nowTime", StringUtil.parses(nowTime, DateUtil.yyyyMMddHHmmss));
        } else {
            model.addAttribute("nowTime", entity.getDateCreate());
        }
        Principal principal = getLoginId();
        switch (principal.getUserType()) {
            case org:
                entity.setFrom_name("message.template.org");
                Organization nowOrg = doctorProfileService.find(principal.getId()).getOrg();
                entity.setFrom_id(nowOrg.getId());
                entity.setFrom_type(principal.getUserType());
                break;
            default:
                entity.setFrom_name("message.template.admin");
                entity.setFrom_id(principal.getId());
                entity.setFrom_type(principal.getUserType());
                break;
        }

        model.addAttribute("id", id);
        model.addAttribute("entity", entity);
        return "/admin/message/addEdit";
    }

    /**
     * 编辑医生提交
     */
    @RequestMapping(value = "/save", method = RequestMethod.POST)
    @ResponseBody
    public Message save(MessageInfo message,Date dateRecevied_time,Long iId) {
        if (!StringUtil.checkObj(message)) {
            message = new MessageInfo();
        }

        if (dateRecevied_time != null) {
            message.setRecevied_time(dateRecevied_time.getTime() / 1000);
        }

        if (iId!=null&&iId>0) {
            message.setId(iId);
            messageService.update(message);
        }
        else {
            message.setiDeal(0);
            messageService.save(message);
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
        messageService.delete(ids);
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
     * 接受/拒绝邀请
     */
    @RequestMapping(value = "/operMessage", method = RequestMethod.POST)
    @ResponseBody
    public Message operMessage(Long iId,Integer iType) {
        MessageInfo message = messageService.find(iId);

        //逻辑：
        //先更新原来的信息的处理状态，然后再新增一个消息
        if(message!=null)
        {
            message.setiDeal(1);
            messageService.update(message);
        }
        MessageInfo newMessage = new MessageInfo();

        //先更新当前操作的消息状态，然后再把这以外的消息设成拒绝的状态一 start
        Principal principal = getLoginId();
        switch (principal.getUserType()) {
            case org:
                newMessage.setFrom_name(principal.getName());
                Organization nowOrg = doctorProfileService.find(principal.getId()).getOrg();
                newMessage.setFrom_id(nowOrg.getId());
                newMessage.setFrom_type(principal.getUserType());
                break;
            default:
                newMessage.setFrom_name(principal.getName());
                newMessage.setFrom_id(principal.getId());
                newMessage.setFrom_type(principal.getUserType());
                break;
        }
        newMessage.setTo_id(message.getFrom_id());
        newMessage.setTo_name(message.getFrom_name());
        newMessage.setTo_type(message.getFrom_type());
        newMessage.setRecevied(0);
        newMessage.setRecevied_time(-1l);

        if (iType==1) {//拒绝
            newMessage.setType(MessageInfo.MessageType.patientRefuse);
//            newMessage.setContent(I18Util.getMessage("common.message.refuseMsg", principal.getName()));
            newMessage.setContent("message.template.key5");
        }

        if (iType==2) {//接受
            PatientProfile patientProfile = null;
            Organization organization = null;
            switch (message.getFrom_type()) {
                case org:
                    patientProfile = patientProfileService.find(message.getTo_id());//改变散户的机构ID同时设置成机构创建
                    organization = organizationService.find(message.getFrom_id());
                    patientProfile.setOrg(organization);
                    patientProfile.setBindType(PatientProfile.BindType.self);
                    patientProfileService.update(patientProfile);
                    break;
                default:
                    patientProfile = patientProfileService.find(message.getTo_id());//改变散户的机构ID同时设置成机构创建
                    DoctorProfile doctorProfile = doctorProfileService.find(message.getFrom_id());
                    patientProfile.setOrg(doctorProfile.getOrg());
                    patientProfile.setDoctor(doctorProfile);
                    patientProfile.setBindType(PatientProfile.BindType.self);
                    patientProfileService.update(patientProfile);
                    break;
            }

            newMessage.setType(MessageInfo.MessageType.patientComfireAdd);
//            newMessage.setContent(I18Util.getMessage("common.message.acceptMsg", principal.getName(), message.getFrom_name()));
            newMessage.setContent("message.template.key4");

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
        }
        messageService.save(newMessage);
        //先更新当前操作的消息状态，然后再把这以外的消息设成拒绝的状态一 end

        //先更新当前操作的消息状态，然后再把这以外的消息设成拒绝的状态二 start
        List<Filter> filters = new ArrayList<Filter>();
        filters.add(Filter.ne("id", message.getId()));
        filters.add(Filter.eq("to_id", message.getTo_id()));
        filters.add(Filter.isNull("iDeal"));
        List<MessageInfo> messageInfoList = messageService.findList(null,filters,null);
        if(messageInfoList!=null&&messageInfoList.size()>0)
        {
            for (int i = 0; i < messageInfoList.size(); i++) {
                MessageInfo unDoMessage = messageInfoList.get(i);
                unDoMessage.setiDeal(1);
                messageService.update(unDoMessage);

                MessageInfo refuseMessage = new MessageInfo();
                //先更新当前操作的消息状态，然后再把这以外的消息设成拒绝的状态一 start
                switch (principal.getUserType()) {
                    case org:
                        refuseMessage.setFrom_name(principal.getName());
                        Organization nowOrg = doctorProfileService.find(principal.getId()).getOrg();
                        refuseMessage.setFrom_id(nowOrg.getId());
                        refuseMessage.setFrom_type(principal.getUserType());
                        break;
                    default:
                        refuseMessage.setFrom_name(principal.getName());
                        refuseMessage.setFrom_id(principal.getId());
                        refuseMessage.setFrom_type(principal.getUserType());
                        break;
                }
                refuseMessage.setTo_id(unDoMessage.getFrom_id());
                refuseMessage.setTo_name(unDoMessage.getFrom_name());
                refuseMessage.setTo_type(unDoMessage.getFrom_type());
                refuseMessage.setRecevied(0);
                refuseMessage.setRecevied_time(-1l);

                //拒绝
                refuseMessage.setType(MessageInfo.MessageType.patientRefuse);
//                refuseMessage.setContent(I18Util.getMessage("common.message.refuseMsg", principal.getName()));
                refuseMessage.setContent("message.template.key5");
                messageService.save(refuseMessage);
                //先更新当前操作的消息状态，然后再把这以外的消息设成拒绝的状态一 end
            }
        }
        //先更新当前操作的消息状态，然后再把这以外的消息设成拒绝的状态二 end

        return SUCCESS_MSG;
    }

    /**
     * 发送邀请
     * @param iId  患者ID
     * @return
     */
    @RequestMapping(value = "/inviteAdd")
    @ResponseBody
    public Message inviteAdd(Long iId) {
        PatientProfile patientProfile = patientProfileService.find(iId);
        MessageInfo newMessage = new MessageInfo();

        Principal principal = getLoginId();
        Organization nowOrg = doctorProfileService.find(principal.getId()).getOrg();
        switch (principal.getUserType()) {
            case org:
                newMessage.setFrom_id(nowOrg.getId());
                newMessage.setFrom_type(principal.getUserType());
                newMessage.setFrom_name(nowOrg.getName());
                newMessage.setContent("message.template.key3");
//                newMessage.setContent(I18Util.getMessage("common.message.invitePatient",nowOrg.getName()));
                break;
            default:
                newMessage.setFrom_name(principal.getName());
                newMessage.setFrom_id(principal.getId());
                newMessage.setFrom_type(principal.getUserType());
                newMessage.setContent("message.template.key3");
//                newMessage.setContent(I18Util.getMessage("common.message.invitePatient",nowOrg.getName()));
                break;
        }

        newMessage.setTo_id(patientProfile.getId());
        newMessage.setTo_name(patientProfile.getFullName());
        newMessage.setTo_type(UserType.patient);
        newMessage.setRecevied(0);
        newMessage.setRecevied_time(-1l);
        newMessage.setiDeal(0);
        newMessage.setType(MessageInfo.MessageType.invitePatient);
        messageService.save(newMessage);

        return SUCCESS_MSG;
    }

    /**
     * 患者列表（for 下拉框）
     */
    @RequestMapping(value = "/select", method = RequestMethod.POST)
    @ResponseBody
    public List<EnumBean> select(Integer to_type,Integer iType, String name) {
        List<EnumBean> resultList = new ArrayList<EnumBean>();

        //规则：当前只有平台管理员和机构现在消息，其中，平台管理员可以给所有人发消息，机构管理员职能给本机构的医生、患者发消息。
        Long iOrgId = null;
        Organization nowOrg = null;
        Principal principal = getLoginId();
        switch (principal.getUserType()) {
            case admin:
                iOrgId = null;
                break;
            case org:
                nowOrg = doctorProfileService.find(principal.getId()).getOrg();
                iOrgId = nowOrg.getId();
                break;
            case doctor:
                nowOrg = doctorProfileService.find(principal.getId()).getOrg();
                iOrgId = nowOrg.getId();
                break;
            case patient:
                nowOrg = patientProfileService.find(principal.getId()).getOrg();
                iOrgId = nowOrg.getId();
                break;
            default:
                break;
        }

        //所选择的接收对象类型:1:机构管理员 2：医生 3：患者
        if(to_type==1)
        {
            resultList = organizationService.sel(name);
        }
        if(to_type==2)
        {
            resultList = doctorProfileService.sel(name,iOrgId,null);
        }
        if(to_type==3)
        {
            resultList = patientProfileService.sel(iOrgId,null,iType, name);
        }
        return resultList;
    }

}
