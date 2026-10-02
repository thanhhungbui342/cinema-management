package com.thanhhungbui342.cinema.mapper;

import org.mapstruct.Mapper;

import com.thanhhungbui342.cinema.dto.respone.GenreResponse;
import com.thanhhungbui342.cinema.entity.Genre;

@Mapper(componentModel = "spring")
public interface GenreMapper {

    GenreResponse toGenreResponse(Genre genre);
}
