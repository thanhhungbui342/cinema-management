package com.thanhhungbui342.cinema.exception;

import java.time.LocalDateTime;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.Builder;

@Builder 
@JsonInclude(JsonInclude.Include.NON_NULL)
public record ErrorResponse(
    int status,
    int code,
    String message,
    LocalDateTime timestamp,
    Map<String, String> errors
) {

}
