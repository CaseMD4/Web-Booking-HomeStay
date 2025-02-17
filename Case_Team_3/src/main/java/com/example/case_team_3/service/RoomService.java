package com.example.case_team_3.service;

import com.example.case_team_3.model.Booking;
import com.example.case_team_3.model.Room;
import com.example.case_team_3.model.RoomType;
import com.example.case_team_3.model.User;
import com.example.case_team_3.model.cashier.TransactionHistory;
import com.example.case_team_3.repository.BookingRepository;
import com.example.case_team_3.repository.RoomRepository;
import com.example.case_team_3.repository.RoomTypeRepository;
import com.example.case_team_3.repository.UserRepository;
import com.example.case_team_3.repository.cashier_and_cleaner.TransactionHistoryRepository;
import jakarta.persistence.*;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class RoomService {
    @Autowired
    private  RoomRepository roomRepository;

    @Autowired
    private TransactionHistoryRepository transactionHistoryRepository;

    @Autowired
    UserRepository userRepository;

    @Autowired
    private RoomTypeRepository roomTypeRepository;

    @Autowired
    private BookingRepository bookingRepository;

    @PersistenceContext
    private EntityManager entityManager;

    @Autowired
    private SimpMessagingTemplate messagingTemplate;


    @Transactional
    public Room updateRoomStatus(Integer roomId, String status) {
        // Validate input
        if (roomId == null) {
            throw new IllegalArgumentException("ID phòng không được để trống");
        }

        if (status == null || status.trim().isEmpty()) {
            throw new IllegalArgumentException("Trạng thái phòng không được để trống");
        }

        // Normalize status
        status = status.toLowerCase().trim();

        // Find room
        Room room = roomRepository.findById(Long.valueOf(roomId))
                .orElseThrow(() -> new RuntimeException("Không tìm thấy phòng với ID: " + roomId));

        // Convert status to enum, handling case-insensitivity
        try {
            Room.RoomStatus newStatus = Room.RoomStatus.valueOf(status);
            room.setRoomStatus(newStatus);
        } catch (IllegalArgumentException e) {
            // Map common variations or provide helpful error
            switch (status) {
                case "sẵn":
                case "available":
                    room.setRoomStatus(Room.RoomStatus.available);
                    break;
                case "thuê":
                case "booked":
                    room.setRoomStatus(Room.RoomStatus.booked);
                    break;
                case "dọn":
                case "cleaning":
                    room.setRoomStatus(Room.RoomStatus.cleaning);
                    break;
                case "bẩn":
                case "dirty":
                    room.setRoomStatus(Room.RoomStatus.dirty);
                    break;
                default:
                    throw new IllegalArgumentException("Trạng thái phòng không hợp lệ: " + status);
            }
        }

        // Save and return
        return roomRepository.save(room);
    }

    public Room findById(Long id) {
        return roomRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Room not found with id: " + id));
    }

    public void updateRoom(Long id, Room roomDetails) {
        Room existingRoom = findById(id);

        // Update fields
        existingRoom.setRoomDescription(roomDetails.getRoomDescription());
        existingRoom.setRoomType(roomDetails.getRoomType());
        existingRoom.setRoomPrice(roomDetails.getRoomPrice());
        existingRoom.setRoomStatus(roomDetails.getRoomStatus());
        existingRoom.setRoomImg(roomDetails.getRoomImg());

        roomRepository.save(existingRoom);
    }

    public void deleteRoom(Long id) {
        Room room = findById(id);
        roomRepository.delete(room);
    }



    @Transactional
    public Room getRoomById(Integer roomId) {
        return roomRepository.findById(Long.valueOf(roomId))
                .orElseThrow(() -> new RuntimeException("Phòng không tồn tại"));
    }

    @Transactional
    public void deleteRoom(Integer roomId) {
        Room room = getRoomById(roomId);
        roomRepository.delete(room);
    }


    public List<RoomType> getAllRoomTypes() {
        return roomTypeRepository.findAll();
    }

    public RoomType getRoomTypeById(Integer id) {
        return roomTypeRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy loại phòng"));
    }

    public RoomType createRoomType(String roomTypeName) {
        // Trim and validate room type name
        if (roomTypeName == null || roomTypeName.trim().isEmpty()) {
            throw new IllegalArgumentException("Tên loại phòng không được để trống");
        }

        // Normalize the room type name
        roomTypeName = roomTypeName.trim();

        // Check if room type already exists (case-insensitive)
        RoomType existingRoomType = roomTypeRepository.findByRoomTypeName(roomTypeName);
        if (existingRoomType != null) {
            return existingRoomType;
        }

        // Create new room type
        RoomType newRoomType = new RoomType();
        newRoomType.setRoomTypeName(roomTypeName);
        return roomTypeRepository.save(newRoomType);
    }

    public Room createRoom(Room room) {
        // Validate room
        if (room.getRoomType() == null) {
            throw new IllegalArgumentException("Phòng phải có loại phòng");
        }

        // Set default status if not set
        if (room.getRoomStatus() == null) {
            room.setRoomStatus(Room.RoomStatus.available);
        }

        return roomRepository.save(room);
    }

    public List<Room> getAllRooms() {
        return roomRepository.findAll();
    }

    public List<Room> getRoomsByStatus(Room.RoomStatus status) {
        return roomRepository.findByRoomStatus(status);
    }
    public List<Room> getRoomsByType(String roomType) {
        return roomRepository.findByRoomType_RoomTypeName(roomType);
    }
    public Float getUnpaidAmount(Long roomId) {
        List<Booking> unpaidBookings = bookingRepository.findByRoom_RoomIdAndBookingStatus(roomId, Booking.BookingStatus.confirmed);
        float totalAmount = 0f;
        for (Booking booking : unpaidBookings) {
            long totalSeconds = ChronoUnit.SECONDS.between(booking.getBookingCheckInDate(), booking.getBookingCheckOutDate());
            float totalDay = totalSeconds / 86400;
            totalAmount += (booking.getRoom().getRoomPrice()*totalDay);
        }

        return totalAmount;
    }
    public boolean updateRoomStatus(Long roomId, String status) {
        if ("available".equals(status)) {
            Float unpaidAmount = getUnpaidAmount(roomId);
            if (unpaidAmount > 0) {
                return false;
            }
        }
        Room room = roomRepository.findById(roomId).orElseThrow(() -> new RuntimeException("Room not found"));
        room.setRoomStatus(Room.RoomStatus.valueOf(status));
        roomRepository.save(room);
        return true;
    }

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


    public void updateRoomStatusToAvailable(Integer roomId) {
        Room room = roomRepository.findById(Long.valueOf(roomId)).orElse(null);
        if (room != null && room.getRoomStatus() == Room.RoomStatus.cleaning) {
            room.setRoomStatus(Room.RoomStatus.available);
            roomRepository.save(room);
        }
    }

    public void processPayment(Long roomId, Float amountPaid) {
        Room room = roomRepository.findById(roomId).orElse(null);
        if (room != null) {
            TransactionHistory transaction = new TransactionHistory();
            transaction.setRoom(room);
            transaction.setAmountPaid(amountPaid);
            transaction.setTransactionTime(LocalDateTime.now());
            transactionHistoryRepository.save(transaction);

            room.setRoomStatus(Room.RoomStatus.cleaning);
            roomRepository.save(room);

            Map<String, Object> cleaningNotification = new HashMap<>();
            cleaningNotification.put("roomId", room.getRoomId());
            cleaningNotification.put("roomType", room.getRoomType().getRoomTypeName());
            cleaningNotification.put("roomDescription", room.getRoomDescription());

            messagingTemplate.convertAndSend("/topic/cleaning", cleaningNotification);
        }
    }

    public List<TransactionHistory> fetchTransactionHistory(Integer roomId) {
        return transactionHistoryRepository.findByRoom_RoomId(roomId);
    }



//    @todo
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
