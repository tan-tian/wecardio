package com.hiteam.common.service.impl;

import com.hiteam.common.service.CacheService;
import net.sf.ehcache.CacheManager;
import net.sf.ehcache.Ehcache;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * 缓存 - Service
 */
@Service("cacheServiceImpl")
public class CacheServiceImpl implements CacheService {

	@Resource(name = "ehCacheManager")
	private CacheManager cacheManager;

	@Resource(name = "messageSource")
	private ReloadableResourceBundleMessageSource reloadableResourceBundleMessageSource;

	@Override
	public String getDiskStorePath() {
		return cacheManager.getConfiguration().getDiskStoreConfiguration().getPath();
	}

	@Override
	public int getCacheSize() {
		int cacheSize = 0;
		String[] cacheNames = cacheManager.getCacheNames();

		for (String cacheName : cacheNames) {
			Ehcache cache = cacheManager.getEhcache(cacheName);
			if (cache != null) {
				cacheSize += cache.getSize();
			}
		}

		return cacheSize;
	}

	@Override
//	@CacheEvict(value = { "authorization", "menus" }, allEntries = true)
	public void clear() {
		reloadableResourceBundleMessageSource.clearCache();

		String[] cacheNames = cacheManager.getCacheNames();

		for (String cacheName : cacheNames) {
			Ehcache cache = cacheManager.getEhcache(cacheName);
			if (cache != null) {
				cache.removeAll();
			}
		}
	}

}
