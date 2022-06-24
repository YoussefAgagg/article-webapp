package com.example.articlewebapp.domain;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.PositiveOrZero;
import java.time.Instant;
import java.util.HashSet;
import java.util.Set;

@Table(name = "articles")
@Entity(name = "Article")
public class Article {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "article_id")
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
            joinColumns = {@JoinColumn(name = "article_id")},
            inverseJoinColumns = {@JoinColumn(name = "category_id")}
    )
    private Set<Category> categories = new HashSet<>();

    @OneToMany
    @JoinColumn(name = "article_id")
    private Set<Comment> comments = new HashSet<>();

    public Set<Comment> getComments() {
        return comments;
    }

    public Article() {
    }

    public Long getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getSummary() {
        return summary;
    }

    public String getContent() {
        return content;
    }

    public Instant getDateCreated() {
        return dateCreated;
    }

    public Instant getLastCreated() {
        return lastCreated;
    }

    public Long getViews() {
        return views;
    }

    public Long getLikes() {
        return likes;
    }

    public Long getDislikes() {
        return dislikes;
    }

    public User getUser() {
        return user;
    }

    public Set<Category> getCategories() {
        return categories;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public void setDateCreated(Instant dateCreated) {
        this.dateCreated = dateCreated;
    }

    public void setLastCreated(Instant lastCreated) {
        this.lastCreated = lastCreated;
    }

    public void setViews(Long views) {
        this.views = views;
    }

    public void setLikes(Long likes) {
        this.likes = likes;
    }

    public void setDislikes(Long dislikes) {
        this.dislikes = dislikes;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public void setCategories(Set<Category> categories) {
        this.categories = categories;
    }
}
