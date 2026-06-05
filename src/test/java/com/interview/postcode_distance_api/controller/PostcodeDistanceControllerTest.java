package com.interview.postcode_distance_api.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import static org.hamcrest.Matchers.is;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
public class PostcodeDistanceControllerTest {
    @Autowired
    private WebApplicationContext context;

    private MockMvc mockMvc;

    private String token;

    @BeforeEach
    void setup() throws Exception {
        mockMvc = MockMvcBuilders
                .webAppContextSetup(context)
                .apply(springSecurity())
                .build();

        token = mockMvc.perform(post("/api/auth/get-token")
                        .contentType("application/json")
                        .content("""
                            {
                              "username": "test-user",
                              "password": "test-password"
                            }
                            """))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();
    }

    @Test
    @DisplayName("Should return HTTP 200 OK with response when getPostcodeDistance with valid postcodes and authentication")
    void shouldReturn200OKWhenGetPostcodeDistanceProvidedValidPostcodesAndAuthentication() throws Exception {
        mockMvc.perform(post("/api/v1/postcodes/distance")
                        .header("Authorization", "Bearer " + token)
                        .param("country", "UK")
                        .contentType(APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.origin.postcode", is("SW1A 1AA")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.destination.postcode",is("SW1A 2AA")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.measureUnit", is("km")));
    }

    @Test
    @DisplayName("Should return HTTP 201 create with response when addPostcodeLocation with valid postcodes and authentication")
    void shouldReturn201CreateWhenAddPostcodeLocationProvidedValidPostcodesAndAuthentication() throws Exception {
        mockMvc.perform(post("/api/v1/postcodes")
                        .header("Authorization", "Bearer " + token)
                        .param("country", "UK")
                        .contentType(APPLICATION_JSON)
                        .content(addRequestBody))
                .andExpect(status().isCreated())
                .andExpect(MockMvcResultMatchers.jsonPath("$.postcode", is("SW1A 3AA")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.coordinate.latitude",is(51.501009)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.coordinate.longitude", is(-0.141588)));
    }

    @Test
    @DisplayName("Should get HTTP 409 Conflict with response when addPostcodeLocation with existing postcode and authentication")
    void shouldReturn409ConflictWhenAddPostcodeLocationProvidedExistingPostcodesAndAuthentication() throws Exception {
        mockMvc.perform(post("/api/v1/postcodes")
                        .header("Authorization", "Bearer " + token)
                        .param("country", "UK")
                        .contentType(APPLICATION_JSON)
                        .content(addExistingRequestBody))
                .andExpect(status().isConflict())
                .andExpect(MockMvcResultMatchers.jsonPath("$.message", is("Postcode already exists in the system: SW1A 2AA")));
      }

    @Test
    @DisplayName("Should return HTTP 200 OK with response when updatePostcodeLocation with provide valid postcodes and authentication")
    void shouldReturn200OKWhenUpdatePostcodeLocationProvidedValidPostcodesAndAuthentication() throws Exception {
        mockMvc.perform(put("/api/v1/postcodes")
                        .header("Authorization", "Bearer " + token)
                        .param("country", "UK")
                        .param("postcode", "AB10 1XG")
                        .contentType(APPLICATION_JSON)
                        .content(updateRequestBody))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.postcode", is("AB10 1XG")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.coordinate.latitude",is(12.22)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.coordinate.longitude", is(-0.141588)));
    }

    @Test
    @DisplayName("Should return HTTP 404 Not Found with response when updatePostcodeLocation with missing postcode and authentication")
    void shouldReturn404NotFoundWhenUpdatePostcodeLocationProvidedMissingPostcodesAndAuthentication() throws Exception {
        mockMvc.perform(put("/api/v1/postcodes")
                        .header("Authorization", "Bearer " + token)
                        .param("country", "UK")
                        .param("postcode", "AB10 XXX")
                        .contentType(APPLICATION_JSON)
                        .content(updateRequestBody))
                .andExpect(status().isNotFound())
                .andExpect(MockMvcResultMatchers.jsonPath("$.message",
                        is("Postcode not found: AB10 XXX")));
    }

    @Test
    @DisplayName("Should return HTTP 204 No Content with response when deletePostcode with provide valid postcodes and authentication")
    void shouldReturn204NoContentWhenDeletePostcodeProvidedValidPostcodesAndAuthentication() throws Exception {
        mockMvc.perform(delete("/api/v1/postcodes")
                        .header("Authorization", "Bearer " + token)
                        .param("country", "UK")
                        .param("postcode", "AB10 1XG")
                        .contentType(APPLICATION_JSON))
                .andExpect(status().isNoContent());
     }

    String requestBody = """
            {
              "origin": "SW1A 1AA",
              "destination": "SW1A 2AA"
            }
            """;

    String addRequestBody = """
            {
                "postcode": "SW1A 3AA",
                "coordinate": {
                    "latitude": 51.501009,
                    "longitude": -0.141588
                }
            }
            """;

    String addExistingRequestBody = """
            {
                "postcode": "SW1A 2AA",
                "coordinate": {
                    "latitude": 51.5035410,
                    "longitude": -0.141588
                }
            }
            """;

    String updateRequestBody = """
            {
                "latitude": 12.22,
                "longitude": -0.141588
            }
            """;

}
