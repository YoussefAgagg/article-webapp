package com.example.articlewebapp.service;

import com.example.articlewebapp.aop.logging.Loggable;
import com.example.articlewebapp.domain.*;
import com.example.articlewebapp.exception.BadRequestException;
import com.example.articlewebapp.repository.ArticleLikesDislikesRepository;
import com.example.articlewebapp.repository.ArticleRepository;
import com.example.articlewebapp.repository.UserRepository;
import com.example.articlewebapp.security.SecurityUtils;
import com.example.articlewebapp.dto.ArticleDTO;
import com.example.articlewebapp.dto.mapper.ArticleMapper;

import com.example.articlewebapp.web.rest.payload.LikeRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.authentication.AuthenticationCredentialsNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.Optional;

/**
 * Service Implementation for managing {@link Article}.
 *
 * @author Youssef Agagg
 */
@Service
@Transactional
@Slf4j
@RequiredArgsConstructor
public class ArticleService {


    private final ArticleRepository articleRepository;

    private final ArticleLikesDislikesRepository articleLikesDislikesRepository;

    private final UserRepository userRepository;

    private final ArticleMapper articleMapper;


    @Loggable
    public ArticleDTO save(ArticleDTO articleDTO) {
        log.debug("Request to save Article : {}", articleDTO);
        articleDTO.setDateCreated(Instant.now());
        articleDTO.setViews(0l);
        Article article = articleMapper.toEntity(articleDTO);
        article = articleRepository.save(article);
        return articleMapper.toDto(article);
    }

    @Loggable
    @Transactional(readOnly = true)
    public Optional<ArticleDTO> getArticle(Long id) {
        log.debug("Request to get Article : {}", id);
        return articleRepository.findById(id)
                .map(existingArticle -> {
            existingArticle.setViews(existingArticle.getViews()+1);
            return existingArticle;
        }).map(articleRepository::save)
                .map(articleMapper::toDto);
    }
    @Loggable
    @Transactional(readOnly = true)
    public Optional<ArticleDTO> findOne(Long id) {
        log.debug("Request to get Article : {}", id);
        return articleRepository.findById(id)
                .map(articleMapper::toDto);
    }


    @Loggable
    public void delete(Long id) {
        log.debug("Request to delete Article : {}", id);
        articleRepository.deleteById(id);
    }
    @Loggable
    public Page<ArticleDTO> getAllArticles(Pageable pageable) {
        return articleRepository.findAll(pageable).map(articleMapper::toDto);
    }

    @Loggable
    public Optional<ArticleDTO> update(ArticleDTO articleDTO) {
        return articleRepository
                .findById(articleDTO.getId())
                .map(existingArticle -> {
                    articleDTO.setViews(existingArticle.getViews());
                    articleDTO.setLastEdited(Instant.now());
                    articleMapper.partialUpdate(existingArticle, articleDTO);
                    return existingArticle;
                })
                .map(articleRepository::save)
                .map(articleMapper::toDto);
    }
    @Loggable
    public void likeArticle(LikeRequest likeRequest) {
        User userLogin = getLoginUser();
        Article article=articleRepository.findById(likeRequest.getArticleId())
                .orElseThrow(()->new BadRequestException("article not found"));

        ArticleLikesDisLikes.ArticleLikesDislikesId id =new ArticleLikesDisLikes.ArticleLikesDislikesId(userLogin, article);
        ArticleLikesDisLikes likes=new ArticleLikesDisLikes();
        likes.setId(id);
        likes.setLikeType(likeRequest.getLikeType());
        articleLikesDislikesRepository.save(likes);

    }
    @Loggable
    public void removeLikeArticle(Long articleId) {
        User userLogin = getLoginUser();
        Article article=articleRepository.findById(articleId)
                .orElseThrow(()->new BadRequestException("article not found"));

        ArticleLikesDisLikes.ArticleLikesDislikesId id =new ArticleLikesDisLikes.ArticleLikesDislikesId(userLogin, article);
        ArticleLikesDisLikes likes=new ArticleLikesDisLikes();
        likes.setId(id);
        articleLikesDislikesRepository.delete(likes);

    }

    private User getLoginUser() {
        return SecurityUtils
                .getCurrentUserUsername()
                .flatMap(userRepository::findOneByUsername)
                .orElseThrow(() -> new AuthenticationCredentialsNotFoundException("Current user login not found"));
    }
}
