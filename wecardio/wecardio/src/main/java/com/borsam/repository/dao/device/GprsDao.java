package com.borsam.repository.dao.device;

import com.borsam.pojo.forum.QueryForumInfoData;
import com.borsam.repository.entity.device.Gprs;
import com.borsam.repository.entity.forum.ForumInfo;
import com.borsam.repository.entity.patient.PatientProfile;
import com.hiteam.common.base.pojo.search.Page;
import com.hiteam.common.base.repository.dao.BaseDao;

/**
 * <pre>
 * @Description:
 * @author :tantian
 * @version: Ver 1.0
 * @Date: 2015-08-07 09:14
 * </pre>
 */
public interface GprsDao extends BaseDao<Gprs,Long> {

    boolean isBind(PatientProfile patientProfile);

    boolean isImeExist(String ime);
}