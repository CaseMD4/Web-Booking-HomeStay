package com.example.case_team_3.model;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "emails")
public class Email {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name; // Tên thể loại email

    @Column(nullable = false)
    private String type; // "text", "image_link", "html"

    @Column(nullable = false)
    private String content; // Nội dung email (có thể là văn bản, link ảnh, hoặc mã HTML)
}