package com.app.mysns.config;

import java.util.Map;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.http.HttpSession;

import com.app.mysns.handler.ChatHandler;
// import lombok.RequiredArgsConstructor;
import com.app.mysns.service.JwtService;

import org.springframework.context.annotation.Configuration;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.http.server.ServletServerHttpRequest;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;
// import org.springframework.web.socket.server.support.HttpSessionHandshakeInterceptor;
import org.springframework.web.socket.server.HandshakeInterceptor;

@Configuration
// @RequiredArgsConstructor
@EnableWebSocket
public class WebSocketConfig implements WebSocketConfigurer {
    private final ChatHandler chatHandler;
    private final String regex = "mysns_token=([a-zA-Z0-9_=]+\\.[a-zA-Z0-9_=]+\\.[a-zA-Z0-9_\\-\\+\\/=]*)";

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
        .addInterceptors(new HttpHandshakeInterceptor())
        .withSockJS();

        registry.addHandler(chatHandler, "/ws")
        // .setAllowedOriginPatterns("localhost:*")
        .addInterceptors(new HttpHandshakeInterceptor())
        .setAllowedOriginPatterns("*");
    }


   public class HttpHandshakeInterceptor implements HandshakeInterceptor {

        @Override
        public boolean beforeHandshake(ServerHttpRequest request, ServerHttpResponse response, WebSocketHandler wsHander, Map attributes) {
            try {

                if(request instanceof ServletServerHttpRequest) {
                    ServletServerHttpRequest servletRequest = (ServletServerHttpRequest) request;
                    // HttpSession session = servletRequest.getServletRequest().getSession();

                    // System.out.println("Request : \n" + servletRequest.getHeaders());
                    // servletRequest.getHeaders().Split(",")
                    // Map<String, Object> cookies = servletRequest. .getRequestCookieMap();
                    // cookies = servletRequest.getRequestCookieMap();

                    String line = servletRequest.getHeaders().toString();
                    

                    // Create a Pattern object
                    Pattern r = Pattern.compile(regex);
                    System.out.println(line);

                    // Now create matcher object.
                    Matcher m = r.matcher(line);
                    if (m.find()) {
                        String token = m.group(1);
                        System.out.println(token);
                        JwtService jwtService = new JwtService();
                        String username = jwtService.getUsernameFromToken(token);

                        // profile info into socket
                        attributes.put("username", username);
                        System.out.println("set username : " + username);

                    } else {
                        return true;
                    }

                }
            }
            catch (Exception e) {
                System.out.println(e.getMessage());
            }

            return true;
        }

        @Override
        public void afterHandshake(ServerHttpRequest request, ServerHttpResponse response,
                WebSocketHandler wsHandler, Exception exception) {
            // TODO Auto-generated method stub
            
        }
    }
    

}