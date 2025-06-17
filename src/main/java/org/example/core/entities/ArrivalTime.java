package org.example.core.entities;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalTime;

@Getter
@Setter
@Entity
@Table(name = "arrival_times")
public class ArrivalTime {
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "checkIn")
    private LocalTime checkIn;
    @Column(name = "checkOut")
    private LocalTime checkOut;
}

