package com.example.OneToOneChat.domain.chatMessage.service;

import com.example.OneToOneChat.domain.chatRomm.dto.Request.ChatMessageCreateRequest;
import com.example.OneToOneChat.domain.chatMessage.dto.response.ChatRoomAllResponse;
import com.example.OneToOneChat.domain.chatRomm.dto.Response.ChatRoomMessageResnpose;
import com.example.OneToOneChat.domain.chatRomm.entity.ChatMessage;
import com.example.OneToOneChat.domain.chatMessage.entity.ChatRoom;
import com.example.OneToOneChat.domain.chatMessage.exception.ChatRoomError;
import com.example.OneToOneChat.domain.chatRomm.repository.ChatMessageRepository;
import com.example.OneToOneChat.domain.chatMessage.repository.ChatRoomRepository;
import com.example.OneToOneChat.global.exception.GlobalException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class ChatMessageService {
    private final ChatMessageRepository chatMessageRepository;
    private final ChatRoomRepository chatRoomRepository;

    /**
     * message save
     */
    @Transactional
    public Long saveMessage(ChatMessageCreateRequest request, Long chatRoomId){

        ChatRoom findChatRoom= chatRoomRepository.findById(chatRoomId)
                .orElseThrow(() -> new GlobalException(ChatRoomError.CHAT_ROOM_NOT_FOUND));


        ChatMessage chatMessage = ChatMessage.builder()
                .writer(request.writer())
                .content(request.content())
                .chatRoom(findChatRoom)
                .build();
        chatMessageRepository.save(chatMessage);
        return chatMessage.getId();
    }

    /**
     * message ALL READ
     */

    public List<ChatRoomMessageResnpose> readChatMessageByWriter(Long id) {

        ChatRoom findChatRoom= chatRoomRepository.findById(id)
                .orElseThrow(() -> new GlobalException(ChatRoomError.CHAT_ROOM_NOT_FOUND));


        List<ChatMessage> allChatMessage =
                chatMessageRepository.findChatMessageByChatRoomId(findChatRoom);

        return allChatMessage.stream()
                .map(chatMessage -> new ChatRoomMessageResnpose(chatMessage.getWriter(), chatMessage.getContent()))
                .collect(Collectors.toList());
    }
    public List<ChatRoomAllResponse> all(){
        List<ChatRoom> allChat = chatRoomRepository.findAll();
        List<ChatRoomAllResponse> chatRoomMessageResnposes= new ArrayList<>();

        allChat.stream().forEach(chat -> {
            ChatRoomAllResponse chatRoomAllResponse = ChatRoomAllResponse.makeAll(chat);
            chatRoomMessageResnposes.add(chatRoomAllResponse);
        });

        return chatRoomMessageResnposes;
    }



}

