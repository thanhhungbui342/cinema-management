package com.thanhhungbui342.cinema.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.thanhhungbui342.cinema.entity.Movie;
import com.thanhhungbui342.cinema.entity.Movie.MovieStatus;

import java.time.LocalDate;
import java.util.List;

public interface MovieRepository extends JpaRepository<Movie, Long>{
    Page<Movie> findByStatus(MovieStatus status, Pageable pageable);

    @Query("SELECT m FROM Movie m WHERE m.releaseDate <= :today AND m.status != 'ENDED'")
    List<Movie> findNowShowingMovies(
        @Param("today") LocalDate today);

    @Query("SELECT m FROM Movie m WHERE m.releaseDate >= :today")
    List<Movie> findComingSoonMovies(@Param("today") LocalDate today);
}
