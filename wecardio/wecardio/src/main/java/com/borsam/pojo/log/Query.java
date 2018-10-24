package com.borsam.pojo.log;

import ch.qos.logback.classic.Level;

import java.io.Serializable;
import java.util.Date;

/**
 * <pre>
 * @Description: 日志过滤条件
 * @author :tantian
 * @version: Ver 1.0
 * @Date: 2015-08-22 18:11
 * </pre>
 */
public class Query implements Serializable {
    /**日志时间*/
    private Date date;
    /**日志级别*/
    private Level level;
    /**日志名称*/
    private String logger;

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public Level getLevel() {
        return level;
    }

    public void setLevel(Level level) {
        this.level = level;
    }

    public String getLogger() {
        return logger;
    }

    public void setLogger(String logger) {
        this.logger = logger;
    }
}
