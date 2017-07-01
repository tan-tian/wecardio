package com.borsam.repository.entity.wf;

import com.hiteam.common.base.repository.entity.BaseEntity;
import com.hiteam.common.base.repository.entity.LongEntity;

import org.hibernate.search.annotations.DocumentId;

import javax.persistence.*;
import java.util.Date;

/**
 * Entity - 流程实例
 * Created by Sebarswee on 2015/7/3.
 */
@Entity
@Table(name = "t_pub_wf_inst")
public class WfInst extends LongEntity {

    /**
     * 流程状态
     */
    public enum Status {
        running,        // 运行中
        finished,       // 归档
        forceEnd,       // 强制结束
        pending,        // 挂起
        cancel          // 撤销
    }

    private String wfCode;          // 流程代码
    private Status status;		    // 流程状态
    private Long time;				// 运行时长
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
            valueColumnName = "maker_value", pkColumnValue = "t_pub_wf_inst", allocationSize = 1)
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
}
