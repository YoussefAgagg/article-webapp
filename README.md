# article-webapp
Production ready maven based Spring Boot starter kit application with example cases of handling transactions with Spring.

## Description
An API for simple article management system where users can write articles of interest to them. registered users can write, edit, and delete content.

## Technology
* [Spring Boot 2.7.0](https://projects.spring.io/spring-boot/) - Inversion of Control Framework
* [Lombok](https://projectlombok.org/)
* [H2](http://www.h2database.com/) - In-Memory Database for development
* [Liquibase](https://liquibase.org/) - Rapidly manage database schema changes.
* [Spring Security](https://docs.spring.io/spring-boot/docs/2.6.7/reference/htmlsingle/#boot-features-security)
* **JJWT**            - JWT tokens for API authentication
* [Java Mail Sender](https://docs.spring.io/spring-boot/docs/2.6.7/reference/htmlsingle/#boot-features-email)
* [Spring Data JPA](https://docs.spring.io/spring-boot/docs/2.6.7/reference/htmlsingle/#boot-features-jpa-and-spring-data)
* [Validation](https://docs.spring.io/spring-boot/docs/2.6.7/reference/htmlsingle/#boot-features-validation)

## Features
- Registration new users
- activation email
- API Login Controller
- Authentication and Authorization with JWT
- [Security Configuration Without WebSecurityConfigurerAdapter](https://spring.io/blog/2022/02/21/spring-security-without-the-websecurityconfigureradapter#ldap-authentication)
- Rest end points for the entities 
- Email Service with HTML and attachment support
- Unit and Integration Tests
## Entity Relational Diagram (ERD
![](../../new projects/digrame.png)
* The following environment variables can be customized.:

  - The defaults are:

          EMAIL_USER_NAME : your gmail 
          EMAIL_PASSWORD : the App password , you can  generate the app password from your account managment
          JWT_SECRET=salt
          SPRING_PROFILES_ACTIVE=dev

## api use
- to login use:
    - http://localhost:8080/api/authenticate
        - with request body:
          ```
          {
          "username":"admin",
          "password":"admin"
          }
          ```
- other end points:
    - http://localhost:8080/api/users
    - http://localhost:8080/api/articles
    - http://localhost:8080/api/{article_id}/comments
* The `get` endpoints are not secured and are open to all users.