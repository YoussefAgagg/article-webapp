package com.example.articlewebapp.repository;

import com.example.articlewebapp.domain.Authority;
import org.springframework.data.jpa.repository.JpaRepository;
/**
 * Repository for the Authority roles.
 *
 * @author Youssef Agagg
 *
 */
public interface AuthorityRepository extends JpaRepository<Authority,String> {
}
