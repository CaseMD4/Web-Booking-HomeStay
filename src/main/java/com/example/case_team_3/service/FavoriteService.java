package com.example.case_team_3.service;

import com.example.case_team_3.model.Favorite;
import com.example.case_team_3.model.Room;
import com.example.case_team_3.repository.FavoriteRepository;
import com.example.case_team_3.repository.RoomRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
public class FavoriteService {

    @Autowired
    private RoomRepository roomRepository;

    @Autowired
    private FavoriteRepository favoriteRepository;

    public List<Favorite> getFavoritesByUserId(Long userId) {
        return favoriteRepository.findByUserId(userId);
    }


    public List<Room> getRoomByUserFavorites(List<Favorite> favorites) {
        return favorites.stream()
                .map(fav -> roomRepository.findById(fav.getRoomId()).orElse(null))
                .filter(Objects::nonNull)
                .collect(Collectors.toList());
    }


    public Map<Long, Long> getFavoriteCounts() {
        List<Object[]> results = favoriteRepository.countFavoritesByRoom();
        return results.stream().collect(Collectors.toMap(
                row -> (Long) row[0],  // roomId
                row -> (Long) row[1]   // favoriteCount
        ));
    }

    public List<Favorite> getAllFavorites() {
        return favoriteRepository.findAll();
    }

    public void addFavorite(Long userId, Long roomId) {
        Favorite favorite = new Favorite(userId, roomId);
        favoriteRepository.save(favorite);
    }

    public void removeFavorite(Long userId, Long roomId) {
        favoriteRepository.deleteByUserIdAndRoomId(userId, roomId);
    }
}