package com.thanhhungbui342.cinema.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

import com.thanhhungbui342.cinema.dto.request.MovieCreateRequest;
import com.thanhhungbui342.cinema.dto.request.MovieUpdateRequest;
import com.thanhhungbui342.cinema.dto.respone.MovieResponse;
import com.thanhhungbui342.cinema.entity.Movie;

@Mapper(componentModel = "spring")
public interface MovieMapper {

    MovieResponse toMovieResponse(Movie movie);

    Movie toMovieEntity(MovieCreateRequest movieCreateRequest);

    void updateMovieFromRequest(MovieUpdateRequest request, @MappingTarget Movie movie);
}
