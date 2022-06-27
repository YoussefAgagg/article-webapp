package com.example.articlewebapp.web.rest.payload;


import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Model object representing a LoginRequest required data - username and  password.
 *
 * @author Youssef Agagg
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public final class LoginRequest {

    @NotBlank(message = "Username shouldn't be blank")
    @Size(min = 4, max = 50, message = "Username should be at least 4 and at most 50 characters")
    private String username;

    @NotBlank(message = "Password shouldn't be blank")
    @Size(min = 4, message = "Password should be at least 4 character")
    private String password;
}
