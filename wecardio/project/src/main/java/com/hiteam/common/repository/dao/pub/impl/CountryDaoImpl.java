package com.hiteam.common.repository.dao.pub.impl;

import com.hiteam.common.base.repository.dao.impl.BaseDaoImpl;
import com.hiteam.common.repository.dao.pub.CountryDao;
import com.hiteam.common.repository.entity.pub.Country;
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
 * 国家 - Dao
 */
@Repository("countryDaoImpl")
public class CountryDaoImpl extends BaseDaoImpl<Country, Long> implements CountryDao {

    @Override
    public List<EnumBean> countrySel(String name) {
        CriteriaBuilder criteriaBuilder = this.entityManager.getCriteriaBuilder();
        CriteriaQuery<EnumBean> criteriaQuery = criteriaBuilder.createQuery(EnumBean.class);
        Root<Country> root = criteriaQuery.from(Country.class);
        Predicate restrictions = criteriaBuilder.conjunction();

        criteriaQuery.select(criteriaBuilder.construct(EnumBean.class, root.get("id"), root.get("name")));

        if (!StringUtils.isEmpty(name)) {
            restrictions = criteriaBuilder.like(root.<String> get("name"), "%" + name + "%");
        }
        criteriaQuery.where(restrictions);

        TypedQuery<EnumBean> query = entityManager.createQuery(criteriaQuery).setFlushMode(FlushModeType.COMMIT);
        return query.getResultList();
    }
}
