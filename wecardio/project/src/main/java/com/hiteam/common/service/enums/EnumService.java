package com.hiteam.common.service.enums;

import com.hiteam.common.base.service.BaseService;
import com.hiteam.common.pojo.enums.EnumTransformData;
import com.hiteam.common.repository.entity.enums.Enum;
import com.hiteam.common.util.pojo.EnumBean;

import java.util.List;
import java.util.Map;

/**
 * Service - 枚举
 * Created by Sebarswee on 2015/7/15.
 */
public interface EnumService extends BaseService<Enum, Long> {

    /**
     * 获取枚举
     * @param tblName 关联表名
     * @param colName 关联字段名
     * @return 枚举列表
     */
    public List<EnumBean> getEnum(String tblName, String colName,String notIn);

    /**
     * 清除缓存
     */
    public void clearCache();

    /**
     * 以target对象中的valField枚举值翻译中文到nameField字段中，其中枚举源条件为whsEnum
     * @param target 需要翻译的对象，接收单个对象、集合、集合Hashmap
     * @param enumDatas 枚举翻译的条件配置
     */
    public void transformEnum(Object target, List<EnumTransformData> enumDatas);

    /**
     * 根据target对象中的枚举注解(com.hiteam.common.pojo.enums.EnumTrans)进行枚举翻译
     * @param target 目标对象（接收单个对象、集合，不支持Map，及目标对象中必须使用EnumTrans 注解）
     */
    public void transformEnum(Object target);
}
