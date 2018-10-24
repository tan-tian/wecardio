package com.borsam.web.controller.admin;

import com.borsam.pojo.security.Principal;
import com.borsam.pojo.statistics.CountData;
import com.borsam.pojo.statistics.YearCountData;
import com.borsam.repository.entity.doctor.DoctorProfile;
import com.borsam.service.doctor.DoctorProfileService;
import com.borsam.service.statistics.StatisticService;
import com.hiteam.common.web.controller.BaseController;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authz.annotation.Logical;
import org.apache.shiro.authz.annotation.RequiresRoles;
import org.apache.shiro.subject.Subject;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * <pre>
 * @Description:
 * @author :tantian
 * @version: Ver 1.0
 * @Date: 2015-08-11 15:59
 * </pre>
 */
@RestController("StatisticController")
public class StatisticController extends BaseController {
    @Resource(name = "statisticServiceImpl")
    private StatisticService statisticService;

    @Resource(name = "doctorProfileServiceImpl")
    private DoctorProfileService doctorProfileService;

    /**
     * 获取统计数据
     * 平台管理员统计：机构数量、医生数量、患者数量、诊单数量
     * 机构管理员统计：医生数量、患者数量、诊单数量、服务包数量
     *
     * @return
     */
    @RequiresRoles(value = {"admin", "org"}, logical = Logical.OR)
    @RequestMapping(value = "/sum", method = RequestMethod.POST)
    public CountData getCount() {
        Principal principal = getPrincipal();
        return statisticService.getCount(principal, getKey(principal));
    }

    /**
     * 获取年度统计
     *
     * @return 平台管理员：PaltformYearCountData，机构管理员：OrgYearCountData
     */
    @RequiresRoles(value = {"admin", "org"}, logical = Logical.OR)
    @RequestMapping(value = "/yearSum", method = RequestMethod.POST)
    public YearCountData getYearCount() {
        Principal principal = getPrincipal();
        return statisticService.getYearCount(principal, getKey(principal));
    }

    private Long getKey(Principal principal) {
        Long key = 0L;

        switch (principal.getUserType()) {
            case admin:
                key = -9999999L;
                break;
            case org:

                Long oid = -1L;

                DoctorProfile doctorProfile = doctorProfileService.find(principal.getId());

                if (doctorProfile.getOrg() != null) {
                    oid = doctorProfile.getOrg().getId();
                }

                key = oid;

                break;
            default:
                break;
        };

        return key;
    }

    private Principal getPrincipal() {
        Subject subject = SecurityUtils.getSubject();
        return (Principal) subject.getPrincipal();
    }
}
