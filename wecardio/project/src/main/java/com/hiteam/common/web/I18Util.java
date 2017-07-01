package com.hiteam.common.web;

import com.hiteam.common.util.spring.SpringUtils;
import org.springframework.web.servlet.LocaleResolver;

import java.util.Locale;

/**
 * <pre>
 * Description: 国际化工具类
 * Author: Zhang zhongtao
 * Version:
 * Since: Ver 1.1
 * Date: 2014-11-26 12:27
 * </pre>
 */
public class I18Util {
    /**
     * 获取国际化消息
     * @param code		代码
     * @param args		参数
     * @return
     */
    public static String getMessage(String code, Object... args) {
        LocaleResolver resolver = SpringUtils.getBean("localeResolver");
        Locale localLocale = resolver.resolveLocale(WebUtil.getRequest());
        return SpringUtils.getApplicationContext().getMessage(code, args, localLocale);
    }
}
