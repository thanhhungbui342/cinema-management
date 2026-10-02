package com.thanhhungbui342.cinema.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.thanhhungbui342.cinema.dto.respone.GenreResponse;
import com.thanhhungbui342.cinema.service.GenreService;

import lombok.RequiredArgsConstructor;

@RestController 
@RequiredArgsConstructor 
@RequestMapping("/api/v1/genres")
public class GenreController {

    private final GenreService genreService;

    public ResponseEntity<List<GenreResponse>> getAllGenres(){
        return ResponseEntity.ok(genreService.findAllGenre());
    }
}
