package com.thanhhungbui342.cinema.scheduler;

import java.time.Instant;
import java.util.List;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.thanhhungbui342.cinema.entity.Showtime.ShowtimeStatus;
import com.thanhhungbui342.cinema.repository.ShowtimeRepository;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Component 
@RequiredArgsConstructor 
@Slf4j 
public class ShowtimeScheduler {
    
    private final ShowtimeRepository showtimeRepository;

    @Scheduled(fixedRate = 60000) 
    @Transactional 
    public void updateShowtimeStatus(){

        Instant now = Instant.now();

        int startedCount = showtimeRepository.updateStatusForStartedShowtime(
            ShowtimeStatus.SHOWING, 
            List.of(ShowtimeStatus.SCHEDULED, ShowtimeStatus.SELLING),
            now
        );

        int endedCount = showtimeRepository.updateStatusForEndedShowtime(
            ShowtimeStatus.FINISHED, 
            List.of(ShowtimeStatus.SCHEDULED, ShowtimeStatus.SELLING, ShowtimeStatus.SHOWING), 
            now
        );

        if (startedCount > 0 || endedCount > 0) {
        log.info("Updated showtime statuses at {}: {} moved to IN_PROGRESS, {} moved to COMPLETED", 
                now, startedCount, endedCount
            );
    }
    }
}
