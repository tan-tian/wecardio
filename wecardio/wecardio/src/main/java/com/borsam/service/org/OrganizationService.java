package com.borsam.service.org;

import com.borsam.repository.entity.org.Organization;
import com.hiteam.common.base.service.BaseService;
import com.hiteam.common.util.pojo.EnumBean;

import java.util.List;

/**
 * Service - 机构信息
 * Created by Sebarswee on 2015/7/1.
 */
public interface OrganizationService extends BaseService<Organization, Long> {

    /**
     * 申请开通机构
     * @param organization 机构信息
     * @param isSubmit 是否提交审批
     */
    public void create(Organization organization, Boolean isSubmit);

    /**
     * 更新机构信息
     * @param organization 机构信息
     * @param isSubmit 是否提交审批
     */
    public void reCreate(Organization organization, Boolean isSubmit);

    /**
     * 机构审核
     * @param oid 机构ID
     * @param isPass 是否通过
     * @param remark 备注信息
     */
    public void audit(Long oid, Boolean isPass, String remark);

    /**
     * 机构列表（for 下拉框）
     * @param name 机构名称
     * @return 机构列表
     */
    public List<EnumBean> sel(String name);

    /**
     * 查找机构系统账号，多个取第一个
     * @param oid 机构ID
     * @return 系统账号
     */
    public String getAccount(Long oid);
    
    
}
