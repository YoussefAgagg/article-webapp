package com.example.articlewebapp.web.rest;

import com.example.articlewebapp.domain.User;
import com.example.articlewebapp.repository.UserRepository;
import com.example.articlewebapp.security.AuthoritiesConstants;
import com.example.articlewebapp.service.dto.mapper.UserMapper;
import com.example.articlewebapp.web.rest.payload.UpdateUserData;
import liquibase.repackaged.net.sf.jsqlparser.util.validation.metadata.NamedObject;
import liquibase.repackaged.org.apache.commons.lang3.RandomStringUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import java.time.Instant;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.function.Consumer;

import static com.example.articlewebapp.web.rest.UserResourceIT.DEFAULT_USERNAME;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Integration tests for the {@link UserResource} REST controller.
 *
 * @author Youssef Agagg
 */
@AutoConfigureMockMvc
@SpringBootTest
class UserResourceIT {

    public static final String DEFAULT_USERNAME = "youssef";

    static final String DEFAULT_EMAIL = "youssef@gmail.com";
    private static final String UPDATED_EMAIL = "jo@gmail.com";

    static final String DEFAULT_FIRSTNAME = "youssef";
    private static final String UPDATED_FIRSTNAME = "jo";

    static final String DEFAULT_LASTNAME = "mohamed";
    private static final String UPDATED_LASTNAME = "agagg";



    @Autowired
    private UserRepository userRepository;


    @Autowired
    private MockMvc restUserMockMvc;

    private User user;


    @BeforeEach
    public void initTest() {
        user = new User();
        user.setUsername(DEFAULT_USERNAME );
        user.setPassword("jskdjsi");
        user.setActivated(true);
        user.setEmail(DEFAULT_EMAIL);
        user.setFirstName(DEFAULT_FIRSTNAME);
        user.setLastName(DEFAULT_LASTNAME);
        user.setUsername(DEFAULT_USERNAME);
        user.setEmail(DEFAULT_EMAIL);

    }

    @Test
    @Transactional
    @WithMockUser(username= DEFAULT_USERNAME,authorities ={"ROLE_ADMIN"})
    void getAllUsers() throws Exception {

        userRepository.saveAndFlush(user);
        restUserMockMvc
            .perform(get("/api/users?sort=id,desc").accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].username").value(hasItem(DEFAULT_USERNAME)))
            .andExpect(jsonPath("$.[*].firstName").value(hasItem(DEFAULT_FIRSTNAME)))
            .andExpect(jsonPath("$.[*].lastName").value(hasItem(DEFAULT_LASTNAME)))
            .andExpect(jsonPath("$.[*].email").value(hasItem(DEFAULT_EMAIL)));
    }

    @Test
    @Transactional
    void getUser() throws Exception {

        userRepository.saveAndFlush(user);

        restUserMockMvc
            .perform(get("/api/users/{username}", user.getUsername()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.username").value(user.getUsername()))
            .andExpect(jsonPath("$.firstName").value(DEFAULT_FIRSTNAME))
            .andExpect(jsonPath("$.lastName").value(DEFAULT_LASTNAME))
            .andExpect(jsonPath("$.email").value(DEFAULT_EMAIL));
    }

    @Test
    @Transactional
    void getNonExistingUser() throws Exception {
        restUserMockMvc.perform(get("/api/users/unknown")).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    @WithMockUser(username= DEFAULT_USERNAME,authorities ={"ROLE_USER"})
    void updateUser() throws Exception {
        userRepository.saveAndFlush(user);
        int databaseSizeBeforeUpdate = userRepository.findAll().size();

        User updatedUser = userRepository.findById(user.getId()).get();

        UpdateUserData userData = new UpdateUserData();
        userData.setId(updatedUser.getId());
        userData.setUsername(updatedUser.getUsername());
        userData.setFirstName(UPDATED_FIRSTNAME);
        userData.setLastName(UPDATED_LASTNAME);
        userData.setEmail(UPDATED_EMAIL);

        restUserMockMvc
            .perform(
                put("/api/users/"+DEFAULT_USERNAME).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(userData))
            )
            .andExpect(status().isOk());


        assertPersistedUsers(users -> {
            assertThat(users).hasSize(databaseSizeBeforeUpdate);
            User testUser = users.stream().filter(usr -> usr.getId().equals(updatedUser.getId())).findFirst().get();
            assertThat(testUser.getFirstName()).isEqualTo(UPDATED_FIRSTNAME);
            assertThat(testUser.getLastName()).isEqualTo(UPDATED_LASTNAME);
            assertThat(testUser.getEmail()).isEqualTo(UPDATED_EMAIL);
        });
    }

    @Test
    @Transactional
    @WithMockUser(username= "Lolo",authorities ={"ROLE_USER"})
    void updateUserWithOtherUserAccount() throws Exception {

        userRepository.saveAndFlush(user);
        UpdateUserData userData = new UpdateUserData();
        userData.setId(user.getId());
        userData.setFirstName(UPDATED_FIRSTNAME);
        userData.setLastName(UPDATED_LASTNAME);
        userData.setEmail(UPDATED_EMAIL);
        userData.setUsername(user.getUsername());

        restUserMockMvc
            .perform(
                put("/api/users/"+DEFAULT_USERNAME).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(userData))
            )
            .andExpect(status().isForbidden());

    }

    @Test
    @Transactional
    @WithMockUser(username= DEFAULT_USERNAME,authorities ={"ROLE_USER"})
    void updateUserExistingEmail() throws Exception {

        userRepository.saveAndFlush(user);

        User anotherUser = new User();
        anotherUser.setUsername("lolo");
        anotherUser.setPassword(RandomStringUtils.random(60));
        anotherUser.setActivated(true);
        anotherUser.setEmail("lol@localhost.com");
        anotherUser.setFirstName("java");
        anotherUser.setLastName("ds");
        anotherUser.setImageUrl("");
        userRepository.saveAndFlush(anotherUser);

        User updatedUser = userRepository.findById(user.getId()).get();

        UpdateUserData userData = new UpdateUserData();
        userData.setId(updatedUser.getId());
        userData.setUsername(updatedUser.getUsername());
        userData.setFirstName(updatedUser.getFirstName());
        userData.setLastName(updatedUser.getLastName());
        userData.setEmail("lol@localhost.com"); // this email should already be used by anotherUser
        userData.setImageUrl(updatedUser.getImageUrl());

        restUserMockMvc
            .perform(
                put("/api/users/"+DEFAULT_USERNAME).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(userData))
            )
            .andExpect(status().isBadRequest());
    }

    @Test
    @Transactional
    @WithMockUser(username= DEFAULT_USERNAME,authorities ={"ROLE_USER"})
    void updateUserUsername() throws Exception {
        userRepository.saveAndFlush(user);
        user.setUsername("newUser");
        restUserMockMvc
            .perform(
                put("/api/users/"+DEFAULT_USERNAME).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(user))
            )
            .andExpect(status().isBadRequest());
    }

    @Test
    @Transactional
    @WithMockUser(username= DEFAULT_USERNAME,authorities ={"ROLE_USER"})
    void deleteUser() throws Exception {

        userRepository.saveAndFlush(user);
        int databaseSizeBeforeDelete = userRepository.findAll().size();


        restUserMockMvc
            .perform(delete("/api/users/{username}", user.getUsername()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        assertPersistedUsers(users -> assertThat(users).hasSize(databaseSizeBeforeDelete - 1));
    }


    private void assertPersistedUsers(Consumer<List<User>> userAssertion) {
        userAssertion.accept(userRepository.findAll());
    }
}
