package com.thanhhungbui342.cinema.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.thanhhungbui342.cinema.dto.request.LoginRequest;
import com.thanhhungbui342.cinema.dto.request.RegisterRequest;
import com.thanhhungbui342.cinema.dto.request.RequestRefreshToken;
import com.thanhhungbui342.cinema.dto.respone.AuthRespone;
import com.thanhhungbui342.cinema.security.CustomUserDetailsService;
import com.thanhhungbui342.cinema.security.JwtService;
import com.thanhhungbui342.cinema.service.AuthService;

import lombok.RequiredArgsConstructor;

@RestController 
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor 
public class AuthenticationController {
    private final AuthenticationManager authenticationManager;
    private final CustomUserDetailsService customUserDetailsService;
    private final JwtService jwtService;
    private final AuthService authService;

    @PostMapping("/register")
    public ResponseEntity<AuthRespone> register(@RequestBody RegisterRequest registerRequest) {
        return ResponseEntity.ok(authService.register(registerRequest));
    }


    @PostMapping("/login")
    public ResponseEntity<AuthRespone> login(@RequestBody LoginRequest loginRequest){
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword()));

        UserDetails userDetails = customUserDetailsService.loadUserByUsername(loginRequest.getUsername());
        String accessToken = jwtService.generateToken(userDetails);
        String refreshToken = jwtService.generateRefreshToken(userDetails);

        return ResponseEntity.ok(
            AuthRespone.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .build()
        );
    }

    @PostMapping("/refresh-token")
    public ResponseEntity<?> refreshToken(@RequestBody RequestRefreshToken request){
        String refreshToken = request.getRefreshToken();

        String username = jwtService.extractUsername(refreshToken);

        if(username != null){
            UserDetails userDetails = customUserDetailsService.loadUserByUsername(username);

            if(jwtService.isTokenValid(refreshToken, userDetails)){
                String newAccesToken = jwtService.generateToken(userDetails);

                return ResponseEntity.ok(
                    AuthRespone.builder()
                        .accessToken(newAccesToken)
                        .refreshToken(refreshToken)
                        .build()
                );
            }
        }

        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid or expirated refresh token");
    }
}
