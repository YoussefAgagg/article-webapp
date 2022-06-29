package com.example.articlewebapp.web.rest;

import com.example.articlewebapp.domain.User;
import com.example.articlewebapp.repository.AuthorityRepository;
import com.example.articlewebapp.repository.UserRepository;
import com.example.articlewebapp.security.AuthoritiesConstants;
import com.example.articlewebapp.service.UserService;
import com.example.articlewebapp.service.dto.AuthorityDTO;
import com.example.articlewebapp.service.dto.UserDTO;

import com.example.articlewebapp.web.rest.payload.KeyAndPasswordRequest;
import com.example.articlewebapp.web.rest.payload.PasswordChange;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.Collections;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Integration tests for the {@link AccountResource} REST controller.
 */
@AutoConfigureMockMvc
@WithMockUser
@SpringBootTest
class AccountResourceIT {


    @Autowired
    private UserRepository userRepository;

    @Autowired
    private AuthorityRepository authorityRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private MockMvc restAccountMockMvc;


    @Test
    @Transactional
    void testRegisterValid() throws Exception {
        UserDTO validUser = new UserDTO();
        validUser.setUsername("test-register-valid");
        validUser.setPassword("password");
        validUser.setFirstName("Alice");
        validUser.setLastName("Test");
        validUser.setEmail("test-register-valid@example.com");
        validUser.setImageUrl("http://placehold.it/50x50");
        AuthorityDTO authority=new AuthorityDTO();
        authority.setName(AuthoritiesConstants.ADMIN);
        validUser.setAuthorities(Collections.singleton(authority));
        assertThat(userRepository.findOneByUsername("test-register-valid")).isEmpty();

        restAccountMockMvc
            .perform(post("/api/register").contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(validUser)))
            .andExpect(status().isCreated());

        assertThat(userRepository.findOneByUsername("test-register-valid")).isPresent();
    }

    @Test
    @Transactional
    void testRegisterInvalidUsername() throws Exception {
        UserDTO invalidUser = new UserDTO();
        invalidUser.setUsername("gfd"); // <-- invalid
        invalidUser.setPassword("password");
        invalidUser.setFirstName("Funky");
        invalidUser.setLastName("One");
        invalidUser.setEmail("funky@example.com");
        invalidUser.setActivated(true);

        AuthorityDTO authority=new AuthorityDTO();
        authority.setName(AuthoritiesConstants.ADMIN);
        invalidUser.setAuthorities(Collections.singleton(authority));
        restAccountMockMvc
            .perform(post("/api/register").contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(invalidUser)))
            .andExpect(status().isBadRequest());

        Optional<User> user = userRepository.findOneByEmailIgnoreCase("funky@example.com");
        assertThat(user).isEmpty();
    }

    @Test
    @Transactional
    void testRegisterInvalidEmail() throws Exception {
        UserDTO invalidUser = new UserDTO();
        invalidUser.setUsername("bob1");
        invalidUser.setPassword("password");
        invalidUser.setFirstName("Bob");
        invalidUser.setLastName("Green");
        invalidUser.setEmail("invalid"); // <-- invalid
        invalidUser.setActivated(true);
        invalidUser.setImageUrl("http://placehold.it/50x50");
        AuthorityDTO authority=new AuthorityDTO();
        authority.setName(AuthoritiesConstants.ADMIN);
        invalidUser.setAuthorities(Collections.singleton(authority));
        restAccountMockMvc
            .perform(post("/api/register").contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(invalidUser)))
            .andExpect(status().isBadRequest());

        Optional<User> user = userRepository.findOneByUsername("bob1");
        assertThat(user).isEmpty();
    }

    @Test
    @Transactional
    void testRegisterInvalidPassword() throws Exception {
        UserDTO invalidUser = new UserDTO();
        invalidUser.setUsername("bob1");
        invalidUser.setPassword("123"); // password with only 3 digits
        invalidUser.setFirstName("Bob");
        invalidUser.setLastName("Green");
        invalidUser.setEmail("bob@example.com");
        invalidUser.setActivated(true);
        AuthorityDTO authority=new AuthorityDTO();
        authority.setName(AuthoritiesConstants.ADMIN);
        invalidUser.setAuthorities(Collections.singleton(authority));
        restAccountMockMvc
            .perform(post("/api/register").contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(invalidUser)))
            .andExpect(status().isBadRequest());

        Optional<User> user = userRepository.findOneByUsername("bob1");
        assertThat(user).isEmpty();
    }

    @Test
    @Transactional
    void testRegisterNullPassword() throws Exception {
        UserDTO invalidUser = new UserDTO();
        invalidUser.setUsername("bob1");
        invalidUser.setPassword(null); // invalid null password
        invalidUser.setFirstName("Bob");
        invalidUser.setLastName("Green");
        invalidUser.setEmail("bob@example.com");
       
        AuthorityDTO authority=new AuthorityDTO();
        authority.setName(AuthoritiesConstants.USER);
        invalidUser.setAuthorities(Collections.singleton(authority));

        restAccountMockMvc
            .perform(post("/api/register").contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(invalidUser)))
            .andExpect(status().isBadRequest());

        Optional<User> user = userRepository.findOneByUsername("bob1");
        assertThat(user).isEmpty();
    }

    @Test
    @Transactional
    void testRegisterDuplicateUsername() throws Exception {
        // First registration
        UserDTO firstUser = new UserDTO();
        firstUser.setUsername("alice");
        firstUser.setPassword("password");
        firstUser.setFirstName("Alice");
        firstUser.setLastName("Something");
        firstUser.setEmail("alice@example.com");
        firstUser.setImageUrl("http://placehold.it/50x50");
       
        AuthorityDTO authority=new AuthorityDTO();
        authority.setName(AuthoritiesConstants.USER);
        firstUser.setAuthorities(Collections.singleton(authority));

        // Duplicate username, different email
        UserDTO secondUser = new UserDTO();
        secondUser.setUsername(firstUser.getUsername());
        secondUser.setPassword(firstUser.getPassword());
        secondUser.setFirstName(firstUser.getFirstName());
        secondUser.setLastName(firstUser.getLastName());
        secondUser.setEmail("alice2@example.com");
        secondUser.setImageUrl(firstUser.getImageUrl());
      
        secondUser.setAuthorities(new HashSet<>(firstUser.getAuthorities()));

        // First user
        restAccountMockMvc
            .perform(post("/api/register").contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(firstUser)))
            .andExpect(status().isCreated());

        // Second (non activated) user
        restAccountMockMvc
            .perform(post("/api/register").contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(secondUser)))
            .andExpect(status().is4xxClientError());


    }

    @Test
    @Transactional
    void testRegisterDuplicateEmail() throws Exception {
        // First user
        UserDTO firstUser = new UserDTO();
        firstUser.setUsername("test-register-duplicate-email");
        firstUser.setPassword("password");
        firstUser.setFirstName("Alice");
        firstUser.setLastName("Test");
        firstUser.setEmail("test-register-duplicate-email@example.com");
        firstUser.setImageUrl("http://placehold.it/50x50");
        
        AuthorityDTO authority=new AuthorityDTO();
        authority.setName(AuthoritiesConstants.USER);
        firstUser.setAuthorities(Collections.singleton(authority));

        // Register first user
        restAccountMockMvc
            .perform(post("/api/register").contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(firstUser)))
            .andExpect(status().isCreated());

        Optional<User> testUser1 = userRepository.findOneByUsername("test-register-duplicate-email");
        assertThat(testUser1).isPresent();

        // Duplicate email, different username
        UserDTO secondUser = new UserDTO();
        secondUser.setUsername("test-register-duplicate-email-2");
        secondUser.setPassword(firstUser.getPassword());
        secondUser.setFirstName(firstUser.getFirstName());
        secondUser.setLastName(firstUser.getLastName());
        secondUser.setEmail(firstUser.getEmail());
        secondUser.setImageUrl(firstUser.getImageUrl());
        secondUser.setAuthorities(new HashSet<>(firstUser.getAuthorities()));

        // Register second
        restAccountMockMvc
            .perform(post("/api/register").contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(secondUser)))
            .andExpect(status().is4xxClientError());

        Optional<User> testUser2 = userRepository.findOneByUsername("test-register-duplicate-email");
        assertThat(testUser2).isPresent();

        Optional<User> testUser3 = userRepository.findOneByUsername("test-register-duplicate-email-2");
        assertThat(testUser3).isEmpty();

        // Duplicate email - with uppercase email address
        UserDTO userWithUpperCaseEmail = new UserDTO();
        userWithUpperCaseEmail.setId(firstUser.getId());
        userWithUpperCaseEmail.setUsername("test-register-duplicate-email-3");
        userWithUpperCaseEmail.setPassword(firstUser.getPassword());
        userWithUpperCaseEmail.setFirstName(firstUser.getFirstName());
        userWithUpperCaseEmail.setLastName(firstUser.getLastName());
        userWithUpperCaseEmail.setEmail("TEST-register-duplicate-email@example.com");
        userWithUpperCaseEmail.setImageUrl(firstUser.getImageUrl());
        
        userWithUpperCaseEmail.setAuthorities(new HashSet<>(firstUser.getAuthorities()));

        // Register third
        restAccountMockMvc
            .perform(
                post("/api/register")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(userWithUpperCaseEmail))
            )
            .andExpect(status().is4xxClientError());

        Optional<User> testUser4 = userRepository.findOneByUsername("test-register-duplicate-email-3");
        assertThat(testUser4).isEmpty();
    }

    @Test
    @Transactional
    void testRegisterAdminIsIgnored() throws Exception {
        UserDTO validUser = new UserDTO();
        validUser.setUsername("badguy");
        validUser.setPassword("password");
        validUser.setFirstName("Bad");
        validUser.setLastName("Guy");
        validUser.setEmail("badguy@example.com");
        validUser.setActivated(true);
        validUser.setImageUrl("http://placehold.it/50x50");
             AuthorityDTO authority=new AuthorityDTO();
        authority.setName(AuthoritiesConstants.ADMIN);
        validUser.setAuthorities(Collections.singleton(authority));

        restAccountMockMvc
            .perform(post("/api/register").contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(validUser)))
            .andExpect(status().isCreated());

        Optional<User> userDup = userRepository.findOneWithAuthoritiesByUsername("badguy");
        assertThat(userDup).isPresent();
        assertThat(userDup.get().getAuthorities())
            .hasSize(1)
            .containsExactly(authorityRepository.findById(AuthoritiesConstants.USER).get());
    }

    @Test
    @Transactional
    void testActivateAccount() throws Exception {
        final String activationKey = "some activation key";
        User user = new User();
        user.setUsername("activate-account");
        user.setEmail("activate-account@example.com");
        user.setPassword("KLSIESKS");
        user.setActivated(false);
        user.setActivationKey(activationKey);
        user.setLastName("active");
        user.setFirstName("acc");
        userRepository.saveAndFlush(user);

        restAccountMockMvc.perform(get("/api/activate?key={activationKey}", activationKey)).andExpect(status().isOk());

        user = userRepository.findOneByUsername(user.getUsername()).orElse(null);
        assertThat(user.isActivated()).isTrue();
    }

    @Test
    @Transactional
    void testActivateAccountWithWrongKey() throws Exception {
        restAccountMockMvc.perform(get("/api/activate?key=wrongActivationKey")).andExpect(status().isInternalServerError());
    }

    @Test
    @Transactional
    @WithMockUser("change-password-wrong-existing-password")
    void testChangePasswordWrongExistingPassword() throws Exception {
        User user = new User();
        String currentPassword = "HEKLSPX";
        user.setPassword(passwordEncoder.encode(currentPassword));
        user.setUsername("change-password-wrong-existing-password");
        user.setEmail("change-password-wrong-existing-password@example.com");
        user.setLastName("change");
        user.setFirstName("password");
        userRepository.saveAndFlush(user);

        restAccountMockMvc
            .perform(
                post("/api/account/change-password")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(new PasswordChange("1" + currentPassword, "new password")))
            )
            .andExpect(status().isBadRequest());

        User updatedUser = userRepository.findOneByUsername("change-password-wrong-existing-password").orElse(null);
        assertThat(passwordEncoder.matches("new password", updatedUser.getPassword())).isFalse();
        assertThat(passwordEncoder.matches(currentPassword, updatedUser.getPassword())).isTrue();
    }

    @Test
    @Transactional
    @WithMockUser("change-password")
    void testChangePassword() throws Exception {
        User user = new User();
        String currentPassword = "FJDKEUS";
        user.setPassword(passwordEncoder.encode(currentPassword));
        user.setUsername("change-password");
        user.setEmail("change-password@example.com");
        user.setLastName("change");
        user.setFirstName("password");
        userRepository.saveAndFlush(user);

        restAccountMockMvc
            .perform(
                post("/api/account/change-password")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(new PasswordChange(currentPassword, "new password")))
            )
            .andExpect(status().isOk());

        User updatedUser = userRepository.findOneByUsername("change-password").orElse(null);
        assertThat(passwordEncoder.matches("new password", updatedUser.getPassword())).isTrue();
    }

    @Test
    @Transactional
    @WithMockUser("change-password-too-small")
    void testChangePasswordTooSmall() throws Exception {
        User user = new User();
        String currentPassword = "DSSDE";
        user.setPassword(passwordEncoder.encode(currentPassword));
        user.setUsername("change-password-too-small");
        user.setEmail("change-password-too-small@example.com");
        user.setLastName("change");
        user.setFirstName("password");
        userRepository.saveAndFlush(user);

        String newPassword = "ds";

        restAccountMockMvc
            .perform(
                post("/api/account/change-password")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(new PasswordChange(currentPassword, newPassword)))
            )
            .andExpect(status().isBadRequest());

        User updatedUser = userRepository.findOneByUsername("change-password-too-small").orElse(null);
        assertThat(updatedUser.getPassword()).isEqualTo(user.getPassword());
    }

    @Test
    @Transactional
    @WithMockUser("change-password-too-long")
    void testChangePasswordTooLong() throws Exception {
        User user = new User();
        String currentPassword = "dseews";
        user.setPassword(passwordEncoder.encode(currentPassword));
        user.setUsername("change-password-too-long");
        user.setEmail("change-password-too-long@example.com");
        user.setLastName("change");
        user.setFirstName("password");
        userRepository.saveAndFlush(user);

        String newPassword = "aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaasssssssssssssssssssswwwwwwwwwwwwwwwwwwwwaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaawwwwwwwwwwwwwwwwwwwwwwwwddddddddddddd";

        restAccountMockMvc
            .perform(
                post("/api/account/change-password")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(new PasswordChange(currentPassword, newPassword)))
            )
            .andExpect(status().isBadRequest());

        User updatedUser = userRepository.findOneByUsername("change-password-too-long").orElse(null);
        assertThat(updatedUser.getPassword()).isEqualTo(user.getPassword());
    }

    @Test
    @Transactional
    @WithMockUser("change-password-empty")
    void testChangePasswordEmpty() throws Exception {
        User user = new User();
        String currentPassword = "dkseo";
        user.setPassword(passwordEncoder.encode(currentPassword));
        user.setUsername("change-password-empty");
        user.setEmail("change-password-empty@example.com");
        user.setLastName("change");
        user.setFirstName("password");
        userRepository.saveAndFlush(user);

        restAccountMockMvc
            .perform(
                post("/api/account/change-password")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(new PasswordChange(currentPassword, "")))
            )
            .andExpect(status().isBadRequest());

        User updatedUser = userRepository.findOneByUsername("change-password-empty").orElse(null);
        assertThat(updatedUser.getPassword()).isEqualTo(user.getPassword());
    }

    @Test
    @Transactional
    void testRequestPasswordReset() throws Exception {
        User user = new User();
        user.setPassword("dsesa");
        user.setActivated(true);
        user.setUsername("password-reset");
        user.setEmail("password-reset@example.com");
        user.setLastName("rest");
        user.setFirstName("password");
        userRepository.saveAndFlush(user);

        restAccountMockMvc
            .perform(post("/api/account/reset-password/init").content("password-reset@example.com"))
            .andExpect(status().isOk());
    }

    @Test
    @Transactional
    void testRequestPasswordResetUpperCaseEmail() throws Exception {
        User user = new User();
        user.setPassword("jdksk");
        user.setActivated(true);
        user.setUsername("password-reset-upper-case");
        user.setEmail("password-reset-upper-case@example.com");
        user.setLastName("rest");
        user.setFirstName("password");
        userRepository.saveAndFlush(user);

        restAccountMockMvc
            .perform(post("/api/account/reset-password/init").content("password-reset-upper-case@EXAMPLE.COM"))
            .andExpect(status().isOk());
    }

    @Test
    void testRequestPasswordResetWrongEmail() throws Exception {
        restAccountMockMvc
            .perform(post("/api/account/reset-password/init").content("password-reset-wrong-email@example.com"))
            .andExpect(status().isOk());
    }

    @Test
    @Transactional
    void testFinishPasswordReset() throws Exception {
        User user = new User();
        user.setPassword("kdlsks");
        user.setUsername("finish-password-reset");
        user.setEmail("finish-password-reset@example.com");
        user.setLastName("finsh");
        user.setFirstName("password");

        user.setResetKey("reset key");
        userRepository.saveAndFlush(user);

        KeyAndPasswordRequest keyAndPassword = new KeyAndPasswordRequest();
        keyAndPassword.setKey(user.getResetKey());
        keyAndPassword.setNewPassword("new password");

        restAccountMockMvc
            .perform(
                post("/api/account/reset-password/finish")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(keyAndPassword))
            )
            .andExpect(status().isOk());

        User updatedUser = userRepository.findOneByUsername(user.getUsername()).orElse(null);
        assertThat(passwordEncoder.matches(keyAndPassword.getNewPassword(), updatedUser.getPassword())).isTrue();
    }

    @Test
    @Transactional
    void testFinishPasswordResetTooSmall() throws Exception {
        User user = new User();
        user.setPassword("klsls");
        user.setUsername("finish-password-reset-too-small");
        user.setEmail("finish-password-reset-too-small@example.com");
        user.setLastName("finsh");
        user.setFirstName("password");
        user.setResetKey("reset key too small");
        userRepository.saveAndFlush(user);

        KeyAndPasswordRequest keyAndPassword = new KeyAndPasswordRequest();
        keyAndPassword.setKey(user.getResetKey());
        keyAndPassword.setNewPassword("foo");

        restAccountMockMvc
            .perform(
                post("/api/account/reset-password/finish")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(keyAndPassword))
            )
            .andExpect(status().isBadRequest());

        User updatedUser = userRepository.findOneByUsername(user.getUsername()).orElse(null);
        assertThat(passwordEncoder.matches(keyAndPassword.getNewPassword(), updatedUser.getPassword())).isFalse();
    }

    @Test
    @Transactional
    void testFinishPasswordResetWrongKey() throws Exception {
        KeyAndPasswordRequest keyAndPassword = new KeyAndPasswordRequest();
        keyAndPassword.setKey("wrong reset key");
        keyAndPassword.setNewPassword("new password");

        restAccountMockMvc
            .perform(
                post("/api/account/reset-password/finish")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(keyAndPassword))
            )
            .andExpect(status().isInternalServerError());
    }
}
