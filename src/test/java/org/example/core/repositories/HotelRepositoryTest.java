package org.example.core.repositories;

import org.example.core.entities.*;
import org.example.dto.HistogramDTO;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
@DataJpaTest
class HotelRepositoryTest {

    @Autowired
    private HotelRepository hotelRepository;

    @Autowired
    private AmenityRepository amenityRepository;

    @Test
    void shouldBeEmptyIfNoHotelsSaved() {
        List<Hotel> hotels = hotelRepository.findAll();
        assertThat(hotels).isEmpty();
    }

    @Test
    void shouldSaveAndFindHotelById() {
        Hotel hotelToSave = createHotel("Hilton", "Hilton", "Minsk", "Belarus", Set.of());
        Hotel savedHotel = hotelRepository.save(hotelToSave);
        assertNotNull(savedHotel.getId());

        Optional<Hotel> foundHotelOpt = hotelRepository.findById(savedHotel.getId());

        assertTrue(foundHotelOpt.isPresent());
        assertEquals("Hilton", foundHotelOpt.get().getName());
        assertEquals(savedHotel.getId(), foundHotelOpt.get().getId());
    }

    @Test
    void shouldCountHotelsByBrand() {
        createHotel("Hilton Minsk", "BRAND1", "Minsk", "Belarus", Set.of());
        createHotel("Marriott Moscow", "BRAND2", "Moscow", "Russia", Set.of());
        createHotel("Hilton London", "BRAND1", "London", "UK", Set.of());

        List<HistogramDTO> result = hotelRepository.countHotelsByBrands();

        assertEquals(2, result.size());
        findAndAssertHistogramDTO(result, "BRAND1", 2L);
        findAndAssertHistogramDTO(result, "BRAND2", 1L);
    }

    @Test
    void shouldCountHotelsByCity() {
        createHotel("Hilton Minsk", "Hilton", "CITY1", "Belarus", Set.of());
        createHotel("Marriott Moscow", "Marriott", "CITY2", "Russia", Set.of());
        createHotel("Radisson Minsk", "Radisson", "CITY1", "Belarus", Set.of());

        List<HistogramDTO> result = hotelRepository.countHotelsByCities();

        assertEquals(2, result.size());
        findAndAssertHistogramDTO(result, "CITY1", 2L);
        findAndAssertHistogramDTO(result, "CITY2", 1L);
    }

    @Test
    void shouldCountHotelsByCountry() {
        createHotel("Hilton Minsk", "Hilton", "Minsk", "COUNTRY1", Set.of());
        createHotel("Marriott Moscow", "Marriott", "Minsk", "COUNTRY1", Set.of());
        createHotel("Radisson Minsk", "Radisson", "Moscow", "COUNTRY2", Set.of());

        List<HistogramDTO> result = hotelRepository.countHotelsByCountries();

        assertEquals(2, result.size());
        findAndAssertHistogramDTO(result, "COUNTRY1", 2L);
        findAndAssertHistogramDTO(result, "COUNTRY2", 1L);
    }

    @Test
    void shouldCountHotelsByAmenities() {
        Amenity wifi = amenityRepository.save(new Amenity("WiFi"));
        Amenity parking = amenityRepository.save(new Amenity("Parking"));
        Amenity pool = amenityRepository.save(new Amenity("Pool"));

        createHotel("Hotel A", "Brand A", "City A", "Country A", Set.of(wifi, parking));
        createHotel("Hotel B", "Brand B", "City B", "Country B", Set.of(wifi, pool));
        createHotel("Hotel C", "Brand C", "City C", "Country C", Set.of(wifi, parking, pool));

        List<HistogramDTO> result = hotelRepository.countHotelsByAmenities();

        assertEquals(3, result.size());
        findAndAssertHistogramDTO(result, "WiFi", 3L);
        findAndAssertHistogramDTO(result, "Parking", 2L);
        findAndAssertHistogramDTO(result, "Pool", 2L);
    }

    private Hotel createHotel(String name, String brand, String city, String country, Set<Amenity> amenities) {
        Address address = new Address();
        address.setCity(city);
        address.setCountry(country);

        Hotel hotel = new Hotel();
        hotel.setName(name);
        hotel.setBrand(brand);
        hotel.setAddress(address);
        hotel.setArrivalTime(new ArrivalTime());
        hotel.setContacts(new Contacts());
        hotel.setAmenities(amenities);

        return hotelRepository.save(hotel);
    }

    private void findAndAssertHistogramDTO(List<HistogramDTO> dtoList, String expectedLabel, long expectedCount) {
        Optional<HistogramDTO> foundDto = dtoList.stream()
                .filter(dto -> dto.getKey().equals(expectedLabel))
                .findFirst();

        assertTrue(foundDto.isPresent());
        assertEquals(expectedCount, foundDto.get().getCount());
    }
}