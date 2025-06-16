package org.example.dto;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class HotelShortInfoDTO {
    private long id;
    private String name;
    private String description;
    private String address;
    private String phone;
}
