package com.example.OneToOneChat.domain.chatRoom.entity;

import com.example.OneToOneChat.domain.chatMessage.entity.ChatRoom;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class ChatMessage {

    @Id@GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="chat_message_id")
    private Long id;
    private String content;
    private String writer;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="chat_room_id")
    private ChatRoom chatRoom;

    @Builder
    public ChatMessage(String content,String writer,ChatRoom chatRoom){
        this.content=content;
        this.writer=writer;
        this.chatRoom=chatRoom;
    }


}
