package com.example.case_team_3.model.chat;

import com.example.case_team_3.model.Employee;
import com.example.case_team_3.model.User;
import jakarta.persistence.*;
import lombok.Data;

    @Data
    @Entity
    @Table(name = "chat_room")
    public class ChatRoom {
        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        private Integer id;

        @ManyToOne
        @JoinColumn(name = "user_id", nullable = false)
        private User user;

        @ManyToOne
        @JoinColumn(name = "employee_id")
        private Employee employee;

        @Enumerated(EnumType.STRING)
        private ChatRoomStatus chatRoomStatus;

        public enum ChatRoomStatus {
            pending, active, closed
        }
}
