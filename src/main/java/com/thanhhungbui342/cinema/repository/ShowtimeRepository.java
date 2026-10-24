package com.thanhhungbui342.cinema.repository;

import java.time.Instant;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.thanhhungbui342.cinema.entity.Showtime;
import com.thanhhungbui342.cinema.entity.Showtime.ShowtimeStatus;

public interface ShowtimeRepository extends JpaRepository<Showtime, Long>{

    @Query(
        """
        SELECT s FROM Showtime s 
        JOIN FETCH s.movie m
        JOIN FETCH s.room r
        WHERE s.startTime >= :start 
          AND s.startTime <= :end 
          AND s.status NOT IN ('CANCELED')
        ORDER BY s.startTime ASC
        """
    )
    List<Showtime> findShowtimeByTime(@Param("start") Instant start,@Param("end") Instant end);

    @Query("""
        SELECT s FROM Showtime s 
        JOIN FETCH s.movie m
        JOIN FETCH s.room r
        WHERE s.room.id = :roomId
          AND s.startTime >= :start 
          AND s.startTime <= :end 
          AND s.status NOT IN ('CANCELED')
        ORDER BY s.startTime ASC
    """)
    List<Showtime> findShowtimeByRoom(@Param("roomId") Long roomId, @Param("start") Instant start, @Param("end") Instant end);

    @Query(
        """
        SELECT s FROM Showtime s 
        JOIN FETCH s.movie m
        JOIN FETCH s.room r
        WHERE s.movie.id = :movieId
            AND s.startTime >= :start 
            AND s.startTime <= :end 
            AND s.status NOT IN ('CANCELED')
        ORDER BY s.startTime ASC
        """)
    List<Showtime> findShowtimeByMovie(@Param("movieId") Long movieId, @Param("start") Instant start,@Param("end") Instant end);

    @Query("""
        SELECT COUNT(s) > 0 FROM Showtime s
        WHERE s.room.id = :roomId
        AND (:excludeShowtimeId IS NULL OR s.id != :excludeShowtimeId)
        AND s.status NOT IN ('CANCELED')
        AND s.startTime < :endTime
        AND s.endTime > :startTime
    """)
    boolean existOverlappingShowtime(
        @Param("roomId") Long roomId,
        @Param("startTime") Instant startTime,
        @Param("endTime") Instant endTime,
        @Param("excludeShowtimeId") Long excludeShowtimeId
    );

    @Modifying 
    @Query(
        """
        UPDATE Showtime s 
        SET s.status = :newStatus 
        WHERE s.status IN :currentStatus 
            AND s.startTime <= :now 
            AND s.endTime > :now
        """
    )
    int updateStatusForStartedShowtime(
        @Param("newStatus") ShowtimeStatus newStatus, 
        @Param("currentStatus") List<ShowtimeStatus> currentStatus, 
        @Param("now") Instant now
    ); 

    @Modifying 
    @Query(
        """
        UPDATE Showtime s 
        SET s.status = :newStatus 
        WHERE s.status IN :currentStatuses 
            AND s.endTime <= :now
        """
    )
    int updateStatusForEndedShowtime(
        @Param("newStatus") ShowtimeStatus newStatus, 
        @Param("currentStatuses") List<ShowtimeStatus> currentStatuses, 
        @Param("now") Instant now
    ); 
}
