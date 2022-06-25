package com.example.articlewebapp.repository;

import com.example.articlewebapp.domain.ArticleLikesDisLikes;
import com.example.articlewebapp.domain.ArticleLikesDislikesId;
import org.springframework.data.jpa.repository.JpaRepository;
/**
 *
 * @author Youssef Agagg
 *
 */
public interface ArticleLikesDislikesRepository extends JpaRepository<ArticleLikesDisLikes, ArticleLikesDislikesId> {
}
