package com.thanhhungbui342.cinema.repository;

import java.util.Set;

import org.springframework.data.jpa.repository.JpaRepository;

import com.thanhhungbui342.cinema.entity.Genre;

public interface GenreRepository extends JpaRepository<Genre, Long>{
    
    Set<Genre> findAllByIdIn(Set<Long> genreIds);
}
