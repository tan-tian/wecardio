package com.borsam.web.controller.org;

import org.apache.shiro.authz.annotation.RequiresUser;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by Zhang zhongtao on 2015/7/22.
 */
@RequiresUser
@RestController("orgServiceController")
@RequestMapping("/org/service")
public class ServiceController extends com.borsam.web.controller.admin.ServiceController {

}
