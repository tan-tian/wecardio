package com.borsam.web.controller.org;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * Controller - 消息管理
 * Created by liujieming on 2015/7/7.
 */
@Controller("orgMessageController")
@RequestMapping("/org/message")
public class MessageController extends com.borsam.web.controller.admin.MessageController {
}
