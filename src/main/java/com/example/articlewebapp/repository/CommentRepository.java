package com.example.articlewebapp.repository;

import com.example.articlewebapp.domain.Comment;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
/**
 *
 * @author Youssef Agagg
 *
 */
public interface CommentRepository extends JpaRepository<Comment, Long> {


    public Page<Comment> findAllByArticleId(Long articleId, Pageable pageable);
}
