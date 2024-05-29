package com.example.OneToOneChat.domain.chatRoom.repository;

import com.example.OneToOneChat.domain.chatRoom.entity.ChatMessage;
import com.example.OneToOneChat.domain.chatMessage.entity.ChatRoom;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
public interface ChatMessageRepository extends JpaRepository<ChatMessage,Long> {

    @Query("select cm from ChatMessage cm where cm.chatRoom = :chatRoom ")
    List<ChatMessage> findChatMessageByChatRoomId(@Param("chatRoom") ChatRoom chatRoom);


}