package com.example.articlewebapp.service.dto.mapper;

import com.example.articlewebapp.domain.ArticleLikesDislikesId;
import com.example.articlewebapp.service.dto.ArticleLikesDislikesIdDTO;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring", uses = {UserMapper.class,ArticleMapper.class})
public interface ArticleLikesDislikesIdMapper extends EntityMapper<ArticleLikesDislikesIdDTO, ArticleLikesDislikesId> {
}
