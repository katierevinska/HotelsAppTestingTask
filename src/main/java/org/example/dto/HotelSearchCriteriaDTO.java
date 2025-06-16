package org.example.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class HotelSearchCriteriaDTO {
    private String name;
    private String brand;
    private String city;
    private String country;
    private List<String> amenities;
}
