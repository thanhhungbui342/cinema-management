package com.thanhhungbui342.cinema.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.thanhhungbui342.cinema.entity.User;
import java.util.UUID;

public interface UserRepository extends JpaRepository<User, Long>{
    Optional<User> findByUuid(UUID uuid);  
    
    Optional<User> findByEmail(String email);

    Optional<User> findByUsername(String username);

    boolean existsByEmail(String email);

    boolean existsByUsername(String username);
}
