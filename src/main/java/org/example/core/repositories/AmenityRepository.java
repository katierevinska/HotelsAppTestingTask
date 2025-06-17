package org.example.core.repositories;

import org.example.core.entities.Amenity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Set;

public interface AmenityRepository extends JpaRepository<Amenity, Long> {
    Set<Amenity> findByNameIn(Set<String> names);
}
