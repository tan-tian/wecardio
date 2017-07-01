package com.hiteam.common.repository.dao.pub.impl;

import com.hiteam.common.base.repository.dao.impl.BaseDaoImpl;
import com.hiteam.common.repository.dao.pub.AreaDao;
import com.hiteam.common.repository.entity.pub.Area;
import com.hiteam.common.util.pojo.EnumBean;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Repository;

import javax.persistence.FlushModeType;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.List;

/**
 * 地区 - Dao
 */
@Repository("areaDaoImpl")
public class AreaDaoImpl extends BaseDaoImpl<Area, Long> implements AreaDao {

    @Override
    public List<EnumBean> areaSel(Long countryId, String name) {
        CriteriaBuilder criteriaBuilder = this.entityManager.getCriteriaBuilder();
        CriteriaQuery<EnumBean> criteriaQuery = criteriaBuilder.createQuery(EnumBean.class);
        Root<Area> root = criteriaQuery.from(Area.class);
        Predicate restrictions = criteriaBuilder.conjunction();

        criteriaQuery.select(criteriaBuilder.construct(EnumBean.class, root.get("id"), root.get("name")));

        restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.equal(root.get("country"), countryId));

        if (!StringUtils.isEmpty(name)) {
            restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.like(root.<String> get("name"), "%" + name + "%"));
        }
        criteriaQuery.where(restrictions);

        TypedQuery<EnumBean> query = entityManager.createQuery(criteriaQuery).setFlushMode(FlushModeType.COMMIT);
        return query.getResultList();
    }
}
