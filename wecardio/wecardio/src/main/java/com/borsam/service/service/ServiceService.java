package com.borsam.service.service;

import com.borsam.pojo.service.QueryServiceData;
import com.borsam.repository.entity.service.Service;
import com.borsam.repository.entity.service.ServiceKey;
import com.hiteam.common.base.pojo.search.Page;
import com.hiteam.common.base.service.BaseService;

import java.util.List;
import java.util.Map;

/**
 * Created by Zhang zhongtao on 2015/7/25.
 */
public interface ServiceService extends BaseService<Service, ServiceKey> {
    /**
     * 编辑服务（新增、修改)
     *
     * @param services 服务集合
     * @param oid  机构ID
     * @return List
     */
    public List<Service> editServices(List<Service> services, Long oid);

    /**
     * 分页查询服务
     * @param data 查询参数
     * @return page
     */
    public Page<Map> queryService(QueryServiceData data);

    /**
     * 设置服务启用或禁用
     * @param ids 标识数组(规则：机构ID-服务项ID)
     * @param isEnable true:启用，false:禁用
     */
    public void setEnableVal(String[] ids,Boolean isEnable);

}
