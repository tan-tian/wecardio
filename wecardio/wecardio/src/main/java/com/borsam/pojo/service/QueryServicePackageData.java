package com.borsam.pojo.service;

import com.hiteam.common.base.pojo.search.Pageable;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by tantian on 2015/7/25.
 */
public class QueryServicePackageData extends Pageable {
    /**
     * 标题
     */
    private String title;
    /**
     *
     * 机构ID
     */
    private Long orgId;
    /**
     * 状态
     */
    private List<Boolean> status = new ArrayList<Boolean>();
    /**
     * 服务项ID
     */
    private Long typeId;
    /**
     * 起始时间
     */
    private Date startTime;
    /**
     * 结束时间
     */
    private Date endTime;

    private Boolean isDel;

    public Boolean getDel() {
        return isDel;
    }

    public void setDel(Boolean del) {
        isDel = del;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Long getOrgId() {
        return orgId;
    }

    public void setOrgId(Long orgId) {
        this.orgId = orgId;
    }

    public List<Boolean> getStatus() {
        return status;
    }

    public void setStatus(List<Boolean> status) {
        this.status = status;
    }

    public Long getTypeId() {
        return typeId;
    }

    public void setTypeId(Long typeId) {
        this.typeId = typeId;
    }

    public Date getStartTime() {
        return startTime;
    }

    public void setStartTime(Date startTime) {
        this.startTime = startTime;
    }

    public Date getEndTime() {
        return endTime;
    }

    public void setEndTime(Date endTime) {
        this.endTime = endTime;
    }
}
