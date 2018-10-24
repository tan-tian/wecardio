package com.borsam.service.forum.impl;

import com.borsam.pojo.forum.QueryForumInfoData;
import com.borsam.repository.dao.forum.ForumInfoDao;
import com.borsam.repository.dao.org.OrganizationDao;
import com.borsam.repository.dao.patient.PatientProfileDao;
import com.borsam.repository.entity.forum.ForumInfo;
import com.borsam.repository.entity.org.Organization;
import com.borsam.repository.entity.patient.PatientProfile;
import com.borsam.service.forum.ForumInfoService;
import com.hiteam.common.base.pojo.search.Page;
import com.hiteam.common.base.service.impl.BaseServiceImpl;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * <pre>
 * @Description:
 * @author :tantian
 * @version: Ver 1.0
 * @Date: 2015-08-07 09:16
 * </pre>
 */
@Service("forumInfoServiceImpl")
public class ForumInfoServiceImpl extends BaseServiceImpl<ForumInfo, Long> implements ForumInfoService {
    @Resource(name = "forumInfoDaoImpl")
    private ForumInfoDao forumInfoDao;

    @Resource(name = "patientProfileDaoImpl")
    private PatientProfileDao patientProfileDao;

    @Resource(name = "organizationDaoImpl")
    private OrganizationDao organizationDao;

    @Resource(name = "forumInfoDaoImpl")
    public void setBaseDao(ForumInfoDao forumInfoDao) {
        super.setBaseDao(forumInfoDao);
    }

    @Override
    public Page<ForumInfo> query(QueryForumInfoData data) {
        Page<ForumInfo> forumInfoPage = forumInfoDao.query(data);

        if (forumInfoPage.getTotalPages() > 0) {
            List<ForumInfo> infoList = forumInfoPage.getContent();
            //患者ID
            List<Long> uIds = infoList.stream().parallel().map(ForumInfo::getCreateId).collect(Collectors.toList());
            //患者对象
            List<PatientProfile> profiles = patientProfileDao.findList(uIds);
            //机构ID
            List<Long> oIds = infoList.stream().parallel().map(ForumInfo::getOid).collect(Collectors.toList());
            //机构对象
            List<Organization> organizations = organizationDao.findList(oIds);

            infoList.stream().parallel().forEach(forumInfo -> {

                //region 设置患者名称及图像地址
                Optional<PatientProfile> firstPatient = profiles.stream().parallel()
                        .filter(patientProfile -> Objects.equals(forumInfo.getCreateId(), patientProfile.getId()))
                        .findFirst();

                if (!firstPatient.equals(Optional.empty())) {
                    PatientProfile patientProfile = firstPatient.get();

                    forumInfo.setCreateName(patientProfile.getName());
                    forumInfo.setCreateIdHeadPath(patientProfile.getHeadPath());
                }

                //endregion

                //region 设置机构名称
                Optional<Organization> firstOrg = organizations.stream().parallel()
                        .filter(org -> Objects.equals(forumInfo.getOid(), org.getId()))
                        .findFirst();

                if (!firstOrg.equals(Optional.empty())) {
                    Organization organization = firstOrg.get();
                    forumInfo.setOrgName(organization.getName());
                }

                //endregion

            });


        }


        return forumInfoPage;
    }
}
