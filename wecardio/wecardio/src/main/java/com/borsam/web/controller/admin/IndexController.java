package com.borsam.web.controller.admin;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

/**
 * Controller - 平台管理员主页
 * Created by Sebarswee on 2015/6/18.
 */
@RestController("adminIndexController")
@RequestMapping(value = "/admin")
public class IndexController extends StatisticController {

    /**
     * 主页
     */
    @RequestMapping(method = RequestMethod.GET)
    public ModelAndView index() {
        return new ModelAndView("/admin/index/main");
    }

    /**
     * 首页
     */
    @RequestMapping(value = "/home", method = RequestMethod.GET)
    public ModelAndView home() {
        return new ModelAndView("/admin/index/home");
    }

}
