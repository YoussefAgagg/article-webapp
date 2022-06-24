package com.example.articlewebapp.domain;

import lombok.Data;
import org.hibernate.Hibernate;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Objects;

/**
 *  Developed by : Mohamed Ehab Ali
 *  Date : 24 / 6 / 2022
 *  Description : Created Authority Model :-
 *      1 - Adding essential attributes and its validation annotations
 *      2 - Adding essential relationships between this model and the other models
 *      3 - Overriding equals() and hashCode() methods
 */

@Table(name = "authority")
@Entity(name = "Authority")
@Data
public class Authority {
    @Id
    @Column(name = "name", nullable = false, unique = true)
    private String name;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        Authority authority = (Authority) o;
        return name != null && Objects.equals(name, authority.name);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}
