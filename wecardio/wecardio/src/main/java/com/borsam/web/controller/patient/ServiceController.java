package com.borsam.web.controller.patient;

import org.apache.shiro.authz.annotation.RequiresUser;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by Zhang zhongtao on 2015/7/29.
 */
@RequiresUser
@RestController("patientServiceController")
@RequestMapping("/patient/service")
public class ServiceController extends com.borsam.web.controller.admin.ServiceController {
}
