package com.example.OneToOneChat.websocket.controller;

import com.example.OneToOneChat.domain.chatMessage.dto.request.ChatRoomRequest;
import com.example.OneToOneChat.domain.chatMessage.service.ChatMessageService;
import com.example.OneToOneChat.domain.chatRomm.dto.Request.ChatMessageCreateRequest;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.websocket.OnClose;
import jakarta.websocket.OnMessage;
import jakarta.websocket.OnOpen;
import jakarta.websocket.Session;
import jakarta.websocket.server.ServerEndpoint;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.*;
import java.util.concurrent.CopyOnWriteArrayList;

@Service
@ServerEndpoint("/socket/chatt")
@Slf4j
public class WebSocketChatController {
    private static List<Session> clients = Collections.synchronizedList(new CopyOnWriteArrayList<>());
    private static ChatMessageService chatMessageService ;


    /**
     * 클라이언트가 접속할 때마다 실행
     * @param session
     */
    @OnOpen
    public void onOpen(Session session) {
        log.info("open session : {}, clients={}", session.toString(), clients);

        synchronized (clients) { // clients 리스트를 동기화 (멀티 스레드 환경에서 안전하게 접근)
            // 새로운 세션 추가
            clients.add(session);
            log.info("session open : {}", session);

            if (clients.size() == 3) { // 연결된 클라이언트 수가 3개이면
                // 2번째 세션 종료
                Session targetSession = clients.get(1); // 2번째 세션 객체 추출
                try {
                    targetSession.close(); // 2번째 세션 종료
                    log.info("session close : {}", targetSession);
                } catch (IOException e) {
                    log.error("Failed to close session", e);
                }
                // 2번째 세션 리스트에서 제거
                clients.remove(1);
            }
        }
    }

    /**
     * 메세지 수신 시
     * @param session
     */
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
        Long roomId = (Long) messageMap.get("roomId");

        ChatMessageCreateRequest chatMessageCreateRequest = ChatMessageCreateRequest.builder()
                .content(content)
                .writer(writer).build();
        chatMessageService.saveMessage(chatMessageCreateRequest,roomId);
    }

    /**
     * 연결을 종료 시
     * @param session
     */
    @OnClose
    public void onClose(Session session) {
        log.info("session close : {}", session);
        clients.remove(session);
    }
}
