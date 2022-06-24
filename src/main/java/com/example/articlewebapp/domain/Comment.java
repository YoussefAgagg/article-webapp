package com.example.articlewebapp.domain;

import org.hibernate.validator.constraints.Length;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.Instant;

@Table(name = "comments")
@Entity(name = "Comment")
public class Comment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "comment_id")
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

    public Comment() {
    }

    public Long getId() {
        return id;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public Instant getDateCreated() {
        return dateCreated;
    }

    public void setDateCreated(Instant dateCreated) {
        this.dateCreated = dateCreated;
    }

    public Instant getLastCreated() {
        return lastCreated;
    }

    public void setLastCreated(Instant lastCreated) {
        this.lastCreated = lastCreated;
    }
}
