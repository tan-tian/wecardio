package com.borsam.web.controller.doctor;

import org.apache.shiro.authz.annotation.RequiresUser;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by tantian on 2015/7/29.
 */
@RequiresUser
@RestController("doctorServiceController")
@RequestMapping("/doctor/service")
public class ServiceController extends com.borsam.web.controller.admin.ServiceController {
}
