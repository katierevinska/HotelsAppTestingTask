package org.example.services;

import org.example.dto.*;
import org.example.entities.*;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.stream.Collectors;

@Component
public class ConverterDTOHotelInfo {
    public Hotel toHotelEntity(HotelCreateInfoDTO createInfoDTO) {
        Hotel hotel = new Hotel();
        hotel.setName(createInfoDTO.getName());
        hotel.setDescription(createInfoDTO.getDescription());
        hotel.setBrand(createInfoDTO.getBrand());
        hotel.setContacts(
                this.toContactsEntity(createInfoDTO.getContacts())
        );
        hotel.setAddress(
                this.toAddressEntity(createInfoDTO.getAddress())
        );
        hotel.setArrivalTime(
                this.toArrivalTimeEntity(createInfoDTO.getArrivalTime())
        );
        return hotel;
    }

    public Contacts toContactsEntity(ContactsDTO contactsDTO) {
        Contacts contacts = new Contacts();
        contacts.setEmail(contactsDTO.getEmail());
        contacts.setPhone(contactsDTO.getPhone());
        return contacts;
    }

    public Address toAddressEntity(AddressDTO addressDTO) {
        Address address = new Address();
        address.setCountry(addressDTO.getCountry());
        address.setStreet(addressDTO.getStreet());
        address.setCity(addressDTO.getCity());
        address.setHouseNumber(addressDTO.getHouseNumber());
        address.setPostCode(addressDTO.getPostCode());
        return address;
    }

    public ArrivalTime toArrivalTimeEntity(ArrivalTimeDTO arrivalTimeDTO) {
        ArrivalTime arrivalTime = new ArrivalTime();
        arrivalTime.setCheckIn(arrivalTimeDTO.getCheckIn());
        arrivalTime.setCheckOut(arrivalTimeDTO.getCheckOut());
        return arrivalTime;
    }

    public HotelFullInfoDTO toHotelFullInfoDTO(Hotel hotel) {
        return HotelFullInfoDTO.builder()
                .id(hotel.getId())
                .name(hotel.getName())
                .description(hotel.getDescription())
                .brand(hotel.getBrand())
                .contacts(toContactsDTO(hotel.getContacts()))
                .address(toAddressDTO(hotel.getAddress()))
                .arrivalTime(toArrivalTimeDTO(hotel.getArrivalTime()))
                .amenities((hotel.getAmenities() == null) ? Collections.emptySet() :
                        hotel.getAmenities().stream()
                                .map(Amenity::getName)
                                .collect(Collectors.toSet()))
                .build();
    }

    public HotelShortInfoDTO toHotelShortInfoDTO(Hotel hotel) {
        return HotelShortInfoDTO.builder()
                .id(hotel.getId())
                .name(hotel.getName())
                .description(hotel.getDescription())
                .phone(hotel.getContacts().getPhone())
                .address(hotel.getAddress().toString())
                .build();
    }

    public ContactsDTO toContactsDTO(Contacts contacts) {
        return ContactsDTO.builder()
                .email(contacts.getEmail())
                .phone(contacts.getPhone())
                .build();
    }

    public AddressDTO toAddressDTO(Address address) {
        return AddressDTO.builder()
                .country(address.getCountry())
                .street(address.getStreet())
                .city(address.getCity())
                .houseNumber(address.getHouseNumber())
                .postCode(address.getPostCode())
                .build();
    }

    public ArrivalTimeDTO toArrivalTimeDTO(ArrivalTime arrivalTime) {
        return ArrivalTimeDTO.builder()
                .checkIn(arrivalTime.getCheckIn())
                .checkOut(arrivalTime.getCheckOut())
                .build();
    }
}
