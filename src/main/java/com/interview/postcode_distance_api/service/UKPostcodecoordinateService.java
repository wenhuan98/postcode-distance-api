package com.interview.postcode_distance_api.service;

import com.interview.postcode_distance_api.repository.PostcodeDetailsRepository;
import com.interview.postcode_distance_api.repository.model.PostcodeDetails;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class UKPostcodecoordinateService implements PostcodeCoordinateService {

    private final PostcodeDetailsRepository postcodeDetailsRepository;

    @Override
    public Map<String, PostcodeDetails> getPostcodeDetails(String origin, String destination) {
        List<PostcodeDetails> postCodeList = postcodeDetailsRepository.findByPostcodeIn(List.of(origin, destination));

        return postCodeList
                .stream()
                .collect(Collectors.toMap(PostcodeDetails::getPostcode, Function.identity()));
    }

    @Override
    public Optional<PostcodeDetails> getPostcodeDetail(String postcode) {
        return postcodeDetailsRepository.findByPostcode(postcode);
    }
}
