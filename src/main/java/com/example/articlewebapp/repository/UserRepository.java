package com.example.articlewebapp.repository;

import com.example.articlewebapp.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
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

    Optional<User> findOneWithAuthoritiesByUsername(String badguy);
}
