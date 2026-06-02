package com.interview.postcode_distance_api.repository;

import com.interview.postcode_distance_api.repository.model.PostcodeDetails;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PostcodeDetailsRepository extends JpaRepository<PostcodeDetails, Long> {

    Optional<PostcodeDetails> findByPostcode(String postcode);

    List<PostcodeDetails> findByPostcodeIn(List<String> postcodes);

    void deleteByPostcode(String postcode);
}
