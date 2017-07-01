package com.borsam.service.wf.impl;

import com.borsam.pojo.wf.CreateProcessParam;
import com.borsam.pojo.wf.WfException;
import com.borsam.repository.entity.wf.Act;
import com.borsam.service.wf.ProcessEngine;
import com.borsam.service.wf.RuntimeService;
import com.hiteam.common.util.json.JsonUtils;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.Map;

/**
 * Service - 流程引擎服务
 * Created by Sebarswee on 2015/7/3.
 */
@Transactional
@Service("processEngineImpl")
public class ProcessEngineImpl implements ProcessEngine {

    public static final Logger logger = LoggerFactory.getLogger(ProcessEngineImpl.class);

    @Resource(name = "runtimeServiceImpl")
    private RuntimeService runtimeService;

    @Transactional
    @Override
    public Long startProcessInstance(CreateProcessParam param) {
        logger.info("启动流程，参数：{}", new Object[] { JsonUtils.toJson(param) });

        // 记录创建流程开始时间点
        long startTime = System.currentTimeMillis();

        /*
         * 验证流程启动参数，验证不通过抛出相应异常
         */
        validateCreateParam(param);

        // 启动
        Long guid = runtimeService.start(param.getWfCode(), param.getParticipant(), param.getDatas(), param.getIsPush());

        long endTime = System.currentTimeMillis();
        logger.info("流程启动成功，[guid={}]，共耗时{}毫秒！", new Object[] {guid, (endTime - startTime)});
        return guid;
    }

    /**
     * 验证流程启动参数
     */
    private void validateCreateParam(CreateProcessParam param) {
        if (StringUtils.isEmpty(param.getWfCode())) {
            throw new WfException(WfException.Code.invalidParam, "流程代码不能为空！");
        }
    }

    @Transactional
    @Override
    public boolean pushProcessInstance(Long guid, Map<String, String> participant, Map<String, String> datas) {
        logger.info("推动流程，guid={}, participant={}, datas={}", new Object[] { guid, JsonUtils.toJson(participant), JsonUtils.toJson(datas) });

        // 记录创建流程开始时间点
        long startTime = System.currentTimeMillis();

        boolean result = runtimeService.push(guid, participant, datas);

        long endTime = System.currentTimeMillis();
        logger.info("流程推动成功，[guid={}]，共耗时{}毫秒！", new Object[] {guid, (endTime - startTime)});
        return result;
    }

    @Transactional
    @Override
    public boolean forcePushProcessInstance(Long guid, Map<String, String> participant, Map<String, String> datas) {
        logger.info("推动流程，guid={}, participant={}, datas={}", new Object[] { guid, JsonUtils.toJson(participant), JsonUtils.toJson(datas) });

        // 记录创建流程开始时间点
        long startTime = System.currentTimeMillis();

        boolean result = runtimeService.forcePush(guid, participant, datas);

        long endTime = System.currentTimeMillis();
        logger.info("流程推动成功，[guid={}]，共耗时{}毫秒！", new Object[] {guid, (endTime - startTime)});
        return result;
    }

    @Transactional
    @Override
    public boolean backProcessInstance(Long guid, Map<String, String> participant, Map<String, String> datas) {
        logger.info("推动流程，guid={}, participant={}, datas={}", new Object[] { guid, JsonUtils.toJson(participant), JsonUtils.toJson(datas) });

        // 记录创建流程开始时间点
        long startTime = System.currentTimeMillis();

        boolean result = runtimeService.back(guid, participant, datas);

        long endTime = System.currentTimeMillis();
        logger.info("流程推动成功，[guid={}]，共耗时{}毫秒！", new Object[] {guid, (endTime - startTime)});
        return result;
    }

    @Transactional
    @Override
    public boolean forceBackProcessInstance(Long guid, Map<String, String> participant, Map<String, String> datas) {
        logger.info("推动流程，guid={}, participant={}, datas={}", new Object[] { guid, JsonUtils.toJson(participant), JsonUtils.toJson(datas) });

        // 记录创建流程开始时间点
        long startTime = System.currentTimeMillis();

        boolean result = runtimeService.forceBack(guid, participant, datas);

        long endTime = System.currentTimeMillis();
        logger.info("流程推动成功，[guid={}]，共耗时{}毫秒！", new Object[] {guid, (endTime - startTime)});
        return result;
    }

    @Transactional
    @Override
    public boolean pushProcessInstance(Long guid, Act next, Map<String, String> participant, Map<String, String> datas) {
        logger.info("推动流程，guid={}, participant={}, datas={}", new Object[] { guid, JsonUtils.toJson(participant), JsonUtils.toJson(datas) });

        // 记录创建流程开始时间点
        long startTime = System.currentTimeMillis();

        boolean result = runtimeService.push(guid, next, participant, datas);

        long endTime = System.currentTimeMillis();
        logger.info("流程推动成功，[guid={}]，共耗时{}毫秒！", new Object[] {guid, (endTime - startTime)});
        return result;
    }

    @Transactional
    @Override
    public boolean pushProcessInstance(Long guid, String actCode, Map<String, String> participant, Map<String, String> datas) {
        logger.info("推动流程，guid={}, participant={}, datas={}", new Object[] { guid, JsonUtils.toJson(participant), JsonUtils.toJson(datas) });

        // 记录创建流程开始时间点
        long startTime = System.currentTimeMillis();

        boolean result = runtimeService.push(guid, actCode, participant, datas);

        long endTime = System.currentTimeMillis();
        logger.info("流程推动成功，[guid={}]，共耗时{}毫秒！", new Object[] {guid, (endTime - startTime)});
        return result;
    }
}
