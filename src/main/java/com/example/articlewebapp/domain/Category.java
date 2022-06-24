package com.example.articlewebapp.domain;

import lombok.AccessLevel;
import lombok.Data;
import lombok.Setter;
import org.hibernate.Hibernate;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

/**
 *  Developed by : Mohamed Ehab Ali
 *  Date : 24 / 6 / 2022
 *  Description : Created Category Model :-
 *      1 - Adding essential attributes and its validation annotations
 *      2 - Adding essential relationships between this model and the other models
 *      3 - Overriding equals() and hashCode() methods
 */

@Table(name = "category")
@Entity(name = "Category")
@Data
public class Category {
    @Id
    @Setter(AccessLevel.NONE)
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @Column(name = "id")
    private Long id;

    @NotBlank(message = "Category shouldn't be blank")
    @NotNull(message = "Category shouldn't be null")
    @Column(name = "name")
    private String name;

    @ManyToMany
    private Set<Article> articles = new HashSet<Article>();

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        Category category = (Category) o;
        return id != null && Objects.equals(id, category.id);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}
