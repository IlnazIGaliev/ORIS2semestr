package com.itis.oris.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
@Entity
public class City {

    @Id
    Long id;

    @Column
    String name;

    @ManyToOne
    Country country;
}
