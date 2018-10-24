package com.borsam.repository.entity.org;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.hiteam.common.base.repository.entity.BaseEntity;
import com.hiteam.common.base.repository.entity.LongEntity;

import org.apache.commons.lang.StringUtils;
import org.hibernate.search.annotations.DocumentId;

import javax.persistence.*;

/**
 * Entity - 机构图片
 * Created by tantian on 2015/7/1.
 */
@Entity
@Table(name = "organization_pic")
public class OrganizationImage extends LongEntity {

    private Organization org;       // 机构
    private String source;          // 原图片
    private String large;           // 大图
    private String medium;          // 中图
    private String thumbnail;       // 缩略图
    private String filePath;        // 文件路径

    /**
     * 获取ID
     * @return ID
     */
    @JsonProperty
    @DocumentId
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.TABLE, generator = "tableGenerator")
    @TableGenerator(name = "tableGenerator", table = "id_maker", pkColumnName = "maker_name",
            valueColumnName = "maker_value", pkColumnValue = "organization_pic", allocationSize = 1)
    public Long getId() {
        return super.getId();
    }

    /**
     * 获取机构
     * @return 机构
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "oid")
    public Organization getOrg() {
        return org;
    }

    /**
     * 设置机构
     * @param org 机构
     */
    public void setOrg(Organization org) {
        this.org = org;
    }

    /**
     * 获取原图片
     * @return 原图片
     */
    @Column(name = "s_source")
    public String getSource() {
        return source;
    }

    /**
     * 设置原图片
     * @param source 原图片
     */
    public void setSource(String source) {
        this.source = source;
    }

    /**
     * 获取大图片
     * @return 大图片
     */
    @Column(name = "s_large")
    public String getLarge() {
        return large;
    }

    /**
     * 设置大图片
     * @param large 大图片
     */
    public void setLarge(String large) {
        this.large = large;
    }

    /**
     * 获取中图片
     * @return 中图片
     */
    @Column(name = "s_medium")
    public String getMedium() {
        return medium;
    }

    /**
     * 设置中图片
     * @param medium 中图片
     */
    public void setMedium(String medium) {
        this.medium = medium;
    }

    /**
     * 获取缩略图
     * @return 缩略图
     */
    @Column(name = "s_thumbnail")
    public String getThumbnail() {
        return thumbnail;
    }

    /**
     * 设置缩略图
     * @param thumbnail 缩略图
     */
    public void setThumbnail(String thumbnail) {
        this.thumbnail = thumbnail;
    }

    /**
     * 获取文件路径
     * @return 文件路径
     */
    @Transient
    public String getFilePath() {
        return filePath;
    }

    /**
     * 设置文件路径
     * @param filePath 文件路径
     */
    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    /**
     * 判断是否为空
     * @return 是否为空
     */
    @Transient
    public boolean isEmpty() {
        return (getFilePath() == null || getFilePath().isEmpty()) && (StringUtils.isEmpty(getSource()) || StringUtils.isEmpty(getLarge()) || StringUtils.isEmpty(getMedium()) || StringUtils.isEmpty(getThumbnail()));
    }
}
