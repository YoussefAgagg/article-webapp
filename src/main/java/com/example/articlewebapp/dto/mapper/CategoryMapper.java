package com.example.articlewebapp.dto.mapper;

import com.example.articlewebapp.domain.Category;

import com.example.articlewebapp.dto.CategoryDTO;
import org.mapstruct.Mapper;
/**
 * Mapper for the entity {@link Category} and its DTO {@link CategoryDTO}.
 *
 * @author Youssef Agagg
 */


@Mapper(componentModel = "spring")
public interface CategoryMapper extends EntityMapper<CategoryDTO, Category> {


}
