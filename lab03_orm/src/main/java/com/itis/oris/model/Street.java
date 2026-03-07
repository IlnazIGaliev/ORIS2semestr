package com.itis.oris.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import lombok.Getter;
import lombok.Setter;

@Setter @Getter
@Entity
public class Street {

    @Id
    Long id;

    @Column
    String name;

    @ManyToOne
    City city;
}