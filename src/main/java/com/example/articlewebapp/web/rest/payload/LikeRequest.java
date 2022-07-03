package com.example.articlewebapp.web.rest.payload;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Range;

import javax.validation.constraints.NotNull;

/**
 *  Model object representing a like required data - the article id and  the like_type .
 *
 * @author Youssef Agagg
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class LikeRequest {
    @NotNull
    private Long articleId;
    @NotNull
    @Range(min = 0, max = 1)
    private Integer likeType;
}
