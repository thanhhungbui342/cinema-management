package com.thanhhungbui342.cinema.service;

import java.time.LocalDate;

import org.springframework.stereotype.Component;

import com.thanhhungbui342.cinema.repository.MovieRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Component 
@RequiredArgsConstructor 
@Slf4j 
public class MovieScheduler {
    private final MovieRepository movieRepository;

    public void autoUpdateMovieStatus(){
        log.info("Start scanning and updating");

        LocalDate today = LocalDate.now();

        int updatedToNowShowing = movieRepository.updateStatusToNowShowing(today);

        int updatedToEnded = movieRepository.updateStatusToEnded(today);

        log.info("Update Status success: {}Movie NowShowing, {}Movie Ended", updatedToNowShowing, updatedToEnded);
    }
}
