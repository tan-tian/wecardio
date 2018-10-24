package com.hiteam.common.web;

import com.hiteam.common.util.ConfigUtils;
import com.hiteam.common.util.lang.StringUtil;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.Assert;
import org.springframework.web.util.WebUtils;

import javax.mail.internet.MimeUtility;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;

/**
 * <pre>
 * Description: 获取requet/response等对象，便于获取session时，不用传入HttpServletRequest、HttpServletResponse
 * Author: tantian
 * Version:
 * Since: Ver 1.1
 * Date: 2014-10-30 09:32
 * </pre>
 */
public class WebUtil {
    private static Logger logger = LoggerFactory.getLogger(WebUtil.class);

    private static ThreadLocal<HttpServletRequest> servletRequest = new ThreadLocal<HttpServletRequest>();
    private static ThreadLocal<HttpServletResponse> servletResponse = new ThreadLocal<HttpServletResponse>();

    public static void setRequest(HttpServletRequest request) {
        servletRequest.set(request);
    }

    public static void setResponse(HttpServletResponse response) {
        servletResponse.set(response);
    }

    /**
     * 获取Response对象
     *
     * @return HttpServletResponse
     */
    public static HttpServletResponse getResponse() {
        return servletResponse.get();
    }

    /**
     * 从当前线程变量中获取HttpServletRequest
     *
     * @return HttpServletRequest
     */
    public static HttpServletRequest getRequest() {
        return servletRequest.get();
    }

    /**
     * 获取HttpSession
     *
     * @return HttpSession
     */
    public static HttpSession getSession() {
        return getRequest().getSession();
    }

    /**
     * 从session中获取数据
     *
     * @param attrName 属性名称
     * @param <T>      返回的类型
     * @return T
     */
    public static <T> T getSessionData(String attrName) {
        return (T) getSession().getAttribute(attrName);
    }

    /**
     * 存放数据到session中
     *
     * @param attrName
     * @param obj
     */
    public static void setSessionData(String attrName, Object obj) {
        Assert.hasLength(attrName);
        getSession().setAttribute(attrName, obj);
    }


    /**
     * 从当前请求中，获取cookie信息
     *
     * @param name cookie名称
     * @return Cookie
     */
    public static Cookie getCookie(String name) {
        return WebUtils.getCookie(getRequest(), name);
    }

    /**
     * 移除cookie
     *
     * @param name cookie名称
     */
    public static void removeCookie(HttpServletRequest request, HttpServletResponse response, String name) {
        removeCookie(name, ConfigUtils.config.getProperty("cookiePath"), ConfigUtils.config.getProperty("cookieDomain"));
    }

    /**
     * 移除cookie
     *
     * @param name   cookie名称
     * @param path   路径
     * @param domain 域
     */
    public static void removeCookie(String name, String path, String domain) {
        Assert.hasText(name);

        try {
            name = URLEncoder.encode(name, "UTF-8");
            Cookie cookie = new Cookie(name, null);
            cookie.setMaxAge(0);

            if (StringUtil.isNotEmpty(path)) {
                cookie.setPath(path);
            }

            if (StringUtil.isNotEmpty(domain)) {
                cookie.setDomain(domain);
            }

            getResponse().addCookie(cookie);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }

    /**
     * 添加cookie
     *
     * @param name   cookie名称
     * @param value  cookie值
     * @param maxAge 有效期(秒)
     * @param path   路径
     * @param domain 域
     * @param secure 是否加密
     */
    public static void addCookie(String name, String value, Integer maxAge, String path, String domain, Boolean secure) {
        Assert.hasText(name);

        try {
            name = URLEncoder.encode(name, "UTF-8");
            value = URLEncoder.encode(value, "UTF-8");
            Cookie cookie = new Cookie(name, value);

            if (maxAge != null) {
                cookie.setMaxAge(maxAge.intValue());
            }

            if (StringUtil.isNotEmpty(path)) {
                cookie.setPath(path);
            }

            if (StringUtil.isNotEmpty(domain)) {
                cookie.setDomain(domain);
            }

            if (secure != null) {
                cookie.setSecure(secure.booleanValue());
            }
            getResponse().addCookie(cookie);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }

    /**
     * 添加cookie
     *
     * @param name   cookie名称
     * @param value  cookie值
     * @param maxAge 有效期(秒)
     */
    public static void addCookie(String name, String value, Integer maxAge) {
        addCookie(name, value, maxAge, ConfigUtils.config.getProperty("cookiePath"), ConfigUtils.config.getProperty("cookieDomain"), null);
    }

    /**
     * 添加cookie
     *
     * @param name  cookie名称
     * @param value cookie值
     */
    public static void addCookie(String name, String value) {
        addCookie(name, value, null, ConfigUtils.config.getProperty("cookiePath"), ConfigUtils.config.getProperty("cookieDomain"), null);
    }

    /**
     * 获取cookie值
     *
     * @param name cookie名称
     * @return
     */
    public static String getCookieVal(String name) {
        Assert.hasText(name);

        Cookie[] cookies = getRequest().getCookies();
        if (cookies != null) {
            try {
                name = URLEncoder.encode(name, "UTF-8");
                for (Cookie cookie : cookies) {
                    if (name.equals(cookie.getName())) {
                        return URLDecoder.decode(cookie.getValue(), "UTF-8");
                    }
                }
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    /**
     * 保存当前cookie（有效期不限）到客户端
     *
     * @param name  cookie名称
     * @param value cookie值
     */
    public static void setCookie(String name, String value) {
        //加密保存cookie
        Cookie cookie = new Cookie(name, value);
        cookie.setPath("/");
        //长期保存
        cookie.setMaxAge(-1);
        //保存到客户端
        getResponse().addCookie(cookie);
    }

    /**
     * 判断是否为ajax请求
     *
     * @return true：是；false：不是
     */
    public static boolean isAjaxRequest() {
        String requestedHeader = getRequest().getHeader("X-Requested-With");
        String accept = getRequest().getHeader("accept");
        boolean isAjax = Boolean.FALSE;

        if ((StringUtils.isNotBlank(accept) && accept.contains("application/json"))
                || StringUtil.isNotBlank(requestedHeader) && (requestedHeader.equalsIgnoreCase("XMLHttpRequest"))) {
            isAjax = Boolean.TRUE;
        }

        if (!isAjax) {
            String format = getRequest().getParameter("format");
            String callback = getRequest().getParameter("callback");
            isAjax = (StringUtils.isNotBlank(format) && format.equalsIgnoreCase("json"));
            isAjax = isAjax ? isAjax : getRequest().getRequestURI().contains(".json");
            isAjax = isAjax ? isAjax : StringUtils.isNotBlank(callback);
        }

        return isAjax;
    }

    /**
     * 输出json数据到客户端
     *
     * @param json json格式字符串
     */
    public static void responseJson(String json) {
        responseJson(getResponse(), json);
    }

    /**
     * 输出json数据到客户端
     *
     * @param json json格式字符串
     */
    public static void responseJson(HttpServletResponse response, String json) {
        responseJson(response, getRequest(), json);
    }

    /**
     * 输出json数据到客户端
     *
     * @param json json格式字符串
     */
    public static void responseJson(HttpServletResponse response, HttpServletRequest request, String json) {
        try {
            response.setContentType("application/json; charset=utf-8");
            PrintWriter writer = response.getWriter();
            //jsonp的callback支持
            String callback = request.getParameter("callback");

            if (StringUtils.isNotBlank(callback)) {
                json = callback + "(" + json + ")";
            }

            writer.write(json);
            writer.flush();
            writer.close();
        } catch (IOException e) {
            logger.error("responseJson", e);
        }
    }

    /**
     * 处理中文文件名称
     *
     * @param fileName 文件名称
     * @param request  HttpServletRequest
     * @return 重新编码后的文件名称
     */
    public static String encodingFileName(String fileName, HttpServletRequest request) {
        String agent = request.getHeader("USER-AGENT");
        try {
            if ((agent != null) && (agent.contains("MSIE"))) {
                String newFileName = URLEncoder.encode(fileName, "UTF-8");
                newFileName = StringUtils.replace(newFileName, "+", "%20");
                if (newFileName.length() > 150) {
                    newFileName = new String(fileName.getBytes("GB2312"), "ISO8859-1");
                    newFileName = StringUtils.replace(newFileName, " ", "%20");
                }
                return newFileName;
            }
            if ((agent != null) && (agent.contains("Mozilla"))) {
                return MimeUtility.encodeText(fileName, "UTF-8", "B");
            }

            return fileName;
        } catch (Exception ex) {
            return fileName;
        }
    }
}
