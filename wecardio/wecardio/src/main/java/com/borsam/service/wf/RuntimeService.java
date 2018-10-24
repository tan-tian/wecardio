package com.borsam.service.wf;

import com.borsam.repository.entity.wf.Act;

import java.util.Map;

/**
 * Service - 流程运行服务类
 * Created by tantian on 2015/7/3.
 */
public interface RuntimeService {

    public Long start(String wfCode, Map<String, String> participant, Map<String, String> datas, Boolean isPush);

    /**
     * 向前推动一个环节
     * @param guid 流程guid
     * @param participant 参与者配置
     * @param datas 流程数据区
     */
    public boolean push(Long guid, Map<String, String> participant, Map<String, String> datas);

    /**
     * 强制向前推动一个环节
     * @param guid 流程guid
     * @param participant 参与者配置
     * @param datas 流程数据区
     */
    boolean forcePush(Long guid, Map<String, String> participant, Map<String, String> datas);

    /**
     * 回退流程到上一环节
     * @param guid 流程guid
     * @param participant 参与者配置
     * @param datas 流程数据区
     */
    public boolean back(Long guid, Map<String, String> participant, Map<String, String> datas);

    /**
     * 强制回退流程到上一环节
     * @param guid 流程guid
     * @param participant 参与者配置
     * @param datas 流程数据区
     */
    public boolean forceBack(Long guid, Map<String, String> participant, Map<String, String> datas);

    /**
     * 推动流程到指定环节
     * @param guid 流程guid
     * @param next 下一环节
     * @param participant 参与者配置
     * @param datas 流程数据区
     */
    public boolean push(Long guid, Act next, Map<String, String> participant, Map<String, String> datas);

    /**
     * 推动流程到指定环节
     * @param guid 流程guid
     * @param actCode 下一环节代码
     * @param participant 参与者配置
     * @param datas 流程数据区
     */
    public boolean push(Long guid, String actCode, Map<String, String> participant, Map<String, String> datas);
}
