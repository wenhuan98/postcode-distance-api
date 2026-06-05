package com.interview.postcode_distance_api.factory;


import com.interview.postcode_distance_api.service.UKPostcodeDistanceService;
import com.interview.postcode_distance_api.service.UKPostcodeManagementService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class PostcodeFactoryRegistryTest {

    @Mock
    private Map<String, PostcodeServiceFactory> factories;

    @Mock
    private UKPostcodeManagementService ukPostcodeManagementService;

    @Mock
    private UKPostcodeDistanceService ukPostcodeDistanceService;

    @InjectMocks
    private PostcodeFactoryRegistry registry;

    @Test
    @DisplayName("Should return UK postcode service factory when country is UK")
    void shouldReturnUKPostcodeServiceFactoryWhenCountryIsUK() {
        when(factories.get("UK"))
                .thenReturn(new UKPostcodeServiceFactory(ukPostcodeManagementService, ukPostcodeDistanceService));

        PostcodeServiceFactory factory = registry.getServiceFactory("UK");

        assertInstanceOf(UKPostcodeServiceFactory.class, factory);
    }

    @Test
    @DisplayName("Should throw exception when service factory not found")
    void shouldThrowExceptionWhenServiceFactoryNotFound() {
        when(factories.get("US"))
                .thenReturn(null);

        var exception = assertThrows(IllegalArgumentException.class,
                () -> registry.getServiceFactory("US"));

        assertEquals("US country is not supported yet", exception.getMessage());
    }
}
