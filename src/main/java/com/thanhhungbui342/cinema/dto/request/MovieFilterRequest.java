package com.thanhhungbui342.cinema.dto.request;

import java.time.LocalDate;
import java.util.Set;

import com.thanhhungbui342.cinema.entity.Movie.MovieStatus;

public record MovieFilterRequest(
    Long cinemaId,
    LocalDate showDate,
    Set<Long> genreIds,
    String keyword,
    MovieStatus status
) {

}
