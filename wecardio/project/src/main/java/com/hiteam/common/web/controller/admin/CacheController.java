package com.hiteam.common.web.controller.admin;

import com.hiteam.common.service.CacheService;
import com.hiteam.common.web.controller.BaseController;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.annotation.Resource;

/**
 * 缓存管理 - Controller
 * @author wengsiwei
 *
 */
@Controller("cacheController")
@RequestMapping("/admin/cache")
public class CacheController extends BaseController {

	@Resource(name = "cacheServiceImpl")
	private CacheService cacheService;
	
	/**
	 * 缓存查看
	 */
	@RequestMapping(value = "/index", method = RequestMethod.GET)
	public String index(ModelMap model) {
		Long totalMemory = null;
		Long maxMemory = null;
		Long freeMemory = null;
		try {
			totalMemory = Runtime.getRuntime().totalMemory() / 1024 / 1024;
			maxMemory = Runtime.getRuntime().maxMemory() / 1024 / 1024;
			freeMemory = Runtime.getRuntime().freeMemory() / 1024 / 1024;
		} catch (Exception e) {
		}
		model.addAttribute("totalMemory", totalMemory);
		model.addAttribute("maxMemory", maxMemory);
		model.addAttribute("freeMemory", freeMemory);
		model.addAttribute("cacheSize", cacheService.getCacheSize());
		model.addAttribute("diskStorePath", cacheService.getDiskStorePath());
		
		return "/admin/cache/index";
	}

	/**
	 * 清除缓存
	 */
	@RequestMapping(value = "/clear", method = RequestMethod.POST)
	public String clear() {
		cacheService.clear();
		return "redirect:index";
	}
}
