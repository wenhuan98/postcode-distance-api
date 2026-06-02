package com.interview.postcode_distance_api.factory;

import com.interview.postcode_distance_api.service.PostcodeDistanceService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
@AllArgsConstructor
public class PostcodeFactoryRegistry {

    private final Map<String, PostcodeServiceFactory> factories;

    public PostcodeServiceFactory getServiceFactory(String country) {
        PostcodeServiceFactory service = factories.get(country.toUpperCase());

        if (service == null) {
            throw new IllegalArgumentException(country + "is not supported yet");
        }

        return service;
    }
}
