package com.borsam.repository.dao.wf.impl;

import com.borsam.repository.dao.wf.WfInstDao;
import com.borsam.repository.entity.wf.WfInst;
import com.hiteam.common.base.repository.dao.impl.BaseDaoImpl;
import org.springframework.stereotype.Repository;

/**
 * Dao - 流程实例
 * Created by Sebarswee on 2015/7/3.
 */
@Repository("wfInstDaoImpl")
public class WfInstDaoImpl extends BaseDaoImpl<WfInst, Long> implements WfInstDao {
}
