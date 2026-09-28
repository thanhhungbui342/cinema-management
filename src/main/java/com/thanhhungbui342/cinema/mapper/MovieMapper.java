package com.thanhhungbui342.cinema.mapper;

import org.mapstruct.Mapper;

import com.thanhhungbui342.cinema.dto.respone.MovieResponse;
import com.thanhhungbui342.cinema.entity.Movie;

@Mapper(componentModel = "spring")
public interface MovieMapper {

    MovieResponse toMovieResponse(Movie movie);
}
