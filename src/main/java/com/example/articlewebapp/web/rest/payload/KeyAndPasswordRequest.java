package com.example.articlewebapp.web.rest.payload;

import lombok.Data;
/**
 *  Model object representing a KeyAndPasswordRequest required data - key and  new password.
 *
 * @author Youssef Agagg
 */
@Data
public class KeyAndPasswordRequest {
    private String key;

    private String newPassword;
}
