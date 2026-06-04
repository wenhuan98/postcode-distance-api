package com.interview.postcode_distance_api.controller;

import com.interview.postcode_distance_api.dto.AuthRequest;
import com.interview.postcode_distance_api.util.JwtUtil;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AuthControllerTest {

    @Mock
    private JwtUtil jwtUtil;

    @Mock
    private AuthenticationManager authenticationManager;

    @Mock
    private Authentication authentication;

    @InjectMocks
    private AuthController authController;

    @Test
    @DisplayName("Should return token when valid username and password is provided")
    void shouldReturnTokenWhenValidUserAndPassProvided() {
        AuthRequest authRequest = new AuthRequest("admin", "password");

        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
                .thenReturn(authentication);
        when(jwtUtil.generateToken("admin")).thenReturn("jwt-token");

        String token = authController.getToken(authRequest);

        ArgumentCaptor<UsernamePasswordAuthenticationToken> authenticationCaptor =
                ArgumentCaptor.forClass(UsernamePasswordAuthenticationToken.class);
        verify(authenticationManager).authenticate(authenticationCaptor.capture());

        UsernamePasswordAuthenticationToken authenticationToken = authenticationCaptor.getValue();

        assertEquals("jwt-token", token);
        assertEquals("admin", authenticationToken.getPrincipal());
        assertEquals("password", authenticationToken.getCredentials());
        verify(jwtUtil).generateToken("admin");
    }

    @Test
    @DisplayName("Should not return token when invalid username or password is provided")
    void shouldNotReturnTokenWhenInvalidUserOrPassProvided() {
        AuthRequest authRequest = new AuthRequest("admin", "wrong-password");

        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
                .thenThrow(new BadCredentialsException("Bad credentials"));

        assertThrows(BadCredentialsException.class, () -> authController.getToken(authRequest));

        verify(jwtUtil, never()).generateToken(any());
    }
}
