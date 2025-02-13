package com.example.case_team_3.service;

import com.example.case_team_3.model.Booking;
import com.example.case_team_3.repository.BookingRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class BookingService {
    @Autowired
    private BookingRepository bookingRepository;

    public Booking createBooking(Booking booking) {
        return (Booking) bookingRepository.save(booking);
    }

//    public Booking getBookingById(String id) {
//        return bookingRepository.getReferenceById(Id);
//    }





}
