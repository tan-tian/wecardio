package com.borsam.web.controller.patient;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 * Controller - 患者主页
 * Created by Sebarswee on 2015/6/18.
 */
@Controller("patientIndexController")
@RequestMapping(value = "/patient")
public class IndexController {

    /**
     * 主页
     */
    @RequestMapping(method = RequestMethod.GET)
    public String index() {
        return "/patient/index/main";
    }

    /**
     * 首页
     */
    @RequestMapping(value = "/home", method = RequestMethod.GET)
    public String home() {
        return "/patient/index/home";
    }

}
