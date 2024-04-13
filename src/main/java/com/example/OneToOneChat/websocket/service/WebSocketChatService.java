package com.example.OneToOneChat.websocket.service;


import jakarta.websocket.Session;
import jakarta.websocket.server.ServerEndpoint;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

@Service	//bean 등록
@ServerEndpoint("/socket") //해당 URL로 Socket연결 (Singleton pattern)
public class WebSocketChatService {

    private static Set<Session> clients=
            Collections.synchronizedSet(new HashSet<Session>());
}
