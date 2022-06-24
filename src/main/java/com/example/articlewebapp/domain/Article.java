package com.example.articlewebapp.domain;

import lombok.AccessLevel;
import lombok.Data;
import lombok.Setter;
import org.hibernate.Hibernate;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.PositiveOrZero;
import java.time.Instant;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

/**
 *  Developed by : Mohamed Ehab Ali
 *  Date : 24 / 6 / 2022
 *  Description : Created Article Model :-
 *      1 - Adding essential attributes and its validation annotations
 *      2 - Adding essential relationships between this model and the other models
 *      3 - Overriding equals() and hashCode() methods
 */

@Table(name = "article")
@Entity(name = "Article")
@Data
public class Article {
    @Id
    @Setter(AccessLevel.NONE)
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @Column(name = "id")
    private Long id;

    @NotBlank
    @NotNull
    @Column(name = "title")
    private String title;

    @Column(name = "summary")
    private String summary;

    @NotBlank(message = "Article content shouldn't be blank")
    @NotNull(message = "Article must have a content")
    @Lob
    @Column(name = "content")
    private String content;

    @NotNull(message = "Article must have a creation date")
    @Column(name = "date_created")
    private Instant dateCreated;

    @Column(name = "last_edit_date")
    private Instant lastCreated;

    @PositiveOrZero(message = "Article views must be positive OR zero")
    @Column(name = "views")
    private Long views;

    @PositiveOrZero(message = "Article likes must be positive OR zero")
    @Column(name = "likes")
    private Long likes;

    @PositiveOrZero(message = "Article dislikes must be positive OR zero")
    @Column(name = "dislikes")
    private Long dislikes;

    @ManyToOne
    private User user;

    @ManyToMany
    @JoinTable(
            name = "articles_categories",
            joinColumns = {@JoinColumn(name = "article_id",referencedColumnName = "id")},
            inverseJoinColumns = {@JoinColumn(name = "category_id",referencedColumnName = "id")}
    )
    private Set<Category> categories = new HashSet<Category>();

    @OneToMany
    @JoinColumn(name = "article_id",referencedColumnName = "id")
    private Set<Comment> comments = new HashSet<Comment>();

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        Article article = (Article) o;
        return id != null && Objects.equals(id, article.id);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}