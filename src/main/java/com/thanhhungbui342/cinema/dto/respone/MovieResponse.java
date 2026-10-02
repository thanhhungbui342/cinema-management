package com.thanhhungbui342.cinema.dto.respone;

import java.time.LocalDate;

import com.thanhhungbui342.cinema.entity.Movie.MovieStatus;

import lombok.Builder;

@Builder 
public record MovieResponse(
    Long id,
    String title,
    String description,
    Integer duaration,
    LocalDate releaseDate,
    LocalDate endedDate,
    String ageRating,
    String posterUrl,
    MovieStatus status
){

}
