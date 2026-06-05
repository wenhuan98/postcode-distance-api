package com.interview.postcode_distance_api.controller;

import com.interview.postcode_distance_api.dto.PostcodeCoordinate;
import com.interview.postcode_distance_api.dto.PostcodeDistanceRequest;
import com.interview.postcode_distance_api.dto.PostcodeDistanceResponse;
import com.interview.postcode_distance_api.dto.PostcodeLocation;
import com.interview.postcode_distance_api.factory.PostcodeFactoryRegistry;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/v1/postcodes")
@AllArgsConstructor
@Slf4j
public class PostcodeDistanceController {

    private final PostcodeFactoryRegistry postcodeFactoryRegistry;

    @PostMapping("/distance")
    public ResponseEntity<PostcodeDistanceResponse> getPostcodeDistance (
            @RequestParam String country,
            @RequestBody PostcodeDistanceRequest postcodeDistanceRequest) {

        log.info(
                "Getting postcodes distance with: country={} origin={} destination={}",
                country,
                postcodeDistanceRequest.getOrigin(),
                postcodeDistanceRequest.getDestination()
        );

        var postcodeDistance = postcodeFactoryRegistry
                .getServiceFactory(country)
                .getPostcodeDistanceService()
                .calculateDistance(postcodeDistanceRequest.getOrigin(), postcodeDistanceRequest.getDestination());

        return ResponseEntity.ok(postcodeDistance);
    }

    @PostMapping
    public ResponseEntity<PostcodeLocation> addPostcodeLocation(
            @RequestParam String country,
            @RequestBody @Valid PostcodeLocation postcodeLocation) {
        var savedPostcodeLocation = postcodeFactoryRegistry
                .getServiceFactory(country)
                .getPostcodeManagementService()
                .addPostcodeDetails(postcodeLocation);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(savedPostcodeLocation);
    }

    @PutMapping
    public ResponseEntity<PostcodeLocation> updatePostcodeLocation(
            @RequestParam String country,
            @RequestParam String postcode,
            @RequestBody PostcodeCoordinate postcodeCoordinate) {
        var updatedPostcodeLocation = postcodeFactoryRegistry
                .getServiceFactory(country)
                .getPostcodeManagementService()
                .updatePostcodeDetails(postcode, postcodeCoordinate);

        return ResponseEntity.ok(updatedPostcodeLocation);
    }

    @DeleteMapping
    public ResponseEntity<PostcodeLocation> deletePostcode (
            @RequestParam String country,
            @RequestParam String postcode) {
         postcodeFactoryRegistry
                .getServiceFactory(country)
                .getPostcodeManagementService()
                .deletePostcodeDetails(postcode);

        return ResponseEntity.noContent().build();
    }
}
