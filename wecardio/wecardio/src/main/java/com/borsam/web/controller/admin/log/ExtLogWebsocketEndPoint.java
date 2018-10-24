package com.borsam.web.controller.admin.log;

import com.borsam.pojo.log.Info;
import com.hiteam.common.util.collections.MapUtil;
import com.hiteam.common.util.json.JsonUtils;
import com.hiteam.common.util.lang.StringUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * <pre>
 * @Description:
 * @author :tantian
 * @version: Ver 1.0
 * @Date: 2015-08-22 19:15
 * </pre>
 */
@Component("extLogWebsocketEndPoint")
public class ExtLogWebsocketEndPoint extends TextWebSocketHandler {
    private final Logger logger = LoggerFactory.getLogger(ExtLogWebsocketEndPoint.class);

    private static final List<WebSocketSession> users = new CopyOnWriteArrayList<>();

    private static final String KEY_LOG_LEVEL = "level";
    private static final String KEY_LOG_NAME = "logger";
    private static final String KEY_LOG_KEYWORD = "keyWord";
    private static final String KEY_LOG_USER = "userName";
    private static final String KEY_FILTER = "$Filter";

    /**
     * 是否开启日志自动推送
     */
    private Boolean enablePush = false;

    public Boolean getEnablePush() {
        return enablePush;
    }

    public void setEnablePush(Boolean enablePush) {
        this.enablePush = enablePush;
    }

    @Override
    protected void handleTextMessage(WebSocketSession session,
                                     TextMessage message) throws Exception {
        super.handleTextMessage(session, message);
        TextMessage returnMessage = new TextMessage(message.getPayload() + " received at server");

        Map map = JsonUtils.toObject(message.getPayload(), Map.class);

        addFilter(session, map);

        if (map.containsKey("testMessage")) {
            session.sendMessage(returnMessage);
        }
    }

    /**
     * 新增过滤关键字
     * @param session WebSocketSession
     * @param params 客户端发送的参数
     */
    private void addFilter(WebSocketSession session, Map params) {
        Map $Filter = MapUtil.getMap(session.getAttributes(), KEY_FILTER, new HashMap<>());

        if (params.containsKey(KEY_LOG_LEVEL)) {
            $Filter.put(KEY_LOG_LEVEL, params.get(KEY_LOG_LEVEL));
        }

        if (params.containsKey(KEY_LOG_NAME)) {
            $Filter.put(KEY_LOG_NAME, params.get(KEY_LOG_NAME));
        }

        if (params.containsKey(KEY_LOG_KEYWORD)) {
            $Filter.put(KEY_LOG_KEYWORD, params.get(KEY_LOG_KEYWORD));
        }

        if (params.containsKey(KEY_LOG_USER)) {
            $Filter.put(KEY_LOG_USER, params.get(KEY_LOG_USER));
        }

        session.getAttributes().put(KEY_FILTER, $Filter);
    }

    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        users.add(session);
        setEnablePush(true);
    }

    @Override
    public void handleTransportError(WebSocketSession session, Throwable exception) throws Exception {
        if (session.isOpen()) {
            session.close();
        }
        users.remove(session);

        setPushStatus();
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus closeStatus) throws Exception {
        users.remove(session);
        setPushStatus();
    }

    private void setPushStatus() {
        if (users.size() == 0) {
            setEnablePush(false);
        }
    }

    @Override
    public boolean supportsPartialMessages() {
        return false;
    }

    public void sendAll(Info info) {
        if (users.size() == 0) {
            return;
        }

        for (WebSocketSession session : users) {
            try {
                if (session.isOpen()) {

                    if (filterLog(session, info)) {
                        continue;
                    }

                    TextMessage returnMessage = new TextMessage(JsonUtils.toJson(info));
                    session.sendMessage(returnMessage);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 过滤日志
     * @param session WebSocketSession
     * @param info 日志信息
     * @return true:未包含关键字；false：包含关键字
     */
    private Boolean filterLog(WebSocketSession session, Info info) {
        Boolean result = false;

        Map attr =  MapUtil.getMap(session.getAttributes(), KEY_FILTER, new HashMap<>());

        //过滤日志级别
        if (attr.containsKey(KEY_LOG_LEVEL)) {

            String level = MapUtil.getString(attr, KEY_LOG_LEVEL, "");

            if (StringUtil.isBlank(level) || StringUtil.equalsIgnoreCase(level,"ALL")){
                result = false;
            }  else if (!StringUtil.equalsIgnoreCase(info.getLevel().levelStr, level)) {
                result = true;
            }
        }

        //日志名称
        if (attr.containsKey(KEY_LOG_NAME) && !result) {
            String logger = MapUtil.getString(attr, KEY_LOG_NAME, "");
            if (StringUtil.isBlank(logger) || StringUtil.equalsIgnoreCase(logger, "ALL")) {
                result = false;
            } else if (!StringUtil.equalsIgnoreCase(info.getLogger(), logger)) {
                result = true;
            }
        }

        //关键字
        if (attr.containsKey(KEY_LOG_KEYWORD) && !result) {
            String keyWord = MapUtil.getString(attr, KEY_LOG_KEYWORD, "");
            if (StringUtil.isBlank(keyWord)) {
                result = false;
            } else if (!StringUtil.contains(info.getMessage(), keyWord)) {
                result = true;
            }
        }

        //用户名
        if (attr.containsKey(KEY_LOG_USER) && !result) {
            String userName = MapUtil.getString(attr, KEY_LOG_USER, "");
            if (StringUtil.isBlank(userName) || StringUtil.contains(info.getUsrName(), userName)) {
                result = false;
            } else {
                result = true;
            }
        }

        return result;
    }
}
