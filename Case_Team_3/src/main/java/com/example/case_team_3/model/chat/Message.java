package com.example.case_team_3.model.chat;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "messages")
public class Message {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "chat_room_id", nullable = false)
    private ChatRoom chatRoom;

    private String sender;
    private String receiver;
    private String content;
    private LocalDateTime timestamp;
    private Boolean isReply;

    @Enumerated(EnumType.STRING)
    private SenderType senderType;

    public enum SenderType {
        user, cashier
    }
}
