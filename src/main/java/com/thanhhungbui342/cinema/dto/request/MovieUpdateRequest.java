package com.thanhhungbui342.cinema.dto.request;

import java.time.LocalDate;
import java.util.Set;

import com.thanhhungbui342.cinema.entity.Movie.MovieStatus;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

public record MovieUpdateRequest(
    
    @NotBlank(message = "Must have title")
    String title,
    String description,

    @NotNull(message = "Must add duaration")
    @Min(value = 1, message = "Duration must be > 0")
    Integer duration,

    @NotNull(message = "Release Date is not empty")
    LocalDate releaseDate,

    LocalDate endedDate,

    String ageRating,
    String trailerUrl,
    String posterUrl,
    MovieStatus status,

    @NotEmpty(message = "At least 1 data")
    Set<Long> genreIds
) {

}
