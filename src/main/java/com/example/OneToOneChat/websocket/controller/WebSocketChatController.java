package com.example.OneToOneChat.websocket.controller;

import com.example.OneToOneChat.domain.chatMessage.dto.request.ChatRoomRequest;
import com.example.OneToOneChat.domain.chatMessage.entity.ChatRoom;
import com.example.OneToOneChat.domain.chatMessage.exception.ChatRoomError;
import com.example.OneToOneChat.domain.chatMessage.repository.ChatRoomRepository;
import com.example.OneToOneChat.domain.chatMessage.service.ChatMessageService;
import com.example.OneToOneChat.domain.chatRomm.dto.Request.ChatMessageCreateRequest;
import com.example.OneToOneChat.domain.chatRomm.entity.ChatMessage;
import com.example.OneToOneChat.domain.chatRomm.repository.ChatMessageRepository;
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
@ServerEndpoint("/socket/chatt")
@Slf4j
public class WebSocketChatController {

    private static List<Session> clients = Collections.synchronizedList(new ArrayList<Session>());
    @OnOpen
    public void onOpen(Session session) throws IOException {

        if (!clients.contains(session)) {
            clients.add(session);
            log.info("session open : {}", session);
        } else {
            log.info("이미 연결된 session");
        }
        log.info("open session : {}, clients={}", session.toString(), clients);
    }
   /* @OnMessage
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

        ChatMessageCreateRequest chatMessageCreateRequest = ChatMessageCreateRequest.builder()
                .content(content)
                .writer(writer).build();

*//*        ChatRoom findChatRoom= chatRoomRepository.findById(roomId)
                .orElseThrow(() -> new GlobalException(ChatRoomError.CHAT_ROOM_NOT_FOUND));


        ChatMessage chatMessage = ChatMessage.builder()
                .writer(writer)
                .content(content)
                .chatRoom(findChatRoom)
                .build();
        chatMessageRepository.save(chatMessage);*//*


    }*/

    @OnMessage
    public void onMessage(String message, Session session) throws IOException {
        log.info("receive message : {}", message);

        for (Session s : clients) {
            log.info("send data : {}", message);

            s.getBasicRemote().sendText(message);
        }
    }

    @OnClose
    public void onClose(Session session) {
        log.info("session close : {}", session);

        // 연결 종료 시 빈 소켓 처리 (선택적)
       clients.remove(session);
    }


}
