package com.borsam.repository.entity.service;

import com.borsam.repository.entity.org.Organization;
import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.hiteam.common.base.repository.entity.BaseEntity;
import com.hiteam.common.base.repository.entity.LongEntity;

import org.hibernate.search.annotations.DocumentId;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotEmpty;

import javax.persistence.*;
import javax.validation.constraints.Digits;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.util.Date;

/**
 * Entity - 服务包
 * Created by Sebarswee on 2015/7/20.
 */
@JsonAutoDetect
@Entity
@Table(name = "service_package")
public class ServicePackage extends LongEntity {

    private String title;               // 标题
    private String content;             // 内容
    private BigDecimal price;           // 价格
    private Organization org;           // 机构
    private Long created;               // 创建时间
    private String types;               // 服务包内容：数据结构 [[1,100],[2,200]] [[类型,数目],..]
    private Integer expired;               // 有效时间,天数
    private Boolean isDelete;           // 是否删除
    private Long type;					//是否是内部包  1是 0否
    /**
     * 保存前处理
     */
    @PrePersist
    public void prePersist() {
        if(this.getId() == null){
            this.setCreated(new Date().getTime() / 1000);
        }
    }

    @Override
    @DocumentId
    @Id
    @GeneratedValue(strategy = GenerationType.TABLE, generator = "tableGenerator")
    @TableGenerator(name = "tableGenerator", table = "id_maker", pkColumnName = "maker_name",
            valueColumnName = "maker_value", pkColumnValue = "service_package", allocationSize = 1)
    public Long getId() {
        return super.getId();
    }
    
    @NotEmpty
    @Length(max = 10)
    @Column(name="type",nullable=false)
    public Long getType() {
		return type;
	}

	public void setType(Long type) {
		this.type = type;
	}

	/**
     * 获取标题
     *
     * @return 标题
     */
    @NotEmpty
    @Length(max = 50)
    @Column(name = "title", nullable = false, length = 50)
    public String getTitle() {
        return title;
    }

    /**
     * 设置标题
     *
     * @param title 标题
     */
    public void setTitle(String title) {
        this.title = title;
    }

    /**
     * 获取内容
     *
     * @return 内容
     */
    @NotEmpty
    @Length(max = 200)
    @Column(name = "content", nullable = false, length = 200)
    public String getContent() {
        return content;
    }


    /**
     * 设置内容
     *
     * @param content 内容
     */
    public void setContent(String content) {
        this.content = content;
    }

    /**
     * 获取价格
     *
     * @return 价格
     */
    @NotNull
    @Min(0)
    @Digits(integer = 10, fraction = 2)
    @Column(name = "price", nullable = false, precision = 10, scale = 2)
    public BigDecimal getPrice() {
        return price;
    }

    /**
     * 设置价格
     *
     * @param price 价格
     */
    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    /**
     * 获取机构
     *
     * @return 机构
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "oid", nullable = false)
    public Organization getOrg() {
        return org;
    }

    /**
     * 设置机构
     *
     * @param org 机构
     */
    public void setOrg(Organization org) {
        this.org = org;
    }

    /**
     * 获取创建时间
     *
     * @return 创建时间
     */
    @Column(name = "created", nullable = false, updatable = false)
    public Long getCreated() {
        return created;
    }

    /**
     * 设置创建时间
     *
     * @param created 创建时间
     */
    public void setCreated(Long created) {
        this.created = created;
    }

    /**
     * 获取条目
     *
     * @return 条目，数据结构 [[1,100],[2,200]] [[类型,数目],..]
     */
    @Lob
    @Column(name = "types", nullable = false)
    public String getTypes() {
        return types;
    }

    /**
     * 设置条目
     *
     * @param types 条目，数据结构 [[1,100],[2,200]] [[类型,数目],..]
     */
    public void setTypes(String types) {
        this.types = types;
    }

    /**
     * 获取有效天数
     *
     * @return 有效天数
     */
    @Min(0)
    @NotNull
    @Digits(integer = 10, fraction = 2)
    @Column(name = "expired_day", nullable = false)
    public Integer getExpired() {
        return expired;
    }

    /**
     * 设置有效时间
     *
     * @param expired 有效时间
     */
    public void setExpired(Integer expired) {
        this.expired = expired;
    }

    /**
     * 获取是否删除
     *
     * @return 是否删除
     */
    @NotNull
    @Column(name = "delete_flag", nullable = false)
    public Boolean getIsDelete() {
        return isDelete;
    }

    /**
     * 设置是否删除
     *
     * @param isDelete 是否删除
     */
    public void setIsDelete(Boolean isDelete) {
        this.isDelete = isDelete;
    }
}
