package com.example.articlewebapp.security.jwt;


import com.example.articlewebapp.security.AuthoritiesConstants;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.junit.jupiter.api.*;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.test.util.ReflectionTestUtils;

import java.security.Key;
import java.util.Collections;
import java.util.Date;

import static org.assertj.core.api.Assertions.assertThat;

/**
 *
 * @author Youssef Agagg
 */

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class TokenProviderTest {
    private transient TokenProvider tokenProvider;

    @BeforeAll
     void beforeAll() {
        tokenProvider = new TokenProvider("fd54a45s65fds737b9aafcb3412e07ed99b267f33413274720ddbb7f6c5e64e9f14075f2d7ed041592f0b7657baf8");
        //ReflectionTestUtils.setField(tokenProvider, "jwtSecret", "secret");
    }

    @Test
    void generateJwtTokenThenValidate() {
        UsernamePasswordAuthenticationToken authentication = getAuthentication();
        var jwtToken = tokenProvider.createToken(authentication);
        Assertions.assertTrue(tokenProvider.validateToken(jwtToken));
    }

    @Test
    void generateJwtTokenWithNullThrowsException() {
        Assertions.assertThrows(NullPointerException.class, () -> tokenProvider.createToken(null));
    }



    private UsernamePasswordAuthenticationToken getAuthentication() {
        UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
                "test-user",
                "test-password",
                Collections.singletonList(new SimpleGrantedAuthority(AuthoritiesConstants.USER))
        );
        return authentication;
    }

    @Test
    void getUsernameFromTokenWithNullThrowsException() {
        Assertions.assertThrows(
                IllegalArgumentException.class, () -> tokenProvider.getAuthentication(null));
    }

    @Test
    void generateExpiredJwtTokenThenValidate(TestInfo testInfo) {
        ReflectionTestUtils.setField(tokenProvider, "tokenExpiredAfter", -60*1000);

        Authentication authentication = getAuthentication();
        String token = tokenProvider.createToken(authentication);

        boolean isTokenValid = tokenProvider.validateToken(token);

        assertThat(isTokenValid).isFalse();
    }

    @Test
    void isValidJwtTokenWithIllegalArgument() {
        Assertions.assertFalse(tokenProvider.validateToken(null));
    }

    @Test
    void isValidJwtTokenWithBadSignatureJwtToken(TestInfo testInfo) {
        boolean isTokenValid = tokenProvider.validateToken(createTokenWithDifferentSignature());

        assertThat(isTokenValid).isFalse();
    }

    private String createTokenWithDifferentSignature() {
        Key otherKey = Keys.hmacShaKeyFor(
                Decoders.BASE64.decode("Xfd54a45s65fds737b9aafcb3412e07ed99b267f33413274720ddbb7f6c5e64e9f14075f2d7ed041592f0b7657baf8")
        );

        return Jwts
                .builder()
                .setSubject("anonymous")
                .signWith(otherKey, SignatureAlgorithm.HS512)
                .setExpiration(new Date(new Date().getTime() + 60*1000))
                .compact();
    }
}
