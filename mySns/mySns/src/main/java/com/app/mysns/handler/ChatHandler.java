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

        // cmd ?????? ??????
        // TODO : db ???????????? ???????????? ?????? ?????????????????? ?????? ?????? ???
        if(data.getCmd().startsWith("new") && data.getFrom().startsWith("agent")) {

            for (String key : clientList.keySet().stream().collect(Collectors.toList())) {
                if(clientList.get(key) == session) {
                    // ????????? ???????????? ????????? ???????????? ???????????? ??????!!
                    clientList.remove(key);
                    agentList.put(data.getAgentUUID(), session);
                    agentStatus.put(data.getAgentUUID(), false); // ???????????? ??????
                    TextMessage msg = new TextMessage("{ \"cmd\" : \"upgrade\", \"from\" : \"server\" }".getBytes());
                    session.sendMessage(msg);
                    logger.info("send to agent about successful agent upgraded");
                    break;
                }
            }
            // ???????????? ????????? ??????
            logger.info(MessageFormat.format("{0} : upgrade to agent, {1}", session, payload));

            return;

        }
        
        if(data.getCmd().startsWith("result") && data.getFrom().startsWith("agent")) {
            // ????????????
            Boolean isAllowed = false;

            for (String key : agentList.keySet().stream().collect(Collectors.toList())) {
                if(agentList.get(key) == session) { isAllowed = true; break; }
            }

            if(!isAllowed) { return; }

            // ???????????? ?????? ????????????
            agentStatus.put(data.getAgentUUID(), false);

            // 1. ?????? DB ??????
            // TODO : ???????????? ?????? ?????? ????????? ????????? DB??? ??? ??????!!!!!!!
            // ClientDto clientdto = this.authService.findByUsername(data.getUsername());
            if(data.getUserId() > 0) {
                for (HashMap<String, String> site : data.getResult()) {

                    // ????????? ?????? ????????? ???
                    SnsTypeDto siteTypeDto = this.dao.FindSnsTypeByName(data.getType());
                    SyncSiteDto syncSiteDto = new SyncSiteDto(data.getUserId(), siteTypeDto.getIdx());
                    
                    syncSiteDto.setDesecription(site.get("title"));
                    syncSiteDto.setUrl(site.get("url"));
                }
                
            }
            
            // 2. ?????? ?????? ??????????????? ??????
            if(data.getClientUUID().length() > 0) {
                for (String key : clientList.keySet().stream().collect(Collectors.toList())) {

                    if(key.equals(data.getClientUUID())) { 
                        TextMessage msg = new TextMessage(MessageFormat.format("'{' \"result\" : {0} '}'", data.ToJson()).getBytes("EUC_KR"));
                        clientList.get(key).sendMessage(msg);
                        break;
                    }
                }
            }


            // ???????????? ??????
            logger.info(MessageFormat.format("{0} : get result from agent, {1}", session, payload));

            return;
        }

        // ????????? ????????? ?????? ??????!!!
        // ???????????? ???????????? ????????? ??????, ???????????? ????????? ??????????????? ?????? ????????????????????? ????????? ???!!!
        // ?????? ??????!!!!!

        // ?????? ??????
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

                // ?????? ??????
                data.setCmd("scraping");
                data.setFrom("client");
                data.setClientUUID(key); // ????????? ?????? ?????? ???
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

                break; // for ??? ?????????, ?????? ?????? ???????????? ?????????
                
            }
        }

    }

    /* Client, agent ??? ?????? ??? ???????????? ????????? */
    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {

        // ?????? ????????? uuid ??????
        String uuid = secureUtilsService.generateType1UUID().toString();
        String data = MessageFormat.format("'{' \"uuid\" : \"{0}\" '}'", uuid);
        TextMessage msg = new TextMessage(data.getBytes());

        // ?????? ????????? ?????? ????????? ???????????? ??????, ?????? ??????????????? ??????????????? ?????? ??????
        clientList.put(uuid, session);
        session.sendMessage(msg);

        logger.info(MessageFormat.format("{0} : client connected!!, {1}", session, data));

    }

    /* Client, agent ??? ?????? ?????? ??? ???????????? ????????? */
    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {

        // ???????????? ??????
        // 1. ??????????????? ??????
        List<String> keyList = clientList.keySet().stream().collect(Collectors.toList());

        for (String key : keyList) {
            if(clientList.get(key) == session) {
                clientList.remove(key);
                logger.info(MessageFormat.format("{0} : client disconnected!!", session));
                return;
                // break;
            }
        }

        // 2. ?????????????????? ??????
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