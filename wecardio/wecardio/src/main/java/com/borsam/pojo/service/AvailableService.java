package com.borsam.pojo.service;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * Bean - 可用服务
 * Created by tantian on 2015/10/11.
 */
public class AvailableService implements Serializable {

    private Long oid;                       // 机构ID
    private String orgName;                 // 机构名称
    private Long serviceType;               // 服务类型
    private String serviceName;             // 服务名称
    private Integer type;                   // 类型：0-购买的服务，1-机构发布的服务
    private Integer count;                  // 剩余次数
    private BigDecimal price;               // 服务价格

    /**
     * 获取机构ID
     * @return 机构ID
     */
    public Long getOid() {
        return oid;
    }

    /**
     * 设置机构ID
     * @param oid 机构ID
     */
    public void setOid(Long oid) {
        this.oid = oid;
    }

    /**
     * 获取机构名称
     * @return 机构名称
     */
    public String getOrgName() {
        return orgName;
    }

    /**
     * 设置机构名称
     * @param orgName 机构名称
     */
    public void setOrgName(String orgName) {
        this.orgName = orgName;
    }

    /**
     * 获取服务类型
     * @return 服务类型
     */
    public Long getServiceType() {
        return serviceType;
    }

    /**
     * 设置服务类型
     * @param type 服务类型
     */
    public void setServiceType(Long type) {
        this.serviceType = type;
    }

    /**
     * 获取服务名称
     * @return 服务名称
     */
    public String getServiceName() {
        return serviceName;
    }

    /**
     * 设置服务名称
     * @param serviceName 服务名称
     */
    public void setServiceName(String serviceName) {
        this.serviceName = serviceName;
    }

    /**
     * 获取类型
     * @return 类型
     */
    public Integer getType() {
        return type;
    }

    /**
     * 设置类型
     * @param type 类型
     */
    public void setType(Integer type) {
        this.type = type;
    }

    /**
     * 获取剩余次数
     * @return 剩余次数
     */
    public Integer getCount() {
        return count;
    }

    /**
     * 设置剩余次数
     * @param count 剩余次数
     */
    public void setCount(Integer count) {
        this.count = count;
    }

    /**
     * 获取价格
     * @return 价格
     */
    public BigDecimal getPrice() {
        return price;
    }

    /**
     * 设置价格
     * @param price 价格
     */
    public void setPrice(BigDecimal price) {
        this.price = price;
    }
}
