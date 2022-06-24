package com.example.articlewebapp.domain;

import lombok.AccessLevel;
import lombok.Data;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.Hibernate;
import org.hibernate.validator.constraints.Length;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.Instant;
import java.util.Objects;

/**
 *  @author Mohamed Ehab Ali
 *  @since 24-6-2022
 */

@Table(name = "comment")
@Entity(name = "Comment")
@Data
@Slf4j
public class Comment {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @Column(name = "id")
    private Long id;

    @Length(max = 2000)
    @NotBlank(message = "Comment shouldn't be blank")
    @NotNull(message = "Comment shouldn't be null")
    @Column(name = "text")
    private String text;

    @NotNull(message = "Comment must have a creation date")
    @Column(name = "date_created")
    private Instant dateCreated;

    @Column(name = "last_edit_date")
    private Instant lastCreated;

    @ManyToOne
    private User user;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        Comment comment = (Comment) o;
        return id != null && Objects.equals(id, comment.id);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}
