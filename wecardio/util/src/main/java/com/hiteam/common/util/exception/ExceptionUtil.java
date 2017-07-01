package com.hiteam.common.util.exception;

import org.apache.commons.lang3.exception.ExceptionUtils;

import java.io.PrintWriter;
import java.io.StringWriter;

/**
 * <pre>
 * Description:
 * Author: Zhang zhongtao
 * Version:
 * Since: Ver 1.1
 * Date: 2011-11-24 19:41
 * </pre>
 */
public class ExceptionUtil extends ExceptionUtils{

    /**
     * 错误信息输出到字符串中
     * @param ex
     * @return
     */
    public static String toString(Throwable ex){
        StringWriter stringWriter = new StringWriter();
        PrintWriter printWriter = new PrintWriter(stringWriter);

        ex.printStackTrace(printWriter);

        return stringWriter.toString();
    }
}
