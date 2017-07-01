package com.borsam.service.wf;

import com.borsam.pub.UserType;
import com.borsam.repository.entity.wf.WorkItem;

import java.util.List;
import java.util.Map;

/**
 * Service - 流程查询服务
 * Created by Sebarswee on 2015/7/7.
 */
public interface QueryService {

    /**
     * 查询流程信息
     * @param wfCode 流程代码
     * @param guid 流程guid
     * @return 流程信息
     */
    public List<Map<String, String>> getFlowInfo(String wfCode, Long guid);

    /**
     * 查询流程处理信息
     * @param guid 流程guid
     * @param showAll 是否显示待办工作项
     * @return 处理信息
     */
    public List<WorkItem> queryProcessInfo(Long guid, Boolean showAll);

    /**
     * 获取待办guid
     * @param wfCode 流程代码
     * @param userType 用户类型
     * @param userId 用户ID
     * @return guid列表
     */
    public List<Long> queryTodoGuids(String wfCode, UserType userType, Long userId);
}
