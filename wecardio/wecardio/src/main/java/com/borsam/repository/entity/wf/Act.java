package com.borsam.repository.entity.wf;

import com.hiteam.common.base.repository.entity.BaseEntity;
import com.hiteam.common.base.repository.entity.LongEntity;

import org.hibernate.search.annotations.DocumentId;

import javax.persistence.*;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

/**
 * Entity - 流程活动
 * Created by Sebarswee on 2015/7/3.
 */
@Entity
@Table(name = "t_pub_wf_act")
public class Act extends LongEntity {

    /**
     * 活动类型
     */
    public enum Type {
        start,  // 开始环节
        end,    // 结束环节
        act     // 普通环节
    }

    private String wfName;              // 流程名称
    private String wfCode;              // 流程代码
    private String actName;             // 活动名称
    private String actCode;             // 活动代码
    private Type type;                  // 活动类型
    private String state;               // 显示状态
    private String description;         // 描述
    private Boolean isLoop;             // 是否循环
    private Boolean isMulti;            // 是否多实例
    private Integer seq;                // 序号
    private Long createDate;            // 创建时间
    private Long modifyDate;            // 修改时间

    // 活动参与者列表
    private Set<Actor> actors = new HashSet<Actor>();
    // 工作项列表
    private Set<WorkItem> workItems = new HashSet<WorkItem>();

    /**
     * 获取ID
     * @return ID
     */
    @DocumentId
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.TABLE, generator = "tableGenerator")
    @TableGenerator(name = "tableGenerator", table = "id_maker", pkColumnName = "maker_name",
            valueColumnName = "maker_value", pkColumnValue = "t_pub_wf_act", allocationSize = 1)
    public Long getId() {
        return super.getId();
    }

    /**
     * 获取流程名称
     * @return 流程名称
     */
    @Column(name = "wf_name", nullable = false, length = 100)
    public String getWfName() {
        return wfName;
    }

    /**
     * 设置流程名称
     * @param wfName 流程名称
     */
    public void setWfName(String wfName) {
        this.wfName = wfName;
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
     * 获取活动名称
     * @return 活动名称
     */
    @Column(name = "act_name", nullable = false, length = 100)
    public String getActName() {
        return actName;
    }

    /**
     * 设置活动名称
     * @param actName 活动名称
     */
    public void setActName(String actName) {
        this.actName = actName;
    }

    /**
     * 获取活动代码
     * @return 活动代码
     */
    @Column(name = "act_code", nullable = false, length = 100)
    public String getActCode() {
        return actCode;
    }

    /**
     * 设置活动代码
     * @param actCode 活动代码
     */
    public void setActCode(String actCode) {
        this.actCode = actCode;
    }

    /**
     * 获取活动类型
     * @return 活动类型
     */
    @Column(name = "type", nullable = false)
    public Type getType() {
        return type;
    }

    /**
     * 设置活动类型
     * @param type 活动类型
     */
    public void setType(Type type) {
        this.type = type;
    }

    /**
     * 获取显示状态
     * @return 显示状态
     */
    @Column(name = "state", length = 200)
    public String getState() {
        return state;
    }

    /**
     * 设置显示状态
     * @param state 显示状态
     */
    public void setState(String state) {
        this.state = state;
    }

    /**
     * 获取描述
     * @return 描述
     */
    @Column(name = "description", length = 1000)
    public String getDescription() {
        return description;
    }

    /**
     * 设置描述
     * @param description 描述
     */
    public void setDescription(String description) {
        this.description = description;
    }

    /**
     * 获取是否循环
     * @return 是否循环
     */
    @Column(name = "is_loop", nullable = false)
    public Boolean getIsLoop() {
        return isLoop;
    }

    /**
     * 设置是否循环
     * @param isLoop 是否循环
     */
    public void setIsLoop(Boolean isLoop) {
        this.isLoop = isLoop;
    }

    /**
     * 获取是否多实例
     * @return 是否多实例
     */
    @Column(name = "is_multi", nullable = false)
    public Boolean getIsMulti() {
        return isMulti;
    }

    /**
     * 设置是否多实例
     * @param isMulti 是否多实例
     */
    public void setIsMulti(Boolean isMulti) {
        this.isMulti = isMulti;
    }

    /**
     * 获取序号
     * @return 序号
     */
    @Column(name = "seq", nullable = false)
    public Integer getSeq() {
        return seq;
    }

    /**
     * 设置序号
     * @param seq 序号
     */
    public void setSeq(Integer seq) {
        this.seq = seq;
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

    /**
     * 获取活动参与者列表
     * @return 参与者列表
     */
    @OneToMany(mappedBy = "act", fetch = FetchType.LAZY, cascade = CascadeType.REMOVE)
    public Set<Actor> getActors() {
        return actors;
    }

    /**
     * 设置活动参与者列表
     * @param actors 参与者列表
     */
    public void setActors(Set<Actor> actors) {
        this.actors = actors;
    }

    /**
     * 获取工作项列表
     * @return 工作项列表
     */
    @OneToMany(mappedBy = "act", fetch = FetchType.LAZY, cascade = CascadeType.REMOVE)
    public Set<WorkItem> getWorkItems() {
        return workItems;
    }

    /**
     * 设置工作项列表
     * @param workItems 工作项列表
     */
    public void setWorkItems(Set<WorkItem> workItems) {
        this.workItems = workItems;
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
    }
}
