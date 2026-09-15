package com.thanhhungbui342.cinema.entity;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity 
@Table(name = "cinema_rooms")
@Getter 
@Setter 
@NoArgsConstructor 
@AllArgsConstructor 
@Builder 
public class CinemaRoom {

    @Id 
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "cinema_room_id")
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "cinema_id", nullable = false) 
    private Cinema cinema;

    @Column(name = "room_name", nullable = false, length = 50)
    private String name;

    @Column(name = "room_type", length = 50)
    private String type;

    @Column(name = "status", length = 50)
    private String status;

    @OneToMany(mappedBy = "cinemaRoom", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @Builder.Default
    private List<Seat> seats = new ArrayList<>();
}
