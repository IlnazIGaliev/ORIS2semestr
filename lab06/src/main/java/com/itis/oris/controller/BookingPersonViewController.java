package com.itis.oris.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.itis.oris.dto.BookingPersonViewDto;
import com.itis.oris.dto.BookingsResponse;
import com.itis.oris.dto.BookingsViewResponse;
import com.itis.oris.model.Booking;
import com.itis.oris.repository.BookingRepository;
import com.itis.oris.service.BookingPersonViewService;
import com.itis.oris.service.UserDetailImpl;

import java.util.List;

@RestController
@RequestMapping("/api/booking")
public class BookingPersonViewController {

    private final BookingPersonViewService bookingPersonViewService;

    public BookingPersonViewController(BookingPersonViewService bookingPersonViewService) {
        this.bookingPersonViewService = bookingPersonViewService;
    }

    @GetMapping("/allview")
    public ResponseEntity<BookingsViewResponse> getBookings() {

        UserDetailImpl userDetails =
                (UserDetailImpl) SecurityContextHolder.getContext()
                        .getAuthentication().getPrincipal();

        List<BookingPersonViewDto> bookings = bookingPersonViewService.findByHotelId(userDetails.getUser().getHotel().getId());

        bookings.forEach(b-> System.out.println(b.getId()));

        return ResponseEntity.ok(new BookingsViewResponse(bookings));
    }
}
