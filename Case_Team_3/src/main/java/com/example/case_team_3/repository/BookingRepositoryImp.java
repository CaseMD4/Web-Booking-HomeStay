package com.example.case_team_3.repository;

import com.example.case_team_3.model.Booking;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import org.springframework.stereotype.Repository;

import java.util.List;
@Repository
public class BookingRepositoryImp implements BookingRepository{
    @PersistenceContext
    private EntityManager entityManager;
    @Override
    public List<Booking> findByRoom_RoomIdAndBookingStatus(Long roomId, Booking.BookingStatus bookingStatus) {
        String jpql = "SELECT b FROM Booking b WHERE b.room.id = :roomId AND b.bookingStatus = :status";
        TypedQuery<Booking> query = entityManager.createQuery(jpql, Booking.class);
        query.setParameter("roomId", roomId);
        query.setParameter("status", bookingStatus);
        return query.getResultList();
    }
}
