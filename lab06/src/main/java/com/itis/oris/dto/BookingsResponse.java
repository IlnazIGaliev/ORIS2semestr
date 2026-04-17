package com.itis.oris.dto;

import com.itis.oris.model.Booking;

import java.util.List;

public class BookingsResponse {
    private List<Booking> bookings;

    public BookingsResponse(List<Booking> bookings) {
        this.bookings = bookings;
    }

    public List<Booking> getBookings() {
        return bookings;
    }

    public void setBookings(List<Booking> bookings) {
        this.bookings = bookings;
    }
}
