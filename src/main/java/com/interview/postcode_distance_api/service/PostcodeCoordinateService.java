package com.interview.postcode_distance_api.service;

import com.interview.postcode_distance_api.repository.model.PostcodeDetails;

import java.util.Map;
import java.util.Optional;

public interface PostcodeCoordinateService {

    Map<String, PostcodeDetails> getPostcodeDetails(String origin, String destination);

    Optional<PostcodeDetails> getPostcodeDetail(String postcode);
}
