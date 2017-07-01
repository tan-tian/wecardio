package com.borsam.repository.entity.service;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.io.Serializable;

/**
 * Service的联合主键
 * Created by Sebarswee on 2015/7/20.
 */
@Embeddable
public class ServiceKey implements Serializable {

    private Long oid;           // 机构ID
    private Long type;          // 服务配置ID

    public ServiceKey() {
        super();
    }

    public ServiceKey(Long oid, Long type) {
        super();
        this.oid = oid;
        this.type = type;
    }

    @Override
    public int hashCode() {
        int i = 17;
        i += (getOid() == null ? 0 : getOid().hashCode() * 31);
        i += (getType() == null ? 0 : getType().hashCode() * 31);
        return i;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (this == obj) {
            return true;
        }
        if (!ServiceKey.class.isAssignableFrom(obj.getClass())) {
            return false;
        }
        ServiceKey other = (ServiceKey) obj;
        return getOid() != null && getType() != null && getOid().equals(other.getOid()) && getType().equals(other.getType());
    }

    /**
     * 获取机构ID
     * @return 机构ID
     */
    @Column(name = "oid", nullable = false)
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
     * 获取服务配置ID
     * @return 服务配置ID
     */
    @Column(name = "type", nullable = false)
    public Long getType() {
        return type;
    }

    /**
     * 设置服务配置
     * @param type 服务配置
     */
    public void setType(Long type) {
        this.type = type;
    }
}
