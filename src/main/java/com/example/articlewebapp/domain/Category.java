package com.example.articlewebapp.domain;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.Hibernate;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.util.Objects;

/**
 *  @author Mohamed Ehab Ali
 *  @since 1.0
 */

@Table(name = "category")
@Entity(name = "Category")
@Getter
@Setter
@ToString
public class Category implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

    @NotBlank(message = "Category shouldn't be blank")
    @NotNull(message = "Category shouldn't be null")
    @Size(min = 2, max = 50, message="Category Name should be at least 2 and at most 50 characters")
    @Column(name = "name")
    private String name;


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
