package com.borsam.repository.entity.patient;

import com.borsam.pub.UserType;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.hiteam.common.base.repository.entity.BaseEntity;
import com.hiteam.common.base.repository.entity.LongEntity;
import com.hiteam.common.util.json.DateSerializer;
import org.hibernate.search.annotations.DocumentId;

import javax.persistence.*;
import java.util.Date;

/**
 * Entity - 活动记录
 * Created by liujieming on 2015/7/22.
 */
@Entity
@Table(name = "patient_activity")
public class PatientActivity extends LongEntity {

    private PatientProfile uid;//患者
    private String detail;//活动记录
    private Long created;//创建时间
    private Long create_id;//创建ID
    private String create_name;//创建人员名称
    private UserType type;//创建人员类型
    private Long start_time;//活动时间
    private String headPath;//创建人的图像地址

    /**
     * 保存前处理
     */
    @PrePersist
    public void prePersist() {
        this.setCreated(new Date().getTime() / 1000);
    }

    @JsonProperty
    @DocumentId
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.TABLE, generator = "tableGenerator")
    @TableGenerator(name = "tableGenerator", table = "id_maker", pkColumnName = "maker_name",
            valueColumnName = "maker_value", pkColumnValue = "patient_activity", allocationSize = 1)
    public Long getId() {
        return super.getId();
    }

    @JsonProperty
    @Column(name = "detail")
    public String getDetail() {
        return detail;
    }

    public void setDetail(String detail) {
        this.detail = detail;
    }

    @Column(name = "created")
    public Long getCreated() {
        return created;
    }

    public void setCreated(Long created) {
        this.created = created;
    }

    @JsonProperty
    @Column(name = "create_id")
    public Long getCreate_id() {
        return create_id;
    }

    public void setCreate_id(Long create_id) {
        this.create_id = create_id;
    }

    @Column(name = "type")
    public UserType getType() {
        return type;
    }

    public void setType(UserType type) {
        this.type = type;
    }

    @JsonProperty
    @Column(name = "start_time")
    public Long getStart_time() {
        return start_time;
    }

    public void setStart_time(Long start_time) {
        this.start_time = start_time;
    }

    @JsonProperty
    @Column(name = "create_name")
    public String getCreate_name() {
        return create_name;
    }

    public void setCreate_name(String create_name) {
        this.create_name = create_name;
    }

    @ManyToOne(fetch = FetchType.LAZY)
    public PatientProfile getUid() {
        return uid;
    }

    public void setUid(PatientProfile uid) {
        this.uid = uid;
    }

    /**
     * 获取创建时间
     * @return 创建时间
     */
    @JsonProperty
    @JsonSerialize(using = DateSerializer.class)
    @Transient
    public Date getDateCreated() {
        if (getCreated() != null && getCreated() != 0L) {
            return new Date(getCreated() * 1000);
        }
        return null;
    }


    @JsonProperty
    @Transient
    public String getHeadPath() {
        return headPath;
    }

    public void setHeadPath(String headPath) {
        this.headPath = headPath;
    }
}
