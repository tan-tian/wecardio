package com.hiteam.common.base.repository.dao;

import com.hiteam.common.base.pojo.search.Filter;
import com.hiteam.common.base.pojo.search.Order;
import com.hiteam.common.base.pojo.search.Page;
import com.hiteam.common.base.pojo.search.Pageable;

import javax.persistence.LockModeType;
import java.io.Serializable;
import java.util.List;
import java.util.Map;

/**
 * Dao 基类
 */
public abstract interface BaseDao<T, ID extends Serializable> {

	/**
	 * 获取实体类型
	 * @return
	 */
	Class<T> getEntityClass();

	/**
	 * 查找实体对象
	 *
	 * @param id
	 * @return 实体对象，找不到就返回null
	 */
	public abstract T find(ID id);

	/**
	 * 查找实体对象
	 *
	 * @param id
	 * @param lockModeType
	 *            锁定方式
	 * @return 实体对象，找不到就返回null
	 */
	public abstract T find(ID id, LockModeType lockModeType);

	/**
	 * 查找实体对象集合
	 *
	 * @param first
	 *            起始记录
	 * @param count
	 *            数量
	 * @param filters
	 *            筛选
	 * @param orders
	 *            排序
	 * @return 实体对象集合
	 */
	public abstract List<T> findList(Integer first, Integer count,
									 List<Filter> filters, List<Order> orders);

	/**
	 * 查询所有
	 * @return  实体对象集合
	 */
	public abstract List<T> findAll();

	/**
	 * 查找实体对象集合
	 * @param ids
	 * @return
	 */
	public abstract List<T> findList(ID... ids);

	/**
	 * 查找实体对象集合
	 * @param ids
	 * @return
	 */
	public abstract List<T> findList(List<ID> ids);

	/**
	 * 分页查找实体对象
	 *
	 * @param pageable
	 *            分页对象
	 * @return 分页实体对象
	 */
	public abstract Page<T> findPage(Pageable pageable);

	/**
	 * 原生SQL分页查询对象<br>
	 * 注意：查询的SQL字段需要添加别名，便于在转换会对象使用
	 *
	 * @param sql
	 *            查询语句
	 * @param params
	 *            查询参数
	 * @param pageable
	 *            分页信息
	 * @return 分页数据
	 */
	public abstract <A> Page<A> findPageBySql(String sql,
											  Map<String, Object> params, Pageable pageable, Class<A> clazz);

	/***
	 * 原生SQL统计数量
	 *
	 * @param sql
	 *            查询语句
	 * @param params
	 *            查询参数
	 * @return
	 */
	public abstract Long countBySql(String sql, Map<String, Object> params);

	/**
	 * JQL分页查询对象<br>
	 * 	注：如果需要转换为Map对象，则类似：SELECT new Map(count(1) as tag) FROM ResourceKpiCfgRef
	 * @param jql
	 *            查询语句
	 * @param params
	 *            查询参数
	 * @param pageable
	 *            分页信息
	 * @return 分页数据
	 */
	public abstract <A> Page<A> findPageByJql(String jql,
											  Map<String, Object> params, Pageable pageable, Class<A> clazz);

	/**
	 * JQL查询对象<br>
	 * 	注：如果需要转换为Map对象，则类似：SELECT new Map(count(1) as tag) FROM ResourceKpiCfgRef
	 * @param jql
	 *            查询语句
	 * @param params
	 *            查询参数
	 * @param clazz 查询结果类型
	 * @return List数据
	 */
	public abstract <A> List<A> findListByJql(String jql,
											  Map<String, Object> params, Class<A> clazz);

	/**
	 * JQL查询对象<br>
	 * 	注：转换为Map对象，则类似：SELECT new Map(count(1) as tag) FROM ResourceKpiCfgRef
	 * @param jql
	 *            查询语句
	 * @param params
	 *            查询参数
	 * @return List数据
	 */
	public abstract List<Map> findListByJql(String jql,
											Map<String, Object> params);

	/***
	 * JQL统计数量
	 *
	 * @param jql
	 *            查询语句
	 * @param params
	 *            查询参数
	 * @return
	 */
	public abstract Long countByJql(String jql, Map<String, Object> params);

	/**
	 * 查询实体对象数量
	 *
	 * @param filters
	 *            筛选
	 * @return 实体对象数量
	 */
	public abstract long count(Filter... filters);

	/**
	 * 持久化实体对象
	 *
	 * @param entity
	 *            实体对象
	 */
	public abstract void persist(T entity);

	/**
	 * 批量保存实体
	 * @param entitys 实体对象
	 */
	public abstract void persist(List<T> entitys);

	/**
	 * 合并实体对象
	 *
	 * @param entity
	 *            实体对象
	 * @return
	 */
	public abstract T merge(T entity);
	/**
	 * 合并实体对象
	 *
	 * @param entitys
	 *            实体对象
	 * @return
	 */
	public abstract void merge(List<T> entitys);

	/**
	 * 新增或更新对象
	 * @param entity 实体对象
     */
	public void saveOrUpdate(T entity);
	/**
	 * 移除实体对象
	 *
	 * @param entity
	 *            实体对象
	 */
	public abstract void remove(T entity);

	/**
	 * 删除所有实体对象
	 */
	public abstract void removeAll();

	/**
	 * 刷新实体对象
	 *
	 * @param entity
	 *            实体对象
	 */
	public abstract void refresh(T entity);

	/**
	 * 刷新实体对象
	 *
	 * @param entity
	 *            实体对象
	 * @param lockModeType
	 *            锁定方式
	 */
	public abstract void refresh(T entity, LockModeType lockModeType);

	/**
	 * 获取实体对象ID
	 *
	 * @param entity
	 *            实体对象
	 * @return 实体对象ID
	 */
	public abstract ID getIdentifier(T entity);

	/**
	 * 判断实体对象是否为托管状态
	 *
	 * @param entity
	 *            实体对象
	 * @return
	 */
	public abstract boolean isManaged(T entity);

	/**
	 * 设置实体对象为游离状态
	 *
	 * @param entity
	 *            实体对象
	 */
	public abstract void detach(T entity);

	/**
	 * 锁定实体对象
	 *
	 * @param entity
	 *            实体对象
	 * @param lockModeType
	 *            锁定方式
	 */
	public abstract void lock(T entity, LockModeType lockModeType);

	/**
	 * 清除缓存
	 */
	public abstract void clear();

	/**
	 * 同步数据
	 */
	public abstract void flush();
}
