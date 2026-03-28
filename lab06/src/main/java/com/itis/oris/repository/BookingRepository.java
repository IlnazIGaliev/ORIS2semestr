package com.itis.oris.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Repository;
import com.itis.oris.model.Booking;
import com.itis.oris.model.User;

import java.util.Optional;

public interface BookingRepository extends JpaRepository<Booking, Long> {

}
