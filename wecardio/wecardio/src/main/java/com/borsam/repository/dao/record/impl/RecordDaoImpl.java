package com.borsam.repository.dao.record.impl;

import com.borsam.repository.dao.record.RecordDao;
import com.borsam.repository.entity.record.Record;
import com.hiteam.common.base.repository.dao.impl.BaseDaoImpl;
import org.springframework.stereotype.Repository;

import javax.persistence.FlushModeType;
import java.util.Arrays;

/**
 * Dao - 检查记录
 * Created by tantian on 2015/7/21.
 */
@Repository("recordDaoImpl")
public class RecordDaoImpl extends BaseDaoImpl<Record, Long> implements RecordDao {

    @Override
    public void setFlag(Long[] ids, Record.Flag flag) {
        String jpql = "update Record r set r.flag = :flag where r.id in :ids";
        this.entityManager.createQuery(jpql).setFlushMode(FlushModeType.COMMIT)
                .setParameter("flag", flag)
                .setParameter("ids", Arrays.asList(ids))
                .executeUpdate();
    }
}
