package com.example.articlewebapp.service.dto.mapper;

import com.example.articlewebapp.domain.ArticleLikesDisLikes;
import com.example.articlewebapp.service.dto.ArticleLikesDisLikesDTO;
import org.mapstruct.Mapper;
/**
 * Mapper for the entity {@link ArticleLikesDisLikes} and its DTO {@link ArticleLikesDisLikesDTO}.
 *
 * @author Youssef Agagg
 */

@Mapper(componentModel = "spring", uses = {ArticleLikesDislikesIdMapper.class})
public interface ArticleLikesDisLikesMapper extends EntityMapper<ArticleLikesDisLikesDTO, ArticleLikesDisLikes>{
}
