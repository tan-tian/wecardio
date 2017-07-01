package com.borsam.repository.entity.wf;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.hiteam.common.base.repository.entity.BaseEntity;
import com.hiteam.common.base.repository.entity.LongEntity;
import com.hiteam.common.util.json.DateTimeSerializer;
import com.hiteam.common.web.I18Util;
import org.hibernate.search.annotations.DocumentId;

import javax.persistence.*;
import java.util.Date;

/**
 * Entity - 工作项
 * Created by Sebarswee on 2015/7/3.
 */
@Entity
@Table(name = "t_pub_wf_workitem")
public class WorkItem extends LongEntity {

    /**
     * 工作项状态
     */
    public enum Status {
        running,        // 运行中
        finished,       // 正常结束
        forceEnd,       // 强制结束
        pending,        // 挂起
        cancel          // 撤销
    }

    private String wfCode;          // 流程代码
    private Act act;                // 活动环节
    private Status status;          // 状态
    private Actor.Type type;        // 参与者类型
    private Long actorId;           // 参与者ID
    private String actorName;       // 参与者名称
    private Long exeId;             // 执行人员ID
    private Long time;              // 运行时长
    private String result;          // 运行结果
    private String remark;          // 备注
    private Long guid;              // 流程GUID
    private Long createDate;        // 创建时间
    private Long modifyDate;        // 修改时间

    /**
     * 获取ID
     * @return ID
     */
    @DocumentId
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.TABLE, generator = "tableGenerator")
    @TableGenerator(name = "tableGenerator", table = "id_maker", pkColumnName = "maker_name",
            valueColumnName = "maker_value", pkColumnValue = "t_pub_wf_workitem", allocationSize = 1)
    public Long getId() {
        return super.getId();
    }

    /**
     * 获取流程代码
     * @return 流程代码
     */
    @Column(name = "wf_code", nullable = false, length = 100)
    public String getWfCode() {
        return wfCode;
    }

    /**
     * 设置流程代码
     * @param wfCode 流程代码
     */
    public void setWfCode(String wfCode) {
        this.wfCode = wfCode;
    }

    /**
     * 获取活动环节
     * @return 活动环节
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "act_id", nullable = false)
    public Act getAct() {
        return act;
    }

    /**
     * 设置活动环节
     * @param act 活动环节
     */
    public void setAct(Act act) {
        this.act = act;
    }

    /**
     * 获取状态
     * @return 状态
     */
    @Column(name = "status", nullable = false)
    public Status getStatus() {
        return status;
    }

    /**
     * 设置状态
     * @param status 状态
     */
    public void setStatus(Status status) {
        this.status = status;
    }

    /**
     * 获取参与者类型
     * @return 参与者类型
     */
    @Column(name = "type", nullable = false)
    public Actor.Type getType() {
        return type;
    }

    /**
     * 设置参与者类型
     * @param type 参与者类型
     */
    public void setType(Actor.Type type) {
        this.type = type;
    }

    /**
     * 获取参与者ID
     * @return 参与者ID
     */
    @Column(name = "actor_id", nullable = false)
    public Long getActorId() {
        return actorId;
    }

    /**
     * 设置参与者ID
     * @param actorId 参与者ID
     */
    public void setActorId(Long actorId) {
        this.actorId = actorId;
    }

    /**
     * 获取参与者名称
     * @return 参与者名称
     */
    @JsonProperty
    @Column(name = "actor_name", nullable = false, length = 200)
    public String getActorName() {
        return actorName;
    }

    /**
     * 设置参与者名称
     * @param actorName 参与者名称
     */
    public void setActorName(String actorName) {
        this.actorName = actorName;
    }

    /**
     * 获取执行人员ID
     * @return 执行人员ID
     */
    @Column(name = "exe_id")
    public Long getExeId() {
        return exeId;
    }

    /**
     * 设置执行人员ID
     * @param exeId 执行人员ID
     */
    public void setExeId(Long exeId) {
        this.exeId = exeId;
    }

    /**
     * 获取运行时长
     * @return 运行时长
     */
    @Column(name = "time")
    public Long getTime() {
        return time;
    }

    /**
     * 设置运行时长
     * @param time 运行时长
     */
    public void setTime(Long time) {
        this.time = time;
    }

    /**
     * 获取运行结果
     * @return 运行结果
     */
    @JsonProperty
    @Column(name = "result", length = 200)
    public String getResult() {
        return result;
    }

    /**
     * 设置运行结果
     * @param result 运行结果
     */
    public void setResult(String result) {
        this.result = result;
    }

    /**
     * 获取备注
     * @return 备注
     */
    @JsonProperty
    @Column(name = "remark", length = 200)
    public String getRemark() {
        return remark;
    }

    /**
     * 设置备注
     * @param remark 备注
     */
    public void setRemark(String remark) {
        this.remark = remark;
    }

    /**
     * 获取GUID
     * @return GUID
     */
    @Column(name = "guid", nullable = false)
    public Long getGuid() {
        return guid;
    }

    /**
     * 设置GUID
     * @param guid GUID
     */
    public void setGuid(Long guid) {
        this.guid = guid;
    }

    /**
     * 获取创建时间
     * @return 创建时间
     */
    @Column(name = "create_date", nullable = false)
    public Long getCreateDate() {
        return createDate;
    }

    /**
     * 设置创建时间
     * @param createDate 创建时间
     */
    public void setCreateDate(Long createDate) {
        this.createDate = createDate;
    }

    /**
     * 获取修改时间
     * @return 修改时间
     */
    @Column(name = "modify_date", nullable = false)
    public Long getModifyDate() {
        return modifyDate;
    }

    /**
     * 设置修改时间
     * @param modifyDate 修改时间
     */
    public void setModifyDate(Long modifyDate) {
        this.modifyDate = modifyDate;
    }

    /*----------  持久化事件 ----------*/

    /**
     * 保存前处理
     */
    @PrePersist
    public void prePersist() {
        this.setCreateDate(new Date().getTime() / 1000);
        this.setModifyDate(new Date().getTime() / 1000);
    }

    /**
     * 更新前处理
     */
    @PreUpdate
    public void preUpdate() {
        this.setModifyDate(new Date().getTime() / 1000);
        Long start = this.getCreateDate();
        Long end = this.getModifyDate();
        long time = end - start;
        this.setTime(time / 1000);
    }

    /**
     * 获取处理时间
     * @return 处理时间
     */
    @JsonProperty
    @JsonSerialize(using = DateTimeSerializer.class)
    @Transient
    public Date getExeDate() {
        return new Date(getModifyDate() * 1000);
    }

    /**
     * 获取活动名称
     * @return 活动名称
     */
    @JsonProperty
    @Transient
    public String getActName() {
        return I18Util.getMessage(getAct().getActName());
    }
}
