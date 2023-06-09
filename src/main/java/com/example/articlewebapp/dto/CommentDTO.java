package com.example.articlewebapp.dto;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.time.Instant;

/**
 *  @author Mohamed Ehab Ali
 *  @since 1.0
 */

@Data
public class CommentDTO implements Serializable {

    private Long id;
    @NotBlank(message = "Comment shouldn't be blank")
    @NotNull(message = "Comment shouldn't be null")
    @Size(min = 1, max = 500, message="Comment should be at least 1 and at most 500 characters")
    private String text;

    private Instant dateCreated;

    private Instant lastEdited;
    @NotNull
    private UserDTO user;
    @NotNull
    private ArticleDTO article;

}
