package com.example.articlewebapp.domain;

import lombok.*;
import org.hibernate.Hibernate;
import javax.persistence.*;
import java.io.Serializable;
import java.util.Objects;

/**
 *
 *
 *  @author Youssef Agagg
 *  @since 1.0
 */

@Table(name = "article_likes_dislikes")
@Entity
@Getter
@Setter
@ToString
public class ArticleLikesDisLikes implements Serializable {
    @EmbeddedId
    private ArticleLikesDislikesId id;
    @Column(name = "like_type")
    private Integer likeType;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        ArticleLikesDisLikes likes = (ArticleLikesDisLikes) o;
        return id != null && Objects.equals(id, likes.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
