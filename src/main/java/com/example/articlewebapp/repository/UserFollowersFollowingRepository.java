package com.example.articlewebapp.repository;


import com.example.articlewebapp.domain.UserFollowersFollowing;
import org.springframework.data.jpa.repository.JpaRepository;


/**
 *
 * @author Youssef Agagg
 *
 */
public interface UserFollowersFollowingRepository extends JpaRepository<UserFollowersFollowing, UserFollowersFollowing.UserFollowersFollowingID> {

}
