package com.borsam.service.wf.impl;

import com.borsam.pojo.security.Principal;
import com.borsam.pojo.wf.WfException;
import com.borsam.pub.UserType;
import com.borsam.repository.dao.admin.AdminDao;
import com.borsam.repository.dao.doctor.DoctorAccountDao;
import com.borsam.repository.dao.patient.PatientAccountDao;
import com.borsam.repository.dao.wf.ActDao;
import com.borsam.repository.dao.wf.ActorDao;
import com.borsam.repository.dao.wf.WfInstDao;
import com.borsam.repository.dao.wf.WorkItemDao;
import com.borsam.repository.entity.admin.Admin;
import com.borsam.repository.entity.doctor.DoctorAccount;
import com.borsam.repository.entity.doctor.DoctorProfile;
import com.borsam.repository.entity.patient.PatientAccount;
import com.borsam.repository.entity.patient.PatientProfile;
import com.borsam.repository.entity.wf.Act;
import com.borsam.repository.entity.wf.Actor;
import com.borsam.repository.entity.wf.WfInst;
import com.borsam.repository.entity.wf.WorkItem;
import com.borsam.service.wf.RuntimeService;
import com.hiteam.common.web.I18Util;
import org.apache.commons.beanutils.MethodUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import javax.persistence.LockModeType;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Service - 流程运行服务类
 * Created by Sebarswee on 2015/7/3.
 */
@Transactional
@Service("runtimeServiceImpl")
public class RuntimeServiceImpl implements RuntimeService {

    private static final String PREFIX = "[WorkItem]";

    @Resource(name = "actDaoImpl")
    private ActDao actDao;

    @Resource(name = "actorDaoImpl")
    private ActorDao actorDao;

    @Resource(name = "wfInstDaoImpl")
    private WfInstDao wfInstDao;

    @Resource(name = "workItemDaoImpl")
    private WorkItemDao workItemDao;

    @Resource(name = "adminDaoImpl")
    private AdminDao adminDao;

    @Resource(name = "doctorAccountDaoImpl")
    private DoctorAccountDao doctorAccountDao;

    @Resource(name = "patientAccountDaoImpl")
    private PatientAccountDao patientAccountDao;

    @Transactional
    @Override
    public Long start(String wfCode, Map<String, String> participant, Map<String, String> datas, Boolean isPush) {
        // 生成流程实例
        WfInst wfInst = new WfInst();
        wfInst.setWfCode(wfCode);
        wfInst.setStatus(WfInst.Status.running);
        wfInst.setTime(0L);
        wfInstDao.persist(wfInst);

        Act startAct = actDao.getStartAct(wfCode);  // 开始活动
        Act nextAct = actDao.getAct(wfCode, startAct.getSeq() + 1);

        if (nextAct == null) {
            throw new WfException(WfException.Code.undefineAct, "未定义的环节");
        }

        String currentActor = this.getCurrentActor();
        participant.put("", currentActor);
        List<Actor> actors = this.initParticipants(wfInst.getId(), nextAct, participant);

        List<WorkItem> workItems = new ArrayList<WorkItem>();
        for (Actor actor : actors) {
            WorkItem workItem = new WorkItem();

            workItem.setWfCode(wfCode);
            workItem.setAct(nextAct);
            workItem.setActorId(actor.getActorId());
            workItem.setActorName(actor.getActorName());
            workItem.setGuid(wfInst.getId());
            workItem.setStatus(WorkItem.Status.running);
            workItem.setTime(0L);
            workItem.setType(actor.getType());
            recordWorkItemResult(workItem, datas);
            workItemDao.persist(workItem);
            workItems.add(workItem);
        }

        // 完成工作项
        this.finishWork(workItems, false);

        /*
         * 根据是否推动流程，决定是停留在当前环节，还是推动到下一环节
         */
        if (isPush) {
            // 清除默认参与者
            participant.remove("");
            nextAct = actDao.getAct(wfCode, nextAct.getSeq() + 1);
        }
        actors = this.initParticipants(wfInst.getId(), nextAct, participant);
        for (Actor actor : actors) {
            WorkItem workItem = new WorkItem();

            workItem.setWfCode(wfCode);
            workItem.setAct(nextAct);
            workItem.setActorId(actor.getActorId());
            workItem.setActorName(actor.getActorName());
            workItem.setGuid(wfInst.getId());
            workItem.setStatus(WorkItem.Status.running);
            workItem.setTime(0L);
            workItem.setType(actor.getType());
            workItemDao.persist(workItem);
        }
        return wfInst.getId();
    }

    @Transactional
    @Override
    public boolean push(Long guid, Map<String, String> participant, Map<String, String> datas) {
        Subject subject = SecurityUtils.getSubject();
        if (subject != null) {
            Principal principal = (Principal) subject.getPrincipal();
            if (principal != null) {
                UserType userType = principal.getUserType();
                Long userId = principal.getId();
                WorkItem workItem = this.getCurrentWorkItem(guid, userType, userId);
                if (workItem == null) {
                    throw new WfException(WfException.Code.unexisWorkItem, "当前工作项不存在");
                }

                Act act = workItem.getAct();
                Act nextAct = actDao.getAct(act.getWfCode(), act.getSeq() + 1);
                if (nextAct == null) {
                    throw new WfException(WfException.Code.undefineAct, "未定义的环节");
                }

                this.push(workItem, nextAct, userId, participant, datas);
                return true;
            }
        }
        return false;
    }

    @Transactional
    @Override
    public boolean forcePush(Long guid, Map<String, String> participant, Map<String, String> datas) {
        Subject subject = SecurityUtils.getSubject();
        if (subject != null) {
            Principal principal = (Principal) subject.getPrincipal();
            if (principal != null) {
                Long userId = principal.getId();
                List<WorkItem> workItems = workItemDao.findUnFinishedWorkItems(guid);
                if (workItems == null || workItems.isEmpty()) {
                    throw new WfException(WfException.Code.unexisWorkItem, "当前工作项不存在");
                }
                WorkItem workItem = workItems.get(0);
                Act act = workItem.getAct();
                Act nextAct = actDao.getAct(act.getWfCode(), act.getSeq() + 1);
                if (nextAct == null) {
                    throw new WfException(WfException.Code.undefineAct, "未定义的环节");
                }

                this.push(workItem, nextAct, userId, participant, datas);
                return true;
            }
        }
        return false;
    }

    @Transactional
    @Override
    public boolean back(Long guid, Map<String, String> participant, Map<String, String> datas) {
        Subject subject = SecurityUtils.getSubject();
        if (subject != null) {
            Principal principal = (Principal) subject.getPrincipal();
            if (principal != null) {
                UserType userType = principal.getUserType();
                Long userId = principal.getId();
                WorkItem workItem = this.getCurrentWorkItem(guid, userType, userId);
                if (workItem == null) {
                    throw new WfException(WfException.Code.unexisWorkItem, "当前工作项不存在");
                }

                Act act = workItem.getAct();
                Act nextAct = actDao.getAct(act.getWfCode(), act.getSeq() - 1);
                if (nextAct == null) {
                    throw new WfException(WfException.Code.undefineAct, "未定义的环节");
                }

                this.push(workItem, nextAct, userId, participant, datas);
                return true;
            }
        }
        return false;
    }

    @Transactional
    @Override
    public boolean forceBack(Long guid, Map<String, String> participant, Map<String, String> datas) {
        Subject subject = SecurityUtils.getSubject();
        if (subject != null) {
            Principal principal = (Principal) subject.getPrincipal();
            if (principal != null) {
                Long userId = principal.getId();
                List<WorkItem> workItems = workItemDao.findUnFinishedWorkItems(guid);
                if (workItems == null || workItems.isEmpty()) {
                    throw new WfException(WfException.Code.unexisWorkItem, "当前工作项不存在");
                }

                WorkItem workItem = workItems.get(0);
                Act act = workItem.getAct();
                Act nextAct = actDao.getAct(act.getWfCode(), act.getSeq() - 1);
                if (nextAct == null) {
                    throw new WfException(WfException.Code.undefineAct, "未定义的环节");
                }

                this.push(workItem, nextAct, userId, participant, datas);
                return true;
            }
        }
        return false;
    }

    @Transactional
    @Override
    public boolean push(Long guid, Act next, Map<String, String> participant, Map<String, String> datas) {
        Subject subject = SecurityUtils.getSubject();
        if (subject != null) {
            Principal principal = (Principal) subject.getPrincipal();
            if (principal != null) {
                UserType userType = principal.getUserType();
                Long userId = principal.getId();
                WorkItem workItem = this.getCurrentWorkItem(guid, userType, userId);
                if (workItem == null) {
                    throw new WfException(WfException.Code.unexisWorkItem, "当前工作项不存在");
                }

                this.push(workItem, next, userId, participant, datas);
                return true;
            }
        }
        return false;
    }

    @Transactional
    @Override
    public boolean push(Long guid, String actCode, Map<String, String> participant, Map<String, String> datas) {
        WfInst wfInst = wfInstDao.find(guid);
        Act next = actDao.getAct(wfInst.getWfCode(), actCode);
        return this.push(guid, next, participant, datas);
    }

    /**
     * 初始化参与者
     */
    private List<Actor> initParticipants(Long guid, Act act, Map<String, String> nextParticipant) {
        List<Actor> allActors = new ArrayList<Actor>();
        String participant = this.getParticipant(act, nextParticipant);

        // 1. 从页面传来的配置参与者
        List<Actor> definedActors = this.getDefinedParticipant(act, participant);
        allActors.addAll(definedActors);

        // 2. 从页面传来的普通参与者
        if (allActors.isEmpty()) {
            allActors.addAll(this.findParticipantFromPage(participant));
        }

        // 3. 找该环节的历史执行者
        if (allActors.isEmpty()) {
            allActors.addAll(this.findParticipantFromHistory(guid, act));
        }

        // 4. 找该环节的配置参与者
        if (allActors.isEmpty()) {
            allActors.addAll(this.findParticipantActors(act));
        }
        filterSameActor(allActors);
        return allActors;
    }

    /**
     * 获取当前登录人员的参与者格式串
     * @return 当前参与者
     */
    private String getCurrentActor() {
        Subject subject = SecurityUtils.getSubject();
        if (subject != null) {
            Principal principal = (Principal) subject.getPrincipal();
            if (principal != null) {
                switch (principal.getUserType()) {
                    case admin:
                        return "A(" + principal.getId() + ")";
                    case org:
                        return "O(" + principal.getId() + ")";
                    case doctor:
                        return "D(" + principal.getId() + ")";
                    case patient:
                        return "P(" + principal.getId() + ")";
                }
            }
        }
        return null;
    }

    /**
     * 获取当前登录人员ID
     * @return 当前登录人员ID
     */
    private Long getCurrentId() {
        Subject subject = SecurityUtils.getSubject();
        if (subject != null) {
            Principal principal = (Principal) subject.getPrincipal();
            if (principal != null) {
                return principal.getId();
            }
        }
        return null;
    }

    /**
     * 获取从页面传来的参与者
     * @param act 活动环节
     * @param orgParticipant 页面传来的配置参与者
     * @return 参与者
     */
    private String getParticipant(Act act, Map<String, String> orgParticipant) {
        String participant = "";

        if (orgParticipant == null || orgParticipant.isEmpty()) {
            return participant;
        }

        String code = act.getActCode();	// 活动代码

        // 是否存在与活动代码一样的参与者
        if (orgParticipant.keySet().contains(code)) {
            participant = orgParticipant.get(code);
        }

        // 如果找不到对应活动的参与者，则取默认参与者
        if (StringUtils.isEmpty(participant)) {
            participant = orgParticipant.get("");
        }

        return participant.trim();
    }

    /**
     * 获取配置的参与者
     * @param act 活动环节
     * @param participant 页面传来的配置参与者
     * @return 参与者
     */
    private List<Actor> getDefinedParticipant(Act act, String participant) {
        List<Actor> actorList = new ArrayList<Actor>();

        if (StringUtils.isEmpty(participant)) {
            return actorList;
        }

        // 是否以"["开头，以"]"结尾
        Pattern p = Pattern.compile("^\\[.*\\]$");
        Matcher m = p.matcher(participant);
        if (!m.matches()) {
            return actorList;
        }

        if (!"DEFINITION".equals(m.group(1))) {
            return actorList;
        }

        actorList = findParticipantActors(act);

        if (actorList == null || actorList.isEmpty()) {
            throw new WfException(WfException.Code.canFindActor, "找不到配置的参与者列表！");
        }

        return actorList;
    }

    /**
     * 查找活动历史执行者
     */
    private List<Actor> findParticipantFromHistory(Long guid, Act act) {
        List<Actor> actorList = new ArrayList<Actor>();
        List<WorkItem> workItems = workItemDao.getLastWorkItems(guid, act);

        if (null == workItems || workItems.isEmpty()) {
            return actorList;
        }

        for (WorkItem workItem : workItems) {
            Actor.Type type = workItem.getType();
            Long actorId = workItem.getExeId();
            Actor actor = new Actor();

            switch (type) {
                case admin:
                    Admin admin = adminDao.find(actorId);
                    actor.setActorName(admin.getName());
                    break;
                case organization:
                    DoctorAccount orgAccount = doctorAccountDao.find(actorId);
                    DoctorProfile orgProfile = orgAccount.getDoctorProfile();
                    actor.setActorName((orgProfile != null && StringUtils.isNotEmpty(orgProfile.getName())) ? orgProfile.getName() : orgAccount.getEmail());
                    break;
                case doctor:
                    DoctorAccount doctorAccount = doctorAccountDao.find(actorId);
                    DoctorProfile doctorProfile = doctorAccount.getDoctorProfile();
                    actor.setActorName((doctorProfile != null && StringUtils.isNotEmpty(doctorProfile.getName())) ? doctorProfile.getName() : doctorAccount.getEmail());
                    break;
                case patient:
                    PatientAccount patientAccount = patientAccountDao.find(actorId);
                    PatientProfile patientProfile = patientAccount.getPatientProfile();
                    actor.setActorName((patientProfile != null && StringUtils.isNotEmpty(patientProfile.getName())) ? patientProfile.getName() : patientAccount.getEmail());
                    break;

            }
            actor.setType(type);
            actor.setActorId(actorId);
            actorList.add(actor);
        }

        return actorList;
    }

    /**
     * 从活动配置找参与者
     * @param act 活动环节
     * @return 参与者
     */
    private List<Actor> findParticipantActors(Act act) {
        List<Actor> actorList = new ArrayList<Actor>();
        List<Actor> actors = new ArrayList<Actor>(act.getActors());
        findParticipantActors(actorList, actors);
        return actorList;
    }

    /**
     * 根据活动配置查找不同类型参与者
     */
    private void findParticipantActors(List<Actor> actorList, List<Actor> actors) {
        for (Actor actor : actors) {
            getParticipantByPerson(actorList, actor);						// 人员
//            getParticipantByPaticipant(actInst, actorList, actor, context);	// 活动参与者
//            getParticipantByExcutor(actInst, actorList, actor,context);		// 活动执行者
        }
    }

    /**
     * 获取人员参与者
     */
    private void getParticipantByPerson(List<Actor> actorList, Actor actor) {
        switch (actor.getType()) {
            case admin:
            case organization:
            case doctor:
            case patient:
                actorList.add(actor);
                break;
        }
    }

    /**
     * 获取从页面传来的参与者
     */
    private List<Actor> findParticipantFromPage(String participant) {
        List<Actor> actorList = new ArrayList<Actor>();

        if (!StringUtils.isEmpty(participant)) {
            actorList.addAll(this.splitParticipant(participant));
        }

        return actorList;
    }

    /**
     * 拆分参与者
     */
    private List<Actor> splitParticipant(String orgParticipant) {
        List<Actor> actorList = new ArrayList<Actor>();

        if (!StringUtils.isEmpty(orgParticipant)) {
            String[] participans = orgParticipant.split(",");

            for (String participan : participans) {
                getParticipantByPersion(actorList, participan);
            }
        } else {
            throw new WfException(WfException.Code.canFindActor, "找不到参与者！");
        }

        return actorList;
    }

    /**
     * 获取人员参与者
     */
    private void getParticipantByPersion(List<Actor> list, String participan) {
        Actor actor = new Actor();

        Pattern p = Pattern.compile("([A,O,D,P]?)\\((.*?)\\)");
        Matcher m = p.matcher(participan);
        while (m.find()) {
            String type = m.group(1);
            Long actorId = Long.valueOf(m.group(2));

            switch (type.toCharArray()[0]) {
                case 'A':
                    Admin admin = adminDao.find(actorId);
                    actor.setType(Actor.Type.admin);
                    actor.setActorId(admin.getId());
                    actor.setActorName(admin.getName());
                    break;
                case 'O':
                    DoctorAccount orgAccount = doctorAccountDao.find(actorId);
                    DoctorProfile orgProfile = orgAccount.getDoctorProfile();
                    actor.setType(Actor.Type.organization);
                    actor.setActorId(orgAccount.getId());
                    actor.setActorName((orgProfile != null && StringUtils.isNotEmpty(orgProfile.getName())) ? orgProfile.getName() : orgAccount.getEmail());
                    break;
                case 'D':
                    DoctorAccount doctorAccount = doctorAccountDao.find(actorId);
                    DoctorProfile doctorProfile = doctorAccount.getDoctorProfile();
                    actor.setType(Actor.Type.doctor);
                    actor.setActorId(doctorAccount.getId());
                    actor.setActorName((doctorProfile != null && StringUtils.isNotEmpty(doctorProfile.getName())) ? doctorProfile.getName() : doctorAccount.getEmail());
                    break;
                case 'P':
                    PatientAccount patientAccount = patientAccountDao.find(actorId);
                    actor.setType(Actor.Type.patient);
                    actor.setActorId(patientAccount.getId());
                    PatientProfile patientProfile = patientAccount.getPatientProfile();
                    actor.setActorName((patientProfile != null && StringUtils.isNotEmpty(patientProfile.getName())) ? patientProfile.getName() : patientAccount.getEmail());
                    break;
            }

            list.add(actor);
        }
    }

    /**
     * 过滤重复参与者
     */
    private void filterSameActor(List<Actor> actors) {
        if (null == actors || actors.isEmpty()) {
            return;
        }

        List<Actor> newActors = new ArrayList<Actor>();
        boolean isAdd = true;
        for (Actor actor : actors) {
            for (Actor newActor : newActors) {
                if (actor.getType().equals(newActor.getType()) && actor.getActorId().equals(newActor.getActorId())) {
                    isAdd = false;
                    break;
                }
            }
            if (isAdd) {
                newActors.add(actor);
            }
        }
        actors.clear();
        actors.addAll(newActors);
    }

    /**
     * 设置工作项数据
     */
    private void recordWorkItemResult(WorkItem workItem, Map<String, String> data) {
        if (null == data || data.isEmpty()) {
            return;
        }
        Map<String, String> newData = new HashMap<String, String>();
        for (Iterator<String> it = data.keySet().iterator(); it.hasNext();) {
            String key = it.next();
            if (!key.contains(PREFIX)) {
                newData.put(key, data.get(key));
                continue;
            }
            invokeMethod(workItem, data, key);
        }
        data.clear();
        data.putAll(newData);
    }

    /**
     * 反射方式设置工作项数据
     */
    private void invokeMethod(WorkItem workItem, Map<String, String> data, String key) {
        String[] worlds = key.split("\\.");
        String postfixKey = StringUtils.capitalize(worlds[1]);
        String value = I18Util.getMessage(StringUtils.trimToEmpty(data.get(key)));
        if ("Remark".equals(postfixKey)) {
            if (value != null && value.length() > 200) {
                value = value.substring(0, 200) + "……";
            }
        }
        try {
            MethodUtils.invokeMethod(workItem, "set" + postfixKey, new String[]{ value }, new Class[]{ String.class });
        } catch (Exception e) {
            throw new WfException(WfException.Code.wfError, e.getMessage(), e);
        }
    }

    /**
     * 完成工作项
     */
    private void finishWork(List<WorkItem> workItems, boolean lock) {
        Long exeId = this.getCurrentId();
        for (WorkItem workItem : workItems) {
            if (lock) {
                // 锁定
                workItemDao.lock(workItem, LockModeType.PESSIMISTIC_WRITE);
            }
            workItem.setExeId(exeId);
            workItem.setStatus(WorkItem.Status.finished);
            workItemDao.merge(workItem);
        }
    }

    /**
     * 获取当前正在运行的工作项
     */
    private WorkItem getCurrentWorkItem(Long guid, UserType userType, Long userId) {
        Actor.Type type = null;
        switch (userType) {
            case admin:
                type = Actor.Type.admin;
                break;
            case org:
                type = Actor.Type.organization;
                break;
            case doctor:
                type = Actor.Type.doctor;
                break;
            case patient:
                type = Actor.Type.patient;
                break;
        }
        return workItemDao.findRunningWorkItem(guid, type, userId);
    }

    /**
     * 推动流程
     */
    private void push(WorkItem workItem, Act next, Long userId, Map<String, String> participant, Map<String, String> datas) {
        if (canPush(workItem)) {
            Long guid = workItem.getGuid();
            String wfCode = workItem.getWfCode();
            // 锁定工作项，防止并发
            workItemDao.lock(workItem, LockModeType.PESSIMISTIC_WRITE);
            // 记录工作项信息
            this.recordWorkItemResult(workItem, datas);
            // 完成工作项
            workItem.setStatus(WorkItem.Status.finished);
            workItem.setExeId(userId);
            workItemDao.merge(workItem);
            // 结束其他工作项
            List<WorkItem> unFinishedWorkItems = workItemDao.findUnFinishedWorkItems(guid);
            if (unFinishedWorkItems != null && !unFinishedWorkItems.isEmpty()) {
                for (WorkItem unFinishedWorkItem : unFinishedWorkItems) {
                    if (!WorkItem.Status.finished.equals(unFinishedWorkItem.getStatus())) {
                        workItemDao.lock(unFinishedWorkItem, LockModeType.PESSIMISTIC_WRITE);
                        unFinishedWorkItem.setStatus(WorkItem.Status.finished);
                        workItemDao.merge(unFinishedWorkItem);
                    }
                }
            }

            if (!Act.Type.end.equals(next.getType())) {
                // 推动流程
                List<Actor> actors = this.initParticipants(guid, next, participant);
                for (Actor actor : actors) {
                    WorkItem w = new WorkItem();

                    w.setWfCode(wfCode);
                    w.setAct(next);
                    w.setActorId(actor.getActorId());
                    w.setActorName(actor.getActorName());
                    w.setGuid(guid);
                    w.setStatus(WorkItem.Status.running);
                    w.setTime(0L);
                    w.setType(actor.getType());
                    workItemDao.persist(w);
                }
            } else {
                // 流程归档
                WfInst wfInst = wfInstDao.find(guid);
                wfInst.setStatus(WfInst.Status.finished);
                wfInstDao.merge(wfInst);
            }
        }
    }

    /**
     * 判断流程是否可以推动
     */
    private boolean canPush(WorkItem workItem) {
        if (!workItem.getStatus().equals(WorkItem.Status.running)) {
            throw new WfException(WfException.Code.workItemStatus, "工作项[" + workItem.getId() + "]为非运行状态！");
        }
        return true;
    }

}
