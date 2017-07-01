package com.borsam.repository.dao.record;

import com.borsam.repository.entity.record.Record;
import com.hiteam.common.base.repository.dao.BaseDao;

/**
 * Dao - 检查记录
 * Created by Sebarswee on 2015/7/21.
 */
public interface RecordDao extends BaseDao<Record, Long> {

    /**
     * 设置颜色标识
     * @param ids 检查记录ID
     * @param flag 颜色
     */
    public void setFlag(Long[] ids, Record.Flag flag);
}
