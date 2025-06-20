package org.example.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

@Getter
@AllArgsConstructor
@ToString
public class HistogramDTO {
    private String key;
    private Long count;
}