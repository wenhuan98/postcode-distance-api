package com.interview.postcode_distance_api.factory;

import com.interview.postcode_distance_api.service.PostcodeCoordinateService;
import com.interview.postcode_distance_api.service.PostcodeDistanceService;
import com.interview.postcode_distance_api.service.PostcodeManagementService;

public interface PostcodeServiceFactory {
        PostcodeDistanceService getPostcodeDistanceService();
        PostcodeManagementService getPostcodeManagementService();
}
