package com.chatterbox.chat;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;

@Component
public class CustomSocketHandler extends TextWebSocketHandler {

    public static final Logger LOGGER = LoggerFactory.getLogger(CustomSocketHandler.class);

    List<WebSocketSession> users = new CopyOnWriteArrayList<>();
    Map<String, String> usernameMap = new ConcurrentHashMap<>();

    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        //Invoked after websocket negotiation has succeeded and connection is established
        users.add(session);
        LOGGER.info("Connection Established");
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
        //Invoked after connection is closed or any transport error has occured
        usernameMap.remove(session.getId());
        session.close();
        users.remove(session);
        LOGGER.info("Conection Closed");
    }

    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
        super.handleTextMessage(session, message);
    }


}
