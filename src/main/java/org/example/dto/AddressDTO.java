package org.example.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class AddressDTO {
    private String houseNumber;//9A
    private String street;
    private String city;
    private String country;
    private String postCode;
}
