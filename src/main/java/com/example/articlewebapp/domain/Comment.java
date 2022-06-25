package com.example.articlewebapp.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.Hibernate;
import org.hibernate.validator.constraints.Length;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.time.Instant;
import java.util.Objects;

/**
 *  @author Mohamed Ehab Ali
 *  @since 1.0
 */

@Table(name = "comment")
@Entity
@Getter
@Setter
@ToString
public class Comment implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

    @NotBlank(message = "Comment shouldn't be blank")
    @NotNull(message = "Comment shouldn't be null")
    @Size(min = 1, max = 500, message="Comment should be at least 1 and at most 500 characters")
    @Lob
    @Column(name = "text")
    private String text;

    @NotNull(message = "Comment must have a creation date")
    @Column(name = "date_created")
    private Instant dateCreated;

    @Column(name = "last_edited")
    private Instant lastEdited;


    @ManyToOne(optional = false)
    @NotNull
    @JsonIgnoreProperties(value = { "articles",  "followers", "following"  }, allowSetters = true)
    private User user;


    @ManyToOne(optional = false)
    @NotNull
    @JsonIgnoreProperties(value = { "comments", "categories", "author" }, allowSetters = true)
    private Article article;

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
