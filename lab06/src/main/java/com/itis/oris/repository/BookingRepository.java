package com.itis.oris.repository;

import com.itis.oris.model.Booking;
import com.itis.oris.model.Hotel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface BookingRepository extends JpaRepository<Booking, Long> {
    List<Booking> findByHotel(Hotel hotel);

    @Query("select b from Booking b where b.id = :id and b.hotel.id = :hotelId ")
    Booking findByIdAndHotelId(Long id, Long hotelId);
}
