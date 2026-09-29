package com.thanhhungbui342.cinema.service;


import java.time.LocalDate;
import java.util.List;
import java.util.Set;
import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.thanhhungbui342.cinema.dto.respone.MovieResponse;
import com.thanhhungbui342.cinema.entity.Genre;
import com.thanhhungbui342.cinema.entity.Movie.MovieStatus;
import com.thanhhungbui342.cinema.mapper.MovieMapper;
import com.thanhhungbui342.cinema.repository.MovieRepository;

import lombok.RequiredArgsConstructor;

@Service 
@RequiredArgsConstructor 
@Transactional(readOnly = true)
public class MovieService {

    private final MovieRepository movieRepository;
    private final MovieMapper movieMapper;

    // READ OPERATIONS
    public Page<MovieResponse> getMovieByStatus(MovieStatus status, Pageable pageable){
        return movieRepository.findByStatus(status, pageable)
                                .map(movieMapper::toMovieResponse);
    }

    public List<MovieResponse> getMovieNowShowing(){
        return movieRepository.findNowShowingMovies(LocalDate.now())
                                .stream()
                                .map(movieMapper::toMovieResponse)
                                .toList();
    }

    public List<MovieResponse> getMovieComingSoon(){
        return movieRepository.findComingSoonMovies(LocalDate.now())
                                .stream()
                                .map(movieMapper::toMovieResponse)
                                .toList();
    }

    public MovieResponse getMovieById(UUID uuid){
        return movieRepository.findByUuid(uuid)
                                .map(movieMapper::toMovieResponse)
                                .orElseThrow(() -> new RuntimeException("Error UUID"));
    }

    public Page<MovieResponse> searchByGenres(Set<Genre> genres, Pageable pageable){
        if(genres == null || genres.isEmpty()){
            return Page.empty(pageable);
        }
        
        return movieRepository.findByGenres(genres, pageable)
                                .map(movieMapper::toMovieResponse);
    }

    public Page<MovieResponse> searchByKeyword(String keyword, Pageable pageable){
        if(keyword == null || keyword.trim().isEmpty()){
            return Page.empty(pageable);
        }

        return movieRepository.findByKeyword(keyword, pageable)
                                .map(movieMapper::toMovieResponse);
    }
}
