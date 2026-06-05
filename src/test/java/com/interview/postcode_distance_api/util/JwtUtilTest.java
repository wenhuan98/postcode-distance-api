package com.interview.postcode_distance_api.util;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.Base64;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class JwtUtilTest {

    private JwtUtil jwtUtil;

    @BeforeEach
    void setUp() {
        jwtUtil = new JwtUtil();
        String rawSecret = "12345678901234567890123456789012";
        String base64Secret = Base64.getEncoder().encodeToString(rawSecret.getBytes());

        ReflectionTestUtils.setField(jwtUtil, "jwtSecret", base64Secret);
        ReflectionTestUtils.setField(jwtUtil, "jwtExpirationMs", 60000L);
        jwtUtil.init();
    }

    @Test
    @DisplayName("Should generate and validate JWT token")
    void shouldGenerateAndValidateJwtToken() {
        var result = jwtUtil.generateToken("admin");
        var extractedUsername = jwtUtil.getUsername(result);

        assertNotNull(result);
        assertEquals("admin", extractedUsername);
    }

    @Test
    @DisplayName("Should return true when JWT token is valid")
    void shouldReturnTrueWhenJwtTokenIsValid() {
        String token = jwtUtil.generateToken("admin");

        assertTrue(jwtUtil.validateJwtToken(token));
    }

    @Test
    @DisplayName("Should return false when JWT token is invalid")
    void shouldReturnFalseWhenJwtTokenIsInvalid() {
        String token = jwtUtil.generateToken("admin");
        String invalidToken = token + "test";

        assertFalse(jwtUtil.validateJwtToken(invalidToken));
    }
}
