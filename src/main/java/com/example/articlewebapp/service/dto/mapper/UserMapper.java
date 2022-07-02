package com.example.articlewebapp.service.dto.mapper;

import com.example.articlewebapp.domain.User;
import com.example.articlewebapp.service.dto.UserDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

/**
 * Mapper for the entity {@link User} and its DTO {@link UserDTO}.
 *
 * @author Youssef Agagg
 */

@Mapper(componentModel = "spring")
public interface UserMapper extends EntityMapper<UserDTO, User>{

}
