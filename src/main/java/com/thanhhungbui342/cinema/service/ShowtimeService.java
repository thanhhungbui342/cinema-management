package com.thanhhungbui342.cinema.service;

import com.thanhhungbui342.cinema.repository.RoomRepository;

import java.time.Duration;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneOffset;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.thanhhungbui342.cinema.dto.request.ShowtimeCreateRequest;
import com.thanhhungbui342.cinema.dto.request.ShowtimeUpdateRequest;
import com.thanhhungbui342.cinema.dto.respone.ShowtimeResponse;
import com.thanhhungbui342.cinema.entity.Movie;
import com.thanhhungbui342.cinema.entity.Room;
import com.thanhhungbui342.cinema.entity.Showtime;
import com.thanhhungbui342.cinema.exception.AppException;
import com.thanhhungbui342.cinema.exception.ErrorCode;
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

    private static final int CLEAN_ROOM_TIME = 15;
    
    //READ OPERATIONS
    public Map<Long, List<ShowtimeResponse>> getAllShowtimeByDate(LocalDate date){

        Instant startOfDate = date.atStartOfDay(ZoneOffset.UTC).toInstant();
        Instant endOfDate = date.plusDays(1).atStartOfDay(ZoneOffset.UTC).toInstant().minusNanos(1);

        List<Showtime> showtimes = showtimeRepository.findShowtimeByTime(startOfDate, endOfDate);
        return groupShowtimesBy(showtimes, ShowtimeResponse::movieId);
    }

    public List<ShowtimeResponse> getAllShowtimeGroupByRoom(Long roomId, LocalDate date){

        if(!roomRepository.existsById(roomId)){
            throw new EntityNotFoundException("Room ID is not found:" + roomId);
        }

        Instant startOfDate = date.atStartOfDay(ZoneOffset.UTC).toInstant();
        Instant endOfDate = date.plusDays(1).atStartOfDay(ZoneOffset.UTC).toInstant().minusNanos(1);

        return getAllBy(() -> showtimeRepository.findShowtimeByRoom(roomId, startOfDate, endOfDate));
    }

    public List<ShowtimeResponse> getAllShowtimeGroupByMovie(Long movieId, LocalDate date){

        if(!movieRepository.existsById(movieId)){
            throw new EntityNotFoundException("Movie ID is not found:" + movieId);
        }

        Instant startOfDate = date.atStartOfDay(ZoneOffset.UTC).toInstant();
        Instant endOfDate = date.plusDays(1).atStartOfDay(ZoneOffset.UTC).toInstant().minusNanos(1);

        return getAllBy(() -> showtimeRepository.findShowtimeByMovie(movieId, startOfDate, endOfDate));
    }

    //WRITE OPERATIONS
    @Transactional 
    public ShowtimeResponse createShowtime(ShowtimeCreateRequest request){
        Movie movie = findMovieById(request.movieId());
        Room room = findRoomById(request.roomId());

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
                                            .orElseThrow(() -> new IllegalArgumentException("Showtime is not found"));
        
        Room room = findRoomById(request.roomId());
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

    // HELPED METHOD
    private <K> Map<K, List<ShowtimeResponse>> groupShowtimesBy(
        List<Showtime> showtimes,
        Function<ShowtimeResponse, K> classifier
    ){
        return showtimes.stream()
                        .map(showtimeMapper::toShowtimeResponse)
                        .collect(Collectors.groupingBy(classifier));
    }

    private List<ShowtimeResponse> getAllBy(
        Supplier<List<Showtime>> supplier
    ){
        return supplier.get()
                        .stream()
                        .map(showtimeMapper::toShowtimeResponse)
                        .toList();
    }

    private Movie findMovieById(Long movieId){
        return movieRepository.findById(movieId)
                            .orElseThrow(() -> new AppException(ErrorCode.MOVIE_NOT_FOUND));
    }

    private Room findRoomById(Long roomId){
        return roomRepository.findById(roomId)
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
