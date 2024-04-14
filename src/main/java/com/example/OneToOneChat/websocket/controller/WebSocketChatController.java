package com.example.OneToOneChat.websocket.controller;

import com.example.OneToOneChat.domain.chatMessage.dto.request.ChatRoomRequest;
import com.example.OneToOneChat.domain.chatMessage.entity.ChatRoom;
import com.example.OneToOneChat.domain.chatMessage.exception.ChatRoomError;
import com.example.OneToOneChat.domain.chatMessage.repository.ChatRoomRepository;
import com.example.OneToOneChat.domain.chatMessage.service.ChatMessageService;
import com.example.OneToOneChat.domain.chatRomm.dto.Request.ChatMessageCreateRequest;
import com.example.OneToOneChat.domain.chatRomm.entity.ChatMessage;
import com.example.OneToOneChat.domain.chatRomm.repository.ChatMessageRepository;
import com.example.OneToOneChat.global.config.ServerEndpointConfigurator;
import com.example.OneToOneChat.global.exception.GlobalException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.websocket.*;
import jakarta.websocket.server.ServerEndpoint;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.*;
import java.util.concurrent.CopyOnWriteArrayList;

@Service
@ServerEndpoint(value = "/socket/chatt",configurator = ServerEndpointConfigurator.class)
@Slf4j
@RequiredArgsConstructor
public class WebSocketChatController {

    private static final int MAX_CLIENTS = 3;
    private final ChatMessageService chatMessageService;
    private static List<Session> clients = Collections.synchronizedList(new ArrayList<Session>());
    @OnOpen
    public void onOpen(Session session) throws IOException {
 /*       if (clients.size() >= MAX_CLIENTS) {
            // 최대 연결 수 초과 시 기존 2개의 연결 제거
            for (int i = 0; i < 2; i++) {
                Session removeSession = clients.get(i);
                clients.remove(removeSession);
                log.info("최대 연결 수 초과! 기존 세션 제거 : {}", removeSession);
            }
        }
*/
        sendSessionCountToAllClients();

        if (!clients.contains(session)) {
            clients.add(session);
            log.info("session open : {}", session);
        } else {
            log.info("이미 연결된 session");
        }
        log.info("open session : {}, clients={}", session.toString(), clients);

        log.info("현재 접속 중인 세션의 size{}",clients.size());

    }

    private void sendSessionCountToAllClients() throws IOException {
        // 현재 세션 크기 정보를 담은 JSON 객체 생성
        Map<String, Object> message = new HashMap<>();
        message.put("connectedClients", clients.size());

        // 모든 클라이언트에게 JSON 객체 전송
        for (Session s : clients) {
            s.getBasicRemote().sendText(new ObjectMapper().writeValueAsString(message));
        }
    }
    @OnMessage
    public void onMessage(String message, Session session) throws IOException {
        log.info("receive message : {}", message);

        for (Session s : clients) {
            log.info("send data : {}", message);
            s.getBasicRemote().sendText(message);
        }

        // JSON 파서를 사용하여 객체 해석
        Map<String, Object> messageMap = new ObjectMapper().readValue(message, Map.class);

        String writer = (String) messageMap.get("writer");
        String content = (String) messageMap.get("content");
        Long roomId =  Long.parseLong((String) messageMap.get("roomId"));
        log.info("room id test {}",roomId);

        ChatMessageCreateRequest chatMessageCreateRequest = ChatMessageCreateRequest.builder()
                .content(content)
                .writer(writer).build();

        chatMessageService.saveMessage(chatMessageCreateRequest,roomId);

    }


    @OnClose
    public void onClose(Session session) {
        log.info("session close : {}", session);

        // 연결 종료 시 빈 소켓 처리 (선택적)
       clients.remove(session);
    }


}
