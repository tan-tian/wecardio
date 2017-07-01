package com.hiteam.common.util.cache;

import net.sf.ehcache.CacheManager;
import net.sf.ehcache.Ehcache;
import net.sf.ehcache.Element;

/**
 * Ehcache操作封装
 * @param <K>
 * @param <V>
 */
public class EhcacheWrapper<K, V> implements CacheWrapper<K, V> {

	/**
	 * 缓存cache名称
	 */
	private final String cacheName;
	
	/**
	 * ehcache容器
	 */
    private final CacheManager cacheManager;
	
    public EhcacheWrapper(final String cacheName, final CacheManager cacheManager) {
    	this.cacheName = cacheName;
    	this.cacheManager = cacheManager;
	}
    
	@SuppressWarnings("unchecked")
	@Override
	public V get(K key) {
		Element element = getCache().get(key);
		if (element != null) {
			return (V) element.getObjectValue();
		}
		return null;
	}

	@Override
	public void put(K key, V value) {
		getCache().put(new Element(key, value));
	}
	
	public Ehcache getCache() {
		return this.cacheManager.getEhcache(cacheName);
	}

}
