package com.borsam.repository.dao.wf;

import com.borsam.repository.entity.wf.Act;
import com.hiteam.common.base.repository.dao.BaseDao;

import java.util.List;

/**
 * Dao - 活动环节
 * Created by tantian on 2015/7/3.
 */
public interface ActDao extends BaseDao<Act, Long> {

    /**
     * 获取流程所有活动
     * @param wfCode 流程代码
     * @return 活动列表
     */
    public List<Act> getActList(String wfCode);

    /**
     * 获取流程开始活动环节
     * @param wfCode 流程代码
     * @return 开始活动
     */
    public Act getStartAct(String wfCode);

    /**
     * 根据序号获取活动
     * @param wfCode 流程代码
     * @param seq 活动序号
     * @return 活动环节
     */
    public Act getAct(String wfCode, int seq);

    /**
     * 根据活动代码获取活动
     * @param wfCode 流程代码
     * @param actCode 活动代码
     * @return 活动环节
     */
    public Act getAct(String wfCode, String actCode);
}
