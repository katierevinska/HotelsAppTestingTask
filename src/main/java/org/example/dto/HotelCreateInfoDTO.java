package org.example.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class HotelCreateInfoDTO {
    private String name;
    private String description;
    private String brand;
    private AddressDTO address;
    private ContactsDTO contacts;
    private ArrivalTimeDTO arrivalTime;
}
