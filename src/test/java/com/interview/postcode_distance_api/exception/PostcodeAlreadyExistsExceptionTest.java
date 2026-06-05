package com.interview.postcode_distance_api.exception;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class PostcodeAlreadyExistsExceptionTest {

    @Test
    @DisplayName("Should create PostcodeAlreadyExistsException with message")
    void testPostcodeAlreadyExistsExceptionWithMessage() {
        var exception = new PostcodeAlreadyExistsException("SW1A 1AA");

        assertEquals("Postcode already exists in the system: SW1A 1AA", exception.getMessage()
        );
    }
}
