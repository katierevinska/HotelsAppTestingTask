package org.example.repositories;

import org.example.entities.ArrivalTime;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ArrivalTimeRepository extends JpaRepository<ArrivalTime, Long> {
}
