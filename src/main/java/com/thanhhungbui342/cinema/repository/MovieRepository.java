package com.thanhhungbui342.cinema.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.thanhhungbui342.cinema.entity.Movie;

public interface MovieRepository extends JpaRepository<Movie, Long>{
}
