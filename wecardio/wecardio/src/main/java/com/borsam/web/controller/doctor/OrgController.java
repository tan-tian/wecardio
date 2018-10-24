package com.borsam.web.controller.doctor;

import com.borsam.service.org.OrganizationService;
import com.hiteam.common.util.pojo.EnumBean;
import com.hiteam.common.web.controller.BaseController;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import java.util.List;

/**
 * Controller - 机构管理
 * Created by tantian on 2015/7/7.
 */
@Controller("doctorOrganizationController")
@RequestMapping("/doctor/organization")
public class OrgController extends BaseController {

    @Resource(name = "organizationServiceImpl")
    private OrganizationService organizationService;

    /**
     * 机构列表（for 下拉框）
     */
    @RequestMapping(value = "/sel", method = RequestMethod.POST)
    @ResponseBody
    public List<EnumBean> sel(String name) {
        return organizationService.sel(name);
    }
    
    
}
