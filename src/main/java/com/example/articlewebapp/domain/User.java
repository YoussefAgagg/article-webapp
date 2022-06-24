package com.example.articlewebapp.domain;

import com.example.articlewebapp.domain.enumerations.Gender;
import org.hibernate.validator.constraints.Length;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Table(name = "users")
@Entity(name = "User")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id", nullable = false)
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
    @JoinColumn(name = "user_id", nullable = false)
    Set<Article> user_articles = new HashSet<Article>();

    @OneToMany(mappedBy = "user")
    @JoinColumn(name = "user_id", nullable = false)
    Set<Comment> user_comments = new HashSet<Comment>();

    @ManyToMany
    @JoinTable(
            name = "users_authorities",
            joinColumns = {@JoinColumn(name = "user_id")},
            inverseJoinColumns = {@JoinColumn(name = "authority_id")}
    )
    private Set<Authority> authorities = new HashSet<>();

    @ManyToMany(mappedBy = "following", cascade = CascadeType.ALL)
    @JoinTable(name="UserRel",
            joinColumns={@JoinColumn(name="ParentId")},
            inverseJoinColumns={@JoinColumn(name="UserId")})
    private Set<User> followers = new HashSet<User>();

    @ManyToMany(cascade = CascadeType.ALL)
    @JoinTable(name="UserRel",
            joinColumns={@JoinColumn(name="UserId")},
            inverseJoinColumns={@JoinColumn(name="ParentId")})
    private Set<User> following = new HashSet<User>();



    public User() {
    }

    public User(String firstName, String lastName, String email, String username, String password) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.username = username;
        this.password = password;
    }

    public Long getId() {
        return id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public Gender getGender() {
        return gender;
    }

    public void setGender(Gender gender) {
        this.gender = gender;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public LocalDate getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(LocalDate dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    public Boolean getValidated() {
        return isValidated;
    }

    public void setValidated(Boolean validated) {
        isValidated = validated;
    }

    public String getValidationKey() {
        return validationKey;
    }

    public void setValidationKey(String validationKey) {
        this.validationKey = validationKey;
    }

    public String getResetKey() {
        return resetKey;
    }

    public void setResetKey(String resetKey) {
        this.resetKey = resetKey;
    }

    public Set<Article> getUser_articles() {
        return user_articles;
    }

    public void setUser_articles(Set<Article> user_articles) {
        this.user_articles = user_articles;
    }
}
