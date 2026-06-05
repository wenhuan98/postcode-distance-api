package com.interview.postcode_distance_api.security;

import com.interview.postcode_distance_api.util.JwtUtil;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockFilterChain;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.io.IOException;
import java.util.Objects;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class JwtAuthTokenFilterTest {

    @Mock
    private UserDetailsService userDetailsService;

    @Mock
    private JwtUtil jwtUtil;

    @InjectMocks
    private JwtAuthTokenFilter jwtAuthTokenFilter;

    @AfterEach
    void tearDown() {
        SecurityContextHolder.clearContext();
    }

    @Test
    @DisplayName("Should authenticate user when JWT token is valid")
    void shouldAuthenticateUserWhenJWTTokenIsValid() throws ServletException, IOException {
        MockHttpServletRequest request = new MockHttpServletRequest();
        MockHttpServletResponse response = new MockHttpServletResponse();
        MockFilterChain filterChain = new MockFilterChain();

        UserDetails userDetails = User.builder()
                .username("admin")
                .password("password")
                .build();

        request.addHeader("Authorization", "Bearer valid-jwt-token");
        request.setRequestURI("/api/postcodes/distance");

        when(jwtUtil.getUsername(anyString())).thenReturn("admin");
        when(jwtUtil.validateJwtToken(anyString())).thenReturn(true);
        when(userDetailsService.loadUserByUsername(anyString())).thenReturn(userDetails);

        jwtAuthTokenFilter.doFilterInternal(request, response, filterChain);


        var authentication = SecurityContextHolder.getContext().getAuthentication();
        assertNotNull(authentication);
        assertEquals("admin", ((UserDetails) Objects.requireNonNull(authentication.getPrincipal())).getUsername());
    }

    @Test
    @DisplayName("Should fail authentication when JWT token is invalid")
    void shouldFailAuthenticationWhenJWTTokenIsInvalid() throws ServletException, IOException {
        MockHttpServletRequest request = new MockHttpServletRequest();
        MockHttpServletResponse response = new MockHttpServletResponse();
        MockFilterChain filterChain = new MockFilterChain();

        request.addHeader("Authorization", "Bearer valid-jwt-token");
        request.setRequestURI("/api/postcodes/distance");

        when(jwtUtil.validateJwtToken(anyString())).thenReturn(false);

        jwtAuthTokenFilter.doFilterInternal(request, response, filterChain);

        var authentication = SecurityContextHolder.getContext().getAuthentication();
        assertNull(authentication);
    }

    @Test
    @DisplayName("Should not authenticate user when Authorization header is missing")
    void shouldNotAuthenticateUserWhenAuthorizationHeaderIsMissing() throws ServletException, IOException {
        MockHttpServletRequest request = new MockHttpServletRequest();
        MockHttpServletResponse response = new MockHttpServletResponse();
        MockFilterChain filterChain = new MockFilterChain();

        request.setRequestURI("/api/postcodes/distance");

        jwtAuthTokenFilter.doFilterInternal(request, response, filterChain);

        var authentication = SecurityContextHolder.getContext().getAuthentication();
        assertNull(authentication);
    }

    @Test
    @DisplayName("Should not authenticate user when user not found")
    void shouldNotAuthenticateUserWhenUserNotFound() {
        MockHttpServletRequest request = new MockHttpServletRequest();
        MockHttpServletResponse response = new MockHttpServletResponse();
        MockFilterChain filterChain = new MockFilterChain();

        request.addHeader("Authorization", "Bearer valid-jwt-token");
        request.setRequestURI("/api/postcodes/distance");

        when(jwtUtil.getUsername(anyString())).thenReturn("im_admin");
        when(jwtUtil.validateJwtToken(anyString())).thenReturn(true);
        when(userDetailsService.loadUserByUsername(anyString())).thenThrow(new UsernameNotFoundException("User Not Found: im_admin"));

        assertDoesNotThrow(() -> jwtAuthTokenFilter.doFilterInternal(request, response, filterChain));

        assertNull(SecurityContextHolder.getContext().getAuthentication());
    }

    @Test
    @DisplayName("Should return true on shouldNotFilter when is authentication path")
    void shouldReturnTrueOnShouldNotFilterWhenIsAuthPath() {
        MockHttpServletRequest request = new MockHttpServletRequest();

        request.addHeader("Authorization", "Bearer valid-jwt-token");
        request.setRequestURI("/api/auth/generate-token");

        assertTrue(jwtAuthTokenFilter.shouldNotFilter(request));
    }

    @Test
    @DisplayName("Should return false on shouldNotFilter when is not authentication path")
    void shouldReturnFalseShouldNotFilterWhenIsNotAuthPath() {
        MockHttpServletRequest request = new MockHttpServletRequest();

        request.addHeader("Authorization", "Bearer valid-jwt-token");
        request.setRequestURI("/api/postcodes/distance");

        assertFalse(jwtAuthTokenFilter.shouldNotFilter(request));
    }
}
