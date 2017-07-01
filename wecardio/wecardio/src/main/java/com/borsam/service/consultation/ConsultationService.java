package com.borsam.service.consultation;

import com.borsam.repository.entity.consultation.Consultation;
import com.borsam.repository.entity.forum.ForumInfo;
import com.hiteam.common.base.service.BaseService;

/**
 * Service - 会诊申请
 * Created by Sebarswee on 2015/7/24.
 */
public interface ConsultationService extends BaseService<Consultation, Long> {

    /**
     * 编辑分析
     * @param cid 诊单ID
     * @param qt QT(ms)
     * @param qtc QTC(ms)
     * @param pr PR(ms)
     * @param qrs QRS(ms)
     * @param rr RR(ms)
     * @param opinion 医嘱
     */
    public void edit(Long cid, String qt, String qtc, String pr, String qrs, String rr, String opinion);

    /**
     * 审核
     * @param cid 诊单ID
     * @param isPass 是否通过
     * @param remark 备注信息
     */
    public void audit(Long cid, Boolean isPass, String remark);

    /**
     * 获取远程pdf报告地址
     * @param id 诊单标识
     * @return 远程pdf报告地址
     */
    public String getRemoteFilePath(Long id);

    /**
     * 诊单评价
     * @param cid 诊单标识
     * @param forumInfo 评价信息
     */
    public void evaluate(Long cid, ForumInfo forumInfo);
}
