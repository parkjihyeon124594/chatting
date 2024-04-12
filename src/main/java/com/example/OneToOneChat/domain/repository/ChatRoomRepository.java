package com.example.OneToOneChat.domain.repository;

import com.example.OneToOneChat.domain.entity.ChatRoom;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ChatRoomRepository extends JpaRepository<ChatRoom,Long> {


}

