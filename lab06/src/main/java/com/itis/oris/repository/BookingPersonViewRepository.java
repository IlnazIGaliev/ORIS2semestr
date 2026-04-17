package com.itis.oris.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.itis.oris.model.BookingPersonView;
import com.itis.oris.model.Hotel;
import java.util.List;

public interface BookingPersonViewRepository extends JpaRepository<BookingPersonView, Long> {
    List<BookingPersonView> findByHotelId(Long hotelId);
}
