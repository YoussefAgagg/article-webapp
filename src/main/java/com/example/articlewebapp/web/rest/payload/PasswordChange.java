package com.example.articlewebapp.web.rest.payload;

import lombok.Data;
/**
 * Model object representing a password change required data - current and new password.
 *
 * @author Youssef Agagg
 */
@Data
public class PasswordChange {
    private String currentPassword;
    private String newPassword;
}
