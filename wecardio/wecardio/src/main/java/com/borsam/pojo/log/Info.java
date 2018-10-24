package com.borsam.pojo.log;

import ch.qos.logback.classic.Level;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.hiteam.common.util.json.DateFullTimeSerializer;
import com.hiteam.common.util.json.DateTimeSerializer;

import java.io.Serializable;
import java.util.Date;

/**
 * <pre>
 * @Description: 日志信息
 * @author :tantian
 * @version: Ver 1.0
 * @Date: 2015-08-22 18:08
 * </pre>
 */
public class Info implements Serializable {
    /**日志时间*/
    private Date date;
    /**日志线程名称*/
    private String thread;
    /**日志级别*/
    private Level level;
    /**日志名称*/
    private String logger;
    /**日志详细信息*/
    private String message;
    /**异常信息*/
    private String exceptionMessage;
    /**用户名*/
    private String usrName;

    @JsonSerialize(using = DateFullTimeSerializer.class)
    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String getThread() {
        return thread;
    }

    public void setThread(String thread) {
        this.thread = thread;
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

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getUsrName() {
        return usrName;
    }

    public void setUsrName(String usrName) {
        this.usrName = usrName;
    }

    public String getExceptionMessage() {
        return exceptionMessage;
    }

    public void setExceptionMessage(String exceptionMessage) {
        this.exceptionMessage = exceptionMessage;
    }
}
