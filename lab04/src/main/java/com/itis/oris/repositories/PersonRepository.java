package com.itis.oris.repositories;

import com.itis.oris.model.Person;
import com.itis.oris.model.Phone;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PersonRepository extends JpaRepository<Person, Long> {
}
