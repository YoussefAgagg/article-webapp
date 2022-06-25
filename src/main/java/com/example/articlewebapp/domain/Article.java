package com.example.articlewebapp.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.Hibernate;
import org.hibernate.annotations.Formula;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.PositiveOrZero;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.time.Instant;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

/**
 *  @author Mohamed Ehab Ali
 *  @since 1.0
 */

@Table(name = "article")
@Entity
@Getter
@Setter
@ToString
public class Article implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

    @NotBlank
    @NotNull
    @Size(min = 4, max = 50, message="Title should be at least 4 and at most 50 characters")
    @Column(name = "title")
    private String title;

    @Size(max = 500, message="Summary should be at most 500 characters")
    @Column(name = "summary")
    private String summary;

    @NotBlank(message = "Article content shouldn't be blank")
    @NotNull(message = "Article must have a content")
    @Lob
    @Size(min = 3, message="Article content should be at least 3 characters")
    @Column(name = "content")
    private String content;

    @NotNull(message = "Article must have a creation date")
    @Column(name = "date_created")
    private Instant dateCreated;

    @Column(name = "last_edited")
    private Instant lastEdited;

    @PositiveOrZero(message = "Article views must be positive OR zero")
    @Column(name = "views")
    private Long views;


    @ToString.Exclude
    @ManyToOne(optional = false)
    @NotNull
    @JoinColumn(name = "user_id")
    @JsonIgnoreProperties(value = { "articles",  "followers", "following"  }, allowSetters = true)
    private User author;


    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "article_category",
            joinColumns = {@JoinColumn(name = "article_id",referencedColumnName = "id")},
            inverseJoinColumns = {@JoinColumn(name = "category_id",referencedColumnName = "id")}
    )
    private Set<Category> categories = new HashSet<>();


    @ToString.Exclude
    @OneToMany(mappedBy = "article")
    @JsonIgnoreProperties(value = { "user", "article" }, allowSetters = true)
    private Set<Comment> comments = new HashSet<>();
    @Formula("(select count(user_id) from article_likes_dislikes likes where likes.article_id = id and likes.like_type=1)")
    private Long likes;
    @Formula("(select count(user_id) from article_likes_dislikes likes where likes.article_id = id and likes.like_type=0)")
    private Long dislikes;

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