package com.interview.postcode_distance_api.mapper;

import com.interview.postcode_distance_api.dto.PostcodeDistanceResponse;
import com.interview.postcode_distance_api.dto.PostcodeLocation;
import com.interview.postcode_distance_api.repository.model.PostcodeDetails;
import org.springframework.stereotype.Component;

@Component
public class PostcodeDistanceMapper {
    public PostcodeDistanceResponse constructPostcodeDetailsToPostcodeDetails(PostcodeDetails origin, PostcodeDetails destination, double distance) {
        return PostcodeDistanceResponse.builder()
                .origin(constructPostcodeDetailsToLocationDto(origin))
                .destination(constructPostcodeDetailsToLocationDto(destination))
                .distanceBetween(distance)
                .measureUnit("km")
                .build();
    }

    public PostcodeLocation constructPostcodeDetailsToLocationDto(PostcodeDetails postcodeDetails) {
        return PostcodeLocation.builder()
                .postcode(postcodeDetails.getPostcode())
                .latitude(postcodeDetails.getLatitude())
                .longitude(postcodeDetails.getLongitude())
                .build();
    }

    public PostcodeDetails constructPostcodeLocationToPostcodeDetails(PostcodeLocation postcodeLocation) {
        return PostcodeDetails.builder()
                .postcode(postcodeLocation.getPostcode())
                .latitude(postcodeLocation.getLatitude())
                .longitude(postcodeLocation.getLongitude())
                .build();
    }
}
