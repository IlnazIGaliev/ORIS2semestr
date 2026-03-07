package com.itis.oris.model;

import lombok.Getter;
import lombok.Setter;
import jakarta.persistence.*;

import java.util.HashSet;
import java.util.Set;

import static jakarta.persistence.InheritanceType.*;

@Getter
@Setter
@Entity
@Inheritance(strategy = JOINED)
public class Person {

    @Id
    protected Long id;

    protected String name;

    @ManyToOne
    protected Phone phone;

    @ManyToMany(cascade = {CascadeType.PERSIST, CascadeType.MERGE}, fetch = FetchType.EAGER)
    protected Set<Phone> phones = new HashSet<>();

    @Override
    public String toString() {
        return
                this.getClass().getSimpleName() + "{" +
                        "id=" + id +
                        ", name='" + name + '\'' +
                        ", phone=" + phone +
                        '}';
    }
}