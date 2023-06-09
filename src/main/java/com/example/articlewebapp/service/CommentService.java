package com.example.articlewebapp.service;


import com.example.articlewebapp.aop.logging.Loggable;
import com.example.articlewebapp.domain.Comment;
import com.example.articlewebapp.repository.CommentRepository;
import com.example.articlewebapp.dto.CommentDTO;
import com.example.articlewebapp.dto.mapper.CommentMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.Optional;



/**
 * Service Implementation for managing {@link Comment}.
 *
 * @author Youssef Agagg
 */
@Service
@Transactional
@Slf4j
@RequiredArgsConstructor
public class CommentService {

    private final CommentRepository commentRepository;

    private final CommentMapper commentMapper;

    @Loggable
    public CommentDTO save(CommentDTO commentDTO) {
        log.debug("Request to save Comment : {}", commentDTO);
        commentDTO.setDateCreated(Instant.now());
        Comment comment = commentMapper.toEntity(commentDTO);
        comment = commentRepository.save(comment);
        return commentMapper.toDto(comment);
    }


    @Loggable
    public void delete(Long id) {
        log.debug("Request to delete Comment : {}", id);
        commentRepository.deleteById(id);
    }

    @Loggable
    public Optional<CommentDTO> update(CommentDTO commentDTO) {
        return commentRepository
                .findById(commentDTO.getId())
                .map(existingComment-> {
                    commentDTO.setLastEdited(Instant.now());
                    commentMapper.partialUpdate(existingComment, commentDTO);
                    return existingComment;
                })
                .map(commentRepository::save)
                .map(commentMapper::toDto);
    }
    @Loggable
    public Page<CommentDTO> getAllCommentsForAnArticle(Long article_id, Pageable pageable) {
        return commentRepository.findAllByArticleId(article_id,pageable).map(commentMapper::toDto);
    }

    @Transactional(readOnly = true)
    @Loggable
    public Optional<CommentDTO> findOne(Long id) {
        log.debug("Request to get Comment : {}", id);
        return commentRepository.findById(id).map(commentMapper::toDto);
    }
}
