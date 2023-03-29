package com.tteam.movieland.security.service;

import com.tteam.movieland.AbstractBaseITest;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.security.SignatureException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import io.jsonwebtoken.Claims;

import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class JwtServiceTest extends AbstractBaseITest {

    @Autowired
    private JwtService jwtService;

    @Value("${security.jwTokenExpirationAfterHours}")
    private int jwTokenExpirationAfterHours;

    private UserDetails userDetails;
    private Map<String, Object> claims;

    @BeforeEach
    void setUp() {
        userDetails = new User("testUser", "testPassword", new HashSet<>());
        claims = new HashMap<>();
        claims.put("testClaimKey", "testClaimValue");
    }

    @Test
    @DisplayName("Test JWT token generation")
    void testGenerateTokenShouldGenerateValidJwtToken() {
        String token = jwtService.generateToken(claims, userDetails);

        assertNotNull(token);
        assertTrue(token.length() > 0);
        assertTrue(jwtService.isTokenValid(token, userDetails));
        // Check that token expiration is as expected with 5 seconds error margin
        assertEquals(jwTokenExpirationAfterHours * 60 * 60 * 1000, jwtService.extractExpiration(token).getTime() - new Date().getTime(), 5000);
    }

    @Test
    @DisplayName("Test valid token verification")
    void testIsTokenValidShouldReturnTrueForValidToken() {
        String token = jwtService.generateToken(claims, userDetails);
        System.out.println(token);
        boolean result = jwtService.isTokenValid(token, userDetails);
        assertTrue(result);
    }

    @Test
    @DisplayName("Test invalid token verification")
    void testIsTokenValidShouldReturnFalseForInvalidToken() {
        String invalidToken = "1eyJhbGciOiJIUzI1NiJ9.eyJ0ZXN0Q2xhaW1LZXkiOiJ0ZXN0Q2xhaW1WYWx1ZSIsInN1YiI6InRlc3RVc2VyIiwiaWF0IjoxNjgwMTAzOTI1LCJleHAiOjE2ODAxOTAzMjV9.yOFKL1pj1qTccsRu8EPnIFAiW1XDQw4pawiwXRc_8vA";
        assertThrows(MalformedJwtException.class, () -> {
            jwtService.isTokenValid(invalidToken, userDetails);
        });
    }

    @Test
    @DisplayName("Test username extraction from token")
    void testExtractUsernameShouldExtractCorrectUsernameFromToken() {
        String token = jwtService.generateToken(claims, userDetails);
        String username = jwtService.extractUsername(token);
        assertEquals(userDetails.getUsername(), username);
    }

    @Test
    @DisplayName("Test claim value extraction from token")
    void testExtractClaimShouldExtractCorrectClaimValueFromToken() {
        String token = jwtService.generateToken(claims, userDetails);
        String claimValue = jwtService.extractClaim(token, claims -> (String) claims.get("testClaimKey"));
        assertEquals("testClaimValue", claimValue);
    }


    @Test
    @DisplayName("Test claim value extraction with valid token")
    void testExtractClaim() {
        String claimKey = "testClaim";
        String claimValue = "testClaimValue";
        claims.put(claimKey, claimValue);
        String token = jwtService.generateToken(claims, userDetails);
        String claim = jwtService.extractClaim(token, claims -> (String) claims.get(claimKey));
        assertEquals(claimValue, claim);
    }

    @Test
    @DisplayName("Test claim value extraction with invalid token")
    void testExtractClaimWithInvalidToken() {
        String token = jwtService.generateToken(claims, userDetails);
        String invalidToken = token + "invalid";
        assertThrows(SignatureException.class, () -> {
            jwtService.extractClaim(invalidToken, Claims::getSubject);
        });
    }

}