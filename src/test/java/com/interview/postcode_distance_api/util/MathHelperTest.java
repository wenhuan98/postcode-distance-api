package com.interview.postcode_distance_api.util;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class MathHelperTest {

    @Test
    @DisplayName("Should calculate haversine value correctly")
    void shouldCalculateHaversineValueCorrectly() {
        // Given
        double lat1 = 0.8989737191417272;
        double lat2 = 0.8527085313298616;

        // When
        double distance = MathHelper.haversine(lat1, lat2);

        assertEquals(5.350214575942828E-4, distance);
    }
}
