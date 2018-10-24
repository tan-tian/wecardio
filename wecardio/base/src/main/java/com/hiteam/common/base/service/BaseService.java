package com.hiteam.common.base.service;

import com.hiteam.common.base.pojo.search.Filter;
import com.hiteam.common.base.pojo.search.Order;
import com.hiteam.common.base.pojo.search.Page;
import com.hiteam.common.base.pojo.search.Pageable;

import java.util.List;

/**
 * Service基类
 * @author tantian
 *
 * @param <T>
 * @param <ID>
 */
public interface BaseService<T,ID> {

	void saveOrUpdate(T entity);

	void deleteAll();

	Class<T> getEntityClass();

	void save(T object);

	void saveOrUpdate(List<T> list);
	
	
	T find(ID id);
	
	public List<T> findAll();


    <T> Page<T> findPage(Pageable pageable);

	void delete(ID... ids);

	List<T> findList(ID... ids);

	List<T> findList(Integer count, List<Filter> filters, List<Order> orders);

	void update(T object, String created);

	T update(T entity);
}