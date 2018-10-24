package com.hiteam.common.base.service.impl;

import com.hiteam.common.base.pojo.search.Filter;
import com.hiteam.common.base.pojo.search.Order;
import com.hiteam.common.base.pojo.search.Page;
import com.hiteam.common.base.pojo.search.Pageable;
import com.hiteam.common.base.repository.dao.BaseDao;
import com.hiteam.common.base.repository.entity.BaseEntity;
import com.hiteam.common.base.service.BaseService;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.ArrayUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.FatalBeanException;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import java.beans.PropertyDescriptor;
import java.io.Serializable;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

@Transactional
public class BaseServiceImpl<T, ID extends Serializable> implements BaseService<T, ID> {

	/**
	 * 更新时忽略属性
	 */
	private static final String[] IGNORE_PROPERTIES = {
		BaseEntity.ID_FIELD_NAME
	};
	
	private BaseDao<T, ID> baseDao;

	public void setBaseDao(BaseDao<T, ID> baseDao) {
		this.baseDao = baseDao;
	}

	@Transactional(readOnly = true)
	public T find(ID id) {
		return this.baseDao.find(id);
	}

	@Transactional(readOnly = true)
	public List<T> findAll() {
		return findList(null, null, null, null);
	}

	@Transactional(readOnly = true)
	public List<T> findList(ID... ids) {
		ArrayList<T> list = new ArrayList<T>();
		if (ids != null) {
			for (ID id : ids) {
				T obj = find(id);
				if (obj != null) {
					list.add(obj);
				}
			}
		}
		return list;
	}

	@Transactional(readOnly = true)
	public List<T> findList(Integer count, List<Filter> filters, List<Order> orders) {
		return findList(null, count, filters, orders);
	}

	@Override
	public void update(T object, String created) {

	}

	@Transactional(readOnly = true)
	public List<T> findList(Integer first, Integer count, List<Filter> filters, List<Order> orders) {
		return this.baseDao.findList(first, count, filters, orders);
	}

	@Transactional(readOnly = true)
	public Page<T> findPage(Pageable pageable) {
		return this.baseDao.findPage(pageable);
	}

//	@Override
//	public Page<T> findPageByJql(Pageable pageable) {
//		return this.baseDao.findPageByJql(pageable);
//	}

	@Transactional(readOnly = true)
	public long count() {
		return count(new Filter[0]);
	}

	@Transactional(readOnly = true)
	public long count(Filter... filters) {
		return this.baseDao.count(filters);
	}

	@Transactional(readOnly = true)
	public boolean exists(ID id) {
		return this.baseDao.find(id) != null;
	}

	@Transactional(readOnly = true)
	public boolean exists(Filter... filters) {
		return this.baseDao.count(filters) > 0L;
	}

	@Transactional
	public void save(T entity) {
		this.baseDao.persist(entity);
	}

	@Transactional
	public void saveOrUpdate(List<T> entitys) {
		List<T> upList = new ArrayList<T>();
		List<T> newList = new ArrayList<T>();

		for (T entity : entitys) {
			BaseEntity baseEntity = (BaseEntity)entity;
			if(baseEntity.getId() != null){
				upList.add((T)baseEntity);
			}else{
				newList.add((T)baseEntity);
			}
		}

		if(CollectionUtils.isNotEmpty(upList)){
			this.baseDao.merge(upList);
		}

		if(CollectionUtils.isNotEmpty(newList)){
			this.baseDao.persist(newList);
		}

	}

    @Override
    public void saveOrUpdate(T entity) {
        BaseEntity baseEntity = (BaseEntity)entity;

        if(baseEntity.getId() != null){
            this.baseDao.merge((T)baseEntity);
        }else{
            this.baseDao.persist((T)baseEntity);
        }
    }

    @Transactional
	public T update(T entity) {
		return this.baseDao.merge(entity);
	}

	@Transactional
	public T update(T entity, String... ignoreProperties) {
		Assert.notNull(entity);
		if (this.baseDao.isManaged(entity)) {
			throw new IllegalArgumentException("Entity must not be managed");
		}
		T obj = this.baseDao.find(this.baseDao.getIdentifier(entity));
		if (obj != null) {
			copyProperties(entity, obj, (String[]) ArrayUtils.addAll(ignoreProperties, IGNORE_PROPERTIES));
			return update(obj);
		}
		return update(entity);
	}

	@Transactional
	public void delete(ID id) {
		delete(this.baseDao.find(id));
	}

	@Transactional
	public void delete(ID... ids) {
		if (ids != null) {
			for (ID id : ids) {
				delete(this.baseDao.find(id));
			}
		}
	}

	@Transactional
	public void delete(T entity) {
		this.baseDao.remove(entity);
	}

    @Override
    public void deleteAll() {
        this.baseDao.removeAll();
    }

    /**
	 * 复制属性
	 * @param source
	 * @param target
	 * @param ignoreProperties
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	private void copyProperties(Object source, Object target, String[] ignoreProperties) {
		Assert.notNull(source, "Source must not be null");
		Assert.notNull(target, "Target must not be null");
		
		PropertyDescriptor[] targetPDs = BeanUtils.getPropertyDescriptors(target.getClass());
		List<String> ignoreList = ignoreProperties != null ? Arrays.asList(ignoreProperties) : null;
		
		for (PropertyDescriptor targetPD : targetPDs) {
			if ((targetPD.getWriteMethod() != null) && ((ignoreProperties == null) || (!ignoreList.contains(targetPD.getName())))) {
				PropertyDescriptor sourcePD = BeanUtils.getPropertyDescriptor(source.getClass(), targetPD.getName());
				if ((sourcePD != null) && (sourcePD.getReadMethod() != null)) {
					try {
						Method readMethod = sourcePD.getReadMethod();
						if (!Modifier.isPublic(readMethod.getDeclaringClass().getModifiers())) {
							readMethod.setAccessible(true);
						}
						Object sourceValue = readMethod.invoke(source, new Object[0]);
						Object targetValue = readMethod.invoke(target, new Object[0]);
						if ((sourceValue != null) && (targetValue != null) && ((targetValue instanceof Collection))) {
							Collection collection = (Collection) targetValue;
							collection.clear();
							collection.addAll((Collection) sourceValue);
						} else {
							Method writeMethod = targetPD.getWriteMethod();
							if (!Modifier.isPublic(writeMethod.getDeclaringClass().getModifiers())) {
								writeMethod.setAccessible(true);
							}
							writeMethod.invoke(target, sourceValue);
						}
					} catch (Throwable e) {
						throw new FatalBeanException("Could not copy properties from source to target", e);
					}
				}
			}
		}
	}

	@Override
	public Class<T> getEntityClass() {
		return baseDao.getEntityClass();
	}

}