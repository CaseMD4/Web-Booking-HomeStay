package com.example.case_team_3.model.cashier;

import com.example.case_team_3.model.Room;
import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "transaction_history")
public class TransactionHistory {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "room_id", nullable = false)
    private Room room;

    private Float amountPaid;
    private LocalDateTime transactionTime;
    private String paymentMethod;
}
