package com.example.articlewebapp.dto;

import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
public class AuthorityDTO {
    @NotNull
    private String name;
}
