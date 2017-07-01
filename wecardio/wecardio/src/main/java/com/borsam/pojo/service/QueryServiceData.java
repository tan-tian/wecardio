package com.borsam.pojo.service;

import com.hiteam.common.base.pojo.search.Pageable;

import java.io.Serializable;

/**
 * Created by Zhang zhongtao on 2015/7/26.
 * 服务查询参数
 */
public class QueryServiceData extends Pageable implements Serializable {
    /**当前登录人ID*/
    private Long staffId;
    /**服务项ID*/
    private Long itemId;
    /**服务编号*/
    private String code;
    /**服务名称*/
    private String name;

    private Boolean[] isEnableds;

    private Long[] itemIds;

    public Long[] getItemIds() {
        return itemIds;
    }

    public void setItemIds(Long[] itemIds) {
        this.itemIds = itemIds;
    }

    public Boolean[] getIsEnableds() {
        return isEnableds;
    }

    public void setIsEnableds(Boolean[] isEnableds) {
        this.isEnableds = isEnableds;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public Long getStaffId() {
        return staffId;
    }

    public void setStaffId(Long staffId) {
        this.staffId = staffId;
    }

    public Long getItemId() {
        return itemId;
    }

    public void setItemId(Long itemId) {
        this.itemId = itemId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
