package com.interview.postcode_distance_api.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PostcodeCoordinate {
    private Double latitude;
    private Double longitude;
}
