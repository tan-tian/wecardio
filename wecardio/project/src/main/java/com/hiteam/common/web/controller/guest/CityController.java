package com.hiteam.common.web.controller.guest;

import com.hiteam.common.service.pub.CityService;
import com.hiteam.common.util.pojo.EnumBean;
import com.hiteam.common.web.controller.BaseController;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import java.util.List;

/**
 * 城市 - Controller
 * Created by Sebarswee on 2014/9/28.
 */
@Controller("cityController")
@RequestMapping(value = "/guest/pub/city")
public class CityController extends BaseController {

    @Resource(name = "cityServiceImpl")
    private CityService cityService;

    /**
     * 城市枚举下拉列表
     * @param countryId  国家ID
     * @param areaId     地区ID
     * @param provinceId 省份ID
     * @param name       城市名称
     * @return 城市枚举列表
     */
    @RequestMapping(value = "/citySel", method = RequestMethod.POST)
    @ResponseBody
    public List<EnumBean> citySel(Long countryId, Long areaId, Long provinceId, String name) {
        return cityService.citySel(countryId, areaId, provinceId, name);
    }

    /**
     * 获取城市邮编
     * @param cityId
     * @return
     */
    @RequestMapping(value = "cityZipCode", method = RequestMethod.POST)
    @ResponseBody
    public String getCityZipCode(Long cityId) {
        return cityService.getCityZipCode(cityId);
    }

}
