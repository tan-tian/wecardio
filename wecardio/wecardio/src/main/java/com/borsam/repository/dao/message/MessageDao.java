package com.borsam.repository.dao.message;

import com.borsam.pub.UserType;
import com.borsam.repository.entity.message.MessageInfo;
import com.hiteam.common.base.pojo.search.Page;
import com.hiteam.common.base.pojo.search.Pageable;
import com.hiteam.common.base.repository.dao.BaseDao;

import java.util.Date;
import java.util.List;

/**
 * Dao - 消息管理
 * Created by liujieming on 2015/7/20.
 */
public interface MessageDao extends BaseDao<MessageInfo, Long> {

    /**
     * 通过多条件匹配查询
     * @param name
     * @param startDate
     * @param endDate
     * @param type
     * @param org
     * @param pageable
     * @return
     */
    public Page<MessageInfo> queryMessage(String name, Date startDate, Date endDate, MessageInfo.MessageType[] type, Long org,Long objId,UserType objType, Pageable pageable);

}
