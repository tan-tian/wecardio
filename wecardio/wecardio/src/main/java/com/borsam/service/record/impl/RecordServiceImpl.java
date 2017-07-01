package com.borsam.service.record.impl;

import com.borsam.pojo.wf.CreateProcessParam;
import com.borsam.pojo.wf.WfCode;
import com.borsam.pojo.wf.WfException;
import com.borsam.repository.dao.consultation.ConsultationDao;
import com.borsam.repository.dao.doctor.DoctorProfileDao;
import com.borsam.repository.dao.org.OrganizationDao;
import com.borsam.repository.dao.patient.PatientProfileDao;
import com.borsam.repository.dao.patient.PatientServiceDao;
import com.borsam.repository.dao.patient.PatientWalletDao;
import com.borsam.repository.dao.patient.PatientWalletHistoryDao;
import com.borsam.repository.dao.record.RecordDao;
import com.borsam.repository.dao.service.ServiceDao;
import com.borsam.repository.dao.service.ServiceTypeDao;
import com.borsam.repository.entity.consultation.Consultation;
import com.borsam.repository.entity.doctor.DoctorProfile;
import com.borsam.repository.entity.org.Organization;
import com.borsam.repository.entity.patient.*;
import com.borsam.repository.entity.record.Record;
import com.borsam.repository.entity.service.ServiceKey;
import com.borsam.repository.entity.service.ServiceType;
import com.borsam.service.pub.SnService;
import com.borsam.service.record.RecordService;
import com.borsam.service.wf.ProcessEngine;
import com.hiteam.common.base.service.impl.BaseServiceImpl;
import com.hiteam.common.util.ConfigUtils;
import com.hiteam.common.util.http.HttpClientUtils;
import com.hiteam.common.util.json.JsonUtils;
import com.hiteam.common.web.WebUtil;
import com.hiteam.common.web.template.freemarker.FreemarkerUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import javax.persistence.LockModeType;
import java.io.File;
import java.math.BigDecimal;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Service - 检查记录
 * Created by Sebarswee on 2015/7/21.
 */
@Service("recordServiceImpl")
public class RecordServiceImpl extends BaseServiceImpl<Record, Long> implements RecordService {

    @Resource(name = "patientProfileDaoImpl")
    private PatientProfileDao patientProfileDao;

    @Resource(name = "patientServiceDaoImpl")
    private PatientServiceDao patientServiceDao;

    @Resource(name = "organizationDaoImpl")
    private OrganizationDao organizationDao;

    @Resource(name = "serviceTypeDaoImpl")
    private ServiceTypeDao serviceTypeDao;

    @Resource(name = "doctorProfileDaoImpl")
    private DoctorProfileDao doctorProfileDao;

    @Resource(name = "consultationDaoImpl")
    private ConsultationDao consultationDao;

    @Resource(name = "serviceDaoImpl")
    private ServiceDao serviceDao;

    @Resource(name = "processEngineImpl")
    private ProcessEngine processEngine;

    @Resource(name = "snServiceImpl")
    private SnService snService;

    @Resource(name = "patientWalletDaoImpl")
    private PatientWalletDao patientWalletDao;

    @Resource(name = "patientWalletHistoryDaoImpl")
    private PatientWalletHistoryDao patientWalletHistoryDao;

    @Resource(name = "recordDaoImpl")
    private RecordDao recordDao;

    @Resource(name = "recordDaoImpl")
    public void setBaseDao(RecordDao recordDao) {
        super.setBaseDao(recordDao);
    }

    @Transactional
    @Override
    public void setFlag(Long[] ids, Record.Flag flag) {
        recordDao.setFlag(ids, flag);
    }

    @Transactional
    @Override
    public void createConsultation(Long uid, Long rid, Long oid, Integer type, Long typeId) {
        Record record = recordDao.find(rid);
        PatientProfile patient = patientProfileDao.find(uid);
        Organization org = organizationDao.find(oid);
        ServiceType serviceType = serviceTypeDao.find(typeId);
        // 钱包记录
        PatientWalletHistory history = null;
        // 诊单记录
        Consultation consultation = new Consultation();

        if (type == 0) {
            PatientServiceKey key = new PatientServiceKey();
            key.setPatient(patient);
            key.setOrg(org);
            key.setServiceType(serviceType);
            PatientService patientService = patientServiceDao.find(key, LockModeType.PESSIMISTIC_WRITE);    // 锁定
            // 扣除服务次数
            patientService.setCount(patientService.getCount() - 1);
            patientServiceDao.merge(patientService);

            consultation.setPayKind(Consultation.PayKind.servicePack);
            consultation.setStatus(1);
        } else if (type == 1) {
            ServiceKey key = new ServiceKey();
            key.setOid(oid);
            key.setType(typeId);
            com.borsam.repository.entity.service.Service service = serviceDao.find(key);

            // 直接扣除钱包余额
            PatientWallet patientWallet = patientWalletDao.getWallet(uid);
            patientWalletDao.lock(patientWallet, LockModeType.PESSIMISTIC_WRITE);
            patientWallet.setTotal(patientWallet.getTotal().subtract(service.getPrice()));
            patientWalletDao.merge(patientWallet);

            // 生成钱包历史记录
            history = new PatientWalletHistory();
            history.setUid(uid);
            history.setMoney(service.getPrice());
            history.setPayStyle(0);
            history.setOid(oid);
            history.setType(PatientWalletHistory.Type.consult);
            history.setTradeNo("");
            history.setPayNo("");
            history.setVerdict("记录会诊");
            history.setSuccess(1);

            consultation.setPayKind(Consultation.PayKind.deposit);
            consultation.setStatus(0);
        }

        // 启动流程
        Long guid = createWf(oid);

        // 生成诊断信息
        ServiceKey serviceKey = new ServiceKey();
        serviceKey.setOid(oid);
        serviceKey.setType(typeId);
        com.borsam.repository.entity.service.Service service = serviceDao.find(serviceKey);
        Calendar calendar = Calendar.getInstance();

        consultation.setPatient(patient);
        consultation.setState(0);   // 待技师编辑
        consultation.setRecord(record);
        consultation.setPrice(service != null ? service.getPrice() : new BigDecimal(0));
        consultation.setType(serviceType);
        consultation.setOrg(org);

        consultation.setYear(calendar.get(Calendar.YEAR));
        consultation.setMonth(calendar.get(Calendar.MONTH) + 1);
        consultation.setGuid(guid);
        // 其他字段
        consultation.setVerdict("");
        if (patient.getDoctor() != null && patient.getDoctor().getId() != 0L) {
            // doctor属性被设置为 insertable = false, updatable = false 不能通过实体关联更新
            consultation.setDid(patient.getDoctor().getId());
        }
//        consultation.setEditDoctor(null);
//        consultation.setAuditDoctor(null);
        consultation.setLockDoctor(0L);
        consultation.setLockTime(0L);
        consultation.setDoctorOpinion("");
        consultation.setReportNo("");
        consultation.setTradeNo("");
        consultationDao.persist(consultation);
        // 生成诊单编号
        consultationDao.updateTradeNo(consultation.getId(), snService.generate(consultation.getId()));

        // 设置检查记录状态为已提交
        if (!record.getIsCommit()) {
            record.setIsCommit(true);
        }
        record.setNum(record.getNum() + 1);
        recordDao.merge(record);

        // 机构诊单数量+1
        org.setOrderNum(org.getOrderNum() + 1);
        organizationDao.merge(org);

        // 保存钱包记录
        if (history != null) {
            history.setTradeNo(consultation.getTradeNo());
            patientWalletHistoryDao.persist(history);
        }
    }

    /**
     * 启动流程
     * @param oid 机构
     * @return guid
     */
    private Long createWf(Long oid) {
        /**
         * 查找机构的技师作为下环节参与者
         * 如果没有技师，则查找机构的审核医生，此时流程不再经过审核环节
         * 如果都没有，则找主治医生，流程也不再经过审核环节
         * 以上都没有，则抛出异常
         * 注：主治医生参与全程，即使已找到技师作为参与者，主治医生也依然会被添加到参与者
         */
        List<DoctorProfile> doctors = doctorProfileDao.findEditDoctor(oid);
        if (doctors == null || doctors.isEmpty()) {
            doctors = doctorProfileDao.findAuditDoctor(oid);
        }
        List<DoctorProfile> mainDoctors = doctorProfileDao.findMainDoctor(oid);

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

        if (StringUtils.isEmpty(participant)) {
            throw new WfException(WfException.Code.canFindActor, "机构下找不到相关医生");
        }

        CreateProcessParam processParam = new CreateProcessParam();
        processParam.setWfCode(WfCode.CONSULTATION);
        processParam.setIsPush(true);
        processParam.setDatas(new HashMap<String, String>() {
            {
                put("[WorkItem].result", "common.wf.result.create");
            }
        });
        // 设置参与者
        Map<String, String> participants = new HashMap<String, String>();
        participants.put("edit", participant);
        processParam.setParticipant(participants);
        return processEngine.startProcessInstance(processParam);
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

    @Override
    @Transactional(readOnly = true)
    public boolean localFileExist(Record record) {
        String fileNo = record.getFileNo();
        if (StringUtils.isNotEmpty(fileNo)) {
            try {
                String filePath = getUploadPath() + fileNo + ".pdf";
                File pdf = new File(WebUtil.getSession().getServletContext().getRealPath(filePath));
                return pdf.exists();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return false;
    }

    @Override
    @Transactional(readOnly = true)
    public String getLocalFilePath(Record record) {
        String fileNo = record.getFileNo();
        if (StringUtils.isNotEmpty(fileNo)) {
            return getUploadPath() + fileNo + ".pdf";
        }
        return "";
    }

    @Override
    @Transactional(readOnly = true)
    public String saveFileToLocal(Record record) {
        String fileNo = record.getFileNo();
        String fileUrl;
        try {
            fileUrl = FreemarkerUtils.process(ConfigUtils.config.getProperty("server.url") + ConfigUtils.config.getProperty("fileUrl"), new HashMap<String, String>() {
                {
                    put("type", "0");
                    put("no", fileNo);
                }
            });
            String result = HttpClientUtils.invokeGet(fileUrl, null, "utf-8", 5000);
            Map json = JsonUtils.toObject(result, Map.class);
            String filePath = (String) ((Map) json.get(fileNo)).get("file_report");
            String uploadPath = WebUtil.getSession().getServletContext().getRealPath(getUploadPath() + fileNo + ".pdf");
            HttpClientUtils.download(filePath, uploadPath);
            return uploadPath;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 获取本地pdf报告上传路径
     */
    private String getUploadPath() {
        try {
            String uploadPath = FreemarkerUtils.process(ConfigUtils.config.getProperty("fileUploadPath"), null);
            uploadPath = uploadPath.replaceAll("/[0-9]*?/", "/");
            uploadPath += "report/";
            return uploadPath;
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
    }

    @Override
    @Transactional(readOnly = true)
    public String getRemoteFilePath(Long id) {
        Record record = this.find(id);
        String fileNo = record.getFileNo();
        String fileUrl;
        try {
            fileUrl = FreemarkerUtils.process(ConfigUtils.config.getProperty("server.url") + ConfigUtils.config.getProperty("fileUrl"), new HashMap<String, String>() {
                {
                    put("type", "0");
                    put("no", fileNo);
                }
            });
            String result = HttpClientUtils.invokeGet(fileUrl, null, "utf-8", 5000);
            Map json = JsonUtils.toObject(result, Map.class);
            return (String) ((Map) json.get(fileNo)).get("file_report");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
