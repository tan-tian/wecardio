package com.borsam.web.controller.org;

import com.borsam.web.controller.admin.StatisticController;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

/**
 * Controller - 机构管理员主页
 * Created by Sebarswee on 2015/6/18.
 */
@Controller("orgIndexController")
@RequestMapping(value = "/org")
public class IndexController extends StatisticController {

    /**
     * 主页
     */
    @RequestMapping(method = RequestMethod.GET)
    public ModelAndView index() {
        return new ModelAndView("/org/index/main");
    }

    /**
     * 首页
     */
    @RequestMapping(value = "/home", method = RequestMethod.GET)
    public ModelAndView home() {
        return new ModelAndView("/org/index/home");
    }

}
