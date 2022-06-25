package com.example.articlewebapp.domain;

import com.example.articlewebapp.domain.enumerations.Gender;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.Hibernate;
import org.hibernate.validator.constraints.Length;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

/**
 *  @author Mohamed Ehab Ali
 *  @since 1.0
 */

@Table(name = "`user`")
@Entity
@Getter
@Setter
@ToString
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

    @NotBlank(message = "First Name shouldn't be blank")
    @NotNull
    @Length(max = 50)
    @Column(name = "first_name", length = 50)
    private String firstName;


    @NotBlank(message = "Last Name shouldn't be blank")
    @NotNull
    @Length(max = 50)
    @Column(name = "last_name", length = 50)
    private String lastName;


    @NotBlank(message = "Email shouldn't be blank")
    @NotNull
    @Email(message = "Email not valid", regexp = "^\\w+([\\.-]?\\w+)*@\\w+([\\.-]?\\w+)*(\\.\\w{2,3})+$")
    @Column(name = "email", nullable = false, unique = true)
    private String email;


    @NotBlank(message = "Username shouldn't be blank")
    @NotNull
    @Size(min = 4, max = 50, message="Username should be at least 4 and at most 50 character")
    @Column(name = "username", unique = true)
    private String username;

    @NotBlank(message = "Password shouldn't be blank")
    @NotNull
    @Length(min = 8)
    @Column(name = "password")
    @JsonIgnore
    @ToString.Exclude
    private String password;

    @Pattern(message="Mobile Number is not valid",regexp = "^(\\+\\d{1,3}[- ]?)?\\d{10}$")
    @Column(name = "mobile")
    private String mobile;

    @Enumerated(EnumType.STRING)
    @Column(name = "gender")
    private Gender gender;

    @Column(name = "img_url")
    private String imageUrl;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @Column(name = "date_of_birth")
    private LocalDate dateOfBirth;

    @NotNull
    @Column(nullable = false)
    private boolean activated = false;

    @Column(name = "activation_key")
    @JsonIgnore
    private String activationKey;


    @Column(name = "reset_key")
    private String resetKey;

    @ToString.Exclude
    @OneToMany(mappedBy = "author")
    @JsonIgnoreProperties(value = { "comments", "categories", "author" }, allowSetters = true)
    private Set<Article> articles = new HashSet<>();

    @JsonIgnore
    @ToString.Exclude
    @ManyToMany
    @JoinTable(
            name = "user_authority",
            joinColumns = {@JoinColumn(name = "user_id", referencedColumnName = "id")},
            inverseJoinColumns = {@JoinColumn(name = "authority_name",referencedColumnName = "name")}
    )
    private Set<Authority> authorities = new HashSet<>();

    @ToString.Exclude
    @ManyToMany
    @JoinTable(
            name = "user_followers_following",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "followers_id")
    )
    @JsonIgnoreProperties(value = { "articles", "followers", "following" }, allowSetters = true)
    private Set<User> followers = new HashSet<>();

    @ToString.Exclude
    @ManyToMany(mappedBy = "followers")
    @JsonIgnoreProperties(value = { "articles", "followers", "following" }, allowSetters = true)
    private Set<User> following = new HashSet<>();

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
