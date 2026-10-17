package com.thanhhungbui342.cinema.repository;

import java.time.Instant;
import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.thanhhungbui342.cinema.entity.Showtime;

public interface ShowtimeRepository extends JpaRepository<Showtime, Long>{

    @Query("""
        SELECT s FROM Showtime s 
        WHERE s.movie.id = :movieId 
          AND s.startTime >= :start 
          AND s.startTime <= :end 
          AND s.status = 'SCHEDULED'
        ORDER BY s.startTime ASC
    """)
    List<Showtime> findShowtimeByMovieAndTime(@Param("movieId") Long movieId, @Param("start") LocalDateTime start, @Param("end") LocalDateTime end);

    @Query("""
            SELECT s FROM Showtime s 
            WHERE s.startTime >= :start 
                AND s.startTime <= :end 
                AND s.status = 'SCHEDULED' 
            ORDER BY s.startTime ASC
    """)
    List<Showtime> findShowtimeByTime(@Param("start") LocalDateTime start,@Param("end") LocalDateTime end);

    @Query (
        """
            SELECT COUNT(s) > 0 FROM Showtime s
            WHERE s.Room.id = :roomId
            AND s.status NOT IN ('CANCELED')
            AND s.startTime < :endTime
            AND s.endTime > :startTime         
        """
    )
    boolean existOverlappingShowtime(
        @Param("roomId") Long roomId,
        @Param("startTime") Instant startTime,
        @Param("endTime") Instant endTime
    );
}
