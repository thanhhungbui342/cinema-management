package com.thanhhungbui342.cinema.dto.respone;

import lombok.Builder;

@Builder 
public record GenreResponse(
    Long id,
    String name
) {
    
}
