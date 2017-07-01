package com.borsam.repository.dao.forum;

import com.borsam.pojo.forum.QueryForumInfoData;
import com.borsam.repository.entity.forum.ForumInfo;
import com.hiteam.common.base.pojo.search.Page;
import com.hiteam.common.base.repository.dao.BaseDao;

/**
 * <pre>
 * @Description:
 * @author :Zhang zhongtao
 * @version: Ver 1.0
 * @Date: 2015-08-07 09:14
 * </pre>
 */
public interface ForumInfoDao extends BaseDao<ForumInfo,Long> {
    public Page<ForumInfo> query(QueryForumInfoData data);
}
