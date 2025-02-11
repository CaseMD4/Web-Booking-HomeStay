package com.example.case_team_3.repository;

import com.example.case_team_3.model.cashier.TransactionHistory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TransactionHistoryRepository extends JpaRepository<TransactionHistory, Integer> {
    List<TransactionHistory> findByRoom_RoomId(Integer roomId);
}
