package com.example.OneToOneChat.domain.chatRoom.service;


import com.example.OneToOneChat.domain.chatMessage.dto.request.ChatRoomRequest;
import com.example.OneToOneChat.domain.chatMessage.entity.ChatRoom;
import com.example.OneToOneChat.domain.chatRoom.repository.ChatMessageRepository;
import com.example.OneToOneChat.domain.chatMessage.repository.ChatRoomRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

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
                .chatRoomName(chatRoomRequest.clientName())
                .creationDate(new Date()) // 현재 시각을 설정
                .build();

        chatRoomRepository.save(chatRoom);

        return chatRoom.getChatRoomId();
    }

    /**
     *
     * @return 오늘 상담한 채팅방의 수
     */
    public Long readTodayChatRoom() {
        List<ChatRoom> chatRooms = chatRoomRepository.findAll();

        Calendar cal1 = Calendar.getInstance();
        cal1.setTime(new Date());
        cal1.set(Calendar.HOUR_OF_DAY, 0);
        cal1.set(Calendar.MINUTE, 0);
        cal1.set(Calendar.SECOND, 0);
        cal1.set(Calendar.MILLISECOND, 0);

        Long todayChat = 0L;

        for (ChatRoom chatRoom : chatRooms) {
            Calendar cal2 = Calendar.getInstance();
            cal2.setTime(chatRoom.getCreationDate());
            cal2.set(Calendar.HOUR_OF_DAY, 0);
            cal2.set(Calendar.MINUTE, 0);
            cal2.set(Calendar.SECOND, 0);
            cal2.set(Calendar.MILLISECOND, 0);

            if (cal1.getTime().equals(cal2.getTime())) {
                todayChat++;
            }
        }

        return todayChat;
    }



}
