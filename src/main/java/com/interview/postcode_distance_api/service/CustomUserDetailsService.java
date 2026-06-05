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

import java.util.Optional;

@Service
@AllArgsConstructor
@NullMarked
public class CustomUserDetailsService implements UserDetailsService {
    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<UserCredential> userCredential = userRepository.findByUsername(username);

        if (userCredential.isEmpty()) {
            throw new UsernameNotFoundException("User Not Found: " + username);
        }
        return User.builder()
                .username(userCredential.get().getUsername())
                .password(userCredential.get().getPassword())
                .build();
    }
}