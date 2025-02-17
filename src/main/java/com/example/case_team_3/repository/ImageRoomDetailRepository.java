package com.example.case_team_3.repository;

import com.example.case_team_3.model.ImageRoomDetail;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ImageRoomDetailRepository extends JpaRepository<ImageRoomDetail, Long> {
    List<ImageRoomDetail> findByRoomId(Long roomId);
}