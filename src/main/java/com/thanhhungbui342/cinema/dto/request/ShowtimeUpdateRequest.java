package com.thanhhungbui342.cinema.dto.request;

import java.math.BigDecimal;
import java.time.Instant;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotNull;

public record ShowtimeUpdateRequest(
    @NotNull(message = "Movie ID is required")
    Long movieId,

    @NotNull(message = "Room ID is required")
    Long roomId, 

    @NotNull(message = "Start time is required")
    @Future(message = "Start time must be in future")
    Instant startTime,

    @NotNull(message = "Base price is required")
    @DecimalMin(value = "0.0", inclusive = false, message = "Base price must be greater than 0")
    BigDecimal basePrice
) {

}
