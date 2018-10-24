package com.hiteam.common.web.controller.guest;

import com.hiteam.common.service.pub.AreaService;
import com.hiteam.common.util.pojo.EnumBean;
import com.hiteam.common.web.controller.BaseController;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import java.util.List;

/**
 * 地区 - Controller
 * Created by tantian on 2014/9/28.
 */
@Controller("commonAreaController")
@RequestMapping(value = "/guest/pub/area")
public class AreaController extends BaseController {

    @Resource(name = "areaServiceImpl")
    private AreaService areaService;

    /**
     * 地区枚举下拉列表
     * @param countryId 国家ID
     * @param name 地区名称
     * @return 地区枚举列表
     */
    @RequestMapping(value = "/areaSel", method = RequestMethod.POST)
    @ResponseBody
    public List<EnumBean> areaSel(Long countryId, String name) {
        return areaService.areaSel(countryId, name);
    }

}
