package com.thanhhungbui342.cinema.service;

import com.thanhhungbui342.cinema.repository.RoomRepository;

import java.time.Duration;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.thanhhungbui342.cinema.dto.request.ShowtimeCreateRequest;
import com.thanhhungbui342.cinema.dto.request.ShowtimeUpdateRequest;
import com.thanhhungbui342.cinema.dto.respone.ShowtimeResponse;
import com.thanhhungbui342.cinema.entity.Movie;
import com.thanhhungbui342.cinema.entity.Room;
import com.thanhhungbui342.cinema.entity.Showtime;
import com.thanhhungbui342.cinema.mapper.ShowtimeMapper;
import com.thanhhungbui342.cinema.repository.MovieRepository;
import com.thanhhungbui342.cinema.repository.ShowtimeRepository;

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

    //READ OPERATIONS
    public Map<Long, List<ShowtimeResponse>> getShowtimeGroupByMovieAndDate(Long movieId, LocalDate date){
        
        if(!movieRepository.existsById(movieId)){
            throw new EntityNotFoundException("Movie is not found with ID: " + movieId);
        }

        LocalDateTime startOfDate = date.atStartOfDay();
        LocalDateTime endOfDate = date.atTime(LocalTime.MAX);

        List<Showtime> showtimes = showtimeRepository.findShowtimeByMovieAndTime(movieId, startOfDate, endOfDate);

        return groupShowtimesBy(showtimes, ShowtimeResponse::roomId);
    }

    public Map<Long, List<ShowtimeResponse>> getAllShowtimeGroupByMovie(LocalDate date){
        LocalDateTime startOfDate = date.atStartOfDay();
        LocalDateTime endOfDate = date.atTime(LocalTime.MAX);

        List<Showtime> showtimes = showtimeRepository.findShowtimeByTime(startOfDate, endOfDate);

        return groupShowtimesBy(showtimes, ShowtimeResponse::movieId);
    }

    //WRITE OPERATIONS
    public ShowtimeResponse createShowtime(ShowtimeCreateRequest request){
        Movie movie = findMovieById(request.movieId());
        Room room = findRoomById(request.roomId());

        Instant calculatedEndTime = calculateEndTime(request.startTime(), movie.getDuaration());

        validateNoOverlapping(room.getId(), calculatedEndTime, calculatedEndTime, null);

        Showtime showtime = showtimeMapper.toShowtimeEntity(request);

        showtime.setMovie(movie);
        showtime.setRoom(room);
        showtime.setEndTime(calculatedEndTime);
        showtime.setTotalSeats(room.getTotalSeats()); 

        Showtime saveShowtime = showtimeRepository.save(showtime);

        return showtimeMapper.toShowtimeResponse(saveShowtime);
    }

    public ShowtimeResponse updateShowtime(ShowtimeUpdateRequest request, Long showtimeId){
        Showtime showtime = showtimeRepository.findById(showtimeId)
                                            .orElseThrow(() -> new IllegalArgumentException("Showtime is not found"));
        
        Movie movie = findMovieById(request.movieId());
        Room room = findRoomById(request.roomId());

        Instant calculatedEndTime = calculateEndTime(request.startTime(), movie.getDuaration());

        validateNoOverlapping(request.roomId(), calculatedEndTime, calculatedEndTime, showtimeId);

        showtime.setMovie(movie);
        showtime.setRoom(room);
        showtime.setEndTime(calculatedEndTime);
        showtime.setTotalSeats(room.getTotalSeats());

        Showtime updateShowtime = showtimeRepository.save(showtime);
        return showtimeMapper.toShowtimeResponse(updateShowtime);
    }

    // HELPED METHOD
    private <K> Map<K, List<ShowtimeResponse>> groupShowtimesBy(
        List<Showtime> showtimes,
        Function<ShowtimeResponse, K> classifier
    ){
        return showtimes.stream()
                        .map(showtimeMapper::toShowtimeResponse)
                        .collect(Collectors.groupingBy(classifier));
    }

    private Movie findMovieById(Long movieId){
        return movieRepository.findById(movieId)
                            .orElseThrow(() -> new EntityNotFoundException("Movie is not found"));
    }

    private Room findRoomById(Long roomId){
        return roomRepository.findById(roomId)
                            .orElseThrow(() -> new EntityNotFoundException("Room is not found"));
    }

    private Instant calculateEndTime(Instant startTime, Integer duration){
        return startTime.plus(Duration.ofMinutes(duration + 15));
    }

    private void validateNoOverlapping(Long roomId, Instant startTime, Instant endTime, Long excludeShowtimeId){
        boolean isOverlapping = false;

        if (excludeShowtimeId == null) {
            isOverlapping = showtimeRepository.existOverlappingShowtime(roomId, startTime, endTime);
        }

        if (isOverlapping) {
            throw new IllegalArgumentException("Show time overlapping");
        }
    }
}
