package com.hiteam.common.util.json;

/**
 * <pre>
 * @Description:
 * @author :tantian
 * @version: Ver 1.0
 * @Date: 2015-08-23 12:01
 * </pre>
 */
public class DateFullTimeSerializer extends DateSerializer {
    @Override
    protected String getDateFormat() {
        return "yyyy-MM-dd HH:mm:ss SSS";
    }
}
