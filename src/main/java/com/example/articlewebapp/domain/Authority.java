package com.example.articlewebapp.domain;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.Hibernate;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import java.util.Objects;

/**
 *  @author Mohamed Ehab Ali
 *  @since 1.0
 */

@Table(name = "authority")
@Entity
@Getter
@Setter
@ToString
public class Authority {
    @Id
    @NotNull
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
