package com.interview.postcode_distance_api.controller;

import com.interview.postcode_distance_api.repository.model.UserCredential;
import com.interview.postcode_distance_api.util.JwtUtil;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@AllArgsConstructor
@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final JwtUtil jwtUtil;


    @PostMapping("/get-token")
    public String getToken(@RequestBody UserCredential user) {
            return jwtUtil.generateToken(user.getUsername());

    }
}
