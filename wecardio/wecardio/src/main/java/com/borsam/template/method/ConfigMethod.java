package com.borsam.template.method;

import com.hiteam.common.util.ConfigUtils;
import freemarker.template.SimpleScalar;
import freemarker.template.TemplateMethodModel;
import freemarker.template.TemplateModelException;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * 模板方法 - 获取配置
 * Created by Sebarswee on 2015/6/23.
 */
@Component("configMethod")
public class ConfigMethod implements TemplateMethodModel {

    @Override
    public Object exec(List arguments) throws TemplateModelException {
        if (arguments != null && !arguments.isEmpty() && arguments.get(0) != null && StringUtils.isNotEmpty(arguments.get(0).toString())) {
            String key = arguments.get(0).toString();
            return new SimpleScalar(ConfigUtils.config.getProperty(key));
        }
        return null;
    }
}
