package com.thanhhungbui342.cinema.entity;

import java.math.BigDecimal;
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
import jakarta.persistence.Version;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity 
@Table(name = "showtime_seats")
@Getter 
@Setter 
@NoArgsConstructor 
@AllArgsConstructor 
@Builder 
public class ShowtimeSeat {

    @Id 
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "showtime_seat_id")
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "showtime_id", nullable = false)
    private Showtime showtime;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "seat_id", nullable = false)
    private Seat seat;

    @Column(name = "price", nullable = false, precision = 12, scale = 12)
    private BigDecimal price;

    @Version 
    @Column(name = "row_version")
    private Long rowVersion;
}
