package com.example.OneToOneChat.domain.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

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
