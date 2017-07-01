package com.hiteam.common.base.repository.entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.apache.commons.lang.builder.CompareToBuilder;
import org.hibernate.search.annotations.Field;
import org.hibernate.search.annotations.Index;
import org.hibernate.search.annotations.Store;

import javax.persistence.Column;
import javax.persistence.MappedSuperclass;
import javax.validation.constraints.Min;

/**
 * 排序基类
 */
@MappedSuperclass
public abstract class OrderEntity extends LongEntity implements Comparable<OrderEntity> {

	/**
	 * 
	 */
	private static final long serialVersionUID = -7234535276167904046L;

	/**
	 * 排序字段名
	 */
	public static final String ORDER_FIELD_NAME = "I_SEQ";
	
	/**
	 * 排序
	 */
	private Integer seq;
	
	/**
	 * 实现compareTo方法
	 */
	@Override
	public int compareTo(OrderEntity orderEntity) {
		return new CompareToBuilder().append(getSeq(), orderEntity.getSeq()).append(getId(), orderEntity.getId()).toComparison();
	}


	/*
	 * =------------------------------------------------------------=
	 * get & set methods
	 * =------------------------------------------------------------=
	 */

	@JsonProperty
	@Field(store = Store.YES, index = Index.UN_TOKENIZED)
	@Min(0L)
	@Column(name = "I_SEQ")
	public Integer getSeq() {
		return seq;
	}

	public void setSeq(Integer seq) {
		this.seq = seq;
	}
	
}
