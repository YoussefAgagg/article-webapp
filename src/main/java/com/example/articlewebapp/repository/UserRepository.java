package com.example.articlewebapp.repository;

import com.example.articlewebapp.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
/**
 *
 * @author Youssef Agagg
 */
public interface UserRepository extends JpaRepository<User, Long> {
}
