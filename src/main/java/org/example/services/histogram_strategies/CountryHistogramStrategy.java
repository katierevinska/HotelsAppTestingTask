package org.example.services.histogram_strategies;

import lombok.RequiredArgsConstructor;
import org.example.dto.HistogramDTO;
import org.example.repositories.HotelRepository;
import org.example.services.HistogramStrategy;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class CountryHistogramStrategy implements HistogramStrategy {
    private final HotelRepository hotelRepository;
    @Override
    public List<HistogramDTO> getHistogramDTOs() {
        return hotelRepository.countHotelsByCountries();
    }

    @Override
    public String getStrategyName() {
        return "country";
    }

}
