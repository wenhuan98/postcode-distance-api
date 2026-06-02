package com.interview.postcode_distance_api.service;

import com.interview.postcode_distance_api.repository.UserRepository;
import com.interview.postcode_distance_api.repository.model.UserCredential;
import lombok.AllArgsConstructor;
import org.jspecify.annotations.NullMarked;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
@NullMarked
public class CustomUserDetailsService implements UserDetailsService {
    private UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        UserCredential userCredential = userRepository.findByUsername(username);

        if (userCredential == null) {
            throw new UsernameNotFoundException("User Not Found: " + username);
        }
        return User.builder()
                .username(userCredential.getUsername())
                .password(userCredential.getPassword())
                .build();
    }
}