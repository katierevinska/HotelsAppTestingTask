package org.example.repositories;

import org.example.entities.Amenity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Set;

public interface AmenityRepository extends JpaRepository<Amenity, Long> {
    Set<Amenity> findByNameIn(Set<String> names);
}
