package org.example.core.repositories;

import org.example.core.entities.ArrivalTime;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ArrivalTimeRepository extends JpaRepository<ArrivalTime, Long> {
}
