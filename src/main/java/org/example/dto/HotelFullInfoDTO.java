package org.example.dto;

import lombok.Builder;
import lombok.Getter;

import java.util.Set;

@Builder
@Getter
public class HotelFullInfoDTO {
    private long id;
    private String name;
    private String description;
    private String brand;
    private AddressDTO address;
    private ContactsDTO contacts;
    private ArrivalTimeDTO arrivalTime;
    private Set<String> amenities;
}
