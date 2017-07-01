package com.borsam.repository.entity.patient;

import com.borsam.repository.entity.doctor.DoctorProfile;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.hiteam.common.base.repository.entity.BaseEntity;
import com.hiteam.common.base.repository.entity.LongEntity;
import com.hiteam.common.util.json.DateSerializer;
import org.hibernate.search.annotations.DocumentId;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

/**
 * Entity - 医嘱信息表
 * Created by liujieming on 2015/7/1.
 */
@Entity
@Table(name = "doctor_opinion")
public class PatientDoctorOpinion extends LongEntity {

    private Long created;                   // 创建时间
    private String content;                 // 内容
    private DoctorProfile send_id; //发送者_只会有医生创建
    private PatientProfile receive_id;//接收者

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
            valueColumnName = "maker_value", pkColumnValue = "doctor_opinion", allocationSize = 1)
    public Long getId() {
        return super.getId();
    }

    @JsonProperty
    @Column(name = "created")
    public Long getCreated() {
        return created;
    }

    public void setCreated(Long created) {
        this.created = created;
    }

    @JsonProperty
    @Column(name = "content")
    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    @JsonProperty
    @ManyToOne(fetch = FetchType.LAZY)
    public PatientProfile getReceive_id() {
        return receive_id;
    }

    public void setReceive_id(PatientProfile receive_id) {
        this.receive_id = receive_id;
    }

    @JsonProperty
    @ManyToOne(fetch = FetchType.LAZY)
    public DoctorProfile getSend_id() {
        return send_id;
    }

    public void setSend_id(DoctorProfile send_id) {
        this.send_id = send_id;
    }

    /**
     * 获取创建日期
     * @return 创建日期
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
}
