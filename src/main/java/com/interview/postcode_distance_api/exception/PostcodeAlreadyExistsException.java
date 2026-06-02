package com.interview.postcode_distance_api.exception;

public class PostcodeAlreadyExistsException extends RuntimeException {
    public PostcodeAlreadyExistsException(String postcode) {
        super("Postcode already exists in the system: " + postcode);
    }
}

