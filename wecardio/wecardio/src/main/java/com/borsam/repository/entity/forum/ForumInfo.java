package com.borsam.repository.entity.forum;

import com.borsam.repository.entity.consultation.Consultation;
import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.hiteam.common.base.repository.entity.BaseEntity;
import com.hiteam.common.base.repository.entity.LongEntity;

import org.hibernate.search.annotations.DocumentId;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotEmpty;

import javax.persistence.*;
import javax.validation.constraints.Digits;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.util.Date;

/**
 * <pre>
 * @Description: 评论信息表
 * @author :Zhang zhongtao
 * @version: Ver 1.0
 * @Date: 2015-08-06 20:29
 * </pre>
 */
@JsonAutoDetect
@Entity
@Table(name = "t_forum_info")
public class ForumInfo extends LongEntity {

    /**机构ID*/
    private Long oid;

    /**医生ID*/
    private Long did;

    /**诊单*/
    private Consultation consultation;

    /**内容*/
    private String content;

    /**评分*/
    private Integer score;

    /**删除标识*/
    private Integer deleteState;

    /**回复标识*/
    private Long relayId;

    /**创建人员标识*/
    private Long createId;

    /**创建人员名称*/
    private String createName;

    /**创建时间*/
    private Long created;

    /**创建时间-日期对象*/
    private Date createdDate;

    /***创建人图像地址*/
    private String createIdHeadPath;

    /**机构名称*/
    private String orgName;

    /**
     * 保存前处理
     */
    @PrePersist
    public void prePersist() {
        Long c = new Date().getTime() / 1000;
        this.setCreated(c);
    }

    @PostLoad
    public void postLoad() {
        if (this.getCreated() != null) {
            Date d = new Date(this.getCreated() * 1000L);
            this.setCreatedDate(d);
        }
    }

    @Override
    @DocumentId
    @Id
    @GeneratedValue(strategy = GenerationType.TABLE, generator = "tableGenerator")
    @TableGenerator(name = "tableGenerator", table = "id_maker", pkColumnName = "maker_name",
            valueColumnName = "maker_value", pkColumnValue = "t_forum_info", allocationSize = 1)
    public Long getId() {
        return super.getId();
    }

    @Transient
    public String getCreateIdHeadPath() {
        return createIdHeadPath;
    }

    public void setCreateIdHeadPath(String createIdHeadPath) {
        this.createIdHeadPath = createIdHeadPath;
    }

    @Transient
    public Date getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(Date createdDate) {
        this.createdDate = createdDate;
    }

    @Transient
    public String getOrgName() {
        return orgName;
    }

    public void setOrgName(String orgName) {
        this.orgName = orgName;
    }

    @NotNull
    @Column(name = "oid",nullable = false)
    public Long getOid() {
        return oid;
    }

    public void setOid(Long oid) {
        this.oid = oid;
    }



    @NotNull
    @Column(name = "did", nullable = false)
    public Long getDid() {
        return did;
    }

    public void setDid(Long did) {
        this.did = did;
    }

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "consultation_id", nullable = false)
    public Consultation getConsultation() {
        return consultation;
    }

    public void setConsultation(Consultation consultation) {
        this.consultation = consultation;
    }

    @Transient
    public Long getConsultationId() {
        return consultation.getId();
    }

    @Transient
    public String getConsultationCode() {
        return getConsultation().getTradeNo();
    }

    @Length(max = 1024)
    @Column(name = "content",length = 1024)
    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    @Min(1)
    @Max(5)
    @Column(name = "score")
    public Integer getScore() {
        return score;
    }

    public void setScore(Integer score) {
        this.score = score;
    }

    @NotNull
    @Min(1)
    @Max(2)
    @Digits(fraction = 0,integer = 1)
    @Column(name = "delete_state")
    public Integer getDeleteState() {
        return deleteState;
    }

    public void setDeleteState(Integer deleteState) {
        this.deleteState = deleteState;
    }

    @Column(name = "is_relay")
    public Long getRelayId() {
        return relayId;
    }

    public void setRelayId(Long relayId) {
        this.relayId = relayId;
    }

    @NotNull
    @Column(name = "create_id")
    public Long getCreateId() {
        return createId;
    }

    public void setCreateId(Long createId) {
        this.createId = createId;
    }

    @NotEmpty
    @Column(name = "create_name",length = 64,nullable = false)
    public String getCreateName() {
        return createName;
    }

    public void setCreateName(String createName) {
        this.createName = createName;
    }

    @NotNull
    @Column(name = "created",nullable = false)
    public Long getCreated() {
        return created;
    }

    public void setCreated(Long created) {
        this.created = created;
    }
}
