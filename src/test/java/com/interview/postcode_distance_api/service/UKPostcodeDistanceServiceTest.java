package com.interview.postcode_distance_api.service;

import com.interview.postcode_distance_api.dto.PostcodeCoordinate;
import com.interview.postcode_distance_api.dto.PostcodeDistanceResponse;
import com.interview.postcode_distance_api.dto.PostcodeLocation;
import com.interview.postcode_distance_api.exception.PostcodeNotFoundException;
import com.interview.postcode_distance_api.mapper.PostcodeDistanceMapper;
import com.interview.postcode_distance_api.repository.model.PostcodeDetails;
import com.interview.postcode_distance_api.service.UKPostcodeCoordinateService;
import com.interview.postcode_distance_api.service.UKPostcodeDistanceService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyDouble;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UKPostcodeDistanceServiceTest {

    @Mock
    private UKPostcodeCoordinateService postcodeCoordinateService;

    @Mock
    private PostcodeDistanceMapper postcodeDistanceMapper;

    @InjectMocks
    private UKPostcodeDistanceService ukPostcodeDistanceService;

    @Test
    @DisplayName("Should calculate distance between two postcode successfully")
    void shouldCalculateDistanceBetweenTwoPostcodeSuccessfully() {
        var origin = PostcodeLocation.builder()
                .postcode("SW1A 1AA")
                .coordinate(PostcodeCoordinate.builder()
                        .latitude(51.501009)
                        .longitude(-0.141588)
                        .build())
                .build();

        var destination = PostcodeLocation.builder()
                .postcode("SW1A 2AA")
                .coordinate(PostcodeCoordinate.builder()
                        .latitude(50.501009)
                        .longitude(-0.141588)
                        .build())
                .build();

        var expectedPostcodeDistance = PostcodeDistanceResponse.builder()
                .distanceBetween(111.19)
                .origin(origin)
                .destination(destination)
                .measureUnit("km")
                .build();

        var postcodeDet1 = PostcodeDetails.builder()
                .postcode("SW1A 1AA")
                .latitude(51.501009)
                .longitude(-0.141588)
                .build();

        var psotcodeDet2 = PostcodeDetails.builder()
                .postcode("SW1A 2AA")
                .latitude(50.501009)
                .longitude(-0.141588)
                .build();

        when(postcodeCoordinateService.getPostcodeDetails(anyString(), anyString()))
                .thenReturn(Map.of("SW1A 1AA", postcodeDet1, "SW1A 2AA", psotcodeDet2));

        when(postcodeDistanceMapper.constructPostcodeDetailsToPostcodeDetails(eq(postcodeDet1), eq(psotcodeDet2), anyDouble()))
                .thenReturn(expectedPostcodeDistance);

        var result = ukPostcodeDistanceService.calculateDistance("SW1A 1AA", "SW1A 2AA");

        assertEquals(expectedPostcodeDistance, result);
    }

    @Test
    @DisplayName("Should throw error when origin postcode is not found")
    void shouldThrowErrorWhenOriginPostcodeIsNotFound() {
        var destination = PostcodeDetails.builder()
                .postcode("SW1A 2AA")
                .latitude(50.501009)
                .longitude(-0.141588)
                .build();

        when(postcodeCoordinateService.getPostcodeDetails(anyString(), anyString()))
                .thenReturn(Map.of("SW1A 2AA", destination));

        var exception = assertThrows(PostcodeNotFoundException.class,
                () -> ukPostcodeDistanceService.calculateDistance("SW1A 1AA", "SW1A 2AA"));

        assertEquals("Postcode not found: One or both postcodes not found in the system", exception.getMessage());
    }

    @Test
    @DisplayName("Should throw error when destination postcode is not found")
    void shouldThrowErrorWhenDestinationPostcodeIsNotFound() {
        var origin = PostcodeDetails.builder()
                .postcode("SW1A 1AA")
                .latitude(51.501009)
                .longitude(-0.141588)
                .build();

        when(postcodeCoordinateService.getPostcodeDetails(anyString(), anyString()))
                .thenReturn(Map.of("SW1A 1AA", origin));

        var exception = assertThrows(PostcodeNotFoundException.class,
                () -> ukPostcodeDistanceService.calculateDistance("SW1A 1AA", "SW1A 2AA"));

        assertEquals("Postcode not found: One or both postcodes not found in the system", exception.getMessage());
    }
}
