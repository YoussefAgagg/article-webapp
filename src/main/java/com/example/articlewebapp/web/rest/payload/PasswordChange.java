package com.example.articlewebapp.web.rest.payload;

import lombok.AllArgsConstructor;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

/**
 * Model object representing a password change required data - current and new password.
 *
 * @author Youssef Agagg
 */
@Data
@AllArgsConstructor
public class PasswordChange {
    @NotBlank
    @Size(min = 4, max = 50)
    private String currentPassword;
    @NotBlank
    @Size(min = 4, max = 50)
    private String newPassword;
}
