package com.thanhhungbui342.cinema.entity;

import java.math.BigDecimal;
import java.time.Instant;
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
@Table(name = "bookings")
@Getter 
@Setter 
@NoArgsConstructor 
@AllArgsConstructor 
@Builder 
public class Booking {

    @Id 
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "booking_id")
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "cinema_id", nullable = false)
    private Cinema cinema;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(name = "booking_time", nullable = false)
    private Instant bookingTime;

    @Column(name = "amount", nullable = false, precision = 12, scale = 12)
    private BigDecimal amount;

    @Column(name = "status", length = 20)
    private String status;

    @Column(name = "payment_method", nullable = false, length = 20)
    private String paymentMethod;

    @OneToMany(mappedBy = "booking", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @Builder.Default
    private List<BookingTicket> tickets = new ArrayList<>();

    @OneToMany(mappedBy = "booking", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @Builder.Default
    private List<BookingProduct> products = new ArrayList<>();
}
