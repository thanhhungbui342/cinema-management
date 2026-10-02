package com.thanhhungbui342.cinema.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.thanhhungbui342.cinema.entity.Movie;
import com.thanhhungbui342.cinema.entity.Movie.MovieStatus;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import com.thanhhungbui342.cinema.entity.Genre;
import java.util.Set;



public interface MovieRepository extends JpaRepository<Movie, Long>{ 
    Page<Movie> findByStatus(MovieStatus status, Pageable pageable);

    Optional<Movie> findByUuid(UUID uuid);

    @Query("SELECT DISTINCT m FROM Movie m JOIN m.genres g WHERE g IN :genres")
    Page<Movie> findByGenres(@Param("genres") Set<Genre> genres, Pageable pageable);

    @Query("SELECT m FROM Movie m " +
           "LEFT JOIN m.movieCasts mc " +
           "LEFT JOIN mc.persons p " + 
           "WHERE LOWER(m.title) LIKE LOWER(CONCAT('%', :keyword, '%')) " +
           "    OR LOWER(p.name) LIKE LOWER(CONCAT('%', :keyword, '%'))")
    Page<Movie> findByKeyword(@Param("keyword") String keyword, Pageable pageable);

    @Query("SELECT m FROM Movie m WHERE m.releaseDate <= :today AND m.status != 'ENDED'")
    List<Movie> findNowShowingMovies(
        @Param("today") LocalDate today);

    @Query("SELECT m FROM Movie m WHERE m.releaseDate >= :today")
    List<Movie> findComingSoonMovies(@Param("today") LocalDate today);

    @Modifying 
    @Query("UPDATE Movie m SET m.status = 'NOW_SHOWING' " +
           "WHERE m.status = 'COMING_SOON' AND m.releaseDate <= :today")
    int updateStatusToNowShowing(LocalDate today);

    @Modifying 
    @Query("UPDATE Movie m SET m.status = 'ENDED' "+
           "WHERE m.status = 'NOW_SHOWING' AND m.endedDate < :today")
    int updateStatusToEnded(LocalDate today);
}
