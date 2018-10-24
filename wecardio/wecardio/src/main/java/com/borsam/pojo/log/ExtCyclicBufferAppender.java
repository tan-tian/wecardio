package com.borsam.pojo.log;

import ch.qos.logback.classic.spi.IThrowableProxy;
import ch.qos.logback.classic.spi.LoggingEvent;
import ch.qos.logback.classic.spi.StackTraceElementProxy;
import ch.qos.logback.classic.spi.ThrowableProxy;
import ch.qos.logback.core.CoreConstants;
import ch.qos.logback.core.helpers.Transform;
import ch.qos.logback.core.read.CyclicBufferAppender;
import com.borsam.web.controller.admin.log.ExtLogWebsocketEndPoint;
import com.hiteam.common.util.collections.MapUtil;
import com.hiteam.common.util.exception.ExceptionUtil;
import com.hiteam.common.util.lang.StringUtil;
import com.hiteam.common.util.spring.SpringUtils;

import java.util.Date;
import java.util.List;
import java.util.Objects;

/**
 * <pre>
 * @Description:
 * @author :tantian
 * @version: Ver 1.0
 * @Date: 2015-08-22 18:53
 * </pre>
 */
public class ExtCyclicBufferAppender<E> extends CyclicBufferAppender {
    @Override
    protected void append(Object eventObject) {

        if (eventObject instanceof LoggingEvent) {
            ExtLogWebsocketEndPoint point = SpringUtils.getBean("extLogWebsocketEndPoint");
            //是否启用日志自动推送
            if (point.getEnablePush()) {
                Info info = addInfo((LoggingEvent) eventObject);
                point.sendAll(info);
            }
        }

        super.append(eventObject);
    }

    private Info addInfo(LoggingEvent event) {
        Info info = new Info();
        info.setDate(new Date(event.getTimeStamp()));
        info.setLevel(event.getLevel());
        info.setLogger(event.getLoggerName());
        info.setThread(event.getThreadName());
        info.setMessage(event.getFormattedMessage());

        throwableHandler(event.getThrowableProxy(), info);

        info.setUsrName(MapUtil.getString(event.getMDCPropertyMap(), "loginUserName"));
        return info;
    }

    /**
     * 异常信息处理
     *
     * @param throwableProxy 异常代理
     * @param info           日志信息对象
     */
    private void throwableHandler(IThrowableProxy throwableProxy, Info info) {

        if (throwableProxy != null) {
            String msg = ExceptionUtil.toString(((ThrowableProxy) throwableProxy).getThrowable());
            msg = msg.replaceAll("at\\s+", "<br/>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;at ");
            info.setExceptionMessage(msg);
        }
    }
}
