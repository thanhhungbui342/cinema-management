package com.thanhhungbui342.cinema.service;


import java.time.LocalDate;
import java.util.List;
import java.util.Set;
import java.util.UUID;

import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.thanhhungbui342.cinema.dto.request.MovieCreateRequest;
import com.thanhhungbui342.cinema.dto.request.MovieFilterRequest;
import com.thanhhungbui342.cinema.dto.request.MovieUpdateRequest;
import com.thanhhungbui342.cinema.dto.respone.MovieResponse;
import com.thanhhungbui342.cinema.entity.Genre;
import com.thanhhungbui342.cinema.entity.Movie;
import com.thanhhungbui342.cinema.exception.AppException;
import com.thanhhungbui342.cinema.exception.ErrorCode;
import com.thanhhungbui342.cinema.mapper.MovieMapper;
import com.thanhhungbui342.cinema.repository.GenreRepository;
import com.thanhhungbui342.cinema.repository.MovieRepository;
import com.thanhhungbui342.cinema.repository.specification.MovieSpecification;

import lombok.RequiredArgsConstructor;

@Service 
@RequiredArgsConstructor 
@Transactional(readOnly = true)
public class MovieService {

    private final MovieRepository movieRepository;
    private final GenreRepository genreRepository;
    private final MovieMapper movieMapper;

    // READ OPERATIONS

    @Cacheable(value = "movie_now_showing", key = "#root.methodName")
    public List<MovieResponse> getMovieNowShowing(){
        return movieRepository.findNowShowingMovies(LocalDate.now())
                                .stream()
                                .map(movieMapper::toMovieResponse)
                                .toList();
    }

    @Cacheable(value = "movie_coming_soon", key = "#root.methodName")
    public List<MovieResponse> getMovieComingSoon(){
        return movieRepository.findComingSoonMovies(LocalDate.now())
                                .stream()
                                .map(movieMapper::toMovieResponse)
                                .toList();
    }

    public MovieResponse getMovieById(UUID uuid){
        return movieRepository.findByUuid(uuid)
                                .map(movieMapper::toMovieResponse)
                                .orElseThrow(() -> new AppException(ErrorCode.MOVIE_NOT_FOUND));
    }

    public Page<MovieResponse> filterMovies(MovieFilterRequest filterRequest, Pageable pageable){
        Specification<Movie> specification = MovieSpecification.filterMovies(filterRequest);

        return movieRepository.findAll(specification, pageable)
                                .map(movieMapper::toMovieResponse);
    }

    // WRITE OPERATIONS
    @Transactional 
    @CacheEvict(value = {"movie_now_showing", "movie_coming_soon"}, allEntries = true)
    public MovieResponse createMovie(MovieCreateRequest request){

        Set<Genre> genres = fetchAndValidateGenres(request.genreIds());

        Movie movie = movieMapper.toMovieEntity(request);
        movie.setGenres(genres);

        Movie savedMovie = movieRepository.save(movie);
        return movieMapper.toMovieResponse(savedMovie); 
    }

    @Transactional 
    @CacheEvict(value = {"movie_now_showing", "movie_coming_soon"}, allEntries = true)
    public MovieResponse updateMovie(MovieUpdateRequest request, UUID uuid){

        Movie movie = movieRepository.findByUuid(uuid)
                                    .orElseThrow(() -> new AppException(ErrorCode.MOVIE_NOT_FOUND));

        movieMapper.updateMovieFromRequest(request, movie);

        if (request.genreIds() != null && !request.genreIds().isEmpty()) {
            Set<Genre> genres = fetchAndValidateGenres(request.genreIds());
            movie.setGenres(genres);
        }

        Movie updateMovie = movieRepository.save(movie);
        return movieMapper.toMovieResponse(updateMovie);
    }    

    // HELPED METHODS
    public Set<Genre> fetchAndValidateGenres(Set<Long> genreIds){
        if (genreIds == null || genreIds.isEmpty()) {
            return Set.of();
        }

        Set<Genre> genres = genreRepository.findAllByIdIn(genreIds);
        if(genres.size() != genreIds.size()){
            throw new AppException(ErrorCode.GENRE_NOT_FOUND);
        }

        return genres;
    }
}
