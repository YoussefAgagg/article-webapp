package com.example.articlewebapp.dto.mapper;

import com.example.articlewebapp.domain.Authority;
import com.example.articlewebapp.dto.AuthorityDTO;
import org.mapstruct.Mapper;
/**
 * Mapper for the entity {@link Authority} and its DTO {@link AuthorityDTO}.
 *
 * @author Youssef Agagg
 */

@Mapper(componentModel = "spring")
public interface AuthorityMapper extends EntityMapper<AuthorityDTO, Authority>{
}
