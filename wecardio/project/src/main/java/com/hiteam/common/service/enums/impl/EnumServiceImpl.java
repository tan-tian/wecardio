package com.hiteam.common.service.enums.impl;

import com.hiteam.common.base.pojo.search.Filter;
import com.hiteam.common.base.service.impl.BaseServiceImpl;
import com.hiteam.common.pojo.enums.EnumTrans;
import com.hiteam.common.pojo.enums.EnumTransformData;
import com.hiteam.common.repository.dao.enums.EnumDao;
import com.hiteam.common.repository.entity.enums.Enum;
import com.hiteam.common.service.enums.EnumService;
import com.hiteam.common.util.bean.BeanUtil;
import com.hiteam.common.util.collections.CollectionUtil;
import com.hiteam.common.util.collections.MapUtil;
import com.hiteam.common.util.lang.StringUtil;
import com.hiteam.common.util.pojo.EnumBean;
import com.hiteam.common.web.I18Util;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Service - 枚举
 * Created by Sebarswee on 2015/7/15.
 */
@Service("enumServiceImpl")
public class EnumServiceImpl extends BaseServiceImpl<Enum, Long> implements EnumService {

    private Logger logger = LoggerFactory.getLogger(EnumServiceImpl.class);

    @Resource(name = "enumDaoImpl")
    private EnumDao enumDao;

    @Resource(name = "enumDaoImpl")
    public void setBaseDao(EnumDao enumDao) {
        super.setBaseDao(enumDao);
    }

    @Transactional(readOnly = true)
    // TODO 缓存后无法切换语言
//    @Cacheable(value = "enums", key = "#tblName + '_' + #colName")
    @Override
    public List<EnumBean> getEnum(String tblName, String colName,String notIn) {
        List<EnumBean> list = enumDao.getEnum(tblName, colName,notIn);
        if (list != null && !list.isEmpty()) {
            // 国际化处理
            for (EnumBean enumBean : list) {
                enumBean.setText(I18Util.getMessage(enumBean.getText()));
            }
        }
        return list;
    }

    @Transactional(readOnly = true)
    @CacheEvict(value = { "enums" }, allEntries = true)
    @Override
    public void clearCache() {

    }

    @Transactional(readOnly = true)
    @Override
    public void transformEnum(Object target, List<EnumTransformData> enumDatas) {
        if (target == null) {
            return;
        }

        //根据配置先找对应（表、列 下的所对应的所有枚举）的枚举数据，便于翻译使用
        Map<String, Map> enumMap = getEnumMap(enumDatas);

        //处理List<实体或Map> 类型的数据
        if (target instanceof List) {
            for (Object o : (List) target) {
                setEnumNameByVal(o, enumMap, enumDatas);
            }
        } else { //处理单个对象
            setEnumNameByVal(target, enumMap, enumDatas);
        }
    }

    /**
     * 根据target 对象中的EnumTrans注解，组装 EnumTransformData 对象
     * @param target
     * @return List<EnumTransformData>
     */
    private List<EnumTransformData> getEnumDataByAnnotation(Object target){
        //根据对象中的注解获取枚举配置
        List<EnumTransformData> enumDatas = new ArrayList<EnumTransformData>();

        Object tempTarget = target;

        if (target instanceof List) {
            tempTarget = CollectionUtil.getFirst((List) target);
            if (tempTarget == null) {
                return null;
            }
        }

        //根据tempTarget 组装 EnumTransformData 对象数据
        Method[] fields = tempTarget.getClass().getDeclaredMethods();

        for (Method f : fields) {
            EnumTrans enumTrans = AnnotationUtils.findAnnotation(f, EnumTrans.class);
            if (enumTrans == null) {
                continue;
            }

            String tableName = enumTrans.tableName();
            String colName = enumTrans.colName();
            String nameField = enumTrans.nameField();
            //根据方法查找对应的属性名
            String valField = BeanUtils.findPropertyForMethod(f).getName();
            EnumTransformData enumTransformData = new EnumTransformData(tableName, colName, valField, nameField);
            enumDatas.add(enumTransformData);
        }

        return enumDatas;
    }

    @Transactional(readOnly = true)
    @Override
    public void transformEnum(Object target) {
        if (target == null) {
            return;
        }

        if(target instanceof List && CollectionUtil.isEmpty((List) target)){
            return;
        }

        //根据对象中的注解获取枚举配置
        List<EnumTransformData> enumDatas = getEnumDataByAnnotation(target);

        if (CollectionUtil.isEmpty(enumDatas)) {
            logger.warn("target 对象中的属性方法未配置EnumTrans注解");
            return;
        }

        //翻译
        transformEnum(target, enumDatas);
    }

    /**
     * //TODO 需要考虑缓存操作[zzt]
     * 根据配置参数，从枚举中获取枚举，并使用key/value存储
     * key:表名_列名,value:{枚举值:枚举值名称}
     *
     * @param enumDatas
     * @return
     */
    private Map<String, Map> getEnumMap(List<EnumTransformData> enumDatas) {
        Map<String, Map> enumMap = new HashMap<String, Map>();

        for (EnumTransformData enumData : enumDatas) {
            List<Filter> filters = new ArrayList<Filter>();
            filters.add(Filter.eq("tblName", enumData.getTableName()));
            filters.add(Filter.eq("colName", enumData.getColName()));
            List<Enum> whsEnums = findList(null, filters, null);

            Map map = new HashMap();
            enumMap.put(enumData.getTableName() + "_" + enumData.getColName(), map);

            for (Enum whsEnum : whsEnums) {
                map.put(whsEnum.getValue() + "", I18Util.getMessage(whsEnum.getText()));
            }
        }

        return enumMap;
    }

    /**
     * 根据枚举配置，根据目标对象存储枚举值的字段，及枚举数据，针对保存枚举翻译字段进行赋值
     *
     * @param target    目标对象
     * @param enumMap   枚举数据
     * @param enumDatas 枚举配置
     */
    private void setEnumNameByVal(Object target, Map<String, Map> enumMap, List<EnumTransformData> enumDatas) {
        for (EnumTransformData enumData : enumDatas) {
            try {
                //获取枚举字段值
                String val = BeanUtil.getProperty(target, enumData.getValField());

                if (StringUtil.isBlank(val)) {
                    continue;
                }

                Map tempMap =
                        MapUtil.getMap(enumMap, enumData.getTableName() + "_" + enumData.getColName(),
                                new HashMap());

                String name = "";

                //考虑有些字段存储枚举值数组(以逗号分割的字符串)
                String[] aVal = val.split(",");
                for (int i = 0, len = aVal.length; i < len; i++) {
                    String nameVal = MapUtil.getString(tempMap, aVal[i], "");

                    if (i == 0) {
                        name += nameVal;
                    } else {
                        name += "," + nameVal;
                    }
                }

                if (StringUtil.isBlank(name)) {
                    continue;
                }

                //根据枚举翻译设置翻译后的值
                BeanUtil.setProperty(target, enumData.getNameField(), name);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
