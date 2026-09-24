package com.thanhhungbui342.cinema.security;

import org.springframework.security.core.userdetails.*;
import org.springframework.stereotype.Service;

import com.thanhhungbui342.cinema.entity.User;
import com.thanhhungbui342.cinema.repository.UserRepository;

@Service 
public class CustomUserDetailsService implements UserDetailsService{

    private final UserRepository userRepository;

    public CustomUserDetailsService(UserRepository userRepository){
        this.userRepository = userRepository;
    }

    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException{
        User user = userRepository.findByUsername(username)
                            .orElseThrow(() -> new UsernameNotFoundException(username + ": User not found"));
            
        return new CustomUserDetails(user);
    }

}
