package com.interview.postcode_distance_api.repository;

import com.interview.postcode_distance_api.repository.model.UserCredential;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<UserCredential, Long> {
    UserCredential findByUsername(String username);
}
