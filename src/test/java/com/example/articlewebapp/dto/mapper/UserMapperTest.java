package com.example.articlewebapp.dto.mapper;

import com.example.articlewebapp.domain.Authority;
import com.example.articlewebapp.domain.User;
import com.example.articlewebapp.dto.UserDTO;
import com.example.articlewebapp.dto.mapper.UserMapper;
import liquibase.repackaged.org.apache.commons.lang3.RandomStringUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

class UserMapperTest {

    private UserMapper userMapper;
    private User user;
    private UserDTO userDto;
    @BeforeEach
    void setUp() {
        userMapper = Mappers.getMapper(UserMapper.class);;
        user = new User();
        user.setUsername("user1");
        user.setPassword(RandomStringUtils.random(60));
        user.setActivated(true);
        user.setEmail("youssef@gmail.com");
        user.setFirstName("youssef");
        user.setLastName("agagg");
        user.setImageUrl("image_url");
        user.setDateOfBirth(LocalDate.MIN);


        userDto = new UserDTO();
        userDto.setUsername("user2");
        userDto.setPassword(RandomStringUtils.random(60));
        userDto.setActivated(true);
        userDto.setEmail("youssef22@gmail.com");
        userDto.setFirstName("youssef2");
        userDto.setLastName("agagg2");
        userDto.setImageUrl("image_url2");
        userDto.setDateOfBirth(LocalDate.MAX);
    }

    @Test
    void usersToUserDTOs() {
        List<User> users = new ArrayList<>();
        users.add(user);

        List<UserDTO> userDTOS = userMapper.toDTOs(users);

        assertThat(userDTOS).isNotEmpty().size().isEqualTo(1);
    }

    @Test
    void userDTOsToUsers() {
        List<UserDTO> usersDto = new ArrayList<>();
        usersDto.add(userDto);

        List<User> users = userMapper.toEntities(usersDto);

        assertThat(users).isNotEmpty().size().isEqualTo(1);
    }
    @Test
    void userToUserDTO() {
        List<User> users = new ArrayList<>();
        users.add(user);

        UserDTO userDTO = userMapper.toDto(user);

        assertThat(userDTO).isNotNull();
        assertThat(userDTO.getFirstName()).isEqualTo("youssef");
    }

    @Test
    void userDTOToUser() {


        User user= userMapper.toEntity(userDto);

        assertThat(user).isNotNull();
        assertThat(user.getFirstName()).isEqualTo("youssef2");
    }
    @Test
    void usersToUserDTOsWithAuthoritiesShouldMapToUserDTOsWithAuthoritiesDTO() {
        Set<Authority> authorities = new HashSet<>();
        Authority authority=new Authority();
        authority.setName("ADMIN");
        authorities.add(authority);
        user.setAuthorities(authorities);

        List<User> usersDto = new ArrayList<>();
        usersDto.add(user);

        List<UserDTO> userDTOS = userMapper.toDTOs(usersDto);

        assertThat(userDTOS).isNotEmpty().size().isEqualTo(1);
        assertThat(userDTOS.get(0).getAuthorities()).isNotNull();
        assertThat(userDTOS.get(0).getAuthorities()).isNotEmpty();
        assertThat(userDTOS.get(0).getAuthorities().iterator().next().getName()).isEqualTo("ADMIN");
    }


    @Test
    void userToUserDTOMapWithAuthoritiesShouldReturnUserDTOWithAuthorities() {
        Set<Authority> authorities = new HashSet<>();
        Authority authority=new Authority();
        authority.setName("ADMIN");
        authorities.add(authority);
        user.setAuthorities(authorities);

        UserDTO userDTO = userMapper.toDto(user);

        assertThat(userDTO).isNotNull();
        assertThat(userDTO.getAuthorities()).isNotNull();
        assertThat(userDTO.getAuthorities()).isNotEmpty();
        assertThat(userDTO.getAuthorities().iterator().next().getName()).isEqualTo("ADMIN");
    }



    @Test
    void userDTOToUserMapWithNullUserShouldReturnNull() {
        assertThat(userMapper.toEntity(null)).isNull();
    }


}