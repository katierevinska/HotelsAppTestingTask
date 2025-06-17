package org.example.core.services;

import lombok.RequiredArgsConstructor;
import org.example.core.entities.Amenity;
import org.example.exceptions.ResourceNotFoundException;
import org.example.core.repositories.HotelRepository;
import org.example.core.repositories.HotelSpecification;
import org.example.dto.*;
import org.example.core.entities.Hotel;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.util.*;

@Service
@RequiredArgsConstructor
public class HotelsService {
    private final List<HistogramStrategy> histogramStrategies;
    private final HotelRepository hotelRepository;
    private final AmenitiesService amenityService;
    private final ConverterDTOHotelInfo converter;

    public List<HotelShortInfoDTO> findAllShort() {
        return hotelRepository.findAll().stream()
                .map(converter::toHotelShortInfoDTO)
                .toList();
    }

    public HotelFullInfoDTO findFullById(Long id) {
        return hotelRepository.findById(id)
                .map(converter::toHotelFullInfoDTO)
                .orElseThrow(() -> new ResourceNotFoundException("Hotel not found with id: " + id));
    }

    public List<HotelShortInfoDTO> searchByCriteria(
            HotelSearchCriteriaDTO criteria
    ) {
        Specification<Hotel> spec = Specification.where(null);

        if (StringUtils.hasText(criteria.getName())) {
            spec = spec.and(HotelSpecification.hasName(criteria.getName()));
        }
        if (StringUtils.hasText(criteria.getBrand())) {
            spec = spec.and(HotelSpecification.hasBrand(criteria.getBrand()));
        }
        if (StringUtils.hasText(criteria.getCity())) {
            spec = spec.and(HotelSpecification.inCity(criteria.getCity()));
        }
        if (StringUtils.hasText(criteria.getCountry())) {
            spec = spec.and(HotelSpecification.inCountry(criteria.getCountry()));
        }
        if (!CollectionUtils.isEmpty(criteria.getAmenities())) {
            spec = spec.and(HotelSpecification.hasAmenities(criteria.getAmenities()));
        }

        List<Hotel> hotels = hotelRepository.findAll(spec);

        return hotels.stream()
                .map(converter::toHotelShortInfoDTO)
                .toList();
    }

    public HotelShortInfoDTO createHotel(HotelCreateInfoDTO hotelCreateInfoDTO) {
        return converter.toHotelShortInfoDTO(
                hotelRepository.save(
                        converter.toHotelEntity(hotelCreateInfoDTO)));
    }

    public Map<String, Long> getHistogramBy(String param) {
        Map<String, Long> resultMap = new HashMap<>();
        histogramStrategies.stream()
                .filter(hs -> param.equals(hs.getStrategyName()))
                .findFirst()
                .map(HistogramStrategy::getHistogramDTOs).orElseGet(List::of)
                .forEach(histogramDTO -> resultMap.put(histogramDTO.getKey(), histogramDTO.getCount()));
        return resultMap;
    }

    @Transactional
    public void addAmenitiesToHotel(Long hotelId, Set<String> amenityNames) {
        Hotel hotel = hotelRepository.findById(hotelId)
                .orElseThrow(() -> new ResourceNotFoundException("Hotel with id " + hotelId + " not found"));

        if (amenityNames == null || amenityNames.isEmpty()) {
            return;
        }
        Set<Amenity> amenities = amenityService.findOrCreateAmenities(amenityNames);
        for (Amenity amenity : amenities) {
            hotel.addAmenity(amenity);
        }
        hotelRepository.save(hotel);
    }
}