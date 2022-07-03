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
 *
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

    /**
     *
     *
     *  @author Youssef Agagg
     */
    @Getter
    @Setter
    @ToString
    @AllArgsConstructor
    @NoArgsConstructor
    @Embeddable
    public static class ArticleLikesDislikesId implements Serializable {

        @ManyToOne
        @JoinColumn(name = "user_id")
        private User user;

        @ManyToOne
        @JoinColumn(name = "article_id")
        private Article article;


        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
            ArticleLikesDislikesId idCompose = (ArticleLikesDislikesId) o;
            return user != null && Objects.equals(user, idCompose.user)
                    && article != null && Objects.equals(article, idCompose.article);
        }

        @Override
        public int hashCode() {
            return Objects.hash(user, article);
        }
    }
}
