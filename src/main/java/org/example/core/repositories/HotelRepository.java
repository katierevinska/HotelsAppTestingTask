package org.example.core.repositories;

import org.example.dto.HistogramDTO;
import org.example.core.entities.Hotel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface HotelRepository extends JpaRepository<Hotel, Long>, JpaSpecificationExecutor<Hotel> {
    @Query("SELECT new org.example.dto.HistogramDTO(h.brand, COUNT(h)) " +
            "FROM Hotel h " +
            "GROUP BY h.brand")
    List<HistogramDTO> countHotelsByBrands();

    @Query("SELECT new org.example.dto.HistogramDTO(a.city, COUNT(h)) " +
            "FROM Hotel h JOIN h.address a " +
            "GROUP BY a.city")
    List<HistogramDTO> countHotelsByCities();

    @Query("SELECT new org.example.dto.HistogramDTO(a.country, COUNT(h)) " +
            "FROM Hotel h JOIN h.address a " +
            "GROUP BY a.country")
    List<HistogramDTO> countHotelsByCountries();

    @Query("SELECT new org.example.dto.HistogramDTO(a.name, COUNT(h)) " +
            "FROM Hotel h JOIN h.amenities a " +
            "GROUP BY a.name")
    List<HistogramDTO> countHotelsByAmenities();
}
