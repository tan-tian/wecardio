package com.hiteam.common.web.template;

import com.hiteam.common.util.ConfigUtils;
import com.hiteam.common.web.WebUtil;
import org.beetl.core.Configuration;
import org.beetl.core.GroupTemplate;
import org.beetl.core.Template;
import org.beetl.core.exception.BeetlException;
import org.beetl.core.resource.FileResourceLoader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.util.Map;

/**
 * <pre>
 * Description:
 * Author: Administrator
 * Version:
 * Since: Ver 1.1
 * Date: 2014-05-15 10:27
 * </pre>
 */
public class TemplateUtil {

	private static Logger logger = LoggerFactory.getLogger(TemplateUtil.class);

	private static final String DEFAULT_CODE = "UTF-8";

	/**
	 * 模板对象
	 */
	private static GroupTemplate gt;

	/**
	 * 获取webapp的系统路径
	 */
	private static final String PATH = WebUtil.getRequest().getSession().getServletContext().getRealPath("/");
	
	/**
	 * 存放模板的路径(配置到config.properties)
	 */
	private static final String TEMPLATEFILEPATH = ConfigUtils.config.getProperty("defaultTemplatePath");
	
	// endregion
	// region 初始化模板引擎
	static {
		FileResourceLoader fileResourceLoader = new FileResourceLoader(PATH + TEMPLATEFILEPATH, DEFAULT_CODE);
		logger.info("系统盘模板文件夹:" + PATH + TEMPLATEFILEPATH);
		Configuration cfg = null;
		try {
			cfg = Configuration.defaultConfiguration();
			cfg.setCharset(DEFAULT_CODE);
			gt = new GroupTemplate(fileResourceLoader, cfg);
		} catch (IOException e) {
			e.printStackTrace();
			logger.error("模板引擎初始化失败", e);
		}
	}

	/**
	 * 模板转换为字符串
	 *
	 * @param template
	 *            模板
	 * @param params
	 *            参数
	 * @return 字符串
	 */
	public static String toString(String template, Map params) {

		long start = System.currentTimeMillis();
		String renderStr = null;
		try {
			Template t = gt.getTemplate(template);
			t.binding(params);
			// 模板渲染
			renderStr = t.render();
		} catch (Exception e) {
			logger.error("模板渲染异常,模板内容:\n\r" + template, e);
		}
		logger.info("模板渲染耗时:" + (System.currentTimeMillis() - start) + "耗秒");

		return renderStr;
	}

	/**
	 * 根据模板文件生成静态文件(html)
	 *
	 * @param templateFileName
	 *            模板文件
	 * @param toPath
	 *            生成静态文件后存放的路径
	 * @param toFileName
	 *            生成静态文件后的文件名称
	 * @param param
	 *            对象参数[实体对象，系统路径等]
	 */
	public static void templateToFile(String templateFileName, String toPath,
			String toFileName, Map<String, Object> param) {
		// 测试生成时间
		Long startTime = System.currentTimeMillis();
		
		// 读取模板文件
		Template template = gt.getTemplate(templateFileName);
		logger.info("模板文件存放路径:" + (PATH + TEMPLATEFILEPATH + templateFileName));

		// 模板数据绑定
		template.binding(param);

		logger.info("静态文件存放路径:" + (toPath + toFileName));
		
		try {
			template.renderTo(new OutputStreamWriter(new FileOutputStream(new File(toPath + toFileName)),DEFAULT_CODE));
		} catch (BeetlException e) {
			logger.error("输出静态文件出错...");
			e.printStackTrace();
		} catch (FileNotFoundException e) {
			logger.error("输出静态文件出错...");
			e.printStackTrace();
		}catch (Exception ex){

		}
		logger.info("耗时:" + (System.currentTimeMillis() - startTime) + "'ms");
	}
}
