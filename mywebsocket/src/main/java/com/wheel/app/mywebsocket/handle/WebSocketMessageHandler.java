package com.wheel.app.mywebsocket.handle;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArraySet;

/**
 * @Description
 * @Author zifu.li@hand-china.com
 * @Date 2020/8/23 15:03
 * @Version 1.0
 */
@Component
public class WebSocketMessageHandler extends TextWebSocketHandler {

    private final static Logger logger = LoggerFactory.getLogger(WebSocketMessageHandler.class);

    private static Map<String, CopyOnWriteArraySet<WebSocketSession>> clients = new ConcurrentHashMap<>();


    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        Object token = session.getAttributes().get("token");
        if (null != token){
            CopyOnWriteArraySet<WebSocketSession> socketSessions = new CopyOnWriteArraySet<>();
            socketSessions.add(session);
            clients.put(token.toString(),socketSessions);
        }else{
            logger.error("用户登录过期");
        }
    }

    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
        super.handleTextMessage(session, message);
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
        Object token = session.getAttributes().get("token");

        if (!CollectionUtils.isEmpty(clients.get(token.toString()))){
            clients.get(token.toString()).remove(session);
            if (clients.get(token.toString()).size() == 0) {
                clients.remove(token.toString());
            }
        } else {
            clients.remove(token.toString());
        }
    }
}
