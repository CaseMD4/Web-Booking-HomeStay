package com.example.case_team_3.model;

import java.io.Serializable;
import java.util.Objects;

public class BookingId implements Serializable {

    private Integer room;
    private Integer user;

    public BookingId() {
    }

    public BookingId(Integer room, Integer user) {
        this.room = room;
        this.user = user;
    }

    // Getters và Setters
    public Integer getRoom() {
        return room;
    }

    public void setRoom(Integer room) {
        this.room = room;
    }

    public Integer getUser() {
        return user;
    }

    public void setUser(Integer user) {
        this.user = user;
    }

    // Cần override equals và hashCode để đảm bảo so sánh chính xác
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof BookingId)) return false;
        BookingId that = (BookingId) o;
        return Objects.equals(getRoom(), that.getRoom()) &&
                Objects.equals(getUser(), that.getUser());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getRoom(), getUser());
    }
}
