package com.borsam.web.controller.patient;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * Controller - 消息管理
 * Created by liujieming on 2015/7/7.
 */
@Controller("patientMessageController")
@RequestMapping("/patient/message")
public class MessageController extends com.borsam.web.controller.admin.MessageController {
}
