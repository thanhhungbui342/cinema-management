package com.thanhhungbui342.cinema.service;

import com.thanhhungbui342.cinema.repository.RoomRepository;

import java.time.Duration;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.thanhhungbui342.cinema.dto.request.ShowtimeCreateRequest;
import com.thanhhungbui342.cinema.dto.request.ShowtimeFilterRequest;
import com.thanhhungbui342.cinema.dto.request.ShowtimeUpdateRequest;
import com.thanhhungbui342.cinema.dto.respone.ShowtimeResponse;
import com.thanhhungbui342.cinema.entity.Movie;
import com.thanhhungbui342.cinema.entity.Room;
import com.thanhhungbui342.cinema.entity.Showtime;
import com.thanhhungbui342.cinema.entity.Room.RoomStatus;
import com.thanhhungbui342.cinema.entity.Showtime.ShowtimeStatus;
import com.thanhhungbui342.cinema.exception.AppException;
import com.thanhhungbui342.cinema.exception.ErrorCode;
import com.thanhhungbui342.cinema.mapper.ShowtimeMapper;
import com.thanhhungbui342.cinema.repository.MovieRepository;
import com.thanhhungbui342.cinema.repository.ShowtimeRepository;
import com.thanhhungbui342.cinema.repository.specification.ShowtimeSpecification;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;

@Service 
@RequiredArgsConstructor 
@Transactional(readOnly = true)
public class ShowtimeService{
    private final RoomRepository roomRepository;
    private final MovieRepository movieRepository;
    private final ShowtimeRepository showtimeRepository;
    private final ShowtimeMapper showtimeMapper;

    @Value("${app.cinema.timezone:Asia/Ho_Chi_Minh}")
    private String cinemaTimezone;

    private static final int CLEAN_ROOM_TIME = 15;
    
    //READ OPERATIONS
    public Page<ShowtimeResponse> getShowtimesWithFilter(ShowtimeFilterRequest filterRequest, Pageable pageable){
        Specification<Showtime> specification = ShowtimeSpecification.filterShowtimes(filterRequest);

        Page<Showtime> showtimePage = showtimeRepository.findAll(specification, pageable);

        return showtimePage.map(showtimeMapper::toShowtimeResponse);
    }

    //WRITE OPERATIONS
    @Transactional 
    public ShowtimeResponse createShowtime(ShowtimeCreateRequest request){
        Movie movie = findMovieById(request.movieId());
        Room room = findRoomByIdWithLock(request.roomId());

        validateRoomAndMovie(room, movie, request.startTime());

        Instant calculatedEndTime = calculateEndTime(request.startTime(), movie.getDuration());

        validateNoOverlapping(room.getId(), request.startTime(), calculatedEndTime, null);

        Showtime showtime = showtimeMapper.toShowtimeEntity(request);

        showtime.setMovie(movie);
        showtime.setRoom(room);
        showtime.setEndTime(calculatedEndTime);
        showtime.setTotalSeats(room.getTotalSeats()); 

        Showtime saveShowtime = showtimeRepository.save(showtime);

        return showtimeMapper.toShowtimeResponse(saveShowtime);
    }

    @Transactional 
    public ShowtimeResponse updateShowtime(ShowtimeUpdateRequest request, Long showtimeId){
        Showtime showtime = showtimeRepository.findById(showtimeId)
                                            .orElseThrow(() -> new AppException(ErrorCode.SHOWTIME_NOT_FOUND));
        
        Room room = findRoomByIdWithLock(request.roomId());
        Movie movie = showtime.getMovie();

        Instant calculatedEndTime = calculateEndTime(request.startTime(), movie.getDuration());

        validateNoOverlapping(request.roomId(), request.startTime(), calculatedEndTime, showtimeId);

        showtime.setRoom(room);
        showtime.setStartTime(request.startTime());
        showtime.setEndTime(calculatedEndTime);
        showtime.setTotalSeats(room.getTotalSeats());

        Showtime updateShowtime = showtimeRepository.save(showtime);
        return showtimeMapper.toShowtimeResponse(updateShowtime);
    }

    @Transactional
    public void cancelShowtime(Long showtimeId){
        Showtime showtime = showtimeRepository.findById(showtimeId)
                                            .orElseThrow(() -> new AppException(ErrorCode.SHOWTIME_NOT_FOUND));
        
        if (showtime.getStatus() == ShowtimeStatus.FINISHED || showtime.getStatus() == ShowtimeStatus.CANCELED) {
            throw new AppException(ErrorCode.SHOWTIME_CANCEL_INVALID);
        }

        showtime.setStatus(ShowtimeStatus.CANCELED);
        showtimeRepository.save(showtime);
    }

    // HELPED METHOD
    private void validateRoomAndMovie(Room room, Movie movie, Instant startTime){
        if (room.getStatus() != RoomStatus.ACTIVE) {
            throw new IllegalArgumentException("Room is not active for scheduled");
        }

        if (startTime.isBefore(Instant.now())) {
            throw new IllegalArgumentException("Start time must be in the future");
        }

        LocalDate showDate = startTime.atZone(ZoneId.of(cinemaTimezone)).toLocalDate();

        if (movie.getReleaseDate() != null && showDate.isBefore(movie.getReleaseDate())) {
            throw new IllegalArgumentException("Show Date cannot be before Release Date");
        }

        if (movie.getEndedDate() != null && showDate.isAfter(movie.getEndedDate())) {
            throw new IllegalArgumentException("Show date cannot be after End Date");
        }
    }

    private Movie findMovieById(Long movieId){
        return movieRepository.findById(movieId)
                            .orElseThrow(() -> new AppException(ErrorCode.MOVIE_NOT_FOUND));
    }

    private Room findRoomById(Long roomId){
        return roomRepository.findById(roomId)
                            .orElseThrow(() -> new EntityNotFoundException("Room is not found"));
    }

    private Room findRoomByIdWithLock(Long roomId){
        return roomRepository.findRoomByIdWithLock(roomId)
                            .orElseThrow(() -> new EntityNotFoundException("Room is not found"));
    }

    private Instant calculateEndTime(Instant startTime, Integer duration){
        return startTime.plus(Duration.ofMinutes(duration + CLEAN_ROOM_TIME));
    }

    private void validateNoOverlapping(Long roomId, Instant startTime, Instant endTime, Long excludeShowtimeId){
        boolean isOverlapping = showtimeRepository.existOverlappingShowtime(roomId, startTime, endTime, excludeShowtimeId);
    
        if (isOverlapping) {
            throw new IllegalArgumentException("Show time overlapping");
        }
    }
}
