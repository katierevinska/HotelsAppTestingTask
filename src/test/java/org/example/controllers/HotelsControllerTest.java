package org.example.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.core.services.HotelsService;
import org.example.dto.*;
import org.example.exceptions.ResourceNotFoundException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;
import java.util.Map;
import java.util.Set;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


@SpringBootTest
@AutoConfigureMockMvc
@ExtendWith(SpringExtension.class)
class HotelsControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private HotelsService hotelsService;

    private static final String BASE_URL = "/property-view";

    @Test
    void getAllHotels_shouldReturnListOfHotels() throws Exception {
        HotelShortInfoDTO hotel1 = HotelShortInfoDTO.builder()
                .id(1L)
                .name("Hilton")
                .description("Desc1")
                .address("Addr1")
                .phone("Phone1")
                .build();
        HotelShortInfoDTO hotel2 = HotelShortInfoDTO.builder()
                .id(2L)
                .name("Marriott")
                .description("Desc2")
                .address("Addr2")
                .phone("Phone2")
                .build();
        List<HotelShortInfoDTO> hotels = List.of(hotel1, hotel2);

        when(hotelsService.findAllShort()).thenReturn(hotels);

        mockMvc.perform(get(BASE_URL + "/hotels"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].name").value("Hilton"))
                .andExpect(jsonPath("$[1].name").value("Marriott"));
    }

    @Test
    void getHotelById_whenHotelExists_shouldReturnFullInfo() throws Exception {
        long hotelId = 1L;
        var hotelFullInfo = HotelFullInfoDTO.builder()
                .id(hotelId)
                .name("Hilton Full Info")
                .build();

        when(hotelsService.findFullById(hotelId)).thenReturn(hotelFullInfo);

        mockMvc.perform(get(BASE_URL + "/hotels/{id}", hotelId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(hotelId))
                .andExpect(jsonPath("$.name").value("Hilton Full Info"));
    }

    @Test
    void getHotelById_whenHotelDoesNotExist_shouldReturnNotFound() throws Exception {
        long nonExistentId = 999L;
        when(hotelsService.findFullById(nonExistentId)).thenThrow(new ResourceNotFoundException("Hotel not found"));

        mockMvc.perform(get(BASE_URL + "/hotels/{id}", nonExistentId))
                .andExpect(status().isNotFound());
    }

    @Test
    void searchHotels_byCity_shouldReturnMatchingHotels() throws Exception {
        HotelShortInfoDTO hotel = HotelShortInfoDTO.builder()
                .id(1L)
                .name("Minsk Marriott")
                .description("Desc")
                .address("Minsk")
                .phone("Phone")
                .build();
        List<HotelShortInfoDTO> foundHotels = List.of(hotel);

        when(hotelsService.searchByCriteria(any(HotelSearchCriteriaDTO.class))).thenReturn(foundHotels);

        mockMvc.perform(get(BASE_URL + "/search").param("city", "Minsk"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1))
                .andExpect(jsonPath("$[0].name").value("Minsk Marriott"));
    }

    @Test
    void createHotel_withValidData_shouldReturnCreated() throws Exception {
        HotelCreateInfoDTO requestDto = HotelCreateInfoDTO.builder()
                .name("New Hyatt")
                .build();
        HotelShortInfoDTO responseDto = HotelShortInfoDTO.builder()
                .id(1L)
                .name("New Hyatt")
                .description("Desc")
                .address("Addr")
                .phone("Phone")
                .build();

        when(hotelsService.createHotel(any(HotelCreateInfoDTO.class))).thenReturn(responseDto);

        mockMvc.perform(post(BASE_URL + "/hotels")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDto)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").exists())
                .andExpect(jsonPath("$.name").value("New Hyatt"));
    }

    @Test
    void addAmenitiesToHotel_shouldReturnOk() throws Exception {
        long hotelId = 1L;
        Set<String> amenities = Set.of("Free WiFi", "Parking");

        mockMvc.perform(post(BASE_URL + "/hotels/{id}/amenities", hotelId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(amenities)))
                .andExpect(status().isOk());
    }

    @Test
    void getHistogram_byBrand_shouldReturnMap() throws Exception {
        String param = "brand";
        Map<String, Long> histogramData = Map.of("Hilton", 10L, "Marriott", 5L);

        when(hotelsService.getHistogramBy(param)).thenReturn(histogramData);

        mockMvc.perform(get(BASE_URL + "/histogram/{param}", param))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.Hilton").value(10))
                .andExpect(jsonPath("$.Marriott").value(5));
    }
}