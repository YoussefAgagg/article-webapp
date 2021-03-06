package com.example.articlewebapp.repository;

import com.example.articlewebapp.domain.Category;
import org.springframework.data.jpa.repository.JpaRepository;
/**
 *
 * @author Youssef Agagg
 *
 */
public interface CategoryRepository extends JpaRepository<Category, Long> {
}
