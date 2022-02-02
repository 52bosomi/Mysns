package com.app.mysns.handler;

import java.io.IOException;
import java.text.MessageFormat;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

import javax.websocket.OnError;
import javax.websocket.Session;

import com.app.mysns.service.SecureUtilsService;
import com.google.gson.Gson;

import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import lombok.extern.log4j.Log4j2;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Component
@Log4j2
public class ChatHandler extends TextWebSocketHandler {

    private static List<WebSocketSession> list = new ArrayList<WebSocketSession>();
    Map<String, WebSocketSession> clientList = new HashMap<String, WebSocketSession>();
    private static SecureUtilsService secureUtilsService = new SecureUtilsService();

    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
        String payload = message.getPayload();
        System.out.println("payload : " + payload);

        // 어글리한 코드
        List<String> keyList = clientList.keySet().stream().collect(Collectors.toList());

        // 페이로드 안의 데이터로 대상을 지정 해야 함

        for (String key : keyList) {
            if(clientList.get(key) == session) {
                // clientList.remove(key);
                // System.out.println(session + " 클라이언트 접속 해제");
                break;
            }
        }

        for(WebSocketSession sess: list) {
            sess.sendMessage(message);
        }
    }

    /* Client, agent 가 접속 시 호출되는 메서드 */
    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {

        list.add(session);
        

        // 해당 세션에 uuid 전송
        String uuid = secureUtilsService.generateType1UUID().toString();
        String data = MessageFormat.format("'{' \"uuid\" : \"{0}\" '}'", uuid);
        TextMessage msg = new TextMessage(data.getBytes());

        clientList.put(uuid, session);
        session.sendMessage(msg);

        System.out.println(session + " 클라이언트 접속 " + data);
    }

    /* Client, agent 가 접속 해제 시 호출되는 메서드드 */
    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {

        System.out.println(session + " 클라이언트 접속 해제");
        list.remove(session);

        // 어글리한 코드
        List<String> keyList = clientList.keySet().stream().collect(Collectors.toList());

        for (String key : keyList) {
            if(clientList.get(key) == session) {
                clientList.remove(key);
                System.out.println(session + " 클라이언트 접속 해제");
                break;
            }
        }
    }

    @OnError
    public void onError(Session session, Throwable throwable) {
        System.out.println("Error for " + session.getId() + " caused by: " + throwable.getMessage());
        throwable.printStackTrace();
    }
}