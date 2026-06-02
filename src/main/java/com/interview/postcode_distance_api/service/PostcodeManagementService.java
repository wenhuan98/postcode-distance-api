package com.interview.postcode_distance_api.service;

import com.interview.postcode_distance_api.dto.PostcodeLocation;
import com.interview.postcode_distance_api.repository.model.PostcodeDetails;

public interface PostcodeManagementService {

    void deletePostcodeDetails(String postcode);

    PostcodeLocation addPostcodeDetails(PostcodeLocation postcodeLocation);

    PostcodeLocation updatePostcodeDetails(PostcodeLocation postcodeLocation);


}
