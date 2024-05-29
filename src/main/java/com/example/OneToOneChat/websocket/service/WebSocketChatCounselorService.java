package com.example.OneToOneChat.websocket.service;

import com.example.OneToOneChat.domain.chatMessage.service.ChatMessageService;
import com.example.OneToOneChat.domain.chatRoom.dto.Request.ChatMessageCreateRequest;
import com.example.OneToOneChat.global.config.ServerEndpointConfigurator;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.websocket.*;
import jakarta.websocket.server.ServerEndpoint;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.*;

@Service
@ServerEndpoint(value = "/socket/chat/counselor", configurator = ServerEndpointConfigurator.class)
@Slf4j
@RequiredArgsConstructor
public class WebSocketChatCounselorService {

    private final ChatMessageService chatMessageService;
    private static int currentChatRoom = 0;
    private static List<Session> clients = Collections.synchronizedList(new ArrayList<Session>());
    private static List<Session> counselor = Collections.synchronizedList(new ArrayList<Session>());

    public static List<Session> getClients() {
        return clients;
    }

    public static List<Session> getCounselor() {
        return counselor;
    }

    public void setChatMessageService(int chatRoom) {
        currentChatRoom = chatRoom;
    }

    //1.세션이란?
    //http 요청을 보냈을 때, 응답을 받고 끝내는 것이 아니라 요청을 보낸 순간부터 웹 브라우저를 종료할 떄까지 연결을 유지하는 것

    //2.이떄, 스프링의 http 요청은 개별적인 쓰레드로 처리를 함. 즉, 개별적인 메모리를 가지고 있어 서로서로 접근이 불가능함.
    //3.Collection 인스턴스 동기화가 필요함
    //=>2개 이상의 쓰레드가 컬렉션에 동시에 접근한다는 가정이 필요할 때, 동기화를 해주는 것.즉, 같은 인스턴스에 접근해서 참조하는 것
    //Collections.synchronizedList를 사용해서 Collection 인스턴스 동기화가 가능함.


    /**
     * 1.세션 연결 => client 인지 counselor인지 확인
     * client인지 counselor인지 어떻게 확인할 수 있을까?
     * 2.만약, counselor라면?
     * 1.counselor 리스트에 size가 1이상이면, 새로운 세션이 들어오지 못하게 연결을 바로 끊음
     * 2.counselor 리스트에 size가 1미만이라면, 새로운 세션이 들어오고 counselor size를 1증가시킴
     * <p>
     * 3.만약, client라면?
     * 1.clients 리스트에 size가 1이상이면, 기존의 세션을 연결을 끊고 + client 리스트에 기존의 세션을 삭제함 + client 리스트에 새로운 세션을 저장함
     * 2.clients 리스트에 size가 1미만이면, 세션을 연결 + client 리스트에 기존의 세션 더함.
     * <p>
     * 4.그렇다면, chatRoomId는 어떻게 할것인가?
     * 1.chatRoomId가 만들어지는 시기 onOpen(세션이 연결될떄마다)의 size가 2라면 chatRoomService.saveChatRoom를 호출
     * 2.CurrentChatRoom = chatRoomService.saveChatRoom로 받아서, 서버에서 chatRoom을 계속 관리함.
     * <p>
     * 5.프론트에서 chatRoomID를 받아오는것이 아니라, 서버에서 관리함.
     * => 이렇게되면 서버는 stateless라는 특성이 지켜지지 않음.
     * <p>
     * 그러므로, 프론트에서 관리하도록 하는 것임 옳은듯.
     */


    @OnOpen
    public void onOpen(Session session) {
        counselor.add(session);

/*        try {
            sendSessionCountToAllClients();

            if (!clients.contains(session)) {
                clients.add(session);
                log.info("session open : {}", session);
            } else {
                log.info("이미 연결된 session");
            }
            log.info("open session : {}, clients={}", session.toString(), clients);
            log.info("현재 접속 중인 세션의 size{}", clients.size());
        } catch (IOException e) {
            log.error("Error in onOpen: {}", e.getMessage());
            try {
                session.close(new CloseReason(CloseReason.CloseCodes.UNEXPECTED_CONDITION, "Error during WebSocket open"));
            } catch (IOException ex) {
                log.error("Error closing session: {}", ex.getMessage());
            }
        }*/
    }

    @OnMessage
    public void onMessage(String message, Session session) throws IOException {

        log.info("counselor onMessage {}", message, session.getId());

        try {
            for (Session s : clients) {
                log.info("send data : {}", message);
                s.getBasicRemote().sendText(message);
            }

            // JSON 파서를 사용하여 객체 해석
            Map<String, Object> messageMap = new ObjectMapper().readValue(message, Map.class);

            String writer = (String) messageMap.get("writer");
            String content = (String) messageMap.get("content");
            Long roomId = ((Integer) messageMap.get("chatRoomId")).longValue();


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
        counselor.remove(session);
        log.info("counselor session close : {}", session.getId());
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
