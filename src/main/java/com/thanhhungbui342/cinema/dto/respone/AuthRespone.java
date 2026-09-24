package com.thanhhungbui342.cinema.dto.respone;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data 
@NoArgsConstructor 
@AllArgsConstructor 
@Builder 
public class AuthRespone {
    private String accessToken;
    private String refreshToken;
}
