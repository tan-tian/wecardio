package com.borsam.web.controller.doctor;

import com.borsam.pojo.security.Principal;
import com.borsam.repository.entity.message.MessageInfo;
import com.borsam.service.message.MessageService;
import com.hiteam.common.base.pojo.search.Filter;
import com.hiteam.common.base.pojo.search.Order;
import com.hiteam.common.base.pojo.search.Page;
import com.hiteam.common.base.pojo.search.Pageable;
import com.hiteam.common.service.enums.EnumService;
import com.hiteam.common.util.lang.StringUtil;
import com.hiteam.common.web.Message;
import com.hiteam.common.web.controller.BaseController;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.time.DateUtils;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Controller - 消息管理
 * Created by liujieming on 2015/7/7.
 */
@Controller("doctorMessageController")
@RequestMapping("/doctor/message")
public class MessageController extends com.borsam.web.controller.admin.MessageController {
}
