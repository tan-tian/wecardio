package com.hiteam.common.web.controller.guest;

import com.hiteam.common.service.pub.CountryService;
import com.hiteam.common.util.pojo.EnumBean;
import com.hiteam.common.web.controller.BaseController;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import java.util.List;

/**
 * 国家 - Controller
 * Created by Sebarswee on 2014/9/28.
 */
@Controller("countryController")
@RequestMapping(value = "/guest/pub/country")
public class CountryController extends BaseController {

    @Resource(name = "countryServiceImpl")
    private CountryService countryService;

    /**
     * 国家枚举下拉列表
     * @param name 国家名称
     * @return 国家枚举列表
     */
    @RequestMapping(value = "/countrySel", method = RequestMethod.POST)
    @ResponseBody
    public List<EnumBean> countrySel(String name) {
        return countryService.countrySel(name);
    }

}
