package com.thanhhungbui342.cinema.service;


import java.time.LocalDate;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.thanhhungbui342.cinema.dto.respone.MovieResponse;
import com.thanhhungbui342.cinema.entity.Movie.MovieStatus;
import com.thanhhungbui342.cinema.mapper.MovieMapper;
import com.thanhhungbui342.cinema.repository.MovieRepository;

import lombok.RequiredArgsConstructor;

@Service 
@RequiredArgsConstructor 
public class MovieService {

    private final MovieRepository movieRepository;
    private final MovieMapper movieMapper;

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
}
