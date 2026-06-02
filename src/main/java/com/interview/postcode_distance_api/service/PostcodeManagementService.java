package com.interview.postcode_distance_api.service;

import com.interview.postcode_distance_api.dto.PostcodeCoordinate;
import com.interview.postcode_distance_api.dto.PostcodeLocation;

public interface PostcodeManagementService {

    void deletePostcodeDetails(String postcode);

    PostcodeLocation addPostcodeDetails(PostcodeLocation postcodeLocation);

    PostcodeLocation updatePostcodeDetails(String postcode, PostcodeCoordinate postcodeCoordinate);


}
