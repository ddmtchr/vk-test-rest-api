package com.ddmtchr.vktestrestapi.config;

import com.ddmtchr.vktestrestapi.websocket.ClientEchoWebSocketHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;

@Configuration
@EnableWebSocket
public class WebSocketConfig implements WebSocketConfigurer {
    @Override
    public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
        registry.addHandler(echoHandler(), "/ws").setAllowedOrigins("*");
    }

    @Bean
    WebSocketHandler echoHandler() {
        return new ClientEchoWebSocketHandler();
    }
}
