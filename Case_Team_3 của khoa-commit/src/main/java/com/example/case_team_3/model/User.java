package com.example.case_team_3.model;

import jakarta.persistence.*;
import lombok.Data;
import java.util.List;

@Data
@Entity
@Table(name = "user")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer userId;

    @Column(unique = true)
    private String userUsername;
    
    private String userPassword;
    
    @Column(unique = true)
    private String userEmail;

    @OneToMany(mappedBy = "user")
    private List<Booking> bookings;
}
