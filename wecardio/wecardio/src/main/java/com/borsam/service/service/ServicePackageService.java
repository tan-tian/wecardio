package com.borsam.service.service;

import com.borsam.pojo.service.EditServicePackageData;
import com.borsam.pojo.service.QueryServicePackageData;
import com.borsam.repository.entity.org.Organization;
import com.borsam.repository.entity.service.ServicePackage;
import com.hiteam.common.base.pojo.search.Page;
import com.hiteam.common.base.service.BaseService;
import com.hiteam.common.web.Message;

import java.util.Map;

/**
 * Created by Zhang zhongtao on 2015/7/25.
 */
public interface ServicePackageService extends BaseService<ServicePackage, Long> {
    /**
     * 编辑服务包（新增、修改)
     *
     * @param data 数据
     * @param organization  当前操作人ID(机构)
     * @return List
     */
    public ServicePackage editServicePackages(EditServicePackageData data, Organization organization);

    /**
     * 分页查询服务包信息
     * @param data 查询参数
     * @return Page
     */
    public Page<ServicePackage> queryServicePackage(QueryServicePackageData data);

    /**
     * 逻辑删除
     * @param ids 数组标识
     */
    public void delPackage(Long[] ids);

    /**
     * 根据包ID，获取所属的服务
     * @param packageId 包ID
     * @return Page
     */
    public Page<Map> queryServiceByPackId(Long packageId);

    /***
     * 支付
     * @param servicePackage 包
     * @param patientId 患者ID
     * @return Message
     */
    public Message pay(ServicePackage servicePackage,Long patientId,Long did);
}
