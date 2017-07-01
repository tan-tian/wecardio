package com.hiteam.common.base.pojo.search;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * 分页信息

 *
 */
public class Pageable implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -1612798251454110275L;

	/**
	 * 默认页码
	 */
	private static final int DEFAULT_PAGE_NO = 1;
	
	/**
	 * 默认每页记录数
	 */
	private static final int DEFAULT_PAGE_SIZE = 20;
	
	/**
	 * 默认最大每页记录数
	 */
	private static final int MAX_PAGE_SIZE = 1000;
	
	private int pageNo = DEFAULT_PAGE_NO;		// 页码
	private int pageSize = DEFAULT_PAGE_SIZE;	// 每页记录数
	private String searchProperty;				// 搜索属性
	private String searchValue;					// 搜索值
	private String orderProperty;				// 排序属性
	private Order.Direction orderDirection;			// 排序值
	private List<Filter> filters = new ArrayList<Filter>();	// 筛选
	private List<Order> orders = new ArrayList<Order>();	// 排序
	
	public Pageable() {}
	
	public Pageable(Integer pageNo, Integer pageSize) {
		if ((pageNo != null) && (pageNo.intValue() >= 1)) {
			this.pageNo = pageNo.intValue();
		}
		if ((pageSize != null) && (pageSize.intValue() >= 1) && (pageSize.intValue() <= MAX_PAGE_SIZE)) {
			this.pageSize = pageSize.intValue();
		}
	}

	/*
	 * =------------------------------------------------------------=
	 * get & set methods
	 * =------------------------------------------------------------=
	 */
	public int getPageNo() {
		return pageNo;
	}

	public void setPageNo(int pageNo) {
		if (pageNo < 1) {
			pageNo = DEFAULT_PAGE_NO;
		}
		this.pageNo = pageNo;
	}

	public int getPageSize() {
		return pageSize;
	}

	public void setPageSize(int pageSize) {
		if (pageSize < 1 || pageSize > MAX_PAGE_SIZE) {
			pageSize = DEFAULT_PAGE_SIZE;
		}
		this.pageSize = pageSize;
	}

	public String getSearchProperty() {
		return searchProperty;
	}

	public void setSearchProperty(String searchProperty) {
		this.searchProperty = searchProperty;
	}

	public String getSearchValue() {
		return searchValue;
	}

	public void setSearchValue(String searchValue) {
		this.searchValue = searchValue;
	}

	public String getOrderProperty() {
		return orderProperty;
	}

	/**
	 * @param orderProperty	排序属性
	 */
	public void setOrderProperty(String orderProperty) {
		this.orderProperty = orderProperty;
	}

	public Order.Direction getOrderDirection() {
		return orderDirection;
	}

	/**
	 * @param orderDirection	排序值[asc, desc]
	 */
	public void setOrderDirection(Order.Direction orderDirection) {
		this.orderDirection = orderDirection;
	}

	public List<Filter> getFilters() {
		return filters;
	}

	public void setFilters(List<Filter> filters) {
		this.filters = filters;
	}

	public List<Order> getOrders() {
		return orders;
	}

	public void setOrders(List<Order> orders) {
		this.orders = orders;
	}
	
	/*
	 * =------------------------------------------------------------=
	 * Generate equals & hashCode methods
	 * =------------------------------------------------------------=
	 */
	@Override
	public boolean equals(Object obj) {
		if (obj == null) {
			return false;
		}
		
		if (this.getClass() != obj.getClass()) {
			return false;
		}
		
		if (this == obj) {
			return true;
		}
		
		Pageable pageable = (Pageable) obj;
		
		return new EqualsBuilder()
			.append(this.getPageNo(), pageable.getPageNo())
			.append(this.getPageSize(), pageable.getPageSize())
			.append(this.getSearchProperty(), pageable.getSearchProperty())
			.append(this.getSearchValue(), pageable.getSearchValue())
			.append(this.getOrderProperty(), pageable.getOrderProperty())
			.append(this.getOrderDirection(), pageable.getOrderDirection())
			.append(this.getFilters(), pageable.getFilters())
			.append(this.getOrders(), pageable.getOrders())
			.isEquals();
	}
	
	@Override
	public int hashCode() {
		return new HashCodeBuilder(17, 37)
			.append(this.getPageNo())
			.append(this.getPageSize())
			.append(this.getSearchProperty())
			.append(this.getSearchValue())
			.append(this.getOrderProperty())
			.append(this.getOrderDirection())
			.append(this.getFilters())
			.append(this.getOrders())
			.toHashCode();
	}
}
