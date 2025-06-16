package org.example.controllers;

import lombok.RequiredArgsConstructor;
import org.example.dto.HotelCreateInfoDTO;
import org.example.dto.HotelFullInfoDTO;
import org.example.dto.HotelSearchCriteriaDTO;
import org.example.dto.HotelShortInfoDTO;
import org.example.services.HotelsService;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.Set;

@RestController
@RequestMapping("/property-view")
@RequiredArgsConstructor
public class HotelsController {

    private final HotelsService hotelsService;

    @GetMapping("/hotels")
    public ResponseEntity<List<HotelShortInfoDTO>> getAllHotels() {
        List<HotelShortInfoDTO> hotels = hotelsService.findAllShort();
        return ResponseEntity.ok(hotels);
    }

    @GetMapping("/hotels/{id}")
    public ResponseEntity<HotelFullInfoDTO> getHotelById(@PathVariable Long id) {
        HotelFullInfoDTO hotel = hotelsService.findFullById(id).get();
        return ResponseEntity.ok(hotel);
    }

    @GetMapping("/search")
    public ResponseEntity<List<HotelShortInfoDTO>> searchHotels(HotelSearchCriteriaDTO criteria) {
        List<HotelShortInfoDTO> hotels = hotelsService.searchByCriteria(criteria);
        return ResponseEntity.ok(hotels);
    }

    @PostMapping(
            value = "/hotels",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<HotelShortInfoDTO> createHotel(@RequestBody HotelCreateInfoDTO hotelCreateInfoDTO) {
        HotelShortInfoDTO createdHotel = hotelsService.createHotel(hotelCreateInfoDTO);
        return new ResponseEntity<>(createdHotel, HttpStatus.CREATED);
    }

    @PostMapping(value = "/hotels/{id}/amenities",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Void> addAmenitiesToHotel(@PathVariable Long id, @RequestBody Set<String> amenities) {
        hotelsService.addAmenitiesToHotel(id, amenities);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/histogram/{param}")
    public ResponseEntity<Map<String, Long>> getHistogram(@PathVariable String param) {
        Map<String, Long> histogram = hotelsService.getHistogramBy(param);
        return ResponseEntity.ok(histogram);
    }
}