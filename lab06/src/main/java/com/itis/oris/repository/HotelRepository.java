package com.itis.oris.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.itis.oris.model.Hotel;
import com.itis.oris.model.Person;
import org.springframework.stereotype.Repository;

public interface HotelRepository extends JpaRepository<Hotel, Long> {

}
