package org.example.dto;

import lombok.Builder;
import lombok.Getter;

import java.util.Set;

@Builder
@Getter
public class HotelFullInfoDTO {
    private long id;
    private String name;// /search Hotel
    private String description;
    private String brand;// /search Hotel
    private AddressDTO address; //.city /search Hotel
                     //.country /search Hotel
    private ContactsDTO contacts;
    private ArrivalTimeDTO arrivalTime;
    private Set<String> amenities; // /search Hotel

}
