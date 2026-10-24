package com.thanhhungbui342.cinema.controller;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.thanhhungbui342.cinema.dto.request.ShowtimeCreateRequest;
import com.thanhhungbui342.cinema.dto.request.ShowtimeUpdateRequest;
import com.thanhhungbui342.cinema.dto.respone.ShowtimeResponse;
import com.thanhhungbui342.cinema.service.ShowtimeService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController 
@RequiredArgsConstructor 
@RequestMapping("/api/v1")
public class ShowtimeController {

    private final ShowtimeService showtimeService;

    @GetMapping("/showtimes")
    public ResponseEntity<Map<Long, List<ShowtimeResponse>>> getAllShowtimeByDate(
        @RequestParam LocalDate date
    ){
        Map<Long, List<ShowtimeResponse>> response = showtimeService.getAllShowtimeByDate(date);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/movies/{movieId}/showtimes") 
    public ResponseEntity<List<ShowtimeResponse>> getShowtimeGroupByMovie(
        @PathVariable("moiveId") Long movieId,
        @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date
    ){
        return ResponseEntity.ok(showtimeService.getAllShowtimeGroupByMovie(movieId, date));
    }

    @GetMapping("/rooms/{roomId}/showtimes")
    public ResponseEntity <List<ShowtimeResponse>> getShowtimeGroupByRoom(
        @PathVariable("roomId") Long roomId,
        @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date
    ){
        return ResponseEntity.ok(showtimeService.getAllShowtimeGroupByRoom(roomId, date));
    }

    @PostMapping("/showtimes")
    public ResponseEntity<ShowtimeResponse> createShowtime(
        @Valid @RequestBody ShowtimeCreateRequest request
    ){
        ShowtimeResponse response = showtimeService.createShowtime(request);

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PutMapping("/showtimes/{id}") 
    public ResponseEntity<ShowtimeResponse> updateShowtime(
        @Valid @RequestBody ShowtimeUpdateRequest request,
        @PathVariable ("id")  Long showtimeId
    ){
        ShowtimeResponse response = showtimeService.updateShowtime(request, showtimeId);
        
        return ResponseEntity.ok(response);
    }
}
