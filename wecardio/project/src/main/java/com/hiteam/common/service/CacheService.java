package com.hiteam.common.service;

/**
 * 缓存 - Service
 */
public interface CacheService {

	/**
	 * 获取缓存存储路径
	 * @return
	 */
	public String getDiskStorePath();
	
	/**
	 * 获取缓存大小
	 * @return
	 */
	public int getCacheSize();
	
	/**
	 * 清除缓存
	 */
	public void clear();
}
