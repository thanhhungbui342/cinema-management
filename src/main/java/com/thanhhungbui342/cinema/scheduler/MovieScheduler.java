package com.thanhhungbui342.cinema.scheduler;

import java.time.LocalDate;

import org.springframework.cache.annotation.CacheEvict;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.thanhhungbui342.cinema.repository.MovieRepository;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Component 
@RequiredArgsConstructor 
@Slf4j 
public class MovieScheduler {
    private final MovieRepository movieRepository;

    @Scheduled(cron = "0 0 0 * * * ")
    @Transactional 
    @CacheEvict(value = {"movie_now_showing"})
    public void autoUpdateMovieStatus(){
        log.info("Start scanning and updating");

        LocalDate today = LocalDate.now();

        int updatedToNowShowing = movieRepository.updateStatusToNowShowing(today);

        int updatedToEnded = movieRepository.updateStatusToEnded(today);

        log.info("Update Status success: {}Movie NowShowing, {}Movie Ended", updatedToNowShowing, updatedToEnded);
    }
}
