package com.interview.postcode_distance_api.exception;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class PostcodeNotFoundExceptionTest {

    @Test
    @DisplayName("Should create PostcodeNotFoundException with message")
    void testPostcodeAlreadyExistsExceptionWithMessage() {
        var exception = new PostcodeNotFoundException("SW1A 1AA");

        assertEquals("Postcode not found: SW1A 1AA", exception.getMessage()
        );
    }
}
