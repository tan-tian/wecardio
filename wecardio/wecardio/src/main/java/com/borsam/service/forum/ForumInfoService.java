package com.borsam.service.forum;

import com.borsam.pojo.forum.QueryForumInfoData;
import com.borsam.repository.entity.forum.ForumInfo;
import com.hiteam.common.base.pojo.search.Page;
import com.hiteam.common.base.service.BaseService;

/**
 * <pre>
 * @Description:
 * @author :Zhang zhongtao
 * @version: Ver 1.0
 * @Date: 2015-08-07 09:16
 * </pre>
 */
public interface ForumInfoService extends BaseService<ForumInfo, Long> {
    public Page<ForumInfo> query(QueryForumInfoData data);
}
