package com.example.OneToOneChat.websocket.controller;


import com.example.OneToOneChat.domain.chatMessage.dto.request.ChatRoomRequest;
import com.example.OneToOneChat.domain.chatMessage.entity.ChatRoom;
import com.example.OneToOneChat.domain.chatRomm.repository.ChatMessageRepository;
import com.example.OneToOneChat.domain.chatMessage.repository.ChatRoomRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class ChatRoomSerivce {

    private final ChatRoomRepository chatRoomRepository;
    private final ChatMessageRepository chatMessageRepository;

    /**
     * create
     * @param chatRoomRequest
     * @return
     */
    @Transactional
    public Long saveChatRoom(ChatRoomRequest chatRoomRequest){
        ChatRoom chatRoom = ChatRoom.builder()
                .chatRoomName(chatRoomRequest.clientName()).build();

        chatRoomRepository.save(chatRoom);

        return chatRoom.getChatRoomId();
    }

    /**
     * READ
     */



}
