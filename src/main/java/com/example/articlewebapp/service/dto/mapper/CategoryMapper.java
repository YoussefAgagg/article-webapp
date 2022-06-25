package com.example.articlewebapp.service.dto.mapper;

import com.example.articlewebapp.domain.Category;

import com.example.articlewebapp.domain.User;
import com.example.articlewebapp.service.dto.CategoryDTO;
import com.example.articlewebapp.service.dto.UserDTO;
import org.mapstruct.Mapper;
/**
 * Mapper for the entity {@link Category} and its DTO {@link CategoryDTO}.
 *
 * @author Youssef Agagg
 */


@Mapper(componentModel = "spring")
public interface CategoryMapper extends EntityMapper<CategoryDTO, Category> {


}
