package com.app.mysns.config;

import com.app.mysns.handler.ChatHandler;
import lombok.RequiredArgsConstructor;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;
// import org.springframework.web.socket.server.support.HttpSessionHandshakeInterceptor;

@Configuration
@RequiredArgsConstructor
@EnableWebSocket
public class WebSocketConfig implements WebSocketConfigurer {
    private final ChatHandler chatHandler;

    public WebSocketConfig(ChatHandler chatHandler) {
        this.chatHandler = chatHandler;
    }

    @Override
    // connection을 맺을때 CORS 허용합니다.
    // TODO @유가희 CORS 공부 해야 함
    public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
        // TODO : 로컬 및 도커에서만 접근 가능하게 필요
        registry.addHandler(chatHandler, "/ws")
        // .setAllowedOriginPatterns("localhost:*")
        .setAllowedOriginPatterns("*")
        // .addInterceptors(new HttpSessionHandshakeInterceptor());
        .withSockJS();

        registry.addHandler(chatHandler, "/ws")
        // .setAllowedOriginPatterns("localhost:*")
        .setAllowedOriginPatterns("*");
        // .addInterceptors(new HttpSessionHandshakeInterceptor());
    }
}
