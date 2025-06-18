package org.example.core.services;

import org.example.core.entities.Amenity;
import org.example.core.repositories.AmenityRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AmenitiesServiceTest {

    @Mock
    private AmenityRepository amenityRepository;

    @InjectMocks
    private AmenitiesService amenitiesService;

    @Test
    void findOrCreateAmenities_whenAllAmenitiesExist_shouldNotCreateNew() {
        Set<String> requestedNames = Set.of("WiFi", "Parking");

        Amenity wifi = new Amenity("WiFi");
        Amenity parking = new Amenity("Parking");
        when(amenityRepository.findByNameIn(requestedNames))
                .thenReturn(new HashSet<>(Set.of(wifi, parking)));

        Set<Amenity> result = amenitiesService.findOrCreateAmenities(requestedNames);

        assertEquals(2, result.size());
        assertThat(result).containsExactlyInAnyOrder(wifi, parking);

        verify(amenityRepository, never()).saveAll(any());
    }

    @Test
    void findOrCreateAmenities_whenNoAmenitiesExist_shouldCreateAll() {
        Set<String> requestedNames = Set.of("Pool", "Gym");

        when(amenityRepository.findByNameIn(requestedNames)).thenReturn(new HashSet<>());

        Set<Amenity> result = amenitiesService.findOrCreateAmenities(requestedNames);

        assertThat(result).hasSize(2);

        Set<String> resultNames = result.stream().map(Amenity::getName).collect(Collectors.toSet());
        assertThat(resultNames).containsExactlyInAnyOrder("Pool", "Gym");

        verify(amenityRepository, times(1)).saveAll(any());
    }

    @Test
    void findOrCreateAmenities_whenSomeAmenitiesExist_shouldCreateOnlyNew() {
        Set<String> requestedNames = Set.of("WiFi", "Parking", "Pool");

        Amenity existingWifi = new Amenity("WiFi");

        when(amenityRepository.findByNameIn(requestedNames))
                .thenReturn(new HashSet<>(Set.of(existingWifi)));
        Set<Amenity> result = amenitiesService.findOrCreateAmenities(requestedNames);

        assertThat(result).hasSize(3);
        Set<String> resultNames = result.stream().map(Amenity::getName).collect(Collectors.toSet());
        assertThat(resultNames).containsExactlyInAnyOrder("WiFi", "Parking", "Pool");

        verify(amenityRepository).saveAll(argThat(iterable -> {
            Set<String> names = StreamSupport.stream(iterable.spliterator(), false)
                    .map(Amenity::getName)
                    .collect(Collectors.toSet());
            return names.size() == 2 && names.containsAll(Set.of("Parking", "Pool"));
        }));
    }
}