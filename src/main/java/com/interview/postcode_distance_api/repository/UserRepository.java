package com.interview.postcode_distance_api.repository;

import com.interview.postcode_distance_api.repository.model.UserCredential;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<UserCredential, Long> {
    Optional<UserCredential> findByUsername(String username);
}
