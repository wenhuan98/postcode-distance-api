package com.interview.postcode_distance_api.controller.service;

import com.interview.postcode_distance_api.repository.PostcodeDetailsRepository;
import com.interview.postcode_distance_api.repository.model.PostcodeDetails;
import com.interview.postcode_distance_api.service.UKPostcodecoordinateService;
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
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(MockitoExtension.class)
public class UKPostcodeCoordinateServiceTest {

    @Mock
    private PostcodeDetailsRepository postcodeDetailsRepository;

    @InjectMocks
    private UKPostcodecoordinateService ukPostcodeCoordinateService;

    @Test
    @DisplayName("Should get postcode details successfully")
    public void shouldGetPostcodeDetails_Success() {
        var postcodeDet = PostcodeDetails.builder()
                        .postcode("SW1A 1AA")
                        .latitude(51.501009)
                        .longitude(-0.141588)
                        .build();

        var psotcodeDet2 = PostcodeDetails.builder()
                        .postcode("SW1A 2AA")
                        .latitude(50.501009)
                        .longitude(-0.141588)
                        .build();

        //Given
        when(postcodeDetailsRepository.findByPostcodeIn(anyList()))
                .thenReturn(List.of(postcodeDet, psotcodeDet2));

        var result = ukPostcodeCoordinateService.getPostcodeDetails("SW1A 1AA", "SW1A 2AA");

        assertEquals(2, result.size());
        assertEquals(postcodeDet, result.get("SW1A 1AA"));
        assertEquals(postcodeDet, result.get("SW1A 2AA"));
    }

    @Test
    public void shouldGetEmptyListWhenPostcodeDetailsNotFound() {
        //Given
        when(postcodeDetailsRepository.findByPostcodeIn(anyList()))
                .thenReturn(List.of());

        var result = ukPostcodeCoordinateService.getPostcodeDetails("SW1A 1AA", "SW1A 2AA");

        assertEquals(0, result.size());
    }

}
