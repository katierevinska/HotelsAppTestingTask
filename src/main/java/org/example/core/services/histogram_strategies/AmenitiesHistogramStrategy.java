package org.example.core.services.histogram_strategies;

import lombok.RequiredArgsConstructor;
import org.example.dto.HistogramDTO;
import org.example.core.repositories.HotelRepository;
import org.example.core.services.HistogramStrategy;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class AmenitiesHistogramStrategy implements HistogramStrategy {
    private final HotelRepository hotelRepository;
    @Override
    public List<HistogramDTO> getHistogramDTOs() {
        return hotelRepository.countHotelsByAmenities();
    }

    @Override
    public String getStrategyName() {
        return "amenities";
    }

}
