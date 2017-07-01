package com.hiteam.common.util.collections;

import com.hiteam.common.util.lang.StringUtil;
import org.apache.commons.collections.MapUtils;

import java.beans.BeanInfo;
import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

/**
 * Map工具类
 * 
 */
public class MapUtil extends MapUtils{

    /**
     * 将一个 Map 对象转化为一个 JavaBean
     * @param type 要转化的类型
     * @param map 包含属性值的 map
     * @return 转化出来的 JavaBean 对象
     * @throws java.beans.IntrospectionException
     *             如果分析类属性失败
     * @throws IllegalAccessException
     *             如果实例化 JavaBean 失败
     * @throws InstantiationException
     *             如果实例化 JavaBean 失败
     * @throws java.lang.reflect.InvocationTargetException
     *             如果调用属性的 setter 方法失败
     */
    public static Object convertMap(Class type, Map map)
            throws IntrospectionException, IllegalAccessException,
            InstantiationException, InvocationTargetException {
        BeanInfo beanInfo = Introspector.getBeanInfo(type); // 获取类属性
        Object obj = type.newInstance(); // 创建 JavaBean 对象

        // 给 JavaBean 对象的属性赋值
        PropertyDescriptor[] propertyDescriptors =  beanInfo.getPropertyDescriptors();
        for (int i = 0; i< propertyDescriptors.length; i++) {
            PropertyDescriptor descriptor = propertyDescriptors[i];
            String propertyName = descriptor.getName();

            if (map.containsKey(propertyName)) {
                // 下面一句可以 try 起来，这样当一个属性赋值失败的时候就不会影响其他属性赋值。
                Object value = map.get(propertyName);

                Object[] args = new Object[1];
                args[0] = value;

                descriptor.getWriteMethod().invoke(obj, args);
            }
        }
        return obj;
    }

    /**
     * 将一个 JavaBean 对象转化为一个  Map
     * @param bean 要转化的JavaBean 对象
     * @return 转化出来的  Map 对象
     * @throws IntrospectionException 如果分析类属性失败
     * @throws IllegalAccessException 如果实例化 JavaBean 失败
     * @throws InvocationTargetException 如果调用属性的 setter 方法失败
     */
    public static Map convertBean(Object bean)
            throws IntrospectionException, IllegalAccessException, InvocationTargetException {
        Class type = bean.getClass();
        Map returnMap = new HashMap();
        BeanInfo beanInfo = Introspector.getBeanInfo(type);

        PropertyDescriptor[] propertyDescriptors =  beanInfo.getPropertyDescriptors();
        for (int i = 0; i< propertyDescriptors.length; i++) {
            PropertyDescriptor descriptor = propertyDescriptors[i];
            String propertyName = descriptor.getName();
            if (!propertyName.equals("class")) {
                Method readMethod = descriptor.getReadMethod();
                Object result = readMethod.invoke(bean, new Object[0]);
                if (result != null) {
                    returnMap.put(propertyName, StringUtil.toString(result));
                } else {
                    returnMap.put(propertyName, "");
                }
            }
        }
        return returnMap;
    }

    /**
     * 从
     * @param map
     * @param key
     * @param defaultValue
     * @param clazz
     * @return
     */
    public static Object getObject(Map map, Object key, Object defaultValue,Class clazz){

        if(clazz == Double.class){
            return getDouble(map,key,(Double)defaultValue);
        }else  if(clazz == Integer.class){
            return getInteger(map, key, (Integer) defaultValue);
        }else  if(clazz == Long.class){
            return getLong(map, key, (Long) defaultValue);
        }else  if(clazz == Float.class){
            return getFloat(map, key, (Float) defaultValue);
        }else  if(clazz == Short.class){
            return getShort(map, key, (Short) defaultValue);
        }else  if(clazz == String.class){
            return getString(map, key, (String)defaultValue);
        }else  if(clazz == Byte.class){
            return getByte(map, key, (Byte) defaultValue);
        }else if(clazz == Map.class){
            return getMap(map, key, (Map) defaultValue);
        }else if(clazz == Boolean.class){
            return getBoolean(map, key, (Boolean) defaultValue);
        }else if(clazz == Number.class){
            return getNumber(map, key, (Number) defaultValue);
        }

        return getObject(map,key,defaultValue);
    }

}
