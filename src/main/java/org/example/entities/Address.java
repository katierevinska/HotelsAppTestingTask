package org.example.entities;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Getter
@Setter
@Entity
@Table(name = "addresses")
public class Address {
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "house_number")
    private String houseNumber; //9A
    @Column(name = "street")
    private String street;
    @Column(name = "city")
    private String city;
    @Column(name = "country")
    private String country;
    @Column(name = "post_code")
    private String postCode;// ? "220004"

    @Override
    public String toString() {//9 Pobediteley Avenue, Minsk, 220004, Belarus
        return houseNumber + ' ' + street + ", " + city + ", " + postCode + ", " + country;
    }
}
