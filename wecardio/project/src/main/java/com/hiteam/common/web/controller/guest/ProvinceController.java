package com.hiteam.common.web.controller.guest;

import com.hiteam.common.service.pub.ProvinceService;
import com.hiteam.common.util.pojo.EnumBean;
import com.hiteam.common.web.controller.BaseController;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import java.util.List;

/**
 * 省份 - Controller
 * Created by tantian on 2014/9/28.
 */
@Controller("provinceController")
@RequestMapping(value = "/guest/pub/province")
public class ProvinceController extends BaseController {

    @Resource(name = "provinceServiceImpl")
    private ProvinceService provinceService;

    /**
     * 省份枚举下拉列表
     * @param countryId 国家ID
     * @param areaId    地区ID
     * @param name      省份名称
     * @return 省份枚举列表
     */
    @RequestMapping(value = "/provinceSel", method = RequestMethod.POST)
    @ResponseBody
    public List<EnumBean> provinceSel(Long countryId, Long areaId, String name) {
        return provinceService.provinceSel(countryId,areaId,name);
    }
}
