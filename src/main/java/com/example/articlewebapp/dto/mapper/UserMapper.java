package com.example.articlewebapp.dto.mapper;

import com.example.articlewebapp.domain.User;
import com.example.articlewebapp.dto.UserDTO;
import org.mapstruct.Mapper;

/**
 * Mapper for the entity {@link User} and its DTO {@link UserDTO}.
 *
 * @author Youssef Agagg
 */

@Mapper(componentModel = "spring")
public interface UserMapper extends EntityMapper<UserDTO, User>{

}
