package com.example.articlewebapp.service.dto;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Data
public class CategoryDTO {
    private Long id;
    @NotBlank(message = "Category shouldn't be blank")
    @NotNull(message = "Category shouldn't be null")
    @Size(min = 2, max = 50, message="Category Name should be at least 2 and at most 50 characters")

    private String name;

}
