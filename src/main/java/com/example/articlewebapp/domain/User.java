package com.example.articlewebapp.domain;

import com.example.articlewebapp.domain.enumerations.Gender;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.Hibernate;
import org.hibernate.validator.constraints.Length;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

/**
 *  @author Mohamed Ehab Ali
 *  @since 24-6-2022
 */

@Table(name = "user")
@Entity(name = "User")
@Data
@Slf4j
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @Column(name = "id", nullable = false)
    private Long id;

    @NotBlank(message = "First Name shouldn't be blank")
    @NotNull
    @Length(max = 50)
    @Column(name = "first_name")
    private String firstName;


    @NotBlank(message = "Last Name shouldn't be blank")
    @NotNull
    @Length(max = 50)
    @Column(name = "last_name", nullable = false)
    private String lastName;


    @NotBlank(message = "Email shouldn't be blank")
    @NotNull
    @Email(message = "Email not valid", regexp = "^\\w+([\\.-]?\\w+)*@\\w+([\\.-]?\\w+)*(\\.\\w{2,3})+$")
    @Column(name = "email", nullable = false, unique = true)
    private String email;


    @NotBlank(message = "Username shouldn't be blank")
    @NotNull
    @Column(name = "username", unique = true)
    private String username;

    @NotBlank(message = "Password shouldn't be blank")
    @NotNull
    @Length(min = 8)
    @Column(name = "password")
    private String password;

    @Pattern(message="Mobile Number is not valid",regexp = "^(\\+\\d{1,3}[- ]?)?\\d{10}$")
    @Column(name = "mobile_no")
    private String mobile;

    @Column(name = "gender")
    private Gender gender;

    @Column(name = "img_url")
    private String imageUrl;

    @Column(name = "date_of_birth")
    private LocalDate dateOfBirth;

    @NotNull
    @Column(name = "is_validated")
    private Boolean isValidated;

    @Column(name = "validation_key")
    private String validationKey;

    @Column(name = "reset_key")
    private String resetKey;

    @OneToMany(mappedBy = "user")
    @JoinColumn(name = "id", nullable = false)
    Set<Article> user_articles = new HashSet<Article>();

    @OneToMany(mappedBy = "user")
    @JoinColumn(name = "id", nullable = false)
    Set<Comment> user_comments = new HashSet<Comment>();

    @ManyToMany
    @JoinTable(
            name = "users_authorities",
            joinColumns = {@JoinColumn(name = "user_id", referencedColumnName = "id")},
            inverseJoinColumns = {@JoinColumn(name = "authority_name",referencedColumnName = "id")}
    )
    private Set<Authority> authorities = new HashSet<>();

//    @ManyToMany(mappedBy = "following", cascade = CascadeType.ALL)
//    @JoinTable(name="UserRel",
//            joinColumns={@JoinColumn(name="ParentId")},
//            inverseJoinColumns={@JoinColumn(name="UserId")})
//    private Set<User> followers = new HashSet<User>();
//
//    @ManyToMany(cascade = CascadeType.ALL)
//    @JoinTable(name="UserRel",
//            joinColumns={@JoinColumn(name="UserId")},
//            inverseJoinColumns={@JoinColumn(name="ParentId")})
//    private Set<User> following = new HashSet<User>();

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        User user = (User) o;
        return id != null && Objects.equals(id, user.id);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}
