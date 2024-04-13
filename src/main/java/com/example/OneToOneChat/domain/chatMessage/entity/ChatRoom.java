package com.example.OneToOneChat.domain.chatMessage.entity;

import com.example.OneToOneChat.domain.chatRomm.entity.ChatMessage;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor
public class ChatRoom {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "chat_room_id")
    private Long chatRoomId;

    @OneToMany(mappedBy = "chatRoom", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    @JsonIgnore
    private List<ChatMessage> chatMessageList = new ArrayList<>();

    private String chatRoomName;

    @Builder
    public ChatRoom(Long id,String chatRoomName,List<ChatMessage> chatMessageList){
        this.chatRoomId =id;
        this.chatRoomName=chatRoomName;
        this.chatMessageList=chatMessageList;
    }


}
