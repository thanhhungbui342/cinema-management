package com.thanhhungbui342.cinema.entity;

import java.util.UUID;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity 
@Table(name = "seats")
@Getter 
@Setter 
@NoArgsConstructor 
@AllArgsConstructor 
@Builder 
public class Seat {

    @Id 
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "seat_id")
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "cinema_id", nullable = false)
    private CinemaRoom cinemaRoom;

    @Column(name = "row_seat", nullable = false, length = 20)
    private String row;

    @Column(name = "seat_number", nullable = false, length = 20) 
    private String number;

    @Column(name = "seat_type", length = 20)
    private String type;

    @Column(name = "status")
    private String status;
}
