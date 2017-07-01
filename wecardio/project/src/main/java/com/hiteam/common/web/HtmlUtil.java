package com.hiteam.common.web;

import org.springframework.web.util.HtmlUtils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * <pre>
 * Description:
 * Author: Zhang zhongtao
 * Version:
 * Since: Ver 1.1
 * Date: 2014-10-28 19:30
 * </pre>
 */
public class HtmlUtil extends HtmlUtils {
    /**正则表达式，匹配a href里面的内容*/
    private static  final Pattern PATTERN_HREF = Pattern.compile("(?si)<a\\s+href\\s?=\\s?(\'|\")(.*)(\'|\")>");

    /**
     * 获取href标签中的内容
     * 如：<a href='http://www.baidu.com'>334343</a> 获取出：http://www.baidu.com
     * @param aHref <a href='http://www.baidu.com'>334343</a>
     * @return
     */
    public static String getHrefContent(String aHref){
        String url = "";

        Matcher m = PATTERN_HREF.matcher(aHref);

        while(m.find()) {
            url = m.group(2);
        }

        return url;
    }

    /**
     * url中追加参数
     * @param url 被追加的url
     * @param urlParams url参数
     * @return 追加后的url
     */
    public static String appendUrlParam(String url , String urlParams){
        return (url += url.indexOf('?') == -1 ? "?" + urlParams : "&" + urlParams);
    }
}
