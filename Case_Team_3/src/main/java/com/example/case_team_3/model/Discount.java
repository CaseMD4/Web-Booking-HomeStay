package com.example.case_team_3.model;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "discount")
public class Discount {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer discountId;

    private String discountCode;
    private Float discountAmount;
    private LocalDateTime discountStartTime;
    private LocalDateTime discountEndTime;
}
