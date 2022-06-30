package com.example.articlewebapp.web.rest.payload;

import com.example.articlewebapp.domain.enumerations.Gender;
import lombok.Data;


import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.time.LocalDate;
@Data
public class UpdateUserData {
    @NotNull
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

    private String mobile;

    private Gender gender;

    private String imageUrl;

    private LocalDate dateOfBirth;

}
