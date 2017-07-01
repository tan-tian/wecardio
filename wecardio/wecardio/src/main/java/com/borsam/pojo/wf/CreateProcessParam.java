package com.borsam.pojo.wf;

import java.util.HashMap;
import java.util.Map;

/**
 * 流程启动参数
 * Created by Sebarswee on 2015/7/3.
 */
public class CreateProcessParam {

    private String wfCode;		// 流程代码
    private Boolean isPush;     // 是否推动到下环节
    private Map<String, String> participant = new HashMap<String, String>();
    private Map<String, String> datas = new HashMap<String, String>();

    /**
     * 获取流程代码
     * @return 流程代码
     */
    public String getWfCode() {
        return wfCode;
    }

    /**
     * 设置流程代码
     * @param wfCode 流程代码
     */
    public void setWfCode(String wfCode) {
        this.wfCode = wfCode;
    }

    /**
     * 获取是否推动到下环节
     * @return 是否推动环节
     */
    public Boolean getIsPush() {
        return isPush;
    }

    /**
     * 设置是否推动到下环节
     * @param isPush 是否推动环节
     */
    public void setIsPush(Boolean isPush) {
        this.isPush = isPush;
    }

    /**
     * 获取参与者 <br/>
     * <p>
     *     key为活动代码，value为参与者格式字符串 <br/>
     *     参与者格式：A(id), O(id), D(id), P(id) <br/>
     *     分别代表：平台管理员、机构管理员、医生、患者
     * </p>
     * @return 参与者
     */
    public Map<String, String> getParticipant() {
        return participant;
    }

    /**
     * 设置参与者
     * @param participant 参与者
     */
    public void setParticipant(Map<String, String> participant) {
        this.participant = participant;
    }

    /**
     * 获取数据区
     * @return 数据区
     */
    public Map<String, String> getDatas() {
        return datas;
    }

    /**
     * 设置数据区
     * @param datas 数据区
     */
    public void setDatas(Map<String, String> datas) {
        this.datas = datas;
    }
}
