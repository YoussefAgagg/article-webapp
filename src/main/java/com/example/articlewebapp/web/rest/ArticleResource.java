package com.example.articlewebapp.web.rest;

import com.example.articlewebapp.aop.logging.Loggable;
import com.example.articlewebapp.exception.BadRequestException;
import com.example.articlewebapp.exception.ResourceDoesntExistException;
import com.example.articlewebapp.security.AuthoritiesConstants;
import com.example.articlewebapp.security.SecurityUtils;
import com.example.articlewebapp.service.ArticleService;
import com.example.articlewebapp.service.UserService;
import com.example.articlewebapp.service.dto.ArticleDTO;

import com.example.articlewebapp.web.rest.util.PaginationUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationCredentialsNotFoundException;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;


import javax.validation.Valid;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

/**
 * REST controller for managing {@link com.example.articlewebapp.domain.Article}.
 *
 * @author Youssef Agagg
 */
@RestController
@RequestMapping("/api")
@Slf4j
@RequiredArgsConstructor
public class ArticleResource {


    private final ArticleService articleService;


    private final UserService userService;



    @Loggable
    @PostMapping("/articles")
    public ResponseEntity<ArticleDTO> createArticle(@Valid @RequestBody ArticleDTO articleDTO) throws URISyntaxException {
        log.debug("REST request to save Article : {}", articleDTO);
        if (articleDTO.getId() != null) {
            throw new BadRequestException("A new article cannot already have an ID");
        }
        ArticleDTO result = articleService.save(articleDTO);
        return ResponseEntity
            .created(new URI("/api/articles/" + result.getId())).body(result);
    }


    @PutMapping("/articles/{id}")
    @PreAuthorize("#articleDTO.author.username.toLowerCase() == authentication.principal.username")
    @Loggable
    public ResponseEntity<ArticleDTO> updateArticle(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody ArticleDTO articleDTO )  {
        log.debug("REST request to update Article : {}, {}", id, articleDTO);
        if (articleDTO.getId() == null) {
            throw new BadRequestException("Invalid article id is null");
        }
        if (!Objects.equals(id, articleDTO.getId())) {
            throw new BadRequestException("Invalid article ID ");
        }
        var user=userService.getUserById(articleDTO.getAuthor().getId());
        user.ifPresentOrElse(userDTO -> {
            if (!Objects.equals(userDTO.getUsername(),articleDTO.getAuthor().getUsername())){
                throw new BadRequestException("edited other users articles");
            }
        },()->{throw new BadRequestException("user doesn't exist");});

        return articleService.update(articleDTO)
                .map(article->ResponseEntity.ok().body(article))
                .orElseThrow(()->{throw new ResourceDoesntExistException("article doesn't exist");});

    }

    @GetMapping("/articles")
    @Loggable
    public ResponseEntity<List<ArticleDTO>> getAllArticles(
            @PageableDefault(sort = { "dateCreated" })Pageable pageable) {
        log.debug("REST request to get all Articles ");
        Page<ArticleDTO> page = articleService.getAllArticles(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);

    }



    @GetMapping("/articles/{id}")
    public ResponseEntity<ArticleDTO> getArticle(@PathVariable Long id) {
        log.debug("REST request to get Article : {}", id);
        Optional<ArticleDTO> articleDTO = articleService.getArticle(id);
        return articleDTO.map(article->ResponseEntity.ok().body(article))
                .orElseThrow(()->{throw new ResourceDoesntExistException("article doesn't exist");});
    }

    @DeleteMapping("/articles/{id}")
    @Loggable
    public ResponseEntity<Void> deleteArticle(@PathVariable Long id) {
        log.debug("REST request to delete Article : {}", id);
        String userLogin = SecurityUtils
                .getCurrentUserUsername()
                .orElseThrow(() -> new AuthenticationCredentialsNotFoundException("Current user login not found"));

        var articleDto=articleService.findOne(id);
        articleDto.ifPresent(articleDTO -> {
            if (SecurityUtils.hasCurrentUserThisAuthority(AuthoritiesConstants.ADMIN)||
                    Objects.equals(userLogin,articleDTO.getAuthor().getUsername())){
                articleService.delete(id);
            }else{
                throw new BadRequestException("this user can't delete this article");
            }

        });

        return ResponseEntity
                .noContent()
                .build();
    }
}
