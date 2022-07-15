package com.example.articlewebapp.dto.mapper;

import com.example.articlewebapp.domain.Comment;
import com.example.articlewebapp.dto.CommentDTO;
import org.mapstruct.Mapper;
/**
 * Mapper for the entity {@link Comment} and its DTO {@link CommentDTO}.
 *
 * @author Youssef Agagg
 */

@Mapper(componentModel = "spring", uses = {UserMapper.class, ArticleMapper.class})
public interface CommentMapper extends EntityMapper<CommentDTO, Comment>{
}
