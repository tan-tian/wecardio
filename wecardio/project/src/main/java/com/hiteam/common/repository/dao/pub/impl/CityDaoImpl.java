package com.hiteam.common.repository.dao.pub.impl;

import com.hiteam.common.base.repository.dao.impl.BaseDaoImpl;
import com.hiteam.common.repository.dao.pub.CityDao;
import com.hiteam.common.repository.entity.pub.City;
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
 * 城市 - Dao
 */
@Repository("cityDaoImpl")
public class CityDaoImpl extends BaseDaoImpl<City, Long> implements CityDao {
    @Override
    public List<EnumBean> citySel(Long countryId, Long areaId, Long provinceId, String name) {
        CriteriaBuilder criteriaBuilder = this.entityManager.getCriteriaBuilder();
        CriteriaQuery<EnumBean> criteriaQuery = criteriaBuilder.createQuery(EnumBean.class);
        Root<City> root = criteriaQuery.from(City.class);
        Predicate restrictions = criteriaBuilder.conjunction();

        criteriaQuery.select(criteriaBuilder.construct(EnumBean.class, root.get("id"), root.get("name")));

        restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.equal(root.get("country"), countryId));

        if (areaId != null) {
            restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.equal(root.get("area"), areaId));
        }

        if (provinceId != null) {
            restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.equal(root.get("province"), provinceId));
        }

        if (!StringUtils.isEmpty(name)) {
            restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.like(root.<String> get("name"), "%" + name + "%"));
        }
        criteriaQuery.where(restrictions);

        TypedQuery<EnumBean> query = entityManager.createQuery(criteriaQuery).setFlushMode(FlushModeType.COMMIT);
        return query.getResultList();
    }

    @Override
    public String getCityZipCode(Long cityId) {
        String jpql = "select city.zipCode from City city where city.id = :cityId";
        return (String) this.entityManager.createQuery(jpql).setFlushMode(FlushModeType.COMMIT).setParameter("cityId", cityId).getSingleResult();
    }

}
