package com.example.case_team_3.repository;

import com.example.case_team_3.model.Email;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EmailRepository extends JpaRepository<Email, Long> {
}