package com.interview.postcode_distance_api.service;

import com.interview.postcode_distance_api.dto.PostcodeCoordinate;
import com.interview.postcode_distance_api.dto.PostcodeLocation;
import com.interview.postcode_distance_api.exception.PostcodeAlreadyExistsException;
import com.interview.postcode_distance_api.exception.PostcodeNotFoundException;
import com.interview.postcode_distance_api.mapper.PostcodeDistanceMapper;
import com.interview.postcode_distance_api.repository.PostcodeDetailsRepository;
import com.interview.postcode_distance_api.repository.model.PostcodeDetails;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UKPostcodeManagementServiceTest {

    @Mock
    private PostcodeDetailsRepository postcodeDetailsRepository;

    @Mock
    private UKPostcodeCoordinateService ukPostcodeCoordinateService;

    @Mock
    private PostcodeDistanceMapper postcodeDistanceMapper;

    @InjectMocks
    private UKPostcodeManagementService ukPostcodeManagementService;

    @Test
    @DisplayName("Should add postcode details into database successfully if postcode not exist")
    void shouldAddPostcodeDetailsIntoDatabaseSuccessfullyIfPostcodeNotExist() {
        var postcodeDetails = constructPostcodeDetails();
        var postcodeLocation = constructPostcodeLocation();

        when(ukPostcodeCoordinateService.getPostcodeDetail(anyString()))
                .thenReturn(Optional.empty());

        when(postcodeDistanceMapper.constructPostcodeLocationToPostcodeDetails(any(PostcodeLocation.class)))
                .thenReturn(postcodeDetails);

        when(postcodeDetailsRepository.save(postcodeDetails))
                .thenReturn(postcodeDetails);

        when(postcodeDistanceMapper.constructPostcodeDetailsToLocationDto(any(PostcodeDetails.class)))
                .thenReturn(postcodeLocation);

        var result = ukPostcodeManagementService.addPostcodeDetails(postcodeLocation);

        assertEquals(postcodeLocation, result);
    }

    @Test
    @DisplayName("Should throw PostcodeAlreadyExistsException when postcode already exist in the system")
    void shouldThrowPostcodeAlreadyExistsExceptionWhenPostcodeAlreadyExistInTheSystem() {
        when(ukPostcodeCoordinateService.getPostcodeDetail(anyString()))
                .thenReturn(Optional.of(constructPostcodeDetails()));

        var exception = assertThrows(PostcodeAlreadyExistsException.class, () -> {
                ukPostcodeManagementService.addPostcodeDetails(constructPostcodeLocation());
        });

        assertEquals("Postcode already exists in the system: SW1A 1AA", exception.getMessage());
    }

    @Test
    @DisplayName("Should update existing postcode successfully")
    void shouldUpdateExistingPostcodeSuccessfully () {
        var postcodeDetails = constructPostcodeDetails();

        var updatePostcodeLocation = constructUpdatedPostcodeLocation(0.0, -0.140000);
        var updatePostcodeDetails = constructUpdatedPostcodeDetails(0.0, -0.140000);

        var postcodeCoordinate = PostcodeCoordinate.builder()
                        .latitude(0.0)
                        .longitude(-0.140000)
                        .build();

        when(ukPostcodeCoordinateService.getPostcodeDetail(anyString()))
                .thenReturn(Optional.of(postcodeDetails));

        when(postcodeDetailsRepository.save(any(PostcodeDetails.class)))
                .thenReturn(updatePostcodeDetails);

        when(postcodeDistanceMapper.constructPostcodeDetailsToLocationDto(any(PostcodeDetails.class)))
                .thenReturn(updatePostcodeLocation);

        var result = ukPostcodeManagementService.updatePostcodeDetails("SW1A 1AA", postcodeCoordinate);

        assertEquals(updatePostcodeLocation, result);
        verify(postcodeDistanceMapper).constructPostcodeDetailsToLocationDto(updatePostcodeDetails);
    }

    @Test
    @DisplayName("Should throw PostcodeNotFoundException when postcode not exist")
    void shouldThrowPostcodeNotFoundExceptionWhenPostcodeNotExist () {
        var postcodeDetails =  PostcodeCoordinate.builder()
                .latitude(51.501009)
                .longitude(-0.141588)
                .build();

        when(ukPostcodeCoordinateService.getPostcodeDetail(anyString()))
                .thenReturn(Optional.empty());

        var exception = assertThrows(PostcodeNotFoundException.class, () ->
                ukPostcodeManagementService
                        .updatePostcodeDetails("SW1A 1AA",
                                postcodeDetails));

        assertEquals("Postcode not found: SW1A 1AA", exception.getMessage());
    }

    @Test
    @DisplayName("Should only update latitude when postcode coordinate contains latitude only")
    void shouldOnlyUpdateLatitudeWhenPostcodeCoordinateContainsLatitudeOnly () {
        var postcodeDetails = constructPostcodeDetails();

        var updatePostcodeLocation = constructUpdatedPostcodeLocation(0.0, postcodeDetails.getLongitude());
        var updatePostcodeDetails = constructUpdatedPostcodeDetails(0.0, postcodeDetails.getLongitude());

        var postcodeCoordinate = PostcodeCoordinate.builder()
                .latitude(0.0)
                .build();

        when(ukPostcodeCoordinateService.getPostcodeDetail(anyString()))
                .thenReturn(Optional.of(postcodeDetails));

        when(postcodeDetailsRepository.save(any(PostcodeDetails.class)))
                .thenReturn(updatePostcodeDetails);

        when(postcodeDistanceMapper.constructPostcodeDetailsToLocationDto(any(PostcodeDetails.class)))
                .thenReturn(updatePostcodeLocation);

        var result = ukPostcodeManagementService.updatePostcodeDetails("SW1A 1AA", postcodeCoordinate);

        assertEquals(updatePostcodeLocation, result);
        verify(postcodeDistanceMapper).constructPostcodeDetailsToLocationDto(updatePostcodeDetails);
    }

    @Test
    @DisplayName("Should only update longitude when postcode coordinate contains longitude only")
    void shouldOnlyUpdateLongitudeWhenPostcodeCoordinateContainsLongitudeOnly () {
        var postcodeDetails = constructPostcodeDetails();

        var updatePostcodeLocation = constructUpdatedPostcodeLocation(postcodeDetails.getLatitude(), 0.0);
        var updatePostcodeDetails = constructUpdatedPostcodeDetails(postcodeDetails.getLatitude(), 0.0);

        var postcodeCoordinate = PostcodeCoordinate.builder()
                .longitude(0.0)
                .build();

        when(ukPostcodeCoordinateService.getPostcodeDetail(anyString()))
                .thenReturn(Optional.of(postcodeDetails));

        when(postcodeDetailsRepository.save(any(PostcodeDetails.class)))
                .thenReturn(updatePostcodeDetails);

        when(postcodeDistanceMapper.constructPostcodeDetailsToLocationDto(any(PostcodeDetails.class)))
                .thenReturn(updatePostcodeLocation);

        var result = ukPostcodeManagementService.updatePostcodeDetails("SW1A 1AA", postcodeCoordinate);

        assertEquals(updatePostcodeLocation, result);
        verify(postcodeDistanceMapper).constructPostcodeDetailsToLocationDto(updatePostcodeDetails);
    }

    @Test
    @DisplayName("Should delete postcode successfully")
    void shouldDeletePostcodeSuccessfully() {
        ukPostcodeManagementService.deletePostcodeDetails("SW1A 1AA");

        verify(postcodeDetailsRepository).deleteByPostcode("SW1A 1AA");
    }

    private PostcodeDetails constructPostcodeDetails() {
        return PostcodeDetails.builder()
                .postcode("SW1A 1AA")
                .latitude(51.501009)
                .longitude(-0.141588)
                .build();
    }

    private PostcodeLocation constructPostcodeLocation() {
        return PostcodeLocation.builder()
                .postcode("SW1A 1AA")
                .coordinate(PostcodeCoordinate.builder()
                        .latitude(51.501009)
                        .longitude(-0.141588)
                        .build())
                .build();
    }

    private PostcodeDetails constructUpdatedPostcodeDetails(double latitude, double longitude) {
        return PostcodeDetails.builder()
                .postcode("SW1A 1AA")
                .latitude(latitude)
                .longitude(longitude)
                .build();
    }

    private PostcodeLocation constructUpdatedPostcodeLocation(double latitude, double longitude) {
        return PostcodeLocation.builder()
                .postcode("SW1A 1AA")
                .coordinate(PostcodeCoordinate.builder()
                        .latitude(latitude)
                        .longitude(longitude)
                        .build())
                .build();
    }

}
