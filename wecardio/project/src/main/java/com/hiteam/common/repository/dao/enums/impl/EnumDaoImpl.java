package com.hiteam.common.repository.dao.enums.impl;

import com.hiteam.common.base.repository.dao.impl.BaseDaoImpl;
import com.hiteam.common.repository.dao.enums.EnumDao;
import com.hiteam.common.repository.entity.enums.Enum;
import com.hiteam.common.util.pojo.EnumBean;
import org.springframework.stereotype.Repository;

import javax.persistence.FlushModeType;
import java.util.List;
import java.util.Map;

/**
 * Dao - 枚举
 * Created by Sebarswee on 2015/7/15.
 */
@Repository("enumDaoImpl")
public class EnumDaoImpl extends BaseDaoImpl<Enum, Long> implements EnumDao {

    @Override
    public List<EnumBean> getEnum(String tblName, String colName,String notIn) {
        String jpql = "select new com.hiteam.common.util.pojo.EnumBean(enum.value, enum.text) from Enum enum"
            + " where enum.tblName = :tblName and enum.colName = :colName";
        if(notIn!=null&&!notIn.equals(""))
        {
            jpql+="enum.value not in ("+notIn+")";
        }

        return this.entityManager.createQuery(jpql, EnumBean.class).setFlushMode(FlushModeType.COMMIT).setParameter("tblName", tblName).setParameter("colName", colName).getResultList();
    }
}
