package com.example.articlewebapp.service.dto;

import com.example.articlewebapp.domain.Article;
import com.example.articlewebapp.domain.User;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.Hibernate;

import javax.persistence.Embeddable;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import java.io.Serializable;
import java.util.Objects;

/**
 *
 *
 *  @author Youssef Agagg
 *  @since 1.0
 */
@Data
public class ArticleLikesDislikesIdDTO implements Serializable {

    private UserDTO user;

    private ArticleDTO article;


}
