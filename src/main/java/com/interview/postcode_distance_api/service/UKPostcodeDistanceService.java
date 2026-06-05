package com.interview.postcode_distance_api.service;

import com.interview.postcode_distance_api.dto.PostcodeDistanceResponse;
import com.interview.postcode_distance_api.exception.PostcodeNotFoundException;
import com.interview.postcode_distance_api.mapper.PostcodeDistanceMapper;
import com.interview.postcode_distance_api.repository.model.PostcodeDetails;
import com.interview.postcode_distance_api.util.MathHelper;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
@AllArgsConstructor
public class UKPostcodeDistanceService implements PostcodeDistanceService {

    private final UKPostcodeCoordinateService postcodeCoordinateService;

    private final PostcodeDistanceMapper postcodeDistanceMapper;

    @Override
    public PostcodeDistanceResponse calculateDistance(String origin, String destination) {
        Map<String, PostcodeDetails> postcodeDetailsMap = postcodeCoordinateService.getPostcodeDetails(origin, destination);


        PostcodeDetails originPostcodeDetails = postcodeDetailsMap.get(origin);
        PostcodeDetails destinationPostcodeDetails = postcodeDetailsMap.get(destination);

        if (originPostcodeDetails == null || destinationPostcodeDetails == null) {
            throw new PostcodeNotFoundException("One or both postcodes not found in the system");
        }

        double originLatRadians = Math.toRadians(originPostcodeDetails.getLatitude());
        double destinationLatRadians = Math.toRadians(destinationPostcodeDetails.getLatitude());
        double originLonRadians = Math.toRadians(originPostcodeDetails.getLongitude());
        double destinationLonRadians = Math.toRadians(destinationPostcodeDetails.getLongitude());

        double haversineValue = MathHelper.haversine(originLatRadians, destinationLatRadians)
                + Math.cos(originLatRadians)
                * Math.cos(destinationLatRadians)
                * MathHelper.haversine(originLonRadians, destinationLonRadians);

        double distanceRadians = 2 * Math.atan2(Math.sqrt(haversineValue), Math.sqrt(1 - haversineValue));

        return postcodeDistanceMapper.constructPostcodeDetailsToPostcodeDetails(originPostcodeDetails, destinationPostcodeDetails, EARTH_RADIUS * distanceRadians);
    }
}
