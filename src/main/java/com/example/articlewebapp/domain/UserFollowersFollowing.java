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
 */

@Table(name = "user_followers_following")
@Entity
@Getter
@Setter
@ToString
public class UserFollowersFollowing implements Serializable {
    @EmbeddedId
    private UserFollowersFollowingID id;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        UserFollowersFollowing likes = (UserFollowersFollowing) o;
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
    public static class UserFollowersFollowingID implements Serializable {

        @ManyToOne
        @JoinColumn (name = "user_id")
        private User user;

        @ManyToOne
        @JoinColumn(name = "followers_id")
        private User followerId;

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            UserFollowersFollowingID that = (UserFollowersFollowingID) o;
            return Objects.equals(user, that.user) && Objects.equals(followerId, that.followerId);
        }

        @Override
        public int hashCode() {
            return Objects.hash(user, followerId);
        }
    }
}
