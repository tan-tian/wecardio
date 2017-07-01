package com.borsam.service.record;

import com.borsam.repository.entity.record.Record;
import com.hiteam.common.base.service.BaseService;

/**
 * Service - 检查记录
 * Created by Sebarswee on 2015/7/21.
 */
public interface RecordService extends BaseService<Record, Long> {

    /**
     * 设置颜色标识
     * @param ids 检查记录ID
     * @param flag 颜色
     */
    public void setFlag(Long[] ids, Record.Flag flag);

    /**
     * 会诊申请
     * @param uid 患者
     * @param rid 检查记录
     * @param oid 机构
     * @param type 类型：0-扣除购买服务 1-扣除余额
     * @param typeId 服务类型ID
     */
    public void createConsultation(Long uid, Long rid, Long oid, Integer type, Long typeId);

    /**
     * 本地pdf报告是否存在
     * @param record 检查记录
     * @return 本地pdf报告是否存在
     */
    public boolean localFileExist(Record record);

    /**
     * 获取本地pdf报告路径
     * @param record 检查记录
     * @return 本地pdf报告路径
     */
    public String getLocalFilePath(Record record);

    /**
     * 保存pdf报告到本地
     * @param record 检查记录
     * @return 本地pdf报告路径
     */
    public String saveFileToLocal(Record record);

    /**
     * 获取远程pdf报告地址
     * @param id 检查记录标识
     * @return 远程pdf报告地址
     */
    public String getRemoteFilePath(Long id);
}
