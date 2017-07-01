package com.hiteam.common.base.repository.dao.impl;


import com.hiteam.common.base.pojo.search.Filter;
import com.hiteam.common.base.pojo.search.Order;
import com.hiteam.common.base.pojo.search.Page;
import com.hiteam.common.base.pojo.search.Pageable;
import com.hiteam.common.base.repository.dao.BaseDao;
import com.hiteam.common.base.repository.entity.BaseEntity;
import com.hiteam.common.base.repository.entity.OrderEntity;
import com.hiteam.common.util.collections.CollectionUtil;
import com.hiteam.common.util.collections.MapUtil;
import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang.StringUtils;
import org.hibernate.jpa.criteria.predicate.CompoundPredicate;
import org.hibernate.jpa.internal.QueryImpl;
import org.hibernate.transform.Transformers;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import javax.persistence.*;
import javax.persistence.criteria.*;
import java.io.Serializable;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.*;

/**
 * Dao 基类
 */
public abstract class BaseDaoImpl<T, ID extends Serializable> implements
        BaseDao<T, ID> {

    /**
     * 实体类类型
     */
    private Class<T> entityClass;

    /**
     * 别名数
     */
    private static volatile long aliasCount = 0L;

    @PersistenceContext
    protected EntityManager entityManager;

    @SuppressWarnings("unchecked")
    public BaseDaoImpl() {
        Type type = getClass().getGenericSuperclass();
        Type[] parameterizedType = ((ParameterizedType) type).getActualTypeArguments();
        this.entityClass = (Class<T>) parameterizedType[0];
    }

    public T find(ID id) {
        if (id != null) {
            return this.entityManager.find(this.entityClass, id);
        }
        return null;
    }

    public T find(ID id, LockModeType lockModeType) {
        if (id != null) {
            if (lockModeType != null) {
                return entityManager.find(entityClass, id, lockModeType);
            } else {
                return entityManager.find(entityClass, id);
            }
        }
        return null;
    }

    public List<T> findList(Integer first, Integer count, List<Filter> filters, List<Order> orders) {
        CriteriaBuilder criteriaBuilder = this.entityManager.getCriteriaBuilder();
        CriteriaQuery<T> criteriaQuery = criteriaBuilder.createQuery(this.entityClass);
        criteriaQuery.select(criteriaQuery.from(this.entityClass));
        return findList(criteriaQuery, first, count, filters, orders);
    }

    @Override
    public List<T> findAll() {
        return findList(null, null, null, null);
    }

    @Transactional(readOnly = true)
    public List<T> findList(ID... ids) {
        return this.findList(Arrays.asList(ids));
    }

    @Transactional(readOnly = true)
    public List<T> findList(final List<ID> ids) {
        if (ids.isEmpty()) {
            return new ArrayList<T>();
        }
        return this.findList(null, null, new ArrayList<Filter>() {{
            add(Filter.in("id", ids.toArray()));
        }}, null);
    }

    public Page<T> findPage(Pageable pageable) {
        CriteriaBuilder criteriaBuilder = this.entityManager.getCriteriaBuilder();
        CriteriaQuery<T> criteriaQuery = criteriaBuilder.createQuery(this.entityClass);
        criteriaQuery.select(criteriaQuery.from(this.entityClass));
        return findPage(criteriaQuery, pageable);
    }

    @Override
    public <A> Page<A> findPageBySql(String sql, Map<String, Object> params,
                                     Pageable pageable, Class<A> clazz) {
        Assert.notNull(clazz);
        Assert.hasLength(sql);
        Assert.notNull(pageable);

        // 查询总数
        Long total = countBySql(sql, params);

        int totalPage = (int) Math.ceil((double) total / pageable.getPageSize());
        if (totalPage < pageable.getPageNo()) {
            pageable.setPageNo(totalPage);
        }

        QueryImpl query = (QueryImpl) this.entityManager.createNativeQuery(sql);
        if (clazz.isAssignableFrom(Map.class)) {// Map类型
            query.getHibernateQuery().setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
        } else {// 其他java类型
            query.getHibernateQuery().setResultTransformer(Transformers.aliasToBean(clazz));
        }

        setNativeQueryParameter(sql, query, params);

        query.setFirstResult((pageable.getPageNo() - 1) * pageable.getPageSize());
        query.setMaxResults(pageable.getPageSize());

        return new Page<A>(query.getResultList(), total, pageable);
    }

    @Override
    public Long countBySql(String sql, Map<String, Object> params) {
        Query typedQuery = this.entityManager.createNativeQuery("SELECT count(1) FROM (" + sql + ") T");

        setNativeQueryParameter(sql, typedQuery, params);

        Long count = Long.parseLong(typedQuery.getSingleResult().toString());

        return count;
    }

    @Override
    public <A> Page<A> findPageByJql(String jql, Map<String, Object> params,
                                     Pageable pageable, Class<A> clazz) {
        Assert.notNull(clazz);
        Assert.hasLength(jql);
        Assert.notNull(pageable);

        // 查询总数
        Long total = countByJql(jql, params);

        int totalPage = (int) Math.ceil((double) total / pageable.getPageSize());

        if (totalPage < pageable.getPageNo()) {
            pageable.setPageNo(totalPage);
        }

        int firstResult = (pageable.getPageNo() - 1) * pageable.getPageSize();

        Query query = this.entityManager.createQuery(jql, clazz)
                .setFirstResult(firstResult).setMaxResults(pageable.getPageSize());

        setNativeQueryParameter(jql, query, params);

        return new Page<A>(query.getResultList(), total, pageable);
    }

    @Override
    public <A> List<A> findListByJql(String jql, Map<String, Object> params, Class<A> clazz) {
        Assert.notNull(clazz);
        Assert.hasLength(jql);

        Query query = this.entityManager.createQuery(jql, clazz);

        setNativeQueryParameter(jql, query, params);

        return query.getResultList();
    }

    @Override
    public List<Map> findListByJql(String jql, Map<String, Object> params) {
        return findListByJql(jql, params, Map.class);
    }

    @Override
    public Long countByJql(String jql, Map<String, Object> params) {

        String replaceVal = "SELECT COUNT(*) FROM";

        //处理去重关键字，避免数据统计不准确
        if (jql.toUpperCase().indexOf(" DISTINCT ") != -1) {
            replaceVal = "SELECT COUNT($1) FROM";
        }

        jql = jql.replaceFirst("(?i)SELECT (.*) FROM", replaceVal);
        //去掉fetch 关键字，避免JPA 异常
        jql = jql.replaceAll("(?si)\\s+fetch\\s+", " ");
        Query query = this.entityManager.createQuery(jql);

        setNativeQueryParameter(jql, query, params);

        List<Long> counts = query.getResultList();

        Long count = CollectionUtil.getFirst(counts);

        if (count == null) {
            count = 0L;
        }

        return count;
    }

    /**
     * 设置本地查询参数
     *
     * @param sql    本地查询sql
     * @param query  本地查询query对象
     * @param params 参数值
     */
    private void setNativeQueryParameter(String sql, Query query,
                                         Map<String, Object> params) {
        if (MapUtils.isNotEmpty(params)) {
            for (String dataKey : params.keySet()) {
                if (sql.matches(".*(\\W|^):" + dataKey + "(\\W|$).*")) {
                    Class clazz = query.getParameter(dataKey).getParameterType();

                    Object paraVal = MapUtil.getObject(params, dataKey, null, clazz);
                    if (paraVal == null) {
                        paraVal = MapUtil.getObject(params, dataKey, null);
                    }

                    query.setParameter(dataKey, paraVal);
                }
            }
        }
    }

    /**
     * 设置本地查询参数
     *
     * @param sql    本地查询sql
     * @param query  本地查询query对象
     * @param params 参数值
     */
    private void setNativeQueryParameter(String sql, org.hibernate.Query query,
                                         Map<String, Object> params) {
        if (MapUtils.isNotEmpty(params)) {
            for (String dataKey : params.keySet()) {
                if (sql.matches(".*(\\W|^):" + dataKey + "(\\W|$).*")) {
                    Object paraVal = MapUtil.getObject(params, dataKey, null);
                    query.setParameter(dataKey, paraVal);
                }
            }
        }
    }

    public long count(Filter... filters) {
        CriteriaBuilder criteriaBuilder = this.entityManager
                .getCriteriaBuilder();
        CriteriaQuery<T> criteriaQuery = criteriaBuilder
                .createQuery(this.entityClass);
        criteriaQuery.select(criteriaQuery.from(this.entityClass));
        return count(criteriaQuery,
                filters != null ? Arrays.asList(filters) : null).longValue();
    }

    public void persist(T entity) {
        Assert.notNull(entity);
        this.entityManager.persist(entity);
    }

    @Override
    public void persist(List<T> entitys) {

        for (int i = 0; i < entitys.size(); i++) {
            entityManager.persist(entitys.get(i));
            if (i % 100 == 0) {
                entityManager.flush();
                entityManager.clear();
            }
        }

        entityManager.flush();
        entityManager.clear();
    }

    public T merge(T entity) {
        Assert.notNull(entity);
        return this.entityManager.merge(entity);
    }

    @Override
    public void merge(List<T> entitys) {
        for (int i = 0; i < entitys.size(); i++) {
            entityManager.merge(entitys.get(i));
            if (i % 100 == 0) {
                entityManager.flush();
                entityManager.clear();
            }
        }

        entityManager.flush();
        entityManager.clear();
    }

    public void remove(T entity) {
        if (entity != null) {
            this.entityManager.remove(entity);
        }
    }

    @Override
    public void removeAll() {
        this.entityManager.createQuery("DELETE FROM  " + entityClass);
        entityManager.flush();
        entityManager.clear();
    }

    public void refresh(T entity) {
        Assert.notNull(entity);
        this.entityManager.refresh(entity);
    }

    public void refresh(T entity, LockModeType lockModeType) {
        if (entity != null) {
            if (lockModeType != null) {
                entityManager.refresh(entity, lockModeType);
            } else {
                entityManager.refresh(entity);
            }
        }
    }

    @SuppressWarnings("unchecked")
    public ID getIdentifier(T entity) {
        Assert.notNull(entity);
        return (ID) this.entityManager.getEntityManagerFactory()
                .getPersistenceUnitUtil().getIdentifier(entity);
    }

    public boolean isManaged(T entity) {
        return this.entityManager.contains(entity);
    }

    public void detach(T entity) {
        this.entityManager.detach(entity);
    }

    public void lock(T entity, LockModeType lockModeType) {
        if ((entity != null) && (lockModeType != null)) {
            this.entityManager.lock(entity, lockModeType);
        }
    }

    public void clear() {
        this.entityManager.clear();
    }

    public void flush() {
        this.entityManager.flush();
    }

    protected List<T> findList(CriteriaQuery<T> criteriaQuery, Integer first,
                               Integer count, List<Filter> filters, List<Order> orders) {
        Assert.notNull(criteriaQuery);
        Assert.notNull(criteriaQuery.getSelection());
        Assert.notEmpty(criteriaQuery.getRoots());

        CriteriaBuilder criteriaBuilder = this.entityManager.getCriteriaBuilder();
        Root<T> root = getRoot(criteriaQuery);
        addRestrictions(criteriaQuery, filters);
        addOrders(criteriaQuery, orders);
        if (criteriaQuery.getOrderList().isEmpty()) {
            if (OrderEntity.class.isAssignableFrom(this.entityClass)) {
                criteriaQuery.orderBy(criteriaBuilder.asc(root.get(OrderEntity.ORDER_FIELD_NAME)));
            } else {
//                criteriaQuery.orderBy(criteriaBuilder.asc(root.get("id")));
            }
        }

        TypedQuery<T> typedQuery = this.entityManager.createQuery(criteriaQuery).setFlushMode(FlushModeType.COMMIT);
        if (first != null) {
            typedQuery.setFirstResult(first.intValue());
        }
        if (count != null) {
            typedQuery.setMaxResults(count.intValue());
        }
        return typedQuery.getResultList();
    }

    @Override
    public void saveOrUpdate(T entity) {
        Assert.notNull(entity);

        BaseEntity baseEntity = (BaseEntity)entity;

        if(baseEntity.getId() == null){
            this.entityManager.persist(entity);
        }else {
            this.entityManager.merge(entity);
        }
    }

    protected Page<T> findPage(CriteriaQuery<T> criteriaQuery, Pageable pageable) {
        Assert.notNull(criteriaQuery);
        Assert.notNull(criteriaQuery.getSelection());
        Assert.notEmpty(criteriaQuery.getRoots());

        if (pageable == null) {
            pageable = new Pageable();
        }
        CriteriaBuilder criteriaBuilder = this.entityManager.getCriteriaBuilder();
        Root<T> root = getRoot(criteriaQuery);
        addRestrictions(criteriaQuery, pageable);
        addOrders(criteriaQuery, pageable);

        if (criteriaQuery.getOrderList().isEmpty()) {
            if (OrderEntity.class.isAssignableFrom(this.entityClass)) {
                criteriaQuery.orderBy(criteriaBuilder.asc(root.get("order")));
            } else {
//                criteriaQuery.orderBy(criteriaBuilder.asc(root.get("id")));
            }
        }

        long total = count(criteriaQuery, null).longValue();
        int totalPage = (int) Math.ceil((double) total / pageable.getPageSize());
        if (totalPage < pageable.getPageNo()) {
            pageable.setPageNo(totalPage);
        }

        TypedQuery<T> typedQuery = this.entityManager
                .createQuery(criteriaQuery).setFlushMode(FlushModeType.COMMIT);
        typedQuery.setFirstResult((pageable.getPageNo() - 1) * pageable.getPageSize());
        typedQuery.setMaxResults(pageable.getPageSize());
        return new Page<T>(typedQuery.getResultList(), total, pageable);
    }

    protected Long count(CriteriaQuery<T> criteriaQuery, List<Filter> filters) {
        Assert.notNull(criteriaQuery);
        Assert.notNull(criteriaQuery.getSelection());
        Assert.notEmpty(criteriaQuery.getRoots());

        CriteriaBuilder criteriaBuilder = this.entityManager.getCriteriaBuilder();
        addRestrictions(criteriaQuery, filters);

        CriteriaQuery<Long> countCriteriaQuery = criteriaBuilder.createQuery(Long.class);

        for (Root<?> root : criteriaQuery.getRoots()) {
            Root<?> dest = countCriteriaQuery.from(root.getJavaType());
            dest.alias(getAlias(root));
            copyJoins(root, dest);
        }

        Root<?> countRoot = getRoot(countCriteriaQuery, criteriaQuery.getResultType());
        countCriteriaQuery.select(criteriaBuilder.count(countRoot));

        if (criteriaQuery.getGroupList() != null) {
            countCriteriaQuery.groupBy(criteriaQuery.getGroupList());
        }

        if (criteriaQuery.getGroupRestriction() != null) {
            countCriteriaQuery.having(criteriaQuery.getGroupRestriction());
        }

        if (criteriaQuery.getRestriction() != null) {
            countCriteriaQuery.where(criteriaQuery.getRestriction());
        }

        return this.entityManager.createQuery(countCriteriaQuery)
                .setFlushMode(FlushModeType.COMMIT).getSingleResult();
    }

    private synchronized String getAlias(Selection<?> selection) {
        if (selection != null) {
            String alias = selection.getAlias();
            if (alias == null) {
                if (aliasCount >= 1000L) {
                    aliasCount = 0L;
                }
                alias = "generatedAlias" + aliasCount++;
                selection.alias(alias);
            }
            return alias;
        }
        return null;
    }

    private Root<T> getRoot(CriteriaQuery<T> criteriaQuery) {
        if (criteriaQuery != null) {
            return getRoot(criteriaQuery, criteriaQuery.getResultType());
        }
        return null;
    }

    private Root<T> getRoot(CriteriaQuery<?> criteriaQuery, Class<T> clazz) {
        if ((criteriaQuery != null) && (criteriaQuery.getRoots() != null) && (clazz != null)) {
            for (Root<?> root : criteriaQuery.getRoots()) {
                if (clazz.equals(root.getJavaType())) {
                    return (Root<T>) root.as(clazz);
                }
            }
        }
        return null;
    }

    private void copyJoins(From<?, ?> from, From<?, ?> to) {
        for (Join<?, ?> join : from.getJoins()) {
            Join<?, ?> toJoin = to.join(join.getAttribute().getName(), join.getJoinType());
            toJoin.alias(getAlias(join));
            copyJoins(join, toJoin);
        }

        for (Fetch<?, ?> fetch : from.getFetches()) {
            Fetch<?, ?> toFetch = to.fetch(fetch.getAttribute().getName());
            copyFetches(fetch, toFetch);
        }
    }

    private void copyFetches(Fetch<?, ?> from, Fetch<?, ?> to) {
        for (Fetch<?, ?> fetch : from.getFetches()) {
            Fetch<?, ?> toFetch = to.fetch(fetch.getAttribute().getName());
            copyFetches(fetch, toFetch);
        }
    }

    private void addRestrictions(CriteriaQuery<T> criteriaQuery, List<Filter> filters) {
        if ((criteriaQuery == null) || (filters == null) || (filters.isEmpty())) {
            return;
        }

        Root<T> root = getRoot(criteriaQuery);
        if (root == null) {
            return;
        }

        CriteriaBuilder criteriaBuilder = this.entityManager.getCriteriaBuilder();
        Predicate restrictions = criteriaQuery.getRestriction() != null ? criteriaQuery.getRestriction() : criteriaBuilder.conjunction();

        for (Filter filter : filters) {
            if ((filter != null) && (!StringUtils.isEmpty(filter.getProperty()))) {
                Expression x;
                String searchProperty = filter.getProperty();
                if (searchProperty.indexOf(".") > 0) {
                    String[] arr = searchProperty.split("\\.");
                    boolean flag = false;
                    Path p = null;
                    for (String path : arr) {
                        if (flag) {
                            p = p.get(path);
                        } else {
                            p = root.get(path);
                            flag = true;
                        }
                    }
                    x = p;
                } else {
                    x = root.get(searchProperty);
                }

                if ((filter.getOperator() == Filter.Operator.eq) && (filter.getValue() != null)) {
                    if ((filter.getIgnoreCase() != null) && (filter.getIgnoreCase().booleanValue()) && ((filter.getValue() instanceof String))) {
                        restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.equal(criteriaBuilder.lower(x), ((String) filter.getValue()).toLowerCase()));
                    } else {
                        restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.equal(x, filter.getValue()));
                    }
                } else if ((filter.getOperator() == Filter.Operator.ne) && (filter.getValue() != null)) {
                    if ((filter.getIgnoreCase() != null) && (filter.getIgnoreCase().booleanValue()) && ((filter.getValue() instanceof String))) {
                        restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.notEqual(criteriaBuilder.lower(x), ((String) filter.getValue()).toLowerCase()));
                    } else {
                        restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.notEqual(x, filter.getValue()));
                    }
                } else if ((filter.getOperator() == Filter.Operator.gt) && (filter.getValue() != null)) {
                    restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.gt(x, (Number) filter.getValue()));
                } else if ((filter.getOperator() == Filter.Operator.lt) && (filter.getValue() != null)) {
                    restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.lt(x, (Number) filter.getValue()));
                } else if ((filter.getOperator() == Filter.Operator.ge) && (filter.getValue() != null)) {
                    restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.ge(x, (Number) filter.getValue()));
                } else if ((filter.getOperator() == Filter.Operator.le) && (filter.getValue() != null)) {
                    restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.le(x, (Number) filter.getValue()));
                } else if ((filter.getOperator() == Filter.Operator.dgt) && (filter.getValue() != null)) {
                    restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.greaterThan(x, (Date) filter.getValue()));
                } else if ((filter.getOperator() == Filter.Operator.dlt) && (filter.getValue() != null)) {
                    restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.lessThan(x, (Date) filter.getValue()));
                } else if ((filter.getOperator() == Filter.Operator.dge) && (filter.getValue() != null)) {
                    restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.greaterThanOrEqualTo(x, (Date) filter.getValue()));
                } else if ((filter.getOperator() == Filter.Operator.dle) && (filter.getValue() != null)) {
                    restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.lessThanOrEqualTo(x, (Date) filter.getValue()));
                } else if ((filter.getOperator() == Filter.Operator.like) && (filter.getValue() != null) && ((filter.getValue() instanceof String))) {
                    restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.like(x, "%" + filter.getValue() + "%"));
                } else if ((filter.getOperator() == Filter.Operator.in) && (filter.getValue() != null)) {
                    Object[] objects = convert2Array(filter.getValue());
                    restrictions = criteriaBuilder.and(restrictions, x.in(objects));
                } else if ((filter.getOperator() == Filter.Operator.ni) && (filter.getValue() != null)) {
                    Object[] objects = convert2Array(filter.getValue());
                    restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.not(x.in(objects)));
                } else if (filter.getOperator() == Filter.Operator.isNull) {
                    restrictions = criteriaBuilder.and(restrictions, x.isNull());
                } else if (filter.getOperator() == Filter.Operator.isNotNull) {
                    restrictions = criteriaBuilder.and(restrictions, x.isNotNull());
                }
            }
        }
        criteriaQuery.where(restrictions);

        filterExpressions(criteriaQuery.getRestriction().getExpressions());
    }

    /**
     * 集合转换为数组
     *
     * @param obj
     * @return
     */
    private Object[] convert2Array(Object obj) {
        Object[] objects = null;
        if (obj instanceof List) {
            objects = ((List) obj).toArray();
        } else if (obj instanceof Object[]) {
            objects = (Object[]) obj;
        }

        return objects;
    }

    /**
     * 过滤无用表达式
     *
     * @param expressions
     */
    private void filterExpressions(List<Expression<Boolean>> expressions) {

        if (CollectionUtil.isEmpty(expressions)) {
            return;
        }

        for (int i = expressions.size() - 1; i >= 0; i--) {
            Expression<Boolean> expression = expressions.get(i);

            if (!(expression instanceof CompoundPredicate)) {
                continue;
            }

            CompoundPredicate implementor = (CompoundPredicate) expression;
            if (implementor.getExpressions().isEmpty()) {
                if (implementor.getOperator() == Predicate.BooleanOperator.AND) {
                    expressions.remove(i);
                }
            } else {
                filterExpressions(implementor.getExpressions());
            }

        }
    }

    private void addRestrictions(CriteriaQuery<T> criteriaQuery, Pageable pageable) {
        if ((criteriaQuery == null) || (pageable == null)) {
            return;
        }

        Root<T> root = getRoot(criteriaQuery);
        if (root == null) {
            return;
        }

        CriteriaBuilder criteriaBuilder = this.entityManager.getCriteriaBuilder();
        Predicate restrictions = criteriaQuery.getRestriction() != null ? criteriaQuery.getRestriction() : criteriaBuilder.conjunction();
        if ((StringUtils.isNotEmpty(pageable.getSearchProperty())) && (StringUtils.isNotEmpty(pageable.getSearchValue()))) {
            Expression<String> x;
            String searchProperty = pageable.getSearchProperty();
            if (searchProperty.indexOf(".") > 0) {
                String[] arr = searchProperty.split("\\.");
                x = root.get(arr[0]).<String>get(arr[1]);
            } else {
                x = root.<String>get(searchProperty);
            }
            restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.like(x, "%" + pageable.getSearchValue() + "%"));
        }

        if (pageable.getFilters() != null) {
            for (Filter filter : pageable.getFilters()) {
                if ((filter != null) && (!StringUtils.isEmpty(filter.getProperty()))) {
                    Expression x;
                    String searchProperty = filter.getProperty();
                    if (searchProperty.indexOf(".") > 0) {
                        String[] arr = searchProperty.split("\\.");
                        boolean flag = false;
                        Path p = null;
                        for (String path : arr) {
                            if (flag) {
                                p = p.get(path);
                            } else {
                                p = root.get(path);
                                flag = true;
                            }
                        }
                        x = p;
                    } else {
                        x = root.get(searchProperty);
                    }

                    if ((filter.getOperator() == Filter.Operator.eq) && (filter.getValue() != null)) {
                        if ((filter.getIgnoreCase() != null) && (filter.getIgnoreCase().booleanValue()) && ((filter.getValue() instanceof String))) {
                            restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.equal(criteriaBuilder.lower(x), ((String) filter.getValue()).toLowerCase()));
                        } else {
                            restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.equal(x, filter.getValue()));
                        }
                    } else if ((filter.getOperator() == Filter.Operator.ne) && (filter.getValue() != null)) {
                        if ((filter.getIgnoreCase() != null) && (filter.getIgnoreCase().booleanValue()) && ((filter.getValue() instanceof String))) {
                            restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.notEqual(criteriaBuilder.lower(x), ((String) filter.getValue()).toLowerCase()));
                        } else {
                            restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.notEqual(x, filter.getValue()));
                        }
                    } else if ((filter.getOperator() == Filter.Operator.gt) && (filter.getValue() != null)) {
                        restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.gt(x, (Number) filter.getValue()));
                    } else if ((filter.getOperator() == Filter.Operator.lt) && (filter.getValue() != null)) {
                        restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.lt(x, (Number) filter.getValue()));
                    } else if ((filter.getOperator() == Filter.Operator.ge) && (filter.getValue() != null)) {
                        restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.ge(x, (Number) filter.getValue()));
                    } else if ((filter.getOperator() == Filter.Operator.le) && (filter.getValue() != null)) {
                        restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.le(x, (Number) filter.getValue()));
                    } else if ((filter.getOperator() == Filter.Operator.dgt) && (filter.getValue() != null)) {
                        restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.greaterThan(x, (Date) filter.getValue()));
                    } else if ((filter.getOperator() == Filter.Operator.dlt) && (filter.getValue() != null)) {
                        restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.lessThan(x, (Date) filter.getValue()));
                    } else if ((filter.getOperator() == Filter.Operator.dge) && (filter.getValue() != null)) {
                        restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.greaterThanOrEqualTo(x, (Date) filter.getValue()));
                    } else if ((filter.getOperator() == Filter.Operator.dle) && (filter.getValue() != null)) {
                        restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.lessThanOrEqualTo(x, (Date) filter.getValue()));
                    } else if ((filter.getOperator() == Filter.Operator.like) && (filter.getValue() != null) && ((filter.getValue() instanceof String))) {
                        restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.like(x, "%" + filter.getValue() + "%"));
                    } else if ((filter.getOperator() == Filter.Operator.in) && (filter.getValue() != null)) {
                        Object[] objects = convert2Array(filter.getValue());
                        restrictions = criteriaBuilder.and(restrictions, x.in(objects));
                    } else if ((filter.getOperator() == Filter.Operator.ni) && (filter.getValue() != null)) {
                        Object[] objects = convert2Array(filter.getValue());
                        restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.not(x.in(objects)));
                    } else if (filter.getOperator() == Filter.Operator.isNull) {
                        restrictions = criteriaBuilder.and(restrictions, x.isNull());
                    } else if (filter.getOperator() == Filter.Operator.isNotNull) {
                        restrictions = criteriaBuilder.and(restrictions, x.isNotNull());
                    }
                }
            }
        }
        criteriaQuery.where(restrictions);
    }

    private void addOrders(CriteriaQuery<T> criteriaQuery, List<Order> orders) {
        if ((criteriaQuery == null) || (orders == null) || (orders.isEmpty())) {
            return;
        }
        Root<T> root = getRoot(criteriaQuery);
        if (root == null) {
            return;
        }
        CriteriaBuilder criteriaBuilder = this.entityManager.getCriteriaBuilder();
        ArrayList<javax.persistence.criteria.Order> list = new ArrayList<javax.persistence.criteria.Order>();
        if (!criteriaQuery.getOrderList().isEmpty()) {
            list.addAll(criteriaQuery.getOrderList());
        }
        for (Order order : orders) {
            if (order.getDirection() == Order.Direction.asc) {
                list.add(criteriaBuilder.asc(root.get(order.getProperty())));
            } else if (order.getDirection() == Order.Direction.desc) {
                list.add(criteriaBuilder.desc(root.get(order.getProperty())));
            }
        }
        criteriaQuery.orderBy(list);
    }

    private void addOrders(CriteriaQuery<T> criteriaQuery, Pageable pageable) {
        if ((criteriaQuery == null) || (pageable == null)) {
            return;
        }

        Root<T> root = getRoot(criteriaQuery);
        if (root == null) {
            return;
        }

        CriteriaBuilder criteriaBuilder = this.entityManager.getCriteriaBuilder();
        ArrayList<javax.persistence.criteria.Order> list = new ArrayList<javax.persistence.criteria.Order>();
        if (!criteriaQuery.getOrderList().isEmpty()) {
            list.addAll(criteriaQuery.getOrderList());
        }

        if ((StringUtils.isNotEmpty(pageable.getOrderProperty()))
                && (pageable.getOrderDirection() != null)) {
            if (pageable.getOrderDirection() == Order.Direction.asc) {
                list.add(criteriaBuilder.asc(root.get(pageable
                        .getOrderProperty())));
            } else if (pageable.getOrderDirection() == Order.Direction.desc) {
                list.add(criteriaBuilder.desc(root.get(pageable
                        .getOrderProperty())));
            }
        }

        if (pageable.getOrders() != null) {
            for (Order order : pageable.getOrders()) {
                if (order.getDirection() == Order.Direction.asc) {
                    list.add(criteriaBuilder.asc(root.get(order.getProperty())));
                } else if (order.getDirection() == Order.Direction.desc) {
                    list.add(criteriaBuilder.desc(root.get(order.getProperty())));
                }
            }
        }
        criteriaQuery.orderBy(list);
    }

    @Override
    public Class<T> getEntityClass() {
        return entityClass;
    }

}
