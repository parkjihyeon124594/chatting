package com.example.OneToOneChat.domain.chatMessage.repository;

import com.example.OneToOneChat.domain.chatMessage.entity.ChatRoom;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ChatRoomRepository extends JpaRepository<ChatRoom,Long> {


}

