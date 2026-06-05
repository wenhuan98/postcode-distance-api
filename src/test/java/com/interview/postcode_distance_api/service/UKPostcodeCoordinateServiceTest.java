package com.interview.postcode_distance_api.service;

import com.interview.postcode_distance_api.repository.PostcodeDetailsRepository;
import com.interview.postcode_distance_api.repository.model.PostcodeDetails;
import com.interview.postcode_distance_api.service.UKPostcodeCoordinateService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(MockitoExtension.class)
class UKPostcodeCoordinateServiceTest {

    @Mock
    private PostcodeDetailsRepository postcodeDetailsRepository;

    @InjectMocks
    private UKPostcodeCoordinateService ukPostcodeCoordinateService;

    @Test
    @DisplayName("Should get postcode details successfully")
    void shouldGetPostcodeDetails_Success() {
        var postcodeDet = PostcodeDetails.builder()
                .postcode("SW1A 1AA")
                .latitude(51.501009)
                .longitude(-0.141588)
                .build();

        var postcodeDet2 = PostcodeDetails.builder()
                .postcode("SW1A 2AA")
                .latitude(50.501009)
                .longitude(-0.141588)
                .build();

        //Given
        when(postcodeDetailsRepository.findByPostcodeIn(anyList()))
                .thenReturn(List.of(postcodeDet, postcodeDet2));

        var result = ukPostcodeCoordinateService.getPostcodeDetails("SW1A 1AA", "SW1A 2AA");

        assertEquals(2, result.size());
        assertEquals(postcodeDet, result.get("SW1A 1AA"));
        assertEquals(postcodeDet2, result.get("SW1A 2AA"));
    }

    @Test
    @DisplayName("Should get empty map when postcodes not found")
    void shouldGetEmptyMapWhenPostcodeDetailsNotFound() {
        when(postcodeDetailsRepository.findByPostcodeIn(anyList()))
                .thenReturn(List.of());

        var result = ukPostcodeCoordinateService.getPostcodeDetails("SW1A 1AA", "SW1A 2AA");

        assertEquals(0, result.size());
    }

    @Test
    @DisplayName("Should get postcode detail successfully")
    void shouldGetPostcodeDetailSuccessfully() {
        Optional<PostcodeDetails> postcodeDet = Optional.of(PostcodeDetails.builder()
                .postcode("SW1A 1AA")
                .latitude(51.501009)
                .longitude(-0.141588)
                .build());

        when(postcodeDetailsRepository.findByPostcode(anyString()))
                .thenReturn(postcodeDet);

        var result = ukPostcodeCoordinateService.getPostcodeDetail("SW1A 1AA");

        assertEquals(postcodeDet, result);
    }

    @Test
    @DisplayName("Should get optional empty when postcode detail not found")
    void shouldGetOptionalEmptyWhenPostcodeDetailNotFound() {
        when(postcodeDetailsRepository.findByPostcode(anyString()))
                .thenReturn(Optional.empty());

        var result = ukPostcodeCoordinateService.getPostcodeDetail("SW1A 1AA");

        assertEquals(Optional.empty(), result);
    }
}
