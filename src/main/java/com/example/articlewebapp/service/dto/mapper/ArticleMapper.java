package com.example.articlewebapp.service.dto.mapper;

import com.example.articlewebapp.domain.Article;
import com.example.articlewebapp.service.dto.ArticleDTO;
import org.mapstruct.Mapper;
/**
 * Mapper for the entity {@link Article} and its DTO {@link ArticleDTO}.
 *
 * @author Youssef Agagg
 */

@Mapper(componentModel = "spring", uses = {CategoryMapper.class,UserMapper.class})
public interface ArticleMapper extends EntityMapper<ArticleDTO, Article>{

}
