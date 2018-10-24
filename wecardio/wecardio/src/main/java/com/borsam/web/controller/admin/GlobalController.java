package com.borsam.web.controller.admin;

import com.hiteam.common.web.Message;
import com.hiteam.common.web.WebUtil;
import com.hiteam.common.web.controller.BaseController;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * <pre>
 *  语言国际化
 * @author : tantian
 * @version : Ver 1.0
 * </pre>
 */
@RestController("adminGlobalController")
@RequestMapping("/admin/global")
public class GlobalController extends BaseController{
    private static Logger log = LoggerFactory.getLogger(GlobalController.class);

    @RequestMapping("/switch/lang")
    public void switchLang(@RequestParam(value = "language") String language) {
        WebUtil.addCookie("language", language);
        baseController.SUCCESS_MSG=Message.success("common.message.success", new Object[0]);
        baseController.IMEI_MSG=Message.warn("common.message.imei.exist", new Object[0]);
        baseController.WARN_MSG=Message.warn("common.message.warn", new Object[0]);
        baseController.ERROR_MSG=Message.error("common.message.error", new Object[0]);
        baseController.CODEERROR_MSG=Message.error("common.message.codeerror", new Object[0]);
    }
    
    public static BaseController baseController=new BaseController();
   
  
}
