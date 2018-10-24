package com.borsam.web.controller.doctor;

import org.apache.shiro.authz.annotation.RequiresUser;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * <pre>
 * @Description:
 * @author :tantian
 * @version: Ver 1.0
 * @Date: 2015-08-07 09:47
 * </pre>
 */
@RequiresUser
@RestController("doctorForumInfoController")
@RequestMapping("/doctor/forum/info")
public class ForumInfoController extends com.borsam.web.controller.admin.ForumInfoController {

}
