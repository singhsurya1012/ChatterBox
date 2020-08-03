package com.chatterbox.chat;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;

@Component
public class CustomSocketHandler extends TextWebSocketHandler {

    public static final Logger LOGGER = LoggerFactory.getLogger(CustomSocketHandler.class);
    public static final String secretKey = "$%^&";

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
    protected void handleTextMessage(WebSocketSession session, TextMessage message) throws IOException {
        //Invoked when a message arrives
        if (message.getPayload().contains(secretKey)) {
            String name = message.getPayload().replace(secretKey, "");
            usernameMap.put(session.getId(), name);
        } else {

            //Sending users message
            users.stream()
                    .filter(WebSocketSession::isOpen)
                    .forEach(webSocketSession -> {
                        try {
                            //Sending actual message to all users
                            webSocketSession.sendMessage(message);

                            //Updating list of people online
                            String onlineUsers = "@#$:" + StringUtils.join(usernameMap.values(), ",");
                            webSocketSession.sendMessage(new TextMessage(onlineUsers));
                        } catch (IOException e) {
                            LOGGER.info("Error occured while sending message");
                        }
                    });


        }
    }


}
