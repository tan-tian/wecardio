package com.hiteam.common.util.collections;

import org.apache.commons.collections.CollectionUtils;

import java.util.Collection;

/**
 * <pre>
 * Description:
 * Author: Zhang zhongtao
 * Version:
 * Since: Ver 1.1
 * Date: 2014-11-10 14:43
 * </pre>
 */
public class CollectionUtil extends CollectionUtils {

    /***
     * 获取集合中的第一条记录,方法内部会进行空值判断
     *
     * @param collection 记录
     * @param <T>        返回对象类型
     * @return 结果
     */
    public static <T> T getFirst(Collection<T> collection) {

        if (isNotEmpty(collection)) {
            return collection.iterator().next();
        }

        return null;
    }

    /***
     * 获取集合中的第一条记录,方法内部会进行空值判断
     *
     * @param collection 记录
     * @param defaultVal 为空值时返回的默认值
     * @param <T>        返回对象类型
     * @return 结果
     */
    public static <T> T getFirst(Collection<T> collection, T defaultVal) {
        T t = isEmpty(collection) ? defaultVal : collection.iterator().next();
        return t == null ? defaultVal : t;
    }

    /**
     * 获取最后一个元素
     *
     * @param collection 集合
     * @param <T>        返回结果类型
     * @return T
     */
    public static <T> T getLast(Collection<T> collection) {
        return getLast(collection, null);
    }

    /**
     * 获取最后一个元素
     *
     * @param collection 集合
     * @param defaultVal 为空时的默认值
     * @param <T>        返回结果类型
     * @return T
     */
    public static <T> T getLast(Collection<T> collection, T defaultVal) {
        T t = isEmpty(collection) ? defaultVal : (T) get(collection, collection.size() - 1);
        return t == null ? defaultVal : t;
    }
}
