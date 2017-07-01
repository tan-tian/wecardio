package com.borsam.web.controller.admin;

import com.borsam.pojo.forum.QueryForumInfoData;
import com.borsam.pojo.security.Principal;
import com.borsam.repository.entity.doctor.DoctorProfile;
import com.borsam.repository.entity.forum.ForumInfo;
import com.borsam.repository.entity.org.Organization;
import com.borsam.service.doctor.DoctorProfileService;
import com.borsam.service.forum.ForumInfoService;
import com.borsam.service.org.OrganizationService;
import com.hiteam.common.base.pojo.search.Page;
import com.hiteam.common.web.controller.BaseController;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authz.annotation.RequiresUser;
import org.apache.shiro.subject.Subject;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * <pre>
 * @Description:
 * @author :Zhang zhongtao
 * @version: Ver 1.0
 * @Date: 2015-08-07 09:47
 * </pre>
 */
@RequiresUser
@RestController("forumInfoController")
@RequestMapping("/admin/forum/info")
public class ForumInfoController extends BaseController {

    @Resource(name = "forumInfoServiceImpl")
    private ForumInfoService forumInfoService;

    @Resource(name = "doctorProfileServiceImpl")
    private DoctorProfileService doctorProfileService;

    @Resource(name = "organizationServiceImpl")
    private OrganizationService organizationService;

    @RequestMapping(value = "/query", method = RequestMethod.POST)
    public Page<ForumInfo> query(QueryForumInfoData data) {
        Principal principal = getLoginId();
        Organization org = null;
        DoctorProfile doctorProfile = null;

        //根据医生、机构角色获取机构对象
        switch (principal.getUserType()) {
            case doctor:
                doctorProfile = doctorProfileService.find(principal.getId());
                org = doctorProfile.getOrg();

                data.setDid(doctorProfile.getId());
                data.setOid(org == null ? -1L : org.getId());

                break;
            case org:
                doctorProfile = doctorProfileService.find(principal.getId());
                org = doctorProfile.getOrg();
                data.setOid(org == null ? -1L : org.getId());
                break;
            case patient:
                data.setUid(principal.getId());
                break;
            default:
                break;
        }

        return forumInfoService.query(data);
    }

    /**
     * 当前登录人
     *
     * @return Long
     */
    public Principal getLoginId() {
        Subject subject = SecurityUtils.getSubject();
        Principal principal = (Principal) subject.getPrincipal();
        return principal;
    }
}
