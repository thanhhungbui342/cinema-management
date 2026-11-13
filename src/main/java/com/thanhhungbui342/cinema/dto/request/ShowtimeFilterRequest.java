package com.thanhhungbui342.cinema.dto.request;

import java.time.LocalDate;

public record ShowtimeFilterRequest(
    LocalDate fromDate,
    LocalDate toDate,
    Long movieId,
    Long roomId
) {

}
