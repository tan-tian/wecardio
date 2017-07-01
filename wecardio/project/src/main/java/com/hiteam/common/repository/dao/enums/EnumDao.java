package com.hiteam.common.repository.dao.enums;

import com.hiteam.common.base.repository.dao.BaseDao;
import com.hiteam.common.repository.entity.enums.Enum;
import com.hiteam.common.util.pojo.EnumBean;

import java.util.List;
import java.util.Map;

/**
 * Dao - 枚举
 * Created by Sebarswee on 2015/7/15.
 */
public interface EnumDao extends BaseDao<Enum, Long> {

    /**
     * 获取枚举
     * @param tblName 关联表名
     * @param colName 关联字段名
     * @return 枚举列表
     */
    public List<EnumBean> getEnum(String tblName, String colName,String notIn);
}
