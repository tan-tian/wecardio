package com.borsam.template.method;

import com.hiteam.common.web.I18Util;
import freemarker.template.SimpleScalar;
import freemarker.template.TemplateMethodModel;
import freemarker.template.TemplateModelException;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * 模板方法 - 国际化
 * Created by tantian on 2015/6/23.
 */
@Component("messageMethod")
public class MessageMethod implements TemplateMethodModel {

    @Override
    public Object exec(List arguments) throws TemplateModelException {
        if (arguments != null && !arguments.isEmpty() && arguments.get(0) != null && StringUtils.isNotEmpty(arguments.get(0).toString())) {
            String message;
            String code = arguments.get(0).toString();
            if (arguments.size() > 1) {
                Object[] args = arguments.subList(1, arguments.size()).toArray();
                message = I18Util.getMessage(code, args);
            } else {
                message = I18Util.getMessage(code);
            }
            return new SimpleScalar(message);
        }
        return null;
    }
}
