package com.example.case_team_3.service;

import com.example.case_team_3.model.Booking;
import com.example.case_team_3.model.Room;
import com.example.case_team_3.model.User;
import com.example.case_team_3.model.cashier.TransactionHistory;
import com.example.case_team_3.repository.UserRepository;
import com.example.case_team_3.repository.RoomRepository;
import com.example.case_team_3.repository.TransactionHistoryRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import org.apache.logging.log4j.message.SimpleMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;

@Service
public class RoomService {
    @Autowired
    private  RoomRepository roomRepository;
    @Autowired
    private TransactionHistoryRepository transactionHistoryRepository;
    @Autowired
    UserRepository userRepository;
    @PersistenceContext
    private EntityManager entityManager;
    @Autowired
    private SimpMessagingTemplate messagingTemplate;
    public List<Room> getRoomsByStatus(String status) {

        if (status == null || status.isEmpty()) {
            return roomRepository.findAll();
        }
        try {
            Room.RoomStatus statusEnum = Room.RoomStatus.valueOf(status);
            return roomRepository.findByRoomStatus(statusEnum);
        } catch (IllegalArgumentException e) {
            return roomRepository.findAll();
        }
    }
    public void saveRoom(Room room) {
        roomRepository.save(room);
    }
    public  Room getRoomById(Integer roomId) {
        return roomRepository.findById(roomId).orElse(null);
    }
    public Float getUnpaidAmount(Integer roomId) {
        List<Booking> unpaidBookings = findByRoom_RoomIdAndBookingStatus(roomId, Booking.BookingStatus.confirmed);
        float totalAmount = 0f;
        for (Booking booking : unpaidBookings) {
            long totalSeconds = ChronoUnit.SECONDS.between(booking.getBookingCheckInDate(), booking.getBookingCheckOutDate());
            float totalDay = totalSeconds / 86400;
            totalAmount += (booking.getRoom().getRoomPrice()*totalDay);
        }

        return totalAmount;
    }
    public void updateRoomStatusToAvailable(Integer roomId) {
        Room room = roomRepository.findById(roomId).orElse(null);
        if (room != null && room.getRoomStatus() == Room.RoomStatus.cleaning) {
            room.setRoomStatus(Room.RoomStatus.available);
            roomRepository.save(room);
        }
    }
    public void processPayment(Integer roomId, Float amountPaid) {
        Room room = roomRepository.findById(roomId).orElse(null);
        if (room != null) {
            TransactionHistory transaction = new TransactionHistory();
            transaction.setRoom(room);
            transaction.setAmountPaid(amountPaid);
            transaction.setTransactionTime(LocalDateTime.now());
            transactionHistoryRepository.save(transaction);

            room.setRoomStatus(Room.RoomStatus.cleaning);
            roomRepository.save(room);
            messagingTemplate.convertAndSend("/topic/cleaning", "Phòng " + room.getRoomId() + " cần dọn dẹp.");
        }
    }
    public List<TransactionHistory> fetchTransactionHistory(Integer roomId) {
        return transactionHistoryRepository.findByRoom_RoomId(roomId);
    }
    public String getNameCustomerBooking(Integer roomId) {
     Integer idUser = findUserIdByRoomId(roomId);
        User user =userRepository.findById(idUser).orElse(null);
      String nameUser =user.getUserUsername();
      return nameUser;
    }
    public List<Booking> findByRoom_RoomIdAndBookingStatus(Integer roomId, Booking.BookingStatus bookingStatus) {
        String jpql = "SELECT b FROM Booking b WHERE b.room.id = :roomId AND b.bookingStatus = :status";
        TypedQuery<Booking> query = entityManager.createQuery(jpql, Booking.class);
        query.setParameter("roomId", roomId);
        query.setParameter("status", bookingStatus);
        return query.getResultList();
    }
    public Integer findUserIdByRoomId(Integer roomId) {
        try {
            return entityManager.createQuery(
                            "SELECT b.user.id FROM Booking b WHERE b.room.id = :roomId", Integer.class)
                    .setParameter("roomId", roomId)
                    .getSingleResult();
        } catch (NoResultException e) {
            return null;
        }
    }
}
