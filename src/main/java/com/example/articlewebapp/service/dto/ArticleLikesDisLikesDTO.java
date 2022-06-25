package com.example.articlewebapp.service.dto;

import com.example.articlewebapp.domain.ArticleLikesDislikesId;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.Hibernate;

import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.io.Serializable;
import java.util.Objects;

/**
 *
 *
 *  @author Youssef Agagg
 *  @since 1.0
 */

@Data
public class ArticleLikesDisLikesDTO implements Serializable {

    private ArticleLikesDislikesId id;

    private Integer likeType;

}
