package org.example.dto;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalTime;

@Getter
@Builder
public class ArrivalTimeDTO {
    private LocalTime checkIn;
    private LocalTime checkOut;
}
