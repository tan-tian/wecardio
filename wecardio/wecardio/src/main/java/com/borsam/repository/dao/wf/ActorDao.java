package com.borsam.repository.dao.wf;

import com.borsam.repository.entity.wf.Actor;
import com.hiteam.common.base.repository.dao.BaseDao;

import java.util.List;

/**
 * Dao - 活动参与者
 * Created by tantian on 2015/7/3.
 */
public interface ActorDao extends BaseDao<Actor, Long> {

    /**
     * 获取活动配置的参与者列表
     * @param wfCode 流程代码
     * @param actCode 活动代码
     * @return 参与者列表
     */
    public List<Actor> findActor(String wfCode, String actCode);
}
