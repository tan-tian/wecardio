package com.borsam.repository.dao.forum.impl;

import com.borsam.pojo.forum.QueryForumInfoData;
import com.borsam.repository.dao.forum.ForumInfoDao;
import com.borsam.repository.entity.forum.ForumInfo;
import com.hiteam.common.base.pojo.search.Page;
import com.hiteam.common.base.repository.dao.impl.BaseDaoImpl;
import com.hiteam.common.util.lang.ArrayUtil;
import com.hiteam.common.util.lang.DateUtil;
import com.hiteam.common.util.lang.StringUtil;
import org.springframework.stereotype.Repository;

import java.util.Arrays;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

/**
 * <pre>
 * @Description: 评价管理
 * @author :tantian
 * @version: Ver 1.0
 * @Date: 2015-08-07 09:15
 * </pre>
 */
@Repository("forumInfoDaoImpl")
public class ForumInfoDaoImpl extends BaseDaoImpl<ForumInfo, Long> implements ForumInfoDao {
    @Override
    public Page<ForumInfo> query(QueryForumInfoData data) {
        StringBuilder jql = new StringBuilder("select t from ForumInfo t where 1=1");
        Map params = new HashMap();

        if (StringUtil.isNotBlank(data.getContent())) {
            jql.append(" and t.content = :content ");
            params.put("content",data.getContent());
        }

        if (data.getOid() != null) {
            jql.append(" and t.oid IN :oid ");
            params.put("oid",data.getOid());
        }

        if (data.getDid() != null) {
            jql.append(" and t.did = :did ");
            params.put("did",data.getDid());
        }

        if (data.getUid() != null) {
            jql.append(" and t.createId = :createId ");
            params.put("createId", data.getUid());
        }

        if (ArrayUtil.isNotEmpty(data.getScores())) {
            jql.append(" and t.score in (:score) ");
            params.put("score", Arrays.asList(data.getScores()));
        }

        if (data.getCreatedStart() != null) {
            jql.append(" and t.created >= :startTime ");
            params.put("startTime", data.getCreatedStart().getTime() / 1000);
        }

        if (data.getCreatedEnd() != null) {
            jql.append(" and t.created <= :endTime ");
            Calendar calendar = DateUtil.toCalendar(data.getCreatedEnd());
            calendar.set(Calendar.HOUR_OF_DAY, 23);
            calendar.set(Calendar.MINUTE, 59);
            calendar.set(Calendar.SECOND, 59);
            params.put("endTime", calendar.getTimeInMillis() / 1000);
        }

        return this.findPageByJql(jql.toString(), params, data, ForumInfo.class);
    }
}
