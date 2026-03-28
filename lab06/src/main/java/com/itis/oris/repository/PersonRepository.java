package com.itis.oris.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.itis.oris.model.Booking;
import com.itis.oris.model.Person;

public interface PersonRepository extends JpaRepository<Person, Long> {

}
