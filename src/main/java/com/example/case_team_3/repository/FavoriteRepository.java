package com.example.case_team_3.repository;

import com.example.case_team_3.model.Favorite;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FavoriteRepository extends JpaRepository<Favorite, Long> {
    List<Favorite> findByUserId(Long userId);
    void deleteByUserIdAndRoomId(Long userId, Long roomId);

    @Query("SELECT f.roomId, COUNT(f.id) FROM Favorite f GROUP BY f.roomId")
    List<Object[]> countFavoritesByRoom();
}