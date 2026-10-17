package com.thanhhungbui342.cinema.dto.respone;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

import com.thanhhungbui342.cinema.entity.Showtime.ShowtimeStatus;

public record ShowtimeResponse(
    UUID uuid,
    Long movieId,
    Long roomId,
    Instant startTime, 
    Instant endTime,
    BigDecimal basePrice,
    ShowtimeStatus status
) {

}
