package org.example.core.entities;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@Entity
@Table(name = "hotels")
public class Hotel {
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    @Column(name = "name")
    private String name;
    @Column(name = "description")
    private String description;
    @Column(name = "brand")
    private String brand;
    @ManyToOne(cascade = CascadeType.PERSIST)
    @JoinColumn(name = "address_id")
    private Address address;
    @ManyToOne(cascade = CascadeType.PERSIST)
    @JoinColumn(name = "contacts_id")
    private Contacts contacts;
    @ManyToOne(cascade = CascadeType.PERSIST)
    @JoinColumn(name = "arrivalTime_id")
    private ArrivalTime arrivalTime;
    @ManyToMany(cascade = CascadeType.PERSIST)
    @JoinTable(
            name = "hotel_amenity",
            joinColumns = @JoinColumn(name = "hotel_id"),
            inverseJoinColumns = @JoinColumn(name = "amenity_id")
    )
    private Set<Amenity> amenities = new HashSet<>();

    public void addAmenity(Amenity amenity) {
        this.amenities.add(amenity);
        amenity.getHotels().add(this);
    }
}
