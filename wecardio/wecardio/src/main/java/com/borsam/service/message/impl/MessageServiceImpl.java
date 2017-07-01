package com.borsam.service.message.impl;

import com.borsam.pub.UserType;
import com.borsam.repository.dao.message.MessageDao;
import com.borsam.repository.dao.message.impl.MessageDaoImpl;
import com.borsam.repository.entity.message.MessageInfo;
import com.borsam.service.message.MessageService;
import com.hiteam.common.base.pojo.search.Page;
import com.hiteam.common.base.pojo.search.Pageable;
import com.hiteam.common.base.service.impl.BaseServiceImpl;
import org.springframework.stereotype.Repository;
import javax.annotation.Resource;
import java.util.Date;

/**
 * Dao - 消息管理
 * Created by liujieming on 2015/7/20.
 */
@Repository("messageServiceImpl")
public class MessageServiceImpl extends BaseServiceImpl<MessageInfo, Long> implements MessageService {

    @Resource(name = "messageDaoImpl")
    private MessageDao messageDao;

    @Resource(name = "messageDaoImpl")
    public void setBaseDao(MessageDaoImpl messageDaoImpl) {
        super.setBaseDao(messageDaoImpl);
    }


    /**
     * 通过多条件匹配查询
     *
     * @param name
     * @param startDate
     * @param endDate
     * @param type
     * @param org
     * @param objId
     * @param objType
     * @param pageable  @return
     */
    @Override
    public Page<MessageInfo> queryMessage(String name, Date startDate, Date endDate, MessageInfo.MessageType[] type, Long org, Long objId, UserType objType, Pageable pageable) {
        return messageDao.queryMessage(name, startDate, endDate, type, org, objId, objType, pageable);
    }
}
