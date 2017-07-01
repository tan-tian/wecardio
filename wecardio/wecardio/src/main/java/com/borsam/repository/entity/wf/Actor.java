package com.borsam.repository.entity.wf;

import com.hiteam.common.base.repository.entity.BaseEntity;
import com.hiteam.common.base.repository.entity.LongEntity;

import org.hibernate.search.annotations.DocumentId;

import javax.persistence.*;
import java.util.Date;

/**
 * Entity - 活动参与者
 * Created by Sebarswee on 2015/7/3.
 */
@Entity
@Table(name = "t_pub_wf_actor")
public class Actor extends LongEntity {

    /**
     * 参与者类型
     */
    public enum Type {
        admin,              // 平台管理员
        organization,       // 机构管理员
        doctor,             // 医生
        patient,            // 患者
        participant,        // 历史活动参与者
        executor            // 历史活动执行者
    }

    private String wfCode;          // 流程代码
    private Act act;                // 活动环节
    private Type type;              // 参与者类型
    private Long actorId;           // 参与者ID
    private String actorName;       // 参与者名称
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
            valueColumnName = "maker_value", pkColumnValue = "t_pub_wf_actor", allocationSize = 1)
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
     * 获取参与者类型
     * @return 参与者类型
     */
    @Column(name = "type", nullable = false)
    public Type getType() {
        return type;
    }

    /**
     * 设置参与者类型
     * @param type 参与者类型
     */
    public void setType(Type type) {
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
    }
}
