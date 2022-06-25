package com.example.articlewebapp.repository;

import com.example.articlewebapp.domain.Comment;
import org.springframework.data.jpa.repository.JpaRepository;
/**
 *
 * @author Youssef Agagg
 *
 */
public interface CommentRepository extends JpaRepository<Comment, Long> {
}
