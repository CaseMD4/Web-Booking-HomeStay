package com.example.case_team_3.repository;

import com.example.case_team_3.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Integer> {
    User findByUserUsername(String username);
    User findByUserEmail(String email);
}
