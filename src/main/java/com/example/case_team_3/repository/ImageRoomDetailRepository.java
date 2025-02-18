package com.example.case_team_3.repository;

import com.example.case_team_3.model.ImageRoomDetail;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ImageRoomDetailRepository extends JpaRepository<ImageRoomDetail, Long> {
    List<ImageRoomDetail> findByRoomId(Long roomId);

    @Query("SELECT MAX(i.imageRoomDetailPlaceCount) FROM ImageRoomDetail i")
    Integer findMaxImageRoomDetailPlaceCount();

    @Query("SELECT COUNT(i) > 0 FROM ImageRoomDetail i WHERE i.id = :imageId AND i.roomId = :roomId")
    Boolean existsByImageIdAndRoomId(@Param("imageId") Long imageId, @Param("roomId") Long roomId);
}