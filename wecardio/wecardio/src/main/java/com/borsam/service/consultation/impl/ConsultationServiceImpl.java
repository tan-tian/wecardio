package com.borsam.service.consultation.impl;

import com.borsam.repository.dao.consultation.ConsultationDao;
import com.borsam.repository.dao.doctor.DoctorProfileDao;
import com.borsam.repository.dao.forum.ForumInfoDao;
import com.borsam.repository.dao.org.OrganizationDao;
import com.borsam.repository.dao.org.OrganizationWalletDao;
import com.borsam.repository.dao.org.OrganizationWalletHistoryDao;
import com.borsam.repository.entity.consultation.Consultation;
import com.borsam.repository.entity.doctor.DoctorAccount;
import com.borsam.repository.entity.doctor.DoctorProfile;
import com.borsam.repository.entity.forum.ForumInfo;
import com.borsam.repository.entity.org.Organization;
import com.borsam.repository.entity.patient.PatientAccount;
import com.borsam.service.consultation.ConsultationService;
import com.borsam.service.doctor.DoctorAccountService;
import com.borsam.service.patient.PatientAccountService;
import com.borsam.service.wf.ProcessEngine;
import com.hiteam.common.base.service.impl.BaseServiceImpl;
import com.hiteam.common.util.ConfigUtils;
import com.hiteam.common.util.http.HttpClientUtils;
import com.hiteam.common.util.json.JsonUtils;
import com.hiteam.common.web.template.freemarker.FreemarkerUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import javax.persistence.LockModeType;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Service - 会诊申请
 * Created by tantian on 2015/7/24.
 */
@Service("consultationServiceImpl")
public class ConsultationServiceImpl extends BaseServiceImpl<Consultation, Long> implements ConsultationService {

    @Resource(name = "processEngineImpl")
    private ProcessEngine processEngine;

    @Resource(name = "doctorAccountServiceImpl")
    private DoctorAccountService doctorAccountService;

    @Resource(name = "patientAccountServiceImpl")
    private PatientAccountService patientAccountService;

    @Resource(name = "doctorProfileDaoImpl")
    private DoctorProfileDao doctorProfileDao;

    @Resource(name = "forumInfoDaoImpl")
    private ForumInfoDao forumInfoDao;

    @Resource(name = "organizationDaoImpl")
    private OrganizationDao organizationDao;

    @Resource(name = "organizationWalletDaoImpl")
    private OrganizationWalletDao organizationWalletDao;

    @Resource(name = "organizationWalletHistoryDaoImpl")
    private OrganizationWalletHistoryDao  organizationWalletHistoryDao;

    @Resource(name = "consultationDaoImpl")
    public void setBaseDao(ConsultationDao consultationDao) {
        super.setBaseDao(consultationDao);
    }

    @Transactional
    @Override
    public void edit(Long cid, String qt, String qtc, String pr, String qrs, String rr, String opinion) {
        Consultation consultation = this.find(cid);
        DoctorAccount doctorAccount = doctorAccountService.getCurrent();
        DoctorProfile doctorProfile = doctorAccount.getDoctorProfile();

        // 设置编辑医生ID
        consultation.setEditDoctor(doctorProfile);

        if ((doctorProfile.getRoles() & 8) != 8) {
            /**
             * 不是编辑医生进行编辑操作，流程无需再经过审核环节，直接归档
             */
            processEngine.pushProcessInstance(consultation.getGuid(), "end", null, new HashMap<String, String>() {
                {
                    put("[WorkItem].result", "");
                    put("[WorkItem].remark", opinion);
                }
            });
            // 设置状态为完成
            consultation.setState(2);
            // 钱包支付的诊单，需要审核通过后需要计算机构收入
            if (Consultation.PayKind.deposit.equals(consultation.getPayKind())) {
                // 设置状态为已结算
                consultation.setStatus(1);
                // 调整机构收入及分成后的收入
                organizationWalletDao.adjustTotalInSellService(consultation.getOrg(), consultation.getPrice());
                // 新增机构销售流水记录
                organizationWalletHistoryDao.addLogInConsultation(consultation.getOrg(), consultation.getPrice(), consultation.getPatient().getId(), consultation.getTradeNo(), consultation.getServiceTypeName());
            }
        } else {
            /**
             * 正常推动到审核环节
             * 参与者为机构的所有审核医生加上主治医生
             */
            List<DoctorProfile> doctors = doctorProfileDao.findAuditDoctor(consultation.getOrg().getId());
            List<DoctorProfile> mainDoctors = doctorProfileDao.findMainDoctor(consultation.getOrg().getId());

            String participant = "";
            if (doctors != null && !doctors.isEmpty()) {
                participant += getParticipant(doctors);
            }
            if (mainDoctors != null && !mainDoctors.isEmpty()) {
                if (StringUtils.isNotEmpty(participant)) {
                    participant += ",";
                }
                participant += getParticipant(mainDoctors);
            }
            // 设置参与者
            Map<String, String> participants = new HashMap<String, String>();
            participants.put("audit", participant);
            processEngine.pushProcessInstance(consultation.getGuid(), participants, new HashMap<String, String>() {
                {
                    put("[WorkItem].result", "");
                    put("[WorkItem].remark", opinion);
                }
            });

            // 设置状态为待审核
            consultation.setState(1);
        }
        // 医嘱
        consultation.setDoctorOpinion(opinion);
        // 更新诊单信息
        this.update(consultation);
    }

    /**
     * 转化成参与者格式
     * @param doctors 医生列表
     * @return 参与者
     */
    private String getParticipant(List<DoctorProfile> doctors) {
        String participant = "";
        for (DoctorProfile doctor : doctors) {
            if (StringUtils.isNotEmpty(participant)) {
                participant += ",";
            }
            participant += "D(" + doctor.getId() + ")";
        }
        return participant;
    }

    @Transactional
    @Override
    public void audit(Long cid, Boolean isPass, String remark) {
        Consultation consultation = this.find(cid);
        Long guid = consultation.getGuid();

        // 因在查询列表里面进行审核，该人员不一定在工作项待办里面，需要强制推动
        if (isPass) {
            processEngine.forcePushProcessInstance(guid, null, new HashMap<String, String>() {
                {
                    put("[WorkItem].result", "common.wf.result.pass");
                    put("[WorkItem].remark", remark);
                }
            });
            // 设置状态为已完成
            consultation.setState(2);
            // 钱包支付的诊单，需要审核通过后需要计算机构收入
            if (Consultation.PayKind.deposit.equals(consultation.getPayKind())) {
                // 设置状态为已结算
                consultation.setStatus(1);
                // 调整机构收入及分成后的收入
                organizationWalletDao.adjustTotalInSellService(consultation.getOrg(), consultation.getPrice());
                // 新增机构销售流水记录
                organizationWalletHistoryDao.addLogInConsultation(consultation.getOrg(), consultation.getPrice(), consultation.getPatient().getId(), consultation.getTradeNo(), consultation.getServiceTypeName());
            }
        } else {
            processEngine.forceBackProcessInstance(guid, null, new HashMap<String, String>() {
                {
                    put("[WorkItem].result", "common.wf.result.noPass");
                    put("[WorkItem].remark", remark);
                }
            });
            // 设置审状态为待编辑
            consultation.setState(0);
        }
        // 设置审核医生
        DoctorAccount doctorAccount = doctorAccountService.getCurrent();
        consultation.setAuditDoctor(doctorAccount.getDoctorProfile());
        this.update(consultation);
    }

    @Override
    @Transactional(readOnly = true)
    public String getRemoteFilePath(Long id) {
        Consultation consultation = this.find(id);
        String reportNo = consultation.getReportNo();
        String type, no, fileUrl;

        /**
         * 会诊列表详细页面，当会诊表中report_no为空时，调内部接口时type=0 传的是recorder表中的file_no,
         * 当report_no有值时，说明会诊已经出报告了，这时不再显示自动分析的报告，是会诊的报告，调内部接口时type=1,传的参数是report_no
         */
        if (StringUtils.isNotEmpty(reportNo)) {
            type = "1";
            no = reportNo;
        } else {
            type = "0";
            no = consultation.getRecord().getFileNo();
        }
        try {
            fileUrl = FreemarkerUtils.process(ConfigUtils.config.getProperty("server.url") + ConfigUtils.config.getProperty("fileUrl"), new HashMap<String, String>() {
                {
                    put("type", type);
                    put("no", no);
                }
            });
            String result = HttpClientUtils.invokeGet(fileUrl, null, "utf-8", 5000);
            Map json = JsonUtils.toObject(result, Map.class);
            if ("0".equals(type)) {
                return (String) ((Map) json.get(no)).get("file_report");
            } else if ("1".equals(type)) {
                return (String) json.get(no);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    @Transactional
    public void evaluate(Long cid, ForumInfo forumInfo) {
        if (forumInfo.getScore()  == null || forumInfo.getScore() > 5 || forumInfo.getScore() < 1) {
            throw new RuntimeException("评价分数错误");
        }
        Consultation consultation = this.find(cid);
        DoctorProfile doctorProfile = consultation.getDoctor();
        Organization organization = consultation.getOrg();
        PatientAccount patientAccount = patientAccountService.getCurrent();
        forumInfo.setConsultation(consultation);
        forumInfo.setDid(0L);   // consultation.getDid() 诊单评价只针对机构
        forumInfo.setOid(consultation.getOrg().getId());
        forumInfo.setCreateId(patientAccount.getId());
        forumInfo.setCreateName(patientAccount.getPatientProfile().getName());
        forumInfoDao.persist(forumInfo);

        // 统计评分
        if (doctorProfile != null && doctorProfile.getId() != 0L) {
            doctorProfileDao.lock(doctorProfile, LockModeType.PESSIMISTIC_WRITE);
            doctorProfile.setCommentScore(doctorProfile.getCommentScore() + forumInfo.getScore());
            doctorProfile.setCommentNum(doctorProfile.getCommentNum() + 1);
            doctorProfileDao.merge(doctorProfile);
        }

        // 统计评分
        organizationDao.lock(organization, LockModeType.PESSIMISTIC_WRITE);
        organization.setCommentNum(organization.getCommentNum() + 1);
        organization.setCommentScore(organization.getCommentScore() + forumInfo.getScore());
        organizationDao.merge(organization);
    }
}
