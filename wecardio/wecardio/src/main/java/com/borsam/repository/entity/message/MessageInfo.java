package com.borsam.repository.entity.message;

import com.borsam.pub.UserType;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.hiteam.common.base.repository.entity.BaseEntity;
import com.hiteam.common.base.repository.entity.LongEntity;
import com.hiteam.common.util.json.DateSerializer;
import com.hiteam.common.util.json.DateTimeSerializer;
import org.hibernate.search.annotations.DocumentId;

import javax.persistence.*;
import java.util.Date;

/**
 * Entity - 消息管理
 * Created by tantian on 2015/7/17.
 */
@Entity
@Table(name = "message")
public class MessageInfo extends LongEntity {
    /***国际化模板前缀，与类型组合为一个资源文件的key，如message.template.key9*/
    public static final String TEMPLATE_PREFIX = "message.template.key";
    /**存储模板消息的类型，对应MessageType的序号*/
    public static final int[] TEMPLATE_TYPE_IDS = new int[]{0, 3, 4, 5, 8, 9};

    /**
     * 绑定类型
     */
    public enum BindType {
        org, self, other, unknow
    }

    /**
     * 绑定类型
     */
    public enum MessageType {
        consultationResult, healthMessage,systemMesage, invitePatient, patientComfireAdd,patientRefuse,commonMessage,doctorOpinionMessage,doctorRelievePatientMessage,patientRelievePatientMessage
    }

    private Long from_id;
    private String from_name;
    private String from_HeadPath;//发送者的图像地址
    private UserType from_type;
    private Long to_id;
    private String to_name;
    private UserType to_type;
    private String to_HeadPath;//接收者的图像地址
    private String accessory;
    private String content;
    private Long created;
    private MessageType type;//0 发给患者会诊结果信息;1 常规健康提示（机构发给用户）;2 系统消息（群发，没有接收人员）;3 邀请患者加入机构信息;4 患者确定加入机构信息;5 患者拒绝消息;6 普通消息 7：医嘱消息 8：医生解除病人消息 9：病人解除病人消息
    private Integer recevied;//0 未接收\r\n            1 已接收
    private Integer iDeal;//0 未处理\r\n            1 已处理
    private Long recevied_time;//接收时间 即：阅读时间

    private long total;//总数量 虚拟使用

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
            valueColumnName = "maker_value", pkColumnValue = "message", allocationSize = 1)
    public Long getId() {
        return super.getId();
    }

    @JsonProperty
    @Column(name = "from_id", nullable = false)
    public Long getFrom_id() {
        return from_id;
    }

    public void setFrom_id(Long from_id) {
        this.from_id = from_id;
    }

    @JsonProperty
    @Column(name = "from_name")
    public String getFrom_name() {
        return from_name;
    }

    public void setFrom_name(String from_name) {
        this.from_name = from_name;
    }

    @JsonProperty
    @Column(name = "from_type")
    public UserType getFrom_type() {
        return from_type;
    }

    public void setFrom_type(UserType from_type) {
        this.from_type = from_type;
    }

    @JsonProperty
    @Column(name = "to_id", nullable = false)
    public Long getTo_id() {
        return to_id;
    }

    public void setTo_id(Long to_id) {
        this.to_id = to_id;
    }

    @JsonProperty
    @Column(name = "to_name")
    public String getTo_name() {
        return to_name;
    }

    public void setTo_name(String to_name) {
        this.to_name = to_name;
    }

    @JsonProperty
    @Column(name = "to_type")
    public UserType getTo_type() {
        return to_type;
    }

    public void setTo_type(UserType to_type) {
        this.to_type = to_type;
    }

    @JsonProperty
    @Column(name = "accessory")
    public String getAccessory() {
        return accessory;
    }

    public void setAccessory(String accessory) {
        this.accessory = accessory;
    }

    @JsonProperty
    @Column(name = "content", nullable = false)
    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    @JsonProperty
    @Column(name = "created", nullable = false)
    public Long getCreated() {
        return created;
    }

    public void setCreated(Long created) {
        this.created = created;
    }

    @JsonProperty
    @Column(name = "type", nullable = false)
    public MessageType getType() {
        return type;
    }

    public void setType(MessageType type) {
        this.type = type;
    }

    @JsonProperty
    @Column(name = "recevied", nullable = false)
    public Integer getRecevied() {
        return recevied;
    }

    public void setRecevied(Integer recevied) {
        this.recevied = recevied;
    }

    @JsonProperty
    @Column(name = "recevied_time", nullable = false)
    public Long getRecevied_time() {
        return recevied_time;
    }

    public void setRecevied_time(Long recevied_time) {
        this.recevied_time = recevied_time;
    }

    @JsonProperty
    @Column(name = "IDEAL")
    public Integer getiDeal() {
        return iDeal;
    }

    public void setiDeal(Integer iDeal) {
        this.iDeal = iDeal;
    }

    @JsonProperty
    @Transient
    public String getFrom_HeadPath() {
        return from_HeadPath;
    }

    public void setFrom_HeadPath(String from_HeadPath) {
        this.from_HeadPath = from_HeadPath;
    }

    @JsonProperty
    @Transient
    public String getTo_HeadPath() {
        return to_HeadPath;
    }

    public void setTo_HeadPath(String to_HeadPath) {
        this.to_HeadPath = to_HeadPath;
    }

    /**
     * 获取阅读时间
     * @return 阅读时间
     */
    @JsonProperty
    @JsonSerialize(using = DateTimeSerializer.class)
    @Transient
    public Date getDateRecevied_time() {
        if (getRecevied_time() != null && getRecevied_time() != 0L) {
            return new Date(getRecevied_time() * 1000);
        }
        return null;
    }

    /**
     * 获取阅读时间
     * @return 阅读时间
     */
    @JsonProperty
    @JsonSerialize(using = DateTimeSerializer.class)
    @Transient
    public Date getDateCreate() {
        if (getCreated() != null && getCreated() != 0L) {
            return new Date(getCreated() * 1000);
        }
        return null;
    }

    @JsonProperty
    @Transient
    public long getTotal() {
        return total;
    }

    public void setTotal(long total) {
        this.total = total;
    }
}
