package com.example.articlewebapp.repository;

import com.example.articlewebapp.domain.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.DoubleStream;

/**
 *
 * @author Youssef Agagg
 */
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findOneByUsername(String username);

    Optional<User> findOneByEmailIgnoreCase(String email);

    Optional<User> findOneByActivationKey(String key);

    Optional<User> findOneByResetKey(String key);

    //get users that current user following
    Page<User> findAllByFollowersUsername(String username, Pageable pageable);
    //get users that follow current user
    Page<User> findAllByFollowingUsername(String username, Pageable pageable);
}
