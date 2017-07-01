package com.hiteam.common.base.pojo.search;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * 分页

 *
 * @param <T>
 */
public class Page<T> implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 6421029198128287682L;

	/**
	 * 内容
	 */
	private final List<T> content = new ArrayList<T>();
	
	/**
	 * 总记录数
	 */
	private final long total;
	
	/**
	 * 分页信息
	 */
	private final Pageable pageable;
	
	public Page() {
		this.total = 0L;
		this.pageable = new Pageable();
	}
	
	public Page(List<T> content, long total, Pageable pageable) {
		if(content != null){
			this.content.addAll(content);
		}
		this.total = total;
		this.pageable = pageable;
	}
	
	/**
	 * 获取页码
	 * @return
	 */
	public int getPageNo() {
		return pageable.getPageNo();
	}
	
	/**
	 * 获取每页记录数
	 * @return
	 */
	public int getPageSize() {
		return pageable.getPageSize();
	}
	
	/**
	 * 获取搜索属性
	 * @return
	 */
	public String getSearchProperty() {
		return pageable.getSearchProperty();
	}
	
	/**
	 * 获取搜索值
	 * @return
	 */
	public String getSearchValue() {
		return pageable.getSearchValue();
	}
	
	/**
	 * 获取排序属性
	 * @return
	 */
	public String getOrderProperty() {
		return pageable.getOrderProperty();
	}
	
	/**
	 * 获取排序方向
	 * @return
	 */
	public Order.Direction getOrderDirection() {
		return pageable.getOrderDirection();
	}
	
	/**
	 * 获取排序
	 * @return
	 */
	public List<Order> getOrders() {
		return pageable.getOrders();
	}
	
	/**
	 * 获取筛选
	 * @return
	 */
	public List<Filter> getFilters() {
		return pageable.getFilters();
	}
	
	/**
	 * 获取总页数
	 * @return
	 */
	public int getTotalPages() {
		return (int) Math.ceil((double)getTotal() / (double)getPageSize());
	}
	
	/*
	 * =------------------------------------------------------------=
	 * get & set methods
	 * =------------------------------------------------------------=
	 */
	public List<T> getContent() {
		return this.content;
	}

	public long getTotal() {
		return this.total;
	}

	public Pageable getPageable() {
		return this.pageable;
	}
}
