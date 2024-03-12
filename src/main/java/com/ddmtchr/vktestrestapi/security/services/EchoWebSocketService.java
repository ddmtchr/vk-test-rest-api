package com.ddmtchr.vktestrestapi.security.services;

import com.ddmtchr.vktestrestapi.websocket.ServerEchoWebSocketHandler;
import org.springframework.stereotype.Service;
import org.springframework.web.socket.client.standard.StandardWebSocketClient;

@Service
public class EchoWebSocketService {
    public void connectEchoServer() throws Exception {
        StandardWebSocketClient webSocketClient = new StandardWebSocketClient();
        String uri = "wss://echo.websocket.org";
        ServerEchoWebSocketHandler handler = new ServerEchoWebSocketHandler();
        webSocketClient.execute(handler, uri).get();
    }
}
