package com.example.articlewebapp.repository;

import com.example.articlewebapp.domain.Article;
import org.springframework.data.jpa.repository.JpaRepository;
/**
 * Repository for the articles.
 *
 * @author Youssef Agagg
 *
 */
public interface ArticleRepository extends JpaRepository<Article, Long> {
}
