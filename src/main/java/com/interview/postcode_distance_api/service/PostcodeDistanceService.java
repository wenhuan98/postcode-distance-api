package com.interview.postcode_distance_api.service;

import com.interview.postcode_distance_api.dto.PostcodeDistanceResponse;

public interface PostcodeDistanceService {

    double EARTH_RADIUS = 6371;

    PostcodeDistanceResponse calculateDistance(String origin, String destination);
}
