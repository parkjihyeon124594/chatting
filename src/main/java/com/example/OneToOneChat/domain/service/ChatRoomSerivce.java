package com.example.OneToOneChat.domain.service;


import com.example.OneToOneChat.domain.dto.Request.ChatRoomRequest;
import com.example.OneToOneChat.domain.entity.ChatRoom;
import com.example.OneToOneChat.domain.repository.ChatMessageRepository;
import com.example.OneToOneChat.domain.repository.ChatRoomRepository;
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
