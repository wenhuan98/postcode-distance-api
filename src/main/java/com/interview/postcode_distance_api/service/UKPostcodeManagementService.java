package com.interview.postcode_distance_api.service;

import com.interview.postcode_distance_api.dto.PostcodeCoordinate;
import com.interview.postcode_distance_api.dto.PostcodeLocation;
import com.interview.postcode_distance_api.exception.PostcodeAlreadyExistsException;
import com.interview.postcode_distance_api.exception.PostcodeNotFoundException;
import com.interview.postcode_distance_api.mapper.PostcodeDistanceMapper;
import com.interview.postcode_distance_api.repository.PostcodeDetailsRepository;
import com.interview.postcode_distance_api.repository.model.PostcodeDetails;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@AllArgsConstructor
public class UKPostcodeManagementService implements PostcodeManagementService {

    private final PostcodeDetailsRepository postcodeDetailsRepository;

    private final UKPostcodeCoordinateService ukPostcodecoordinateService;

    private final PostcodeDistanceMapper postcodeDistanceMapper;

    @Transactional
    @Override
    public PostcodeLocation addPostcodeDetails(PostcodeLocation postcodeLocation) {
        Optional<PostcodeDetails> postcodeDetails = ukPostcodecoordinateService
                .getPostcodeDetail(postcodeLocation.getPostcode());

        if (postcodeDetails.isPresent()) {
            throw new PostcodeAlreadyExistsException(postcodeLocation.getPostcode());
        }

        var dto = postcodeDistanceMapper.constructPostcodeLocationToPostcodeDetails(postcodeLocation);

        PostcodeDetails savedPostcodeDetails = postcodeDetailsRepository.save(dto);

        return postcodeDistanceMapper.constructPostcodeDetailsToLocationDto(savedPostcodeDetails);
    }

    @Transactional
    @Override
    public PostcodeLocation updatePostcodeDetails(String postcode, PostcodeCoordinate postcodeCoordinate) {
        PostcodeDetails existingPostcode = ukPostcodecoordinateService
                .getPostcodeDetail(postcode)
                .orElseThrow(() -> new PostcodeNotFoundException(postcode));

        if (postcodeCoordinate.getLatitude() != null) {
            existingPostcode.setLatitude(postcodeCoordinate.getLatitude());
        }

        if (postcodeCoordinate.getLongitude() != null) {
            existingPostcode.setLongitude(postcodeCoordinate.getLongitude());
        }

        PostcodeDetails savedPostcodeDetails = postcodeDetailsRepository.save(existingPostcode);

        return postcodeDistanceMapper.constructPostcodeDetailsToLocationDto(savedPostcodeDetails);
    }

    @Transactional
    @Override
    public void deletePostcodeDetails(String postcode) {
        postcodeDetailsRepository.deleteByPostcode(postcode);
    }
}
