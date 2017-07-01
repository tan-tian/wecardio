package com.borsam.repository.dao.extract.impl;

import com.borsam.repository.dao.extract.OutInfoDao;
import com.borsam.repository.entity.extract.OutInfo;
import com.hiteam.common.base.repository.dao.impl.BaseDaoImpl;
import org.springframework.stereotype.Repository;

/**
 * Dao - 出账信息
 * Created by Sebarswee on 2015/8/10.
 */
@Repository("outInfoDaoImpl")
public class OutInfoDaoImpl extends BaseDaoImpl<OutInfo, Long> implements OutInfoDao {
}
