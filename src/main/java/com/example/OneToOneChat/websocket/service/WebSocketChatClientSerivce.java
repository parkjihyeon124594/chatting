package com.example.OneToOneChat.websocket.service;

import com.example.OneToOneChat.domain.chatMessage.repository.ChatRoomRepository;
import com.example.OneToOneChat.domain.chatMessage.service.ChatMessageService;
import com.example.OneToOneChat.domain.chatRoom.dto.Request.ChatMessageCreateRequest;
import com.example.OneToOneChat.domain.chatRoom.service.ChatRoomSerivce;
import com.example.OneToOneChat.global.config.ServerEndpointConfigurator;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.annotation.PostConstruct;
import jakarta.servlet.http.HttpSession;
import jakarta.websocket.*;
import jakarta.websocket.server.ServerEndpoint;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@Slf4j
@RequiredArgsConstructor
@ServerEndpoint(value = "/socket/chat/client", configurator = ServerEndpointConfigurator.class)
public class WebSocketChatClientSerivce {

    private final WebSocketChatCounselorService webSocketChatCounselorService;
    private final ChatMessageService chatMessageService;
    private final ChatRoomRepository chatRoomRepository;
    private final ChatRoomSerivce chatRoomSerivce;
    
    private static int currentChatRoom = 0;
    private List<Session> clients;
    private List<Session> counselor;

    @PostConstruct
    private void init() {
        this.clients = WebSocketChatCounselorService.getClients();
        this.counselor = WebSocketChatCounselorService.getCounselor();
    }

    /*
    질문, client라는 객체는 내가 생성한 객체이고, session에 담은 것 뿐임. 근데, client 안에 들어가 있는 session을 닫았다고 해서 아까 접속한 세션인지 어떻게 확인이 가능한거지?
     */
    @OnOpen
    public void onOpen(Session session) throws IOException {
        // 새로운 세션이 들어왔을 때 clients의 사이즈가 1인 경우
        if (clients.size() == 1) {
            Session firstSession = clients.get(0);
            log.info("첫 번째 세션을 닫습니다: {}", firstSession.getId());
            try {
                firstSession.close(new CloseReason(CloseReason.CloseCodes.NORMAL_CLOSURE, "새로운 세션으로 인해 기존 세션을 종료합니다."));
            } catch (IOException e) {
                log.error("Error closing first session: {}", e.getMessage());
            }
        }

        // 새로운 세션을 추가하고 currentChatRoom을 증가시킴
        clients.add(session);
        currentChatRoom++;
       // webSocketChatCounselorService.setChatMessageService(currentChatRoom);
        sendSessionToCounselorChatRoomId();

        log.info("client 현재 세션의 수: {}", clients.size());
    }

    @OnMessage
    public void onMessage(String message, Session session) throws IOException {
        log.info("Client onMessage {}", message);
        sendMessageToCounselor(message);

        try {

            // JSON 파서를 사용하여 객체 해석
            Map<String, Object> messageMap = new ObjectMapper().readValue(message, Map.class);

            String writer = (String) messageMap.get("writer");
            String content = (String) messageMap.get("content");

            Object chatRoomIdObj = messageMap.get("chatRoomId");

            Long roomId = null;

            if (chatRoomIdObj instanceof Map) {
                // chatRoomId가 객체일 경우 id 값을 추출하여 roomId에 넣어줌
                Map<String, Object> chatRoomIdMap = (Map<String, Object>) chatRoomIdObj;
                roomId = Long.parseLong((String) chatRoomIdMap.get("id"));
            } else if (chatRoomIdObj instanceof Integer) {
                // chatRoomId가 정수일 경우 바로 넣어줌
                roomId = ((Integer) chatRoomIdObj).longValue();
            }

            ChatMessageCreateRequest chatMessageCreateRequest = ChatMessageCreateRequest.builder()
                    .content(content)
                    .writer(writer).build();

            chatMessageService.saveMessage(chatMessageCreateRequest, roomId);

        } catch (IOException e) {
            log.error("Error in onMessage: {}", e.getMessage());
            try {
                session.close(new CloseReason(CloseReason.CloseCodes.UNEXPECTED_CONDITION, "Error processing message"));
            } catch (IOException ex) {
                log.error("Error closing session: {}", ex.getMessage());
            }
        }
    }

    @OnClose
    public void onClose(Session session) {

        clients.remove(session);
        log.info("clients session close : {}", session.getId());
    }


    private void sendSessionToCounselorChatRoomId() throws IOException {

        Long allChatNum = (long) chatRoomRepository.findAll().size();
        Long todayChatNum = chatRoomSerivce.readTodayChatRoom();

        Map<String, Object> message = new HashMap<>();
        message.put("type", "chatRoomId");
        message.put("chatRoomId", currentChatRoom);
        message.put("allChatNum", allChatNum);
        message.put("todayChatNum", todayChatNum);

        // 모든 클라이언트에게 JSON 객체 전송
        for (Session s : counselor) {
            if (s.isOpen()) { // 세션이 열려있는지 확인
                s.getBasicRemote().sendText(new ObjectMapper().writeValueAsString(message));
            }
        }
    }

    private void sendMessageToCounselor(String message) throws IOException {

        // 모든 클라이언트에게 JSON 객체 전송
        for (Session s : counselor) {
            if (s.isOpen()) { // 세션이 열려있는지 확인
                s.getBasicRemote().sendText(new ObjectMapper().writeValueAsString(message));
            }
        }
    }


    @OnError
    public void onError(Session session, Throwable throwable) {
        log.error("WebSocket error for session {}: {}", session.getId(), throwable.getMessage());
        if (throwable instanceof IOException) {
            log.error("IO Exception: {}", throwable.getMessage());
        } else {
            log.error("Unexpected error", throwable);
        }
    }
}
