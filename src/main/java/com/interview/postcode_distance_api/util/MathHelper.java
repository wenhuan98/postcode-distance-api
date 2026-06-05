package com.interview.postcode_distance_api.util;

import org.springframework.stereotype.Component;

@Component
public final class MathHelper {

    private MathHelper () {}

    public static double haversine(double rad1, double rad2) {
        return square(Math.sin((rad1 - rad2) / 2.0));
    }

    private static double square(double x) {
        return x * x;
    }
}
