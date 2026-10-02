package com.thanhhungbui342.cinema.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.thanhhungbui342.cinema.dto.respone.GenreResponse;
import com.thanhhungbui342.cinema.mapper.GenreMapper;
import com.thanhhungbui342.cinema.repository.GenreRepository;

import lombok.RequiredArgsConstructor;

@Service 
@RequiredArgsConstructor 
public class GenreService {
    private final GenreRepository genreRepository;
    private final GenreMapper genreMapper;

    public List<GenreResponse> findAllGenre(){
        return genreRepository.findAll()
                                .stream()
                                .map(genreMapper::toGenreResponse)
                                .toList();
    }
}
