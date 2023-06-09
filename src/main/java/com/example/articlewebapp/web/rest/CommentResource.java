package com.example.articlewebapp.web.rest;


import com.example.articlewebapp.aop.logging.Loggable;
import com.example.articlewebapp.domain.Comment;
import com.example.articlewebapp.exception.BadRequestException;
import com.example.articlewebapp.exception.ResourceDoesntExistException;

import com.example.articlewebapp.security.AuthoritiesConstants;
import com.example.articlewebapp.security.SecurityUtils;
import com.example.articlewebapp.service.ArticleService;
import com.example.articlewebapp.service.CommentService;
import com.example.articlewebapp.service.UserService;
import com.example.articlewebapp.dto.CommentDTO;
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

/**
 * REST controller for managing {@link Comment}.
 *
 * @author Youssef Agagg
 */
@RestController
@RequestMapping("/api/{article_id}/")
@Slf4j
@RequiredArgsConstructor
public class CommentResource {


    private final CommentService commentService;
    
    private final UserService userService;
    
    private final ArticleService articleService;
    
    @PostMapping("/comments")
    @Loggable
    public ResponseEntity<CommentDTO> createComment(@Valid @RequestBody CommentDTO commentDTO) throws URISyntaxException {
        log.debug("REST request to save Comment : {}", commentDTO);
        if (commentDTO.getId() != null) {
            throw new BadRequestException("A new comment cannot already have an ID");
        }
        checkUserAndArticleAreExist(commentDTO);
        CommentDTO result = commentService.save(commentDTO);
        return ResponseEntity
            .created(new URI("/api/comments/" + result.getId()))
                .body(result);
    }

    private void checkUserAndArticleAreExist(@RequestBody @Valid CommentDTO commentDTO) {
        var user=userService.getUserById(commentDTO.getUser().getId());
        user.ifPresentOrElse(userDTO -> {
            if (!Objects.equals(userDTO.getUsername(),commentDTO.getUser().getUsername())){
                throw new BadRequestException("Invalid user ID");
            }
        },()->{throw new BadRequestException("user doesn't exist");});
        var article=articleService.findOne(commentDTO.getArticle().getId());
        article.ifPresentOrElse(articleDTO -> {
            if (!Objects.equals(articleDTO.getId(),commentDTO.getArticle().getId())){
                throw new BadRequestException("Invalid user ID ");
            }
        },()->{throw new BadRequestException("article doesn't exist");});
    }

    @PutMapping("/comments/{id}")
    @PreAuthorize("#commentDTO.user.username.toLowerCase() == authentication.principal.username")
    @Loggable
    public ResponseEntity<CommentDTO> updateComment(
        @PathVariable final Long id,
        @Valid @RequestBody CommentDTO commentDTO)  {
        log.debug("REST request to update Comment : {}, {}", id, commentDTO);
        if (commentDTO.getId() == null) {
            throw new BadRequestException("Invalid comment id is null");
        }
        if (!Objects.equals(id, commentDTO.getId())) {
            throw new BadRequestException("Invalid comment ID ");
        }
        checkUserAndArticleAreExist(commentDTO);

        return commentService.update(commentDTO)
                .map(comment->ResponseEntity.ok().body(comment))
                .orElseThrow(()->{throw new ResourceDoesntExistException("comment doesn't exist");});

    }
   
    @GetMapping("/comments")
    @Loggable
    public ResponseEntity<List<CommentDTO>> getAllComments(
            @PathVariable Long article_id,
            @PageableDefault(sort = { "dateCreated" })Pageable pageable) {
        log.debug("REST request to get all comments for an article. ");
        Page<CommentDTO> page = commentService.getAllCommentsForAnArticle(article_id,pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }
    
    
    @DeleteMapping("/comments/{id}")
    @Loggable
    public ResponseEntity<Void> deleteComment(@PathVariable Long id) {
        log.debug("REST request to delete Comment : {}", id);
        String userLogin = SecurityUtils
                .getCurrentUserUsername()
                .orElseThrow(() -> new AuthenticationCredentialsNotFoundException("Current user login not found"));

        var commentDto=commentService.findOne(id);
        commentDto.ifPresent(commentDTO -> {
            if (SecurityUtils.hasCurrentUserThisAuthority(AuthoritiesConstants.ADMIN)||
                    Objects.equals(userLogin,commentDTO.getUser().getUsername())){
                commentService.delete(id);
            }else{
                throw new BadRequestException("this user can't delete this comment");
            }

        });

        return ResponseEntity
                .noContent()
                .build();
    }
}
