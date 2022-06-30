package com.example.articlewebapp.repository;

import com.example.articlewebapp.domain.Article;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.stream.DoubleStream;

/**
 * Repository for the articles.
 *
 * @author Youssef Agagg
 *
 */
public interface ArticleRepository extends JpaRepository<Article, Long> {

}
