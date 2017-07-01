package com.borsam.service.wf;

import com.borsam.pojo.wf.CreateProcessParam;
import com.borsam.repository.entity.wf.Act;

import java.util.Map;

/**
 * Service - 流程引擎服务
 * Created by Sebarswee on 2015/7/3.
 */
public interface ProcessEngine {

    /**
     * 创建流程实例
     * @param param 参数
     * @return guid
     */
    public Long startProcessInstance(CreateProcessParam param);

    /**
     * 向前推动一个环节
     * @param guid 流程guid
     * @param participant 参与者配置
     * @param datas 流程数据区
     */
    public boolean pushProcessInstance(Long guid, Map<String, String> participant, Map<String, String> datas);

    /**
     * 强制向前推动一个环节
     * @param guid 流程guid
     * @param participant 参与者配置
     * @param datas 流程数据区
     */
    boolean forcePushProcessInstance(Long guid, Map<String, String> participant, Map<String, String> datas);

    /**
     * 回退流程到上一环节
     * @param guid 流程guid
     * @param participant 参与者配置
     * @param datas 流程数据区
     */
    public boolean backProcessInstance(Long guid, Map<String, String> participant, Map<String, String> datas);

    /**
     * 强制回退流程到上一环节
     * @param guid 流程guid
     * @param participant 参与者配置
     * @param datas 流程数据区
     */
    public boolean forceBackProcessInstance(Long guid, Map<String, String> participant, Map<String, String> datas);

    /**
     * 推动流程到指定环节
     * @param guid 流程guid
     * @param next 下一环节
     * @param participant 参与者配置
     * @param datas 流程数据区
     */
    public boolean pushProcessInstance(Long guid, Act next, Map<String, String> participant, Map<String, String> datas);

    /**
     * 推动流程到指定环节
     * @param guid 流程guid
     * @param actCode 下一环节代码
     * @param participant 参与者配置
     * @param datas 流程数据区
     */
    public boolean pushProcessInstance(Long guid, String actCode, Map<String, String> participant, Map<String, String> datas);
}
