package com.hiteam.common.web.controller.guest;

import com.hiteam.common.service.enums.EnumService;
import com.hiteam.common.util.pojo.EnumBean;
import com.hiteam.common.web.controller.BaseController;
import org.springframework.stereotype.Controller;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import java.util.List;

/**
 * Controller - 枚举
 * Created by Sebarswee on 2015/7/15.
 */
@Controller("enumController")
@RequestMapping({ "/guest/enum" })
public class EnumController extends BaseController {

    @Resource(name = "enumServiceImpl")
    private EnumService enumService;

    /**
     * 获取枚举
     */
    @RequestMapping(value = "/{tblName}/{colName}")
    @ResponseBody
    public List<EnumBean> get(@PathVariable String tblName, @PathVariable String colName,String notIn) {
        Assert.notNull(tblName);
        Assert.notNull(colName);
        return enumService.getEnum(tblName, colName,notIn);
    }

    /**
     * 清除缓存
     */
    @RequestMapping(value = "/cache/clear")
    @ResponseBody
    public boolean clearCache() {
        enumService.clearCache();
        return true;
    }
}
