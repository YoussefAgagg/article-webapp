package com.example.articlewebapp.service.dto;

import com.example.articlewebapp.domain.Authority;
import com.example.articlewebapp.domain.enumerations.Gender;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

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
    @Size(min = 4, max = 50)
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private String password;

    private String mobile;

    private Gender gender;

    private String imageUrl;

    private LocalDate dateOfBirth;

    private boolean activated = false;
    @JsonIgnore
    private String activationKey;
    @JsonIgnore
    private String resetKey;


    private Set<AuthorityDTO> authorities = new HashSet<>();



}
