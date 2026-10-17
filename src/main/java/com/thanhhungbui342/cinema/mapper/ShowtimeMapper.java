package com.thanhhungbui342.cinema.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

import com.thanhhungbui342.cinema.dto.request.ShowtimeCreateRequest;
import com.thanhhungbui342.cinema.dto.request.ShowtimeUpdateRequest;
import com.thanhhungbui342.cinema.dto.respone.ShowtimeResponse;
import com.thanhhungbui342.cinema.entity.Showtime;

@Mapper(componentModel = "spring")  
public interface ShowtimeMapper {   
    
    @Mapping(target = "movieId", source = "movie.id")
    @Mapping(target = "roomId", source = "room.id")
    ShowtimeResponse toShowtimeResponse(Showtime showtime);

    @Mapping(target = "bookedSeats", constant = "0")
    @Mapping(target = "heldSeats", constant = "0")
    @Mapping(target = "status", expression = "java(com.thanhhungbui342.cinema.entity.Showtime.ShowtimeStatus.SCHEDULED)")
    Showtime toShowtimeEntity(ShowtimeCreateRequest request);

    void updateShowtimeFromRequest(ShowtimeUpdateRequest request, @MappingTarget Showtime showtime);
}
