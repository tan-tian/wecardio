package com.borsam.repository.dao.wf;

import com.borsam.repository.entity.wf.Act;
import com.borsam.repository.entity.wf.Actor;
import com.borsam.repository.entity.wf.WorkItem;
import com.hiteam.common.base.repository.dao.BaseDao;

import java.util.List;

/**
 * Dao - 工作项
 * Created by tantian on 2015/7/3.
 */
public interface WorkItemDao extends BaseDao<WorkItem, Long> {

    /**
     * 查找当前运行的工作项
     * @param guid 流程guid
     * @param type 参与者类型
     * @param actorId 参与者ID
     * @return 工作项
     */
    public WorkItem findRunningWorkItem(Long guid, Actor.Type type, Long actorId);

    /**
     * 查找未完成工作项
     * @param guid 流程guid
     * @return 未完成工作项
     */
    public List<WorkItem> findUnFinishedWorkItems(Long guid);

    /**
     * 查找环节历史工作项
     * @param guid 流程guid
     * @param act 活动环节
     * @return 工作项列表
     */
    public List<WorkItem> getLastWorkItems(Long guid, Act act);

    /**
     * 获取待办guid
     * @param wfCode 流程代码
     * @param type 用户类型
     * @param userId 用户ID
     * @return guid列表
     */
    public List<Long> queryTodoGuids(String wfCode, Actor.Type type, Long userId);
}
