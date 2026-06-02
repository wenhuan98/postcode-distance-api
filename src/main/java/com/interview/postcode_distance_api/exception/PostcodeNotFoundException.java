package com.interview.postcode_distance_api.exception;

public class PostcodeNotFoundException extends RuntimeException{
    public PostcodeNotFoundException(String postcode) {
        super("Postcode not found: " + postcode);
    }
}
