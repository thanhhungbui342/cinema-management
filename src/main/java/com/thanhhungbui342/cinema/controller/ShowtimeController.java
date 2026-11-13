package com.thanhhungbui342.cinema.controller;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.thanhhungbui342.cinema.dto.request.ShowtimeCreateRequest;
import com.thanhhungbui342.cinema.dto.request.ShowtimeFilterRequest;
import com.thanhhungbui342.cinema.dto.request.ShowtimeUpdateRequest;
import com.thanhhungbui342.cinema.dto.respone.ShowtimeResponse;
import com.thanhhungbui342.cinema.service.ShowtimeService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController 
@RequiredArgsConstructor 
@RequestMapping("/api/v2/showtimes")
public class ShowtimeController {

    private final ShowtimeService showtimeService;

    @GetMapping 
    public ResponseEntity<Page<ShowtimeResponse>> filterShowtimes(
        ShowtimeFilterRequest filterRequest,
        @PageableDefault(size = 10, sort = "startTime", direction = Sort.Direction.ASC) Pageable pageable
    ){
        Page<ShowtimeResponse> response = showtimeService.getShowtimesWithFilter(filterRequest, pageable);

        return ResponseEntity.ok(response);
    }

    @PostMapping
    public ResponseEntity<ShowtimeResponse> createShowtime(
        @Valid @RequestBody ShowtimeCreateRequest request
    ){
        ShowtimeResponse response = showtimeService.createShowtime(request);

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PutMapping("{id}") 
    public ResponseEntity<ShowtimeResponse> updateShowtime(
        @Valid @RequestBody ShowtimeUpdateRequest request,
        @PathVariable ("id")  Long showtimeId
    ){
        ShowtimeResponse response = showtimeService.updateShowtime(request, showtimeId);
        
        return ResponseEntity.ok(response);
    }

    @PatchMapping("{id}/cancel")
    public ResponseEntity<Void> cancelShowtime(@PathVariable("id") Long showtimeId){
        showtimeService.cancelShowtime(showtimeId);
        return ResponseEntity.noContent().build();
    }
}


