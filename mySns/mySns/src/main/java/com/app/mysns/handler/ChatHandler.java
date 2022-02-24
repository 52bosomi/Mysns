package com.app.mysns.handler;

import java.io.IOException;
import java.text.MessageFormat;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

import javax.websocket.OnError;
import javax.websocket.Session;

import com.app.mysns.dao.ManageDao;
import com.app.mysns.dto.ClientDto;
import com.app.mysns.dto.SnsTypeDto;
import com.app.mysns.dto.SyncSiteDto;
import com.app.mysns.dto.WebSocketDto;
import com.app.mysns.service.AuthService;
import com.app.mysns.service.SecureUtilsService;
import com.google.gson.Gson;

import org.springframework.beans.factory.annotation.Autowired;
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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Component
// @Log4j2
public class ChatHandler extends TextWebSocketHandler {

    // private static List<WebSocketSession> list = new ArrayList<WebSocketSession>();
    Map<String, Boolean> agentStatus = new HashMap<String, Boolean>(); // true is working
    Map<String, WebSocketSession> clientList = new HashMap<String, WebSocketSession>();
    Map<String, WebSocketSession> agentList = new HashMap<String, WebSocketSession>();
    private static SecureUtilsService secureUtilsService = new SecureUtilsService();
    private final Logger logger = LoggerFactory.getLogger(ChatHandler.class);
    @Autowired
    private AuthService authService;
    @Autowired
    private ManageDao dao;


    public ChatHandler(AuthService authService, ManageDao dao) {
        this.authService = authService;
        this.dao = dao;
    }

    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
        String payload = message.getPayload();
        WebSocketDto data = null;

        try {
            data = WebSocketDto.ToData(payload, WebSocketDto.class);
        } catch (Exception e) { logger.error(e.getMessage()); }

        if(data == null) { return; }

        // cmd 인지 확인
        // TODO : db 매칭해서 인지하고 있는 에이전트인지 확인 해야 함
        if(data.getCmd().startsWith("new") && data.getFrom().startsWith("agent")) {

            for (String key : clientList.keySet().stream().collect(Collectors.toList())) {
                if(clientList.get(key) == session) {
                    // 사용자 소켓에서 지우고 에이전트 소켓으로 상승!!
                    clientList.remove(key);
                    agentList.put(data.getAgentUUID(), session);
                    agentStatus.put(data.getAgentUUID(), false); // 스크래핑 가능
                    TextMessage msg = new TextMessage("{ \"cmd\" : \"upgrade\", \"from\" : \"server\" }".getBytes());
                    session.sendMessage(msg);
                    logger.info("send to agent about successful agent upgraded");
                    break;
                }
            }
            // 에이전트 시작시 등록
            logger.info(MessageFormat.format("{0} : upgrade to agent, {1}", session, payload));

            return;

        }
        
        if(data.getCmd().startsWith("result") && data.getFrom().startsWith("agent")) {
            // 에이전트
            Boolean isAllowed = false;

            for (String key : agentList.keySet().stream().collect(Collectors.toList())) {
                if(agentList.get(key) == session) { isAllowed = true; break; }
            }

            if(!isAllowed) { return; }

            // 에이전트 상태 업데이트
            agentStatus.put(data.getAgentUUID(), false);

            // 1. 결과 DB 저장
            // TODO : 누구인지 알수 있는 방법이 없어서 DB에 못 넣음!!!!!!!
            // ClientDto clientdto = this.authService.findByUsername(data.getUsername());
            if(data.getUserId() > 0) {
                for (HashMap<String, String> site : data.getResult()) {

                    // 여기서 정보 받아야 함
                    SnsTypeDto siteTypeDto = this.dao.FindSnsTypeByName(data.getType());
                    SyncSiteDto syncSiteDto = new SyncSiteDto(data.getUserId(), siteTypeDto.getIdx());
                    
                    syncSiteDto.setDesecription(site.get("title"));
                    syncSiteDto.setUrl(site.get("url"));
                }
                
            }
            
            // 2. 결과 해당 사용자에게 전달
            if(data.getClientUUID().length() > 0) {
                for (String key : clientList.keySet().stream().collect(Collectors.toList())) {

                    if(key.equals(data.getClientUUID())) { 
                        TextMessage msg = new TextMessage(MessageFormat.format("'{' \"result\" : {0} '}'", data.ToJson()).getBytes("EUC_KR"));
                        clientList.get(key).sendMessage(msg);
                        break;
                    }
                }
            }


            // 에이전트 결과
            logger.info(MessageFormat.format("{0} : get result from agent, {1}", session, payload));

            return;
        }

        // 사용자 요청시 처리 로직!!!
        // 에이전트 이외에는 메세지 전달, 사용자가 보내는 메세지들로 전부 에이전트에게만 보내야 함!!!
        // 보안 이슈!!!!!

        // 작업 생성
        ArrayList<WebSocketDto> taskQueue = new ArrayList<WebSocketDto>();
        List<String> keyList = clientList.keySet().stream().collect(Collectors.toList());
        for (String key : keyList) {
            if(clientList.get(key) == session) {

                Map<String, Object> info = session.getAttributes();
                if(info.size() < 1) { break; }

                String username = info.get("username").toString();
                ClientDto clientDto = dao.FindClientByUsername(username);
                if(clientDto.getIdx() < 1) {
                    break;
                }

                // 작업 빌드
                data.setCmd("scraping");
                data.setFrom("client");
                data.setClientUUID(key); // 나중에 회신 줘야 함
                data.setUserId(clientDto.getIdx());

                taskQueue.add(data);
                break;
            }
        }


        for (WebSocketDto job : taskQueue) {
            for (String key : agentList.keySet().stream().collect(Collectors.toList())) {
                
                // alerady working
                if(agentStatus.get(key) == true) { 
                    logger.info(MessageFormat.format("already working, {0}", key));
                    continue;
                }

                TextMessage msg = new TextMessage(job.ToJson().getBytes());

                agentList.get(key).sendMessage(msg);
                agentStatus.put(key, true);
                logger.info(MessageFormat.format("working request to {0}", key));

                break; // for 문 나가기, 다른 작업 수행해야 하니까
                
            }
        }

    }

    /* Client, agent 가 접속 시 호출되는 메서드 */
    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {

        // 해당 세션에 uuid 전송
        String uuid = secureUtilsService.generateType1UUID().toString();
        String data = MessageFormat.format("'{' \"uuid\" : \"{0}\" '}'", uuid);
        TextMessage msg = new TextMessage(data.getBytes());

        // 처음 등록시 전부 사용자 소켓으로 저장, 이후 에이전트는 업데이트는 분리 예정
        clientList.put(uuid, session);
        session.sendMessage(msg);

        logger.info(MessageFormat.format("{0} : client connected!!, {1}", session, data));

    }

    /* Client, agent 가 접속 해제 시 호출되는 메서드 */
    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {

        // 어글리한 코드
        // 1. 사용자에서 제거
        List<String> keyList = clientList.keySet().stream().collect(Collectors.toList());

        for (String key : keyList) {
            if(clientList.get(key) == session) {
                clientList.remove(key);
                logger.info(MessageFormat.format("{0} : client disconnected!!", session));
                return;
                // break;
            }
        }

        // 2. 에이전트에서 제거
        keyList = agentList.keySet().stream().collect(Collectors.toList());
            
        for (String key : keyList) {
            if(agentList.get(key) == session) {
                agentList.remove(key);
                agentStatus.remove(key);
                logger.info(MessageFormat.format("{0} : agent disconnected!!", session));
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