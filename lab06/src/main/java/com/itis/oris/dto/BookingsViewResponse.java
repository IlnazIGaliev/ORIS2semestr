package com.itis.oris.dto;

import java.util.List;

public class BookingsViewResponse {
    private List<BookingPersonViewDto> bookings;

    public BookingsViewResponse(List<BookingPersonViewDto> bookings) {
        this.bookings = bookings;
    }

    public List<BookingPersonViewDto> getBookings() {
        return bookings;
    }

    public void setBookings(List<BookingPersonViewDto> bookings) {
        this.bookings = bookings;
    }
}
