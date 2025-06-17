package org.example.core.services;

import org.example.dto.HistogramDTO;

import java.util.List;

public interface HistogramStrategy {
    List<HistogramDTO> getHistogramDTOs();

    String getStrategyName();
}
