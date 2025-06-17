package org.example.repositories;

import org.example.core.entities.*;
import org.example.core.repositories.HotelRepository;
import org.example.dto.HistogramDTO;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@ExtendWith(SpringExtension.class)
@DataJpaTest
class HotelRepositoryTest {

    @Autowired
    private HotelRepository hotelRepository;

    @Test
    void shouldFindAllHotels() {
        List<Hotel> histogramDTOList = hotelRepository.findAll();
        assertTrue(histogramDTOList.isEmpty());
    }

    @Test
    void shouldFindHotelById() {
        Optional<Hotel> hotel = hotelRepository.findById(1L);
        assertTrue(hotel.isEmpty());
    }

    @Test
    void shouldCountHotelsByBrandEmptyList() {
        List<HistogramDTO> histogramDTOList = hotelRepository.countHotelsByBrands();
        assertTrue(histogramDTOList.isEmpty());
    }

    @Test
    void shouldCountHotelsByBrand() {
        saveSomeHotels();
        List<HistogramDTO> histogramDTOList = hotelRepository.countHotelsByBrands();
        assertEquals(2, histogramDTOList.size());
        histogramDTOList = hotelRepository.countHotelsByAmenities();
        assertEquals(3, histogramDTOList.size());
        assertEquals(2, histogramDTOList.get(0).getCount());
        assertEquals(2, histogramDTOList.get(1).getCount());
        assertEquals(2, histogramDTOList.get(2).getCount());
    }

    private void saveSomeHotels() {
        Amenity amenity1 = new Amenity("a1");
        Amenity amenity2 = new Amenity("a2");
        Amenity amenity3 = new Amenity("a3");

        saveHotelWith("Hotel1", "Brand1", Set.of(amenity1, amenity2));
        saveHotelWith("Hotel2", "Brand2", Set.of(amenity3));
        saveHotelWith("Hotel3", "Brand1", Set.of(amenity1, amenity2, amenity3));
    }

    private void saveHotelWith(String hotelName, String brand, Set<Amenity> amenities) {
        Hotel hotel = new Hotel();
        hotel.setName(hotelName);
        hotel.setAddress(new Address());
        hotel.setArrivalTime(new ArrivalTime());
        hotel.setContacts(new Contacts());
        hotel.setAmenities(amenities);
        hotel.setBrand(brand);
        hotelRepository.save(hotel);
    }
}