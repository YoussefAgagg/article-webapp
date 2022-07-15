package com.example.articlewebapp.dto;

import lombok.Data;
import lombok.ToString;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.time.Instant;
import java.util.HashSet;
import java.util.Set;
@Data
public class ArticleDTO implements Serializable {
    private Long id;
    @NotBlank
    @NotNull
    @Size(min = 4, max = 255, message="Title should be at least 4 and at most 255 characters")
    private String title;
    @Size(max = 500, message="Summary should be at most 500 characters")
    private String summary;
    @NotBlank(message = "Article content shouldn't be blank")
    @NotNull(message = "Article must have a content")
    private String content;

    private Instant dateCreated;

    private Instant lastEdited;

    private Long views;

    @ToString.Exclude
    @NotNull
    private UserDTO author;

    private Set<CategoryDTO> categories = new HashSet<>();

    private Long likes;

    private Long dislikes;
}
