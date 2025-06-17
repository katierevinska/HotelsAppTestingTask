package org.example.core.services;

import lombok.RequiredArgsConstructor;
import org.example.core.entities.Amenity;
import org.example.core.repositories.AmenityRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AmenitiesService {

    private final AmenityRepository amenityRepository;

    @Transactional
    public Set<Amenity> findOrCreateAmenities(Set<String> names) {
        Set<Amenity> existingAmenities = amenityRepository.findByNameIn(names);
        Set<String> existingNames = existingAmenities.stream()
                .map(Amenity::getName)
                .collect(Collectors.toSet());

        Set<Amenity> newAmenities = names.stream()
                .filter(name -> !existingNames.contains(name))
                .map(Amenity::new)
                .collect(Collectors.toSet());

        if (!newAmenities.isEmpty()) {
            amenityRepository.saveAll(newAmenities);
        }

        existingAmenities.addAll(newAmenities);
        return existingAmenities;
    }
}