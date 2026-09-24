package com.thanhhungbui342.cinema.service;

import java.util.HashSet;
import java.util.Set;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.thanhhungbui342.cinema.dto.request.LoginRequest;
import com.thanhhungbui342.cinema.dto.request.RegisterRequest;
import com.thanhhungbui342.cinema.dto.respone.AuthRespone;
import com.thanhhungbui342.cinema.entity.Role;
import com.thanhhungbui342.cinema.entity.User;
import com.thanhhungbui342.cinema.repository.RoleRepository;
import com.thanhhungbui342.cinema.repository.UserRepository;
import com.thanhhungbui342.cinema.security.CustomUserDetails;
import com.thanhhungbui342.cinema.security.JwtService;

import lombok.RequiredArgsConstructor;

@Service 
@RequiredArgsConstructor 
public class AuthService {
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;

    public AuthRespone register(RegisterRequest registerRequest){

        if (userRepository.existsByEmail(registerRequest.getEmail())){
            throw new RuntimeException("Email is existed");
        }

        if (userRepository.existsByUsername(registerRequest.getUsername())){
            throw new RuntimeException("Username is existed");
        }

        Role userRole = roleRepository.findByName(Role.RoleName.USER)
                .orElseThrow(() -> new RuntimeException("Error: Role USER non-existent!"));

        Set<Role> roles = new HashSet<>();
        roles.add(userRole);

        User user = User.builder()
                .email(registerRequest.getEmail())
                .username(registerRequest.getUsername())
                .password(passwordEncoder.encode(registerRequest.getPassword()))
                .fullname(registerRequest.getFullname())
                .phone(registerRequest.getPhone())
                .status(User.UserStatus.ACTIVE)
                .roles(roles)
                .build();        

        User savedUser = userRepository.save(user);

        UserDetails userDetails = new CustomUserDetails(savedUser);
        String accessToken = jwtService.generateToken(userDetails);
        String refreshToken = jwtService.generateRefreshToken(userDetails);

        return AuthRespone.builder()
                    .accessToken(accessToken)
                    .refreshToken(refreshToken)
                    .build();
    }   

    public AuthRespone login(LoginRequest loginRequest){

        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        loginRequest.getUsername(), 
                        loginRequest.getPassword()
                )
        );

        UserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();

        String accessToken = jwtService.generateToken(userDetails);
        String refreshToken = jwtService.generateRefreshToken(userDetails);
        return AuthRespone.builder()
                    .accessToken(accessToken)
                    .refreshToken(refreshToken)
                    .build();
    }

}
