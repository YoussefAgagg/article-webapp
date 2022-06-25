package com.example.articlewebapp.service.dto;

import com.example.articlewebapp.domain.enumerations.Gender;
import lombok.Data;
import lombok.ToString;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;
@Data
public class UserDTO {

    private Long id;
    @NotBlank(message = "First Name shouldn't be blank")
    @NotNull
    private String firstName;
    @NotBlank(message = "Last Name shouldn't be blank")
    @NotNull
    private String lastName;
    @NotBlank(message = "Email shouldn't be blank")
    @NotNull
    @Email(message = "Email not valid", regexp = "^\\w+([\\.-]?\\w+)*@\\w+([\\.-]?\\w+)*(\\.\\w{2,3})+$")
    private String email;
    @NotBlank(message = "Username shouldn't be blank")
    @NotNull
    @Size(min = 4, max = 50, message="Username should be at least 4 and at most 50 characters")
    private String username;
    @NotBlank(message = "Password shouldn't be blank")
    @NotNull
    private String password;

    private String mobile;

    private Gender gender;

    private String imageUrl;

    private LocalDate dateOfBirth;

    private boolean activated = false;

    private String activationKey;

    private String resetKey;

    @ToString.Exclude
    private Set<ArticleDTO> articles = new HashSet<>();

    @ToString.Exclude
    private Set<AuthorityDTO> authorities = new HashSet<>();

    @ToString.Exclude
    private Set<UserDTO> followers = new HashSet<>();

    @ToString.Exclude
    private Set<UserDTO> following = new HashSet<>();
}
