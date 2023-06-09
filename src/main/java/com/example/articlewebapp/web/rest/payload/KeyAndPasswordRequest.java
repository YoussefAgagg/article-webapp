package com.example.articlewebapp.web.rest.payload;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

/**
 *  Model object representing a KeyAndPasswordRequest required data - key and  new password.
 *
 * @author Youssef Agagg
 */
@Data
public class KeyAndPasswordRequest {
    private String key;
    @NotBlank
    @Size(min = 4, max = 50)
    private String newPassword;
}
