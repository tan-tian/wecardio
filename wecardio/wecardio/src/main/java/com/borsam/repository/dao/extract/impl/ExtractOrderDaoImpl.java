package com.borsam.repository.dao.extract.impl;

import com.borsam.repository.dao.extract.ExtractOrderDao;
import com.borsam.repository.entity.extract.ExtractOrder;
import com.hiteam.common.base.repository.dao.impl.BaseDaoImpl;
import org.springframework.stereotype.Repository;

/**
 * Dao - 提现单据
 * Created by Sebarswee on 2015/8/10.
 */
@Repository("extractOrderDaoImpl")
public class ExtractOrderDaoImpl extends BaseDaoImpl<ExtractOrder, Long> implements ExtractOrderDao {
}
