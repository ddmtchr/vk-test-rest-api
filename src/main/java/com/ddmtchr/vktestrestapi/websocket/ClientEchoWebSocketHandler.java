package com.ddmtchr.vktestrestapi.websocket;

import com.ddmtchr.vktestrestapi.security.services.EchoWebSocketService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.WebSocketMessage;
import org.springframework.web.socket.WebSocketSession;

@Component
public class ClientEchoWebSocketHandler implements WebSocketHandler {
    @Autowired
    private EchoWebSocketService echoWebSocketService;

    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        Sessions.clientSession = session;
        echoWebSocketService.connectEchoServer();
    }

    @Override
    public void handleMessage(WebSocketSession session, WebSocketMessage<?> message) throws Exception {
        Sessions.serverSession.sendMessage(message);
    }

    @Override
    public void handleTransportError(WebSocketSession session, Throwable exception) throws Exception {
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus closeStatus) throws Exception {
    }

    @Override
    public boolean supportsPartialMessages() {
        return false;
    }
}
