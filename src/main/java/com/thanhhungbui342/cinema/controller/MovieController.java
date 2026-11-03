package com.thanhhungbui342.cinema.controller;

import java.util.List;
import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.thanhhungbui342.cinema.dto.request.MovieFilterRequest;
import com.thanhhungbui342.cinema.dto.respone.MovieResponse;
import com.thanhhungbui342.cinema.service.MovieService;

import lombok.RequiredArgsConstructor;

@RestController 
@RequiredArgsConstructor 
@RequestMapping("/api/v1/movies")
public class MovieController {

    private final MovieService movieService;

    @GetMapping 
    public ResponseEntity<Page<MovieResponse>> filterMovies(
        @ModelAttribute MovieFilterRequest filterRequest,
        @RequestParam(defaultValue = "0") int page,
        @RequestParam(defaultValue = "10") int size,
        @RequestParam(defaultValue = "releaseDate") String sortBy,
        @RequestParam(defaultValue = "DESC") String direction
    ){
        Sort sort = direction.equalsIgnoreCase("ASC")
                ? Sort.by(sortBy).ascending()
                : Sort.by(sortBy).descending();

        PageRequest pageRequest = PageRequest.of(page, size, sort);

        return ResponseEntity.ok(movieService.filterMovies(filterRequest, pageRequest));
    }

    @GetMapping("/details/{uuid}") 
    public ResponseEntity<MovieResponse> getMovieByUuid(@PathVariable UUID uuid){
        return ResponseEntity.ok(movieService.getMovieById(uuid));
    }

    @GetMapping("/now_showing")
    public ResponseEntity<List<MovieResponse>> getMoviesNowShowing(){
        return ResponseEntity.ok(movieService.getMovieNowShowing());
    }

    @GetMapping("/coming_soon")
    public ResponseEntity<List<MovieResponse>> getMoviesComingSoon(){
        return ResponseEntity.ok(movieService.getMovieComingSoon());
    }
}
