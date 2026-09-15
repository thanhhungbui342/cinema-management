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
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity 
@Table(name = "cinemas")
@Getter 
@Setter 
@NoArgsConstructor 
@AllArgsConstructor 
@Builder 
public class Cinema {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "cinema_id") 
    private UUID id;

    @Column(name = "name", nullable = false, length = 100)
    private String name;

    @Column(name = "address", length = 100)
    private String address;

    @Column(name = "status", nullable = false, length = 50)
    private String status;

    @OneToMany(mappedBy = "cinema", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @Builder.Default
    private List<CinemaRoom> cinemaRoom = new ArrayList<>();
}
