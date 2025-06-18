package org.example.core.services;

import org.example.core.entities.Amenity;
import org.example.core.entities.Hotel;
import org.example.core.repositories.HotelRepository;
import org.example.dto.*;
import org.example.exceptions.ResourceNotFoundException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.jpa.domain.Specification;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class HotelsServiceTest {

    @Mock
    private HotelRepository hotelRepository;
    @Mock
    private AmenitiesService amenityService;
    @Mock
    private ConverterDTOHotelInfo converter;
    @Mock
    private List<HistogramStrategy> histogramStrategies;
    @InjectMocks
    private HotelsService hotelsService;

    @Test
    void findAllShort_shouldReturnConvertedDTOs() {
        Hotel hotel = new Hotel();
        hotel.setId(1L);
        when(hotelRepository.findAll()).thenReturn(List.of(hotel));

        HotelShortInfoDTO dto = HotelShortInfoDTO.builder()
                .id(1L)
                .build();
        when(converter.toHotelShortInfoDTO(hotel)).thenReturn(dto);

        List<HotelShortInfoDTO> result = hotelsService.findAllShort();

        assertThat(result).hasSize(1);
        assertThat(result.get(0).getId()).isEqualTo(1L);
        verify(hotelRepository).findAll();
        verify(converter).toHotelShortInfoDTO(hotel);
    }

    @Test
    void findFullById_whenHotelExists_shouldReturnConvertedDTO() {
        Long id = 1L;
        Hotel hotel = new Hotel();
        hotel.setId(id);
        when(hotelRepository.findById(id)).thenReturn(Optional.of(hotel));

        HotelFullInfoDTO dto = HotelFullInfoDTO.builder()
                .id(id)
                .build();
        when(converter.toHotelFullInfoDTO(hotel)).thenReturn(dto);

        HotelFullInfoDTO result = hotelsService.findFullById(id);

        assertThat(result.getId()).isEqualTo(id);
    }

    @Test
    void findFullById_whenHotelDoesNotExist_shouldThrowException() {
        Long id = 99L;
        when(hotelRepository.findById(id)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> hotelsService.findFullById(id))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessage("Hotel not found with id: " + id);
    }

    @Test
    void searchByCriteria_shouldCallRepositoryWithSpecification() {
        HotelSearchCriteriaDTO criteria = new HotelSearchCriteriaDTO();
        criteria.setCity("Minsk");
        when(hotelRepository.findAll(any(Specification.class))).thenReturn(Collections.emptyList());

        hotelsService.searchByCriteria(criteria);

        verify(hotelRepository).findAll(any(Specification.class));
    }

    @Test
    void createHotel_shouldConvertSaveAndConvertBack() {
        HotelCreateInfoDTO createDto = HotelCreateInfoDTO.builder().build();
        Hotel hotelEntity = new Hotel();
        Hotel savedHotelEntity = new Hotel();
        savedHotelEntity.setId(1L);
        HotelShortInfoDTO resultDto = HotelShortInfoDTO.builder()
                .id(1L)
                .build();

        when(converter.toHotelEntity(createDto)).thenReturn(hotelEntity);
        when(hotelRepository.save(hotelEntity)).thenReturn(savedHotelEntity);
        when(converter.toHotelShortInfoDTO(savedHotelEntity)).thenReturn(resultDto);

        HotelShortInfoDTO result = hotelsService.createHotel(createDto);

        assertThat(result.getId()).isEqualTo(1L);
        verify(converter).toHotelEntity(createDto);
        verify(hotelRepository).save(hotelEntity);
        verify(converter).toHotelShortInfoDTO(savedHotelEntity);
    }

    @Test
    void addAmenitiesToHotel_shouldAddAmenitiesAndSave() {
        Long hotelId = 1L;
        Set<String> amenityNames = Set.of("WiFi", "Parking");
        Hotel hotel = spy(new Hotel());
        Amenity wifi = new Amenity("WiFi");
        Amenity parking = new Amenity("Parking");

        when(hotelRepository.findById(hotelId)).thenReturn(Optional.of(hotel));
        when(amenityService.findOrCreateAmenities(amenityNames)).thenReturn(Set.of(wifi, parking));

        hotelsService.addAmenitiesToHotel(hotelId, amenityNames);

        verify(hotel).addAmenity(wifi);
        verify(hotel).addAmenity(parking);
        verify(hotelRepository).save(hotel);
    }

    @Test
    void getHistogramBy_whenStrategyExists_shouldReturnMap() {
        String param = "brand";
        HistogramStrategy mockStrategy = mock(HistogramStrategy.class);
        when(mockStrategy.getStrategyName()).thenReturn("brand");
        when(mockStrategy.getHistogramDTOs()).thenReturn(List.of(
                new HistogramDTO("Hilton", 5L),
                new HistogramDTO("Marriott", 3L)
        ));

        when(histogramStrategies.stream()).thenReturn(Stream.of(mockStrategy));

        Map<String, Long> result = hotelsService.getHistogramBy(param);

        assertThat(result)
                .hasSize(2)
                .containsEntry("Hilton", 5L)
                .containsEntry("Marriott", 3L);
    }

    @Test
    void getHistogramBy_whenStrategyDoesNotExist_shouldReturnEmptyMap() {
        String param = "non_existent_param";
        HistogramStrategy mockStrategy = mock(HistogramStrategy.class);
        when(mockStrategy.getStrategyName()).thenReturn("brand");

        when(histogramStrategies.stream()).thenReturn(Stream.of(mockStrategy));

        Map<String, Long> result = hotelsService.getHistogramBy(param);

        assertThat(result).isEmpty();
    }
}