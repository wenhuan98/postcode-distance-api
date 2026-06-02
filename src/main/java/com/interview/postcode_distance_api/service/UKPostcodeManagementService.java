package com.interview.postcode_distance_api.service;

import com.interview.postcode_distance_api.dto.PostcodeLocation;
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

    private final UKPostcodecoordinateService ukPostcodecoordinateService;

    private final PostcodeDistanceMapper postcodeDistanceMapper;

    @Transactional
    @Override
    public PostcodeLocation addPostcodeDetails(PostcodeLocation postcodeLocation) {
        Optional<PostcodeDetails> postcodeDetails = ukPostcodecoordinateService
                .getPostcodeDetail(postcodeLocation.getPostcode());

        if (postcodeDetails.isPresent()) {
            throw new IllegalArgumentException("Postcode already exists in the system");
        }

        var dto = postcodeDistanceMapper.constructPostcodeLocationToPostcodeDetails(postcodeLocation);

        PostcodeDetails savedPostcodeDetails = postcodeDetailsRepository.save(dto);

        return postcodeDistanceMapper.constructPostcodeDetailsToLocationDto(savedPostcodeDetails);
    }

    @Transactional
    @Override
    public PostcodeLocation updatePostcodeDetails(PostcodeLocation postcodeLocation) {
        PostcodeDetails existingPostcode = ukPostcodecoordinateService
                .getPostcodeDetail(postcodeLocation.getPostcode())
                .orElseThrow(() -> new IllegalArgumentException("Postcode not found in the system"));

        if (postcodeLocation.getLatitude() != null) {
            existingPostcode.setLatitude(postcodeLocation.getLatitude());
        }

        if (postcodeLocation.getLongitude() != null) {
            existingPostcode.setLongitude(postcodeLocation.getLongitude());
        }

        PostcodeDetails savedPostcodeDetails = postcodeDetailsRepository.save(existingPostcode);

        return postcodeDistanceMapper.constructPostcodeDetailsToLocationDto(savedPostcodeDetails);
    }

    @Override
    public void deletePostcodeDetails(String postcode) {
        postcodeDetailsRepository.deleteByPostcode(postcode);
    }
}
