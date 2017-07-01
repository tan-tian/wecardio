package com.borsam.pojo.service;

import com.borsam.repository.entity.org.Organization;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Zhang zhongtao on 2015/7/25.
 */
public class EditServicePackageData implements Serializable {
    private Long id;
    /**
     * 标题
     */
    private String title;
    /**
     * 内容
     */
    private String content;
    /**
     * 价格
     */
    private BigDecimal price;
    /**
     * 机构
     */
    private Organization org;

    /**
     * 有效时间
     */
    private Integer expired;

    /**
     * 服务项与次数
     */
    private List<Types> types = new ArrayList<Types>();
    
    /**
     * 内部服务包（1是 0否）
     */
    private Long type;

    public Long getType() {
		return type;
	}

	public void setType(Long type) {
		this.type = type;
	}

	public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public Organization getOrg() {
        return org;
    }

    public void setOrg(Organization org) {
        this.org = org;
    }

    public List<Types> getTypes() {
        return types;
    }

    public void setTypes(List<Types> types) {
        this.types = types;
    }

    public Integer getExpired() {
        return expired;
    }

    public void setExpired(Integer expired) {
        this.expired = expired;
    }

    public static class Types {
        private Long type;
        private Integer times;

        public Integer getTimes() {
            return times;
        }

        public void setTimes(Integer times) {
            this.times = times;
        }

        public Long getType() {
            return type;
        }

        public void setType(Long type) {
            this.type = type;
        }
    }
}
