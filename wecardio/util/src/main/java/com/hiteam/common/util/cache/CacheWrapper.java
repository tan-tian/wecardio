package com.hiteam.common.util.cache;

/**
 * Cache 操作封装
 * @param <K>
 * @param <V>
 */
public interface CacheWrapper<K, V> {

	/**
	 * 通过key值获取value
	 * @param key
	 * @return
	 */
	public V get(K key);
	
	/**
	 * 以key值设置value
	 * @param key
	 * @param value
	 */
	public void put(K key, V value);
}
