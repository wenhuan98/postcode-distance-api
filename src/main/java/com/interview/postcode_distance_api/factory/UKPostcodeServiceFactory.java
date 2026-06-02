package com.interview.postcode_distance_api.factory;

import com.interview.postcode_distance_api.service.*;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component("UK")
@AllArgsConstructor
public class UKPostcodeServiceFactory implements PostcodeServiceFactory {

    private final UKPostcodeManagementService ukPostcodeManagementService;
    private final UKPostcodeDistanceService ukPostcodeDistanceService;


    @Override
    public PostcodeManagementService getPostcodeManagementService() {
        return ukPostcodeManagementService;
    }

    @Override
    public PostcodeDistanceService getPostcodeDistanceService() {
        return ukPostcodeDistanceService;
    }


}
