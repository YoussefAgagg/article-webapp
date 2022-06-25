package com.example.articlewebapp.service.dto.mapper;

import com.example.articlewebapp.domain.Authority;
import com.example.articlewebapp.domain.User;
import com.example.articlewebapp.service.dto.AuthorityDTO;
import com.example.articlewebapp.service.dto.UserDTO;
import org.mapstruct.Mapper;
/**
 * Mapper for the entity {@link Authority} and its DTO {@link AuthorityDTO}.
 *
 * @author Youssef Agagg
 */

@Mapper(componentModel = "spring")
public interface AuthorityMapper extends EntityMapper<AuthorityDTO, Authority>{
}
