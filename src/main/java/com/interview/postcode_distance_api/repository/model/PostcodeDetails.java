package com.interview.postcode_distance_api.repository.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "postcodelatlng")
@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class PostcodeDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String postcode;

    private Double latitude;

    private Double longitude;

}
