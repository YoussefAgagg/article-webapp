package com.example.articlewebapp.domain;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.Hibernate;

import javax.persistence.Embeddable;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import java.io.Serializable;
import java.util.Objects;

/**
 *
 *
 *  @author Youssef Agagg
 *  @since 1.0
 */
@Getter
@Setter
@ToString
@Embeddable
public class ArticleLikesDislikesId implements Serializable {

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
